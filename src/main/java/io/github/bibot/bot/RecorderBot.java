package io.github.bibot.bot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import io.github.bibot.domain.candleStick.CandleStick;
import io.github.bibot.exchangeclient.CandleStickClient;
import io.github.bibot.exchangeclient.OrderClient;
import io.github.bibot.exchangeclient.PriceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.bibot.domain.CurrencyPair;
import io.github.bibot.domain.Order;
import io.github.bibot.domain.price.Price;


public class RecorderBot {

	private static final Logger LOG = LoggerFactory.getLogger(RecorderBot.class);
	private static final DateFormat DF = new SimpleDateFormat("ddMMyyyy HH:mm:ss");
	
	private CurrencyPair currencyPair;
	private PriceClient priceClient;
	private OrderClient orderClient;
	private CandleStickClient candleStickClient;
	
	public RecorderBot(CurrencyPair currencyPair, PriceClient priceClient, OrderClient orderClient, CandleStickClient candleStickClient) {
		this.currencyPair = currencyPair;
		this.priceClient = priceClient;
		this.orderClient = orderClient;
		this.candleStickClient = candleStickClient;

	}

	public void tick() {
		tickAsync();
	}

	private void tickAsync() {
		Runnable priceReader = new Runnable() {
			@Override
			public void run() {
				logPrice(priceClient.latestPrice(currencyPair));
				logAsks(orderClient.getAsks(currencyPair));
				logBids(orderClient.getBids(currencyPair));
				logCandleSticks(candleStickClient.getLatestCandleStick(currencyPair));
			}
		};	
		
		Thread thread = new Thread(priceReader);
		thread.start();
	}

	private void logPrice(Price price) {
		LOG.info(currencyPair.toString() +"\t"+ DF.format(price.datetime) +"\t"+ price.price.toString());
	}

	private void logAsks(List<Order> asks) {
		for(Order ask : asks) {
			LOG.info(currencyPair.toString() +"\tASK\t"+ DF.format(ask.datetime) +"\t"+ ask.price.toString() +"\t"+ ask.quantity.doubleValue());			
		}
	}

	private void logBids(List<Order> bids) {
		for(Order bid : bids) {
			LOG.info(currencyPair.toString() +"\tBID\t"+ DF.format(bid.datetime) +"\t"+ bid.price.toString() +"\t"+ bid.quantity.doubleValue());
		}
	}

	private void logCandleSticks(CandleStick candleStick){
			//TODO
			//Fill in logging code
	}
}
