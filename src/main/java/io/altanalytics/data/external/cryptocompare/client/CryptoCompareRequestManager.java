package io.altanalytics.data.external.cryptocompare.client;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

import io.altanalytics.domain.currency.IntervalPrice;
import io.altanalytics.domain.currency.IntervalPriceRequest;

public class CryptoCompareRequestManager {

	private CryptoCompareClient client;

	public CryptoCompareRequestManager(CryptoCompareClient client) {
		this.client = client;
	}

	public List<IntervalPrice> fetch(List<IntervalPriceRequest> requests) throws InterruptedException, ExecutionException {

		List<IntervalPrice> results = new ArrayList<IntervalPrice>();
		List<Future<IntervalPrice>> futures = new ArrayList<Future<IntervalPrice>>();
		ExecutorService service = new ForkJoinPool(10);
		
		for(IntervalPriceRequest request : requests) {
			CryptoCompareRequestConsumer consumer = new CryptoCompareRequestConsumer(client, request);
			futures.add(service.submit(consumer));
		}
		
		for(Future<IntervalPrice> future : futures)  {
			results.add(future.get());
		}

		return results;
	}
}
