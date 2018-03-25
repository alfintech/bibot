package io.github.bibot.logging;

import java.io.IOException;
import java.net.InetAddress;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.bibot.domain.CandleStick;
import io.github.bibot.domain.CurrencyPair;
import io.github.bibot.domain.price.Price;

public class ElasticPublisher {

	private static final Logger LOG = LoggerFactory.getLogger(ElasticPublisher.class);

	private TransportClient client;
	
	public ElasticPublisher(String host, int port) throws IOException {

		Settings settings = Settings.builder()
				.put("cluster.name", "elasticsearch")
				.put("node.name", "node-1")
				.build();
		
		client = new PreBuiltTransportClient(settings);
		client.addTransportAddress(new TransportAddress(InetAddress.getByName(host), port));
	}
	
	public void publish(CurrencyPair currencyPair, Price price) throws IOException {

		XContentBuilder builder = XContentFactory.jsonBuilder()
				.startObject()
				.field("currencyPair", currencyPair.toString())
				.field("date", price.datetime)
				.field("price", price.price)
				.endObject();
		
		IndexResponse response = client.prepareIndex("raw", "price", currencyPair.toString()+price.datetime.getTime()).setSource(builder).get();
		LOG.debug(response.toString());
	}

	public void publish(CurrencyPair currencyPair, CandleStick candleStick) throws IOException {

		XContentBuilder builder = XContentFactory.jsonBuilder()
				.startObject()
				.field("currencyPair", currencyPair.toString())
				.field("openDate", candleStick.getOpenTime())
				.field("closeDate", candleStick.getCloseTime())
				.field("openPrice", candleStick.getOpen())
				.field("closePrice", candleStick.getClose())
				.field("highPrice", candleStick.getHigh())
				.field("lowPrice", candleStick.getLow())
				.field("volume", candleStick.getVolume())
				.endObject();
		
		IndexResponse response = client.prepareIndex("candlestick", "minutely", currencyPair.toString()+candleStick.getOpenTime().getTime()).setSource(builder).get();
		LOG.debug(response.toString());
	}

	public void close() {
		client.close();
	}

}
