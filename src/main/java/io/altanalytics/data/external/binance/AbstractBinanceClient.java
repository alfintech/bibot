package io.altanalytics.data.external.binance;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;

public abstract class AbstractBinanceClient {

    protected BinanceApiRestClient client;

    public AbstractBinanceClient(APICredentials credentials){
        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance(credentials.key, credentials.secret);
        this.client = factory.newRestClient();
    }
}
