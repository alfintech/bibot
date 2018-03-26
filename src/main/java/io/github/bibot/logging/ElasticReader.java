package io.github.bibot.logging;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.function.Consumer;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
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

	public List<CandleStick> getCandleSticks(Date fromDate, Date toDate, CurrencyPair currencyPair) throws IOException {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));

		List<CandleStick> candleSticks = new ArrayList<CandleStick>();

		SearchResponse response = client.prepareSearch("candlestick")
				.setTypes("minutely")
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				.setQuery(QueryBuilders.matchQuery("currencyPair", currencyPair.toString()))   
				.setPostFilter(QueryBuilders.rangeQuery("openDate").from(fromDate.getTime()).to(toDate.getTime())) 
				.addSort("openDate", SortOrder.ASC)
				.get();

		response.getHits().forEach(new Consumer<SearchHit>() {

			@Override
			public void accept(SearchHit hit) {
				try {
					Date openDate = df.parse((String) hit.getSourceAsMap().get("openDate"));
					Date closeDate = df.parse((String) hit.getSourceAsMap().get("closeDate"));
					Double openPrice = (Double) hit.getSourceAsMap().get("openPrice");
					Double lowPrice = (Double) hit.getSourceAsMap().get("lowPrice");
					Double highPrice = (Double) hit.getSourceAsMap().get("highPrice");
					Double closePrice = (Double) hit.getSourceAsMap().get("closePrice");
					Double volume = (Double) hit.getSourceAsMap().get("volume");
					candleSticks.add(new CandleStick(openDate, closeDate, BigDecimal.valueOf(openPrice), BigDecimal.valueOf(lowPrice), BigDecimal.valueOf(highPrice), BigDecimal.valueOf(closePrice), BigDecimal.valueOf(volume)));
				} catch(Exception e) {
					throw new RuntimeException(e);
				}
			}
		});

		return candleSticks;
	}

	public void close() {
		client.close();
	}

}
