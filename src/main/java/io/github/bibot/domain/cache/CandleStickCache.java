package io.github.bibot.domain.cache;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.domain.market.Candlestick;
import com.binance.api.client.domain.market.CandlestickInterval;
import io.github.bibot.domain.CurrencyPair;
import io.github.bibot.strategy.Strategy;
import org.springframework.beans.factory.annotation.Value;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Illustrates how to use the klines/candlesticks event stream to create a local cache of bids/asks for a symbol.
 */
public class CandleStickCache {

    /**
     * Key is the start/open time of the candle, and the value contains candlestick date.
     */
    private LinkedList<Candlestick> candlesticksCache;

//    private List<Strategy>


    private final int cacheSize = 10000;

    public CandleStickCache(String symbol, CandlestickInterval interval) {
        initializeCandlestickCache(symbol, interval);
//        startCandlestickEventStreaming(symbol, interval);
    }

    /**
     * Initializes the candlestick cache by using the REST API.
     */
    private void initializeCandlestickCache(String symbol, CandlestickInterval interval) {
        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance();
        BinanceApiRestClient client = factory.newRestClient();
        List<Candlestick> candlestickBars = client.getCandlestickBars(symbol.toUpperCase(), interval);



        this.candlesticksCache = new LinkedList<>();
        for (Candlestick candlestickBar : candlestickBars) {
            candlesticksCache.add(candlestickBar);
        }
    }

    /**
     * Begins streaming of depth events.
     */
    private void startCandlestickEventStreaming(CurrencyPair currencyPair, CandlestickInterval interval) {
        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance();
        BinanceApiWebSocketClient client = factory.newWebSocketClient();


        client.onCandlestickEvent(currencyPair.toString(), interval, response -> {
            Long openTime = response.getOpenTime();

            Candlestick updateCandlestick = candlesticksCache.getLast();
            if (updateCandlestick == null) {
                // new candlestick
                updateCandlestick = new Candlestick();
            }

            if(openTime.equals(updateCandlestick.getOpenTime())){
                candlesticksCache.removeLast();
            }

            // update candlestick with the stream data
            updateCandlestick.setOpenTime(response.getOpenTime());
            updateCandlestick.setOpen(response.getOpen());
            updateCandlestick.setLow(response.getLow());
            updateCandlestick.setHigh(response.getHigh());
            updateCandlestick.setClose(response.getClose());
            updateCandlestick.setCloseTime(response.getCloseTime());
            updateCandlestick.setVolume(response.getVolume());
            updateCandlestick.setNumberOfTrades(response.getNumberOfTrades());
            updateCandlestick.setQuoteAssetVolume(response.getQuoteAssetVolume());
            updateCandlestick.setTakerBuyQuoteAssetVolume(response.getTakerBuyQuoteAssetVolume());
            updateCandlestick.setTakerBuyBaseAssetVolume(response.getTakerBuyQuoteAssetVolume());

            // Store the updated candlestick in the cache
            candlesticksCache.add(updateCandlestick);
        });
    }

//    private void updateCache( Candlestick)

    /**
     * @return a klines/candlestick cache, containing the open/start time of the candlestick as the key,
     * and the candlestick data as the value.
     */
    public List<Candlestick> getCandlesticksCache() {
        return candlesticksCache;
    }
}