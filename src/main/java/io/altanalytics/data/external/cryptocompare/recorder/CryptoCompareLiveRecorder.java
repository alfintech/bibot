package io.altanalytics.data.external.cryptocompare.recorder;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import io.altanalytics.data.external.cryptocompare.client.CryptoCompareLiveClient;
import io.altanalytics.domain.currency.CurrencyPair;
import io.altanalytics.domain.currency.IntervalPrice;
import io.altanalytics.domain.currency.IntervalPriceRequest;
import io.altanalytics.util.BigDecimalUtil;
import io.altanalytics.util.CurrencyPairUtil;
import io.altanalytics.util.DateUtil;

@EnableScheduling
@Component
public class CryptoCompareLiveRecorder extends AbstractCryptoCompareRecorder {

	@Value("${recorder.live.active}")
	private boolean active;

	@Value("${recorder.live.interval}")
	private int interval;

	private Map<CurrencyPair, IntervalPrice> currentMarketData = new HashMap<CurrencyPair, IntervalPrice>();

	@Autowired
	public CryptoCompareLiveClient marketDataClient;

	@Scheduled(cron = "${recorder.live.schedule}")
	public void tick() throws Exception {
		if(active) {
			Date requestDate = DateUtil.intervalStart(interval);
			List<CurrencyPair> currencyPairs = CurrencyPairUtil.constructCurrencyPairs(tradeCurrencies, baseCurrencies);

			List<IntervalPriceRequest> requests = requestsForCurrencyPairs(currencyPairs, requestDate);
			List<IntervalPrice> latestIntervalPrices = fetch(marketDataClient, requests);
			List<IntervalPrice> deltas = delta(latestIntervalPrices, requestDate);
			if(!deltas.isEmpty()) {
				publish(deltas);
			}
		}
	}

	private List<IntervalPrice> delta(List<IntervalPrice> latestIntervalPrices, Date requestDate) {

		List<IntervalPrice> deltas = new ArrayList<IntervalPrice>();

		for(IntervalPrice latestIntervalPrice : latestIntervalPrices) {
			if(currentMarketData.containsKey(latestIntervalPrice.getCurrencyPair())) {
				IntervalPrice delta = delta(latestIntervalPrice, currentMarketData.get(latestIntervalPrice.getCurrencyPair()));
				deltas.add(delta);
			}
			currentMarketData.put(latestIntervalPrice.getCurrencyPair(), latestIntervalPrice);
		}

		return deltas;
	} 

	private IntervalPrice delta(IntervalPrice latestIntervalPrice, IntervalPrice priorIntervalPrice) {

		IntervalPrice delta = new IntervalPrice(
				latestIntervalPrice.getCurrencyPair(), 
				DateUtil.intervalStart(interval),
				DateUtil.intervalEnd(interval),
				priorIntervalPrice.getClose(), 
				latestIntervalPrice.getClose(),
				latestIntervalPrice.getClose(),
				latestIntervalPrice.getClose(),
				BigDecimalUtil.subtractToZero(latestIntervalPrice.getIntervalVolume(), priorIntervalPrice.getIntervalVolume()),
				latestIntervalPrice.getDayVolume());

		return delta;
	}

}
