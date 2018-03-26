package io.github.bibot.logging;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.bibot.domain.CurrencyPair;
import io.github.bibot.domain.candleStick.CandleStick;

public class ElasticReader {

	private static final Logger LOG = LoggerFactory.getLogger(ElasticReader.class);

	private TransportClient client;
	
	public ElasticReader(String host, int port) throws IOException {

		Settings settings = Settings.builder()
				.put("cluster.name", "elasticsearch")
				.put("node.name", "node-1")
				.build();
		
		client = new PreBuiltTransportClient(settings);
		client.addTransportAddress(new TransportAddress(InetAddress.getByName(host), port));
	}
	
	public List<CandleStick> getCandleSticks(Date fromDate, Date toDate, CurrencyPair pair) throws IOException {
		return Collections.emptyList();
	}
	
	public void close() {
		client.close();
	}

}
