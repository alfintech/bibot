package io.github.bibot.bot;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.bibot.domain.BotState;
import io.github.bibot.domain.CurrencyPair;
import io.github.bibot.domain.ExecutedTrade;
import io.github.bibot.domain.StrategyFund;
import io.github.bibot.domain.price.Price;
import io.github.bibot.exchangeclient.OrderClient;
import io.github.bibot.exchangeclient.PriceClient;
import io.github.bibot.priceanalysis.BigDecimalOperations;
import io.github.bibot.strategy.Strategy;
import io.github.bibot.strategy.Strategy.Signal;

public class TraderBot {

	private static final Logger LOG = LoggerFactory.getLogger(TraderBot.class);
	
	private StrategyFund fund;
	private CurrencyPair currencyPair;
	private PriceClient priceClient;
	private Strategy strategy;
	private OrderClient orderClient;
	private BotState state;
	
	public TraderBot(Strategy strategy, StrategyFund fund, CurrencyPair currencyPair, PriceClient priceClient, OrderClient orderClient) {
		this.fund = fund;
		this.currencyPair = currencyPair;
		this.priceClient = priceClient;
		this.orderClient = orderClient;
		this.strategy = strategy;
		this.state = new BotState();
		
	}

	public void tick() {
		Price price = priceClient.latestPrice(currencyPair);
		Signal signal = strategy.tick(price, state);
		if(signal == Signal.BUY) {
			BigDecimal quantity = calculateQuantityForFund(price);
			orderClient.buy(quantity, currencyPair, price.price);
			state.openQuantity = quantity;
		} else if(signal == Signal.SELL) {
			orderClient.sell(state.openQuantity, currencyPair, price.price);
			state.clear();
		}
	}

	private BigDecimal calculateQuantityForFund(Price price) {
		return BigDecimalOperations.divide(this.fund.btc, price.price);
	}

	public List<ExecutedTrade> getTrades() {
		return orderClient.getTradeHistory(currencyPair);		
	}
	
}