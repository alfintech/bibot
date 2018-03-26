package io.github.bibot.domain.candleStick;

import io.github.bibot.domain.price.Price;

import java.util.Collections;
import java.util.LinkedList;

public class CandleStickTimeSeries {

    private static final int CAPACITY = 10000;
    private static final double MS_IN_SECOND = 1000;

    private LinkedList<CandleStick> candleSticksSeries = new LinkedList<>();

    public void addCandleStick(CandleStick candleStick) {
        if(candleSticksSeries.size() == CAPACITY) {
            candleSticksSeries.pop();
        }
        candleSticksSeries.push(candleStick);
        Collections.sort(candleSticksSeries);
    }
}
