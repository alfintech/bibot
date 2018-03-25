package io.github.bibot.exchangeclient;

import java.util.List;

import io.github.bibot.domain.CandleStick;
import io.github.bibot.domain.CurrencyPair;

public interface CandleStickClient {

    List<CandleStick> getCandleStickHistory(CurrencyPair pair);
    CandleStick getLatestCandleStick(CurrencyPair pair);
}
