package io.altanalytics.data.external.cryptocompare.client;

import io.altanalytics.domain.currency.IntervalPrice;
import io.altanalytics.domain.currency.IntervalPriceRequest;

public interface CryptoCompareClient {

	public IntervalPrice fetch(IntervalPriceRequest request) throws Exception;

}
