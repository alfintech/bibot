package io.github.bibot.exchangeclient;

import io.github.bibot.domain.CurrencyPair;
import io.github.bibot.domain.price.Price;

public interface PriceClient {

	public Price latestPrice(CurrencyPair pair);

}
