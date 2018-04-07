package io.altanalytics.domain.currency;

import java.util.Date;

public class IntervalPriceRequest {

	private CurrencyPair currencyPair;
	private Date date;
	
	public IntervalPriceRequest(CurrencyPair currencyPair, Date date) {
		this.currencyPair = currencyPair;
		this.date = date;
	}
	
	public CurrencyPair getCurrencyPair() {
		return currencyPair;
	}
	public Date getDate() {
		return date;
	}

	@Override
	public String toString() {
		return "MarketDataRequest [currencyPair=" + currencyPair + ", date=" + date + "]";
	}
	
}
