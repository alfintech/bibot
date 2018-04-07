package io.altanalytics.persistence.elastic;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.function.Consumer;

import javax.annotation.PostConstruct;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.altanalytics.domain.currency.CurrencyPair;
import io.altanalytics.domain.currency.IntervalPrice;
import io.altanalytics.persistence.Reader;

@Component
public class ElasticReader implements Reader {

	@Value("${elastic.cluster.name}")
	private String clusterName;
	
	@Value("${elastic.node.name}")
	private String nodeName;

	@Value("${elastic.host}")
	private String elasticHost;

	@Value("${elastic.port}")
	private int elasticPort;
	
	private TransportClient client;

	@PostConstruct
	public void initialise() throws IOException {

		Settings settings = Settings.builder()
				.put("cluster.name", clusterName)
				.put("node.name", nodeName)
				.build();

		client = new PreBuiltTransportClient(settings);
		client.addTransportAddress(new TransportAddress(InetAddress.getByName(elasticHost), elasticPort));
	}

	@Override
	public IntervalPrice getIntervalPrice(CurrencyPair currencyPair) throws Exception {
		
		SearchResponse response = client.prepareSearch("marketdata")
				.setTypes("minutely")
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				.setQuery(QueryBuilders.matchQuery("currencyPair", currencyPair.toString()))
				.addSort("openDate", SortOrder.DESC)
				.setSize(1)
				.get();

		SearchHit searchHit = response.getHits().getAt(0);
		return parseIntervalPrice(searchHit, currencyPair);
	}

	@Override
	public IntervalPrice getAllTimeHigh(CurrencyPair currencyPair) throws Exception {

		SearchResponse response = client.prepareSearch("marketdata")
				.setTypes("minutely")
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				.setQuery(QueryBuilders.matchQuery("currencyPair", currencyPair.toString()))
				.addSort("closePrice", SortOrder.DESC)
				.setSize(1)
				.get();

		SearchHit searchHit = response.getHits().getAt(0);
		return parseIntervalPrice(searchHit, currencyPair);
	}

	@Override
	public List<IntervalPrice> getIntervalPrices(Date fromDate, Date toDate, CurrencyPair currencyPair) throws Exception {

		List<IntervalPrice> intervalPrices = new ArrayList<IntervalPrice>();

		SearchResponse response = client.prepareSearch("marketdata")
				.setTypes("minutely")
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				.setQuery(QueryBuilders.matchQuery("currencyPair", currencyPair.toString()))   
				.setPostFilter(QueryBuilders.rangeQuery("openDate").from(fromDate.getTime()).to(toDate.getTime())) 
				.addSort("openDate", SortOrder.ASC)
				.setSize(1000)
				.get();

		response.getHits().forEach(new Consumer<SearchHit>() {

			@Override
			public void accept(SearchHit searchHit) {
				try {
					intervalPrices.add(parseIntervalPrice(searchHit, currencyPair));
				} catch(Exception e) {
					throw new RuntimeException(e);
				}
			}
		});

		return intervalPrices;
	}
	
	private IntervalPrice parseIntervalPrice(SearchHit searchHit, CurrencyPair currencyPair) throws ParseException {
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));

		Date openDate = df.parse((String) searchHit.getSourceAsMap().get("openDate"));
		Date closeDate = df.parse((String) searchHit.getSourceAsMap().get("closeDate"));
		Double openPrice = (Double) searchHit.getSourceAsMap().get("openPrice");
		Double lowPrice = (Double) searchHit.getSourceAsMap().get("lowPrice");
		Double highPrice = (Double) searchHit.getSourceAsMap().get("highPrice");
		Double closePrice = (Double) searchHit.getSourceAsMap().get("closePrice");
		Double intervalVolume = (Double) searchHit.getSourceAsMap().get("intervalVolume");
		Double dayVolume = (Double) searchHit.getSourceAsMap().get("dayVolume");
		
		return new IntervalPrice(currencyPair, openDate, closeDate, BigDecimal.valueOf(openPrice), 
				BigDecimal.valueOf(lowPrice), BigDecimal.valueOf(highPrice), BigDecimal.valueOf(closePrice),
				BigDecimal.valueOf(intervalVolume), BigDecimal.valueOf(dayVolume));
	}

}
