package io.altanalytics.data.external.coinmarketcal.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.apache.logging.log4j.core.util.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.altanalytics.domain.calendar.CalendarEvent;

@Component
public class CoinMarketCalClient {

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
	private static final String REST_URL_TEMPLATE = "https://api.coinmarketcal.com/v1/events?access_token=%s";

	private static final String JSON_FIELD_ID = "id";
	private static final String JSON_FIELD_TITLE = "title";
	private static final String JSON_FIELD_DATA_COINS = "coins";
	private static final String JSON_FIELD_DATA_SYMBOL = "symbol";
	private static final String JSON_FIELD_DATA_EVENT_DATE = "date_event";
	private static final String JSON_FIELD_DATA_CREATED_DATE = "created_date";
	private static final String JSON_FIELD_DATA_DESCRIPTION = "description";
	private static final String JSON_FIELD_DATA_VOTE_COUNT = "vote_count";
	private static final String JSON_FIELD_DATA_POSITIVE_VOTE_COUNT = "positive_vote_count";
	private static final String JSON_FIELD_DATA_PERCENTAGE_POSITIVE = "percentage";
	
	@Value("${client.coinmarketcal.client.accesstoken}")
	private String accessToken;
	
	public List<CalendarEvent> fetch() throws Exception {

		String url = String.format(REST_URL_TEMPLATE, accessToken);
		URL httpsUrl = new URL(url);
		HttpsURLConnection httpsConnection = (HttpsURLConnection) httpsUrl.openConnection();
		InputStream inputStream = httpsConnection.getInputStream();
		String response = read(inputStream);
		inputStream.close();

		try {
			return parseResponse(response);
		} catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to parse response: " + response);
		}
	}

	private String read(InputStream inputStream) throws IOException {

		return IOUtils.toString(new InputStreamReader(inputStream));
	}

	@SuppressWarnings("unchecked")
	private List<CalendarEvent> parseResponse(String response) throws Exception {

		List<CalendarEvent> events = new ArrayList<CalendarEvent>();
		JSONParser jsonParser = new JSONParser();
		JSONArray jsonArray = (JSONArray) jsonParser.parse(response);

		for(Object uncastedResult : jsonArray.toArray(new JSONObject[]{})) {
			JSONObject result = (JSONObject) uncastedResult;
			Long eventId = (Long) result.get(JSON_FIELD_ID);
			Date eventDate = DATE_FORMAT.parse((String) result.get(JSON_FIELD_DATA_EVENT_DATE));
			Date createdDate = DATE_FORMAT.parse((String) result.get(JSON_FIELD_DATA_CREATED_DATE));
			String title = (String) result.get(JSON_FIELD_TITLE);
			String description = (String) result.get(JSON_FIELD_DATA_DESCRIPTION);
			Long voteCount = (Long) result.get(JSON_FIELD_DATA_VOTE_COUNT);
			Long positiveVoteCount = (Long) result.get(JSON_FIELD_DATA_POSITIVE_VOTE_COUNT);
			Long percentagePositive = (Long) result.get(JSON_FIELD_DATA_PERCENTAGE_POSITIVE);
			List<String> currencies = parseCurrencies(result);
			events.add(new CalendarEvent(eventId, currencies, title, description, eventDate, createdDate, voteCount, positiveVoteCount, percentagePositive));
		}
		return events;
	}
	
	@SuppressWarnings("unchecked")
	private List<String> parseCurrencies(JSONObject result) {
		JSONArray currencyJsonArray = (JSONArray) result.get(JSON_FIELD_DATA_COINS);
		
		List<String> currencies = new ArrayList<String>();
		
		for(Object uncastedCurrency : currencyJsonArray.toArray(new JSONObject[]{})) {
			JSONObject currency = (JSONObject) uncastedCurrency;
			String symbol = (String) currency.get(JSON_FIELD_DATA_SYMBOL);
			currencies.add(symbol);
		}
		return currencies;
	}

}
