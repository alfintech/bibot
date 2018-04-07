package io.altanalytics.data.external.cryptocompare.client;

import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.logging.log4j.core.util.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.util.concurrent.RateLimiter;

import io.altanalytics.data.external.common.HttpConnectionManager;
import io.altanalytics.domain.currency.CurrencyPair;
import io.altanalytics.domain.currency.IntervalPrice;
import io.altanalytics.domain.currency.IntervalPriceRequest;

@Component
public class CryptoCompareHistoricClient implements CryptoCompareClient {

	@Autowired
	public HttpConnectionManager connectionManager;

	
	@Value("${http.throttle.ccpastmarketdata}")
	private int throttleRate;

	private static final String REST_URL_TEMPLATE = "https://min-api.cryptocompare.com/data/histohour?fsym=%s&tsym=%s&limit=1&aggregate=1&toTs=%d&extraParams=%s";

	private static final String JSON_FIELD_DATA = "Data";
	private static final String JSON_FIELD_DATA_HIGH = "high";
	private static final String JSON_FIELD_DATA_LOW = "low";
	private static final String JSON_FIELD_DATA_VOLUME = "volumeto";
	private static final String JSON_FIELD_DATA_OPEN = "open";
	private static final String JSON_FIELD_DATA_CLOSE = "close";
	private static final String JSON_FIELD_DATA_OPEN_TIME = "TimeFrom";
	private static final String JSON_FIELD_DATA_CLOSE_TIME = "TimeTo";
	private static final String SITE_ID = "altanalyticsio";

	private static final long MS_IN_SEC = 1000;
	
	private RateLimiter rateLimiter;

	@PostConstruct
	public void initialise() {
		rateLimiter = RateLimiter.create(throttleRate);
	}
	
	public IntervalPrice fetch(IntervalPriceRequest request) throws Exception {

		rateLimiter.acquire();
		String requestURL = String.format(REST_URL_TEMPLATE, request.getCurrencyPair().tradeCurrency, request.getCurrencyPair().baseCurrency, convertToEpochSeconds(request.getDate()), SITE_ID);
		HttpGet httpRequest = new HttpGet(requestURL);
		
		CloseableHttpClient httpClient = connectionManager.getHttpConnection();
		CloseableHttpResponse httpResponse = httpClient.execute(httpRequest);
		String response = read(httpResponse);
		httpResponse.close();

		try {
			return parseResponse(request.getCurrencyPair(), response);
		} catch(Exception e) {
			throw new RuntimeException("Failed to parse response: " + response);
		}
	}

	private String read(HttpResponse response) throws IOException {
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + read(response));
		}

		return IOUtils.toString(new InputStreamReader(response.getEntity().getContent()));
	}

	private long convertToEpochSeconds(Date date) {
		return date.getTime() / 1000;
	}

	private IntervalPrice parseResponse(CurrencyPair currencyPair, String response) throws IOException, ParseException {

		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(response);

		JSONArray data = (JSONArray) jsonObject.get(JSON_FIELD_DATA);
		Long closeEpochTime = ((Long) jsonObject.get(JSON_FIELD_DATA_CLOSE_TIME)) * MS_IN_SEC;
		Long openEpochTime = ((Long) jsonObject.get(JSON_FIELD_DATA_OPEN_TIME)) * MS_IN_SEC;

		JSONObject dataRecord =  (JSONObject) data.get(data.size()==0 ? 0 : 1);

		Double high = parseNumber(dataRecord.get(JSON_FIELD_DATA_HIGH));
		Double low = parseNumber(dataRecord.get(JSON_FIELD_DATA_LOW));
		Double volume = parseNumber(dataRecord.get(JSON_FIELD_DATA_VOLUME)); 
		Double close = parseNumber(dataRecord.get(JSON_FIELD_DATA_CLOSE));
		Double open = parseNumber(dataRecord.get(JSON_FIELD_DATA_OPEN));

		return new IntervalPrice(currencyPair, new Date(openEpochTime), new Date(closeEpochTime), new BigDecimal(open), new BigDecimal(low), new BigDecimal(high), new BigDecimal(close), new BigDecimal(volume), new BigDecimal(volume));
	}

	private Double parseNumber(Object number) {
		if(number instanceof Double) {
			return (Double) number;	
		} else {
			return new Double((Long) number);
		}
	}

}
