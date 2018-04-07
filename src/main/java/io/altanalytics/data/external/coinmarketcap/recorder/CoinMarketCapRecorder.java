package io.altanalytics.data.external.coinmarketcap.recorder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import io.altanalytics.data.external.coinmarketcap.client.CoinMarketCapClient;
import io.altanalytics.domain.market.MarketCap;
import io.altanalytics.persistence.Publisher;

@EnableScheduling
@Component
public class CoinMarketCapRecorder {

	@Value("${recorder.marketcap.active}")
	private boolean active;

	@Autowired
	public CoinMarketCapClient client;
	
	@Autowired
	protected Publisher publisher;

	@Scheduled(cron = "${recorder.marketcap.schedule}")
	public void tick() throws Exception {
		if(active) {
			MarketCap marketCap = client.fetch();
			publisher.publishMarketCap(marketCap);
		}
	}

}
