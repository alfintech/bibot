package io.github.bibot.exchangeclient;

import java.math.BigDecimal;
import java.util.List;

import io.github.bibot.domain.CurrencyPair;
import io.github.bibot.domain.Order;
import io.github.bibot.domain.ExecutedTrade;

public interface OrderClient {

	public List<Order> getAsks(CurrencyPair pair);

	public List<Order> getBids(CurrencyPair pair);

	public void buy(BigDecimal quantity, CurrencyPair pair, BigDecimal price);

	public void sell(BigDecimal quantity, CurrencyPair pair, BigDecimal price);

	public List<ExecutedTrade> getTradeHistory(CurrencyPair currencyPair);

}
