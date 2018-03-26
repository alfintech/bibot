package io.github.bibot.exchangeclient;

import io.github.bibot.domain.candleStick.CandleStick;
import io.github.bibot.domain.CurrencyPair;

import java.util.List;

public interface CandleStickClient {

    List<CandleStick> getCandleStickHistory(CurrencyPair pair);
    CandleStick getLatestCandleStick(CurrencyPair pair);
}
