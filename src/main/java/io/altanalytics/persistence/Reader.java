package io.altanalytics.persistence;

import java.util.Date;
import java.util.List;

import io.altanalytics.domain.currency.CurrencyPair;
import io.altanalytics.domain.currency.IntervalPrice;

public interface Reader {

	IntervalPrice getIntervalPrice(CurrencyPair currencyPair) throws Exception;

	IntervalPrice getAllTimeHigh(CurrencyPair currencyPair) throws Exception;

	List<IntervalPrice> getIntervalPrices(Date fromDate, Date toDate, CurrencyPair currencyPair) throws Exception;

}