package io.altanalytics.domain.currency;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((baseCurrency == null) ? 0 : baseCurrency.hashCode());
		result = prime * result + ((tradeCurrency == null) ? 0 : tradeCurrency.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CurrencyPair other = (CurrencyPair) obj;
		if (baseCurrency == null) {
			if (other.baseCurrency != null)
				return false;
		} else if (!baseCurrency.equals(other.baseCurrency))
			return false;
		if (tradeCurrency == null) {
			if (other.tradeCurrency != null)
				return false;
		} else if (!tradeCurrency.equals(other.tradeCurrency))
			return false;
		return true;
	}
	
}
