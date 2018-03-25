package io.github.bibot.exchangeclient;

import io.github.bibot.domain.CandleStick;
import io.github.bibot.domain.CurrencyPair;
import com.binance.api.client.domain.market.Candlestick;

import java.util.List;

public interface CandleStickClient {

    List<CandleStick> getCandleStickHistory(CurrencyPair pair);
    CandleStick getLatestCandleStick(CurrencyPair pair);
}
