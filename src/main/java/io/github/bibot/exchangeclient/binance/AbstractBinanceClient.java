package io.github.bibot.exchangeclient.binance;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import io.github.bibot.domain.credentials.APICredentials;

public abstract class AbstractBinanceClient {

    BinanceApiRestClient client;

    public AbstractBinanceClient(APICredentials credentials){
        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance(credentials.key, credentials.secret);
        this.client = factory.newRestClient();
    }
}
