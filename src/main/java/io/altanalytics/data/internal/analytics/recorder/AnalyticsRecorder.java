package io.altanalytics.data.internal.analytics.recorder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import io.altanalytics.domain.currency.CurrencyPair;
import io.altanalytics.domain.currency.IntervalPrice;
import io.altanalytics.persistence.Publisher;
import io.altanalytics.persistence.Reader;
import io.altanalytics.data.internal.analytics.calculator.VolumeCalculator;
import io.altanalytics.domain.currency.Analytic;
import io.altanalytics.util.BigDecimalUtil;
import io.altanalytics.util.CurrencyPairUtil;
import io.altanalytics.util.DateUtil;

@EnableScheduling
@Component
public class AnalyticsRecorder {
	
	@Value("${recorder.analytics.active}")
	private boolean active;

	@Value("${recorder.analytics.interval}")
	private int interval;

	@Value("${recorder.currencies.trade}")
	protected String[] tradeCurrencies;

	@Value("${recorder.currencies.base}")
	protected String[] baseCurrencies;

	@Autowired
	protected Reader reader;

	@Autowired
	protected Publisher publisher;

	@Scheduled(cron = "${recorder.analytics.schedule}")
	public void tick() throws Exception {
		if(active) {
			List<CurrencyPair> currencyPairs = CurrencyPairUtil.constructCurrencyPairs(tradeCurrencies, baseCurrencies);

			Date analyticsEndDate = DateUtil.now();
			Date analyticsStartDate = DateUtil.shiftToPast(analyticsEndDate, interval);
			
			List<Analytic> analytics = new ArrayList<Analytic>();
			for(CurrencyPair currencyPair : currencyPairs) {
				
				//Volume calcs
				List<IntervalPrice> intervalPrices = reader.getIntervalPrices(analyticsStartDate, analyticsEndDate, currencyPair);
				BigDecimal intervalVolume = VolumeCalculator.cumulative(intervalPrices);
				BigDecimal dayVolume = intervalPrices.get(0).getDayVolume();
				BigDecimal dayAverageVolume = VolumeCalculator.average(dayVolume, interval);
				BigDecimal percentageVolume = BigDecimalUtil.multiply(BigDecimalUtil.divide(intervalVolume, dayAverageVolume), new BigDecimal(100));
				
				//ATH % calcs
				BigDecimal allTimeHigh = reader.getAllTimeHigh(currencyPair).getClose();
				BigDecimal currentPrice = intervalPrices.get(intervalPrices.size()-1).getClose();
				BigDecimal percentageATH = BigDecimalUtil.divide(currentPrice, allTimeHigh);
				
				
				Analytic analytic = new Analytic(currencyPair, intervalVolume, dayAverageVolume, percentageVolume, percentageATH, analyticsEndDate);
				analytics.add(analytic);
			}
			publisher.publishAnalytics(analytics);

		}
	}

}
