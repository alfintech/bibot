package io.altanalytics.util;

import java.util.ArrayList;
import java.util.List;

import io.altanalytics.domain.currency.CurrencyPair;

public class CurrencyPairUtil {
	
	public static List<CurrencyPair> constructCurrencyPairs(String[] tradeCurrencies, String[] baseCurrencies) {

		List<CurrencyPair> currencyPairs = new ArrayList<CurrencyPair>();

		for(String tradeCurrency : tradeCurrencies) {
			for(String baseCurrency : baseCurrencies) {
				if(!baseCurrency.equals(tradeCurrency)) {
					currencyPairs.add(new CurrencyPair(baseCurrency, tradeCurrency));
				}
			}
		}

		return currencyPairs;
	}
}
