package io.github.bibot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import io.github.bibot.bot.RecorderBot;
import io.github.bibot.domain.CurrencyPair;
import io.github.bibot.domain.credentials.APICredentials;
import io.github.bibot.exchangeclient.binance.BinanceOrderClient;
import io.github.bibot.exchangeclient.binance.BinancePriceClient;

@EnableScheduling
@SpringBootApplication
public class Application {

	private List<RecorderBot> bots;

	@Value("${API_KEY}")
	private String apiKey;

	@Value("${API_SECRET}")
	private String apiSecret;

	@Value("${BASE_CURRENCY}")
	private String baseCurrency;

	@Value("${TRADE_CURRENCIES}")
	private String[] tradeCurrencies;

	@Value("${TRADE}")
	private boolean trade;

	@Value("${RECORD}")
	private boolean record;

	@PostConstruct
	public void init() {
		bots = new ArrayList<RecorderBot>();

		for(String tradeCurrency : tradeCurrencies) {
			if(record) {
				BinancePriceClient priceClient = new BinancePriceClient(new APICredentials(apiKey, apiSecret));
				BinanceOrderClient orderClient = new BinanceOrderClient(new APICredentials(apiKey, apiSecret));
				RecorderBot bot = new RecorderBot(new CurrencyPair("BTC", tradeCurrency), priceClient, orderClient);
				bots.add(bot);
			}
			if(trade) {
				//TODO
			}
		}
	}

	@Scheduled(fixedRateString = "${TICK_INTERVAL}")
	public void tick() {
		for(RecorderBot bot : bots) {
			bot.tick();
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class);
	}
}