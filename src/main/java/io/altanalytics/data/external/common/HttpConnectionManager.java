package io.altanalytics.data.external.common;

import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HttpConnectionManager {

	@Value("${http.connections.route.max}")
	private int routeMax;
	
	@Value("${http.connections.total.max}")
	private int totalMax;
	
	private HttpClientBuilder pooledHttpClientBuilder;	
	
	public HttpConnectionManager() {
		HttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
		this.pooledHttpClientBuilder = HttpClients.custom().setConnectionManager(connectionManager);
		this.pooledHttpClientBuilder.setMaxConnPerRoute(routeMax);
		this.pooledHttpClientBuilder.setMaxConnTotal(totalMax);
	}

	public CloseableHttpClient getHttpConnection() {
		return pooledHttpClientBuilder.build();
	}
}
