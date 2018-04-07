package io.altanalytics.data.external.cryptocompare.client;

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
import io.altanalytics.domain.currency.CurrencyPair;
import io.altanalytics.domain.currency.IntervalPrice;
import io.altanalytics.domain.currency.IntervalPriceRequest;

@Component
public class CryptoCompareLiveClient implements CryptoCompareClient {

	@Autowired
	public HttpConnectionManager connectionManager;

	private static final String REST_URL_TEMPLATE = "https://min-api.cryptocompare.com/data/pricemultifull?fsyms=%s&tsyms=%s&extraparams=%s";

	private static final String JSON_FIELD_DATA = "RAW";
	private static final String JSON_FIELD_DATA_DAY_VOLUME = "VOLUME24HOUR";
	private static final String JSON_FIELD_DATA_PRICE = "PRICE";
	private static final String SITE_ID = "altanalyticsio";

	public IntervalPrice fetch(IntervalPriceRequest request) throws Exception {

		String requestURL = String.format(REST_URL_TEMPLATE, request.getCurrencyPair().tradeCurrency, request.getCurrencyPair().baseCurrency, SITE_ID);
		HttpGet httpRequest = new HttpGet(requestURL);
		
		CloseableHttpClient httpClient = connectionManager.getHttpConnection();
		CloseableHttpResponse httpResponse = httpClient.execute(httpRequest);
		String response = read(httpResponse);
		httpResponse.close();

		try {
			return parseResponse(request.getCurrencyPair(), response);
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

	private IntervalPrice parseResponse(CurrencyPair currencyPair, String response) throws IOException, ParseException {

		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(response);

		JSONObject data = (JSONObject) jsonObject.get(JSON_FIELD_DATA);
		JSONObject nestedtradeCurrencyRecord =  (JSONObject) data.get(currencyPair.tradeCurrency);
		JSONObject nestedBaseCurrencyRecord =  (JSONObject) nestedtradeCurrencyRecord.get(currencyPair.baseCurrency);

		Double volume = parseNumber(nestedBaseCurrencyRecord.get(JSON_FIELD_DATA_DAY_VOLUME)); 
		Double price = parseNumber(nestedBaseCurrencyRecord.get(JSON_FIELD_DATA_PRICE));
		Date date = Calendar.getInstance().getTime();

		return new IntervalPrice(currencyPair, date, date, new BigDecimal(price), new BigDecimal(price), new BigDecimal(price), new BigDecimal(price), new BigDecimal(volume), new BigDecimal(volume));
	}

	private Double parseNumber(Object number) {
		if(number instanceof Double) {
			return (Double) number;	
		} else {
			return new Double((Long) number);
		}
	}

}
