package io.github.bibot.domain;

public class CurrencyPair {

	public String baseCurrency;
	public String tradeCurrency;
	
	public CurrencyPair(String baseCurrency, String tradeCurrency) {
		this.baseCurrency = baseCurrency;
		this.tradeCurrency = tradeCurrency;
	}

	@Override
	public String toString() {
		return tradeCurrency + baseCurrency;
	}
	
}
