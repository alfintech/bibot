package io.altanalytics.domain.currency;

import java.util.Collections;
import java.util.LinkedList;

public class PriceTimeSeries {

    private static final int CAPACITY = 10000;
	private static final double MS_IN_SECOND = 1000;


    private LinkedList<IntervalPrice> series = new LinkedList<>();

    public void addSeriesPoint(IntervalPrice intervalPrice) {
        if(series.size() == CAPACITY) {
            series.pop();
        }
        series.push(intervalPrice);
        Collections.sort(series);
    }
    
	public double getSeriesDuration() {
		
		long startDate = series.getFirst().getCloseTime().getTime();
		long endDate = series.getLast().getCloseTime().getTime();
		double millisecondsElapsed = endDate - startDate;
		
		return millisecondsElapsed/MS_IN_SECOND;
	}

	public LinkedList<IntervalPrice> getSeries() {
		return series;
	}
	
}
