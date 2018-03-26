package io.github.bibot.exchangeclient.binance;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import com.binance.api.client.BinanceApiRestClient;

import io.github.bibot.domain.CurrencyPair;
import io.github.bibot.domain.credentials.APICredentials;
import io.github.bibot.domain.price.Price;
import io.github.bibot.exchangeclient.PriceClient;

public class BinancePriceClient extends AbstractBinanceClient implements PriceClient {

	private static final Logger LOG = Logger.getLogger(BinancePriceClient.class);

	public BinancePriceClient(APICredentials credentials) {
		super(credentials);
	}

	public synchronized Price latestPrice(CurrencyPair pair) {
		BigDecimal price =  new BigDecimal(client.get24HrPriceStatistics(pair.toString()).getLastPrice());
		Date date = Calendar.getInstance().getTime();
		return new Price(price, date);
	}

}
