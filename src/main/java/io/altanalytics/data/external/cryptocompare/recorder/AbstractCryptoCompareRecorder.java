package io.altanalytics.data.external.cryptocompare.recorder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import io.altanalytics.data.external.cryptocompare.client.CryptoCompareClient;
import io.altanalytics.data.external.cryptocompare.client.CryptoCompareRequestManager;
import io.altanalytics.domain.currency.CurrencyPair;
import io.altanalytics.domain.currency.IntervalPrice;
import io.altanalytics.domain.currency.IntervalPriceRequest;
import io.altanalytics.persistence.Publisher;

public abstract class AbstractCryptoCompareRecorder {
	
	@Autowired
	protected Publisher publisher;

	@Value("${recorder.currencies.trade}")
	protected String[] tradeCurrencies;

	@Value("${recorder.currencies.base}")
	protected String[] baseCurrencies;

	protected List<IntervalPriceRequest> requestsForCurrencyPairs(List<CurrencyPair> currencyPairs, Date date) {
		
		List<IntervalPriceRequest> requests = new ArrayList<IntervalPriceRequest>();
		
		for(CurrencyPair currencyPair : currencyPairs) {
			requests.add(new IntervalPriceRequest(currencyPair, date));
		}

		return requests;
	}
	
	protected List<IntervalPrice> fetch(CryptoCompareClient marketDataClient, List<IntervalPriceRequest> marketDataRequests) throws Exception {
		CryptoCompareRequestManager marketDataRequestManager = new CryptoCompareRequestManager(marketDataClient);
		return marketDataRequestManager.fetch(marketDataRequests);
	}
	
	protected void publish(List<IntervalPrice> marketDataList) throws Exception {
		publisher.publishMarketData(marketDataList);		
	}

}
