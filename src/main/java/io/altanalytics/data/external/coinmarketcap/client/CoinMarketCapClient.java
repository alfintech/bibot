package io.altanalytics.data.external.coinmarketcap.client;

import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.logging.log4j.core.util.IOUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.altanalytics.data.external.common.HttpConnectionManager;
import io.altanalytics.domain.market.MarketCap;

@Component
public class CoinMarketCapClient {

	@Autowired
	public HttpConnectionManager connectionManager;

	private static final String REST_URL = "https://api.coinmarketcap.com/v1/global/";

	private static final String JSON_FIELD_MARKET_CAP = "total_market_cap_usd";
	private static final String JSON_FIELD_DAY_VOLUME = "total_24h_volume_usd";
	private static final String JSON_FIELD_BTC_DOMINANCE = "bitcoin_percentage_of_market_cap";
	private static final String JSON_FIELD_ACTIVE_CURRENCIES = "active_currencies";

	public MarketCap fetch() throws Exception {

		HttpGet httpRequest = new HttpGet(REST_URL);
		
		CloseableHttpClient httpClient = connectionManager.getHttpConnection();
		CloseableHttpResponse httpResponse = httpClient.execute(httpRequest);
		String response = read(httpResponse);
		httpResponse.close();

		try {
			return parseResponse(response);
		} catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to parse response: " + response);
		}
	}

	private String read(HttpResponse response) throws IOException {
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + read(response));
		}

		return IOUtils.toString(new InputStreamReader(response.getEntity().getContent()));
	}

	private MarketCap parseResponse(String response) throws IOException, ParseException {

		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(response);

		Double marketCap = (Double) parseNumber(jsonObject.get(JSON_FIELD_MARKET_CAP));
		Double volume = (Double) parseNumber(jsonObject.get(JSON_FIELD_DAY_VOLUME));
		Double btcDominance = (Double) parseNumber(jsonObject.get(JSON_FIELD_BTC_DOMINANCE));
		Long activeCurrencies = (Long) jsonObject.get(JSON_FIELD_ACTIVE_CURRENCIES);
		Date date = Calendar.getInstance().getTime();

		return new MarketCap(new BigDecimal(marketCap), new BigDecimal(volume), new BigDecimal(btcDominance), activeCurrencies, date);
	}

	private Double parseNumber(Object number) {
		if(number instanceof Double) {
			return (Double) number;	
		} else {
			return new Double((Long) number);
		}
	}

}
