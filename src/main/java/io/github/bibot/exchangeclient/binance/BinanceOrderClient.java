package io.github.bibot.exchangeclient.binance;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.binance.api.client.domain.account.Trade;
import com.binance.api.client.domain.market.OrderBookEntry;

import io.github.bibot.domain.CurrencyPair;
import io.github.bibot.domain.ExecutedTrade;
import io.github.bibot.domain.ExecutedTrade.Direction;
import io.github.bibot.domain.Order;
import io.github.bibot.domain.credentials.APICredentials;
import io.github.bibot.exchangeclient.OrderClient;

public class BinanceOrderClient extends AbstractBinanceClient implements OrderClient {

	private static final Logger LOG = Logger.getLogger(BinancePriceClient.class);

	private static final int ORDER_BOOK_DEPTH = 5;
	private static final int MAX_TRADE_HISTORY = 500;


	public BinanceOrderClient(APICredentials credentials) {
		super(credentials);
	}

	public synchronized List<Order> getAsks(CurrencyPair pair) {

		List<OrderBookEntry> orderBook = client.getOrderBook(pair.toString(), ORDER_BOOK_DEPTH).getAsks();
		return convertToOrders(orderBook);
	}

	public synchronized List<Order> getBids(CurrencyPair pair) {

		List<OrderBookEntry> orderBook = client.getOrderBook(pair.toString(), ORDER_BOOK_DEPTH).getBids();
		return convertToOrders(orderBook);
	}

	public void buy(BigDecimal quantity, CurrencyPair pair, BigDecimal currentPrice) {
		LOG.info("Buying " +pair+ ":" +quantity+ "@" +currentPrice.doubleValue());
	}

	public void sell(BigDecimal quantity, CurrencyPair pair, BigDecimal currentPrice) {
		LOG.info("Selling " +pair+ ":" +quantity+ "@" +currentPrice.doubleValue());
	}

	public synchronized List<ExecutedTrade> getTradeHistory(CurrencyPair pair) {

		List<Trade> trades = client.getMyTrades(pair.toString(), MAX_TRADE_HISTORY);
		return convertToExecutedTrades(trades);
	}

	private List<Order> convertToOrders(List<OrderBookEntry> orderBook) {
		List<Order> orders = new ArrayList<Order>();
		Date date = Calendar.getInstance().getTime();

		for(OrderBookEntry entry : orderBook) {
			orders.add(new Order(new BigDecimal((entry.getPrice())), new BigDecimal(entry.getQty()), date));
		}
		return orders;
	}

	private List<ExecutedTrade> convertToExecutedTrades(List<Trade> trades) {

		List<ExecutedTrade> executedTrades = new ArrayList<ExecutedTrade>();

		for(Trade trade : trades) {
			executedTrades.add(new ExecutedTrade(
					new BigDecimal(trade.getPrice()), 
					new BigDecimal(trade.getQty()), 
					trade.isBuyer() ? Direction.BUY : Direction.SELL, 
					new Date(trade.getTime())));
		}

		return executedTrades;
	}
}