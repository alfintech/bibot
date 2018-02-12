package io.github.bibot.exchangeclient;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.bibot.domain.CurrencyPair;
import io.github.bibot.domain.ExecutedTrade;
import io.github.bibot.domain.ExecutedTrade.Direction;
import io.github.bibot.domain.Order;

public class SimpleOrderClient implements OrderClient {

	private Map<CurrencyPair, List<ExecutedTrade>> pairTradeHistory;
	
	public SimpleOrderClient() {
		this.pairTradeHistory = new HashMap<>();
	}
	
	@Override
	public List<Order> getAsks(CurrencyPair pair) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Order> getBids(CurrencyPair pair) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void buy(BigDecimal quantity, CurrencyPair pair, BigDecimal price) {
		addTrade(pair, new ExecutedTrade(price, quantity, Direction.BUY, Calendar.getInstance().getTime()));
	}

	@Override
	public void sell(BigDecimal quantity, CurrencyPair pair, BigDecimal price) {
		addTrade(pair, new ExecutedTrade(price, quantity, Direction.SELL, Calendar.getInstance().getTime()));
	}

	@Override
	public List<ExecutedTrade> getTradeHistory(CurrencyPair pair) {
		return pairTradeHistory.get(pair);
	}
	
	private void addTrade(CurrencyPair pair, ExecutedTrade trade) {
		if(!this.pairTradeHistory.containsKey(pair)) {
			this.pairTradeHistory.put(pair, new ArrayList<ExecutedTrade>());
		}
		this.pairTradeHistory.get(pair).add(trade);
	
	}

}
