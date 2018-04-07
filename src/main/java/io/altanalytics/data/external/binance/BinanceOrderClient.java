package io.altanalytics.data.external.binance;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.binance.api.client.domain.market.OrderBookEntry;

import io.altanalytics.domain.currency.CurrencyPair;
import io.altanalytics.domain.currency.Order;

public class BinanceOrderClient extends AbstractBinanceClient {

	private static final Logger LOG = Logger.getLogger(BinanceOrderClient.class);

	private static final int ORDER_BOOK_DEPTH = 5;


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

	private List<Order> convertToOrders(List<OrderBookEntry> orderBook) {
		List<Order> orders = new ArrayList<Order>();
		Date date = Calendar.getInstance().getTime();

		for(OrderBookEntry entry : orderBook) {
			orders.add(new Order(new BigDecimal((entry.getPrice())), new BigDecimal(entry.getQty()), date));
		}
		return orders;
	}

}