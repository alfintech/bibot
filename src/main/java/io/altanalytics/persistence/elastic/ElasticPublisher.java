package io.altanalytics.persistence.elastic;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.altanalytics.domain.calendar.CalendarEvent;
import io.altanalytics.domain.currency.IntervalPrice;
import io.altanalytics.domain.market.MarketCap;
import io.altanalytics.persistence.Publisher;
import io.altanalytics.domain.currency.Analytic;
import io.altanalytics.util.BigDecimalUtil;

@Component
public class ElasticPublisher implements Publisher {

	private static final Logger LOG = LoggerFactory.getLogger(ElasticPublisher.class);

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
	public void publishMarketData(List<IntervalPrice> marketDataList) throws IOException {

		BulkRequestBuilder bulkRequest = client.prepareBulk();

		for(IntervalPrice marketData : marketDataList) {
			System.out.println(marketData);
			XContentBuilder builder = XContentFactory.jsonBuilder()
					.startObject()
					.field("currencyPair", marketData.getCurrencyPair().toString())
					.field("openDate", marketData.getOpenTime())
					.field("closeDate", marketData.getCloseTime())
					.field("openPrice", BigDecimalUtil.toDouble(marketData.getOpen()))
					.field("closePrice", BigDecimalUtil.toDouble(marketData.getClose()))
					.field("highPrice", BigDecimalUtil.toDouble(marketData.getHigh()))
					.field("lowPrice", BigDecimalUtil.toDouble(marketData.getLow()))
					.field("intervalVolume", BigDecimalUtil.toDouble(marketData.getIntervalVolume()))
					.field("dayVolume", BigDecimalUtil.toDouble(marketData.getDayVolume()))
					.endObject();

			bulkRequest.add(client.prepareIndex("marketdata", "minutely", marketData.getCurrencyPair().toString()+marketData.getOpenTime().getTime()).setSource(builder));
		}

		BulkResponse response = bulkRequest.get();
		LOG.debug(response.toString());
	}

	@Override
	public void publishAnalytics(List<Analytic> analytics) throws IOException {

		BulkRequestBuilder bulkRequest = client.prepareBulk();

		for(Analytic analytic : analytics) {
			System.out.println(analytic);

			XContentBuilder builder = XContentFactory.jsonBuilder()
					.startObject()
					.field("currencyPair", analytic.getCurrencyPair().toString())
					.field("intervalVolume", BigDecimalUtil.toDouble(analytic.getIntervalVolume()))
					.field("dayVolume", BigDecimalUtil.toDouble(analytic.getDayAverage()))
					.field("percentageVolume", BigDecimalUtil.toDouble(analytic.getPercentageVolume()))
					.field("percentageAllTimeHigh", BigDecimalUtil.toDouble(analytic.getPercentageAllTimeHigh()))
					.field("date", analytic.getDate())
					.endObject();

			bulkRequest.add(client.prepareIndex("marketdataanalytic", "minutely", analytic.getCurrencyPair().toString()+analytic.getDate().getTime()).setSource(builder));
		}

		BulkResponse response = bulkRequest.get();
		LOG.debug(response.toString());
	}

	@Override
	public void publishEvents(List<CalendarEvent> calendarEvents) throws IOException {
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		Date currentTime = Calendar.getInstance().getTime();

		for(CalendarEvent calendarEvent : calendarEvents) {
			System.out.println(calendarEvent);
			for(String currency : calendarEvent.getCurrencies()) {
				XContentBuilder builder = XContentFactory.jsonBuilder()
						.startObject()
						.field("eventId", calendarEvent.getEventId())
						.field("currency", currency)
						.field("title", calendarEvent.getTitle())
						.field("eventDate", calendarEvent.getEventDate())
						.field("createdDate", calendarEvent.getCreatedDate())
						.field("voteCount", calendarEvent.getVoteCount())
						.field("positiveVoteCount", calendarEvent.getPositiveVoteCount())
						.field("percentagePositiveVoteCount", calendarEvent.getPercentagePositiveVoteCount())
						.field("insertionDate", currentTime)
						.endObject();

				String idAsString = new Long(calendarEvent.getEventId()).toString();
				bulkRequest.add(client.prepareIndex("calendarevents", "minutely", idAsString).setSource(builder));
			}
		}

		BulkResponse response = bulkRequest.get();
		LOG.debug(response.toString());		
	}

	@Override
	public void publishMarketCap(MarketCap marketCap) throws IOException {

		System.out.println(marketCap);
		XContentBuilder builder = XContentFactory.jsonBuilder()
				.startObject()
				.field("activeCurrencies", marketCap.getActiveCurrencies())
				.field("date", marketCap.getDate())
				.field("btcDominance", BigDecimalUtil.toDouble(marketCap.getBtcDominance()))
				.field("dayVolume", BigDecimalUtil.toDouble(marketCap.getDayVolume()))
				.field("marketCap", BigDecimalUtil.toDouble(marketCap.getMarketCap()))
				.endObject();

		String idAsString = new Long(marketCap.getDate().getTime()).toString();
		
		IndexResponse response = client.prepareIndex("marketcap", "minutely", idAsString)
		        .setSource(builder)
		        .get();
		
		LOG.debug(response.toString());
	}

}