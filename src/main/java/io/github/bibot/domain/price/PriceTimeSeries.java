package io.github.bibot.domain.price;

import java.util.Collections;
import java.util.LinkedList;

public class PriceTimeSeries {
	
	private static final int CAPACITY = 10000;
	private static final double MS_IN_SECOND = 1000;
	
	private LinkedList<Price> priceSeries = new LinkedList<>();
	
	public void addPrice(Price price) {
		if(priceSeries.size() == CAPACITY) {
			priceSeries.pop();
		}
		priceSeries.push(price);
		Collections.sort(priceSeries);
	}
	
	/**
	 * Return list. First element is the most recent price.
	 */
	public LinkedList<Price> getSeries() {
		return priceSeries;
	}

	public double getSeriesDuration() {
		
		long startDate = priceSeries.getFirst().datetime.getTime();
		long endDate = priceSeries.getLast().datetime.getTime();
		double millisecondsElapsed = endDate - startDate;
		
		return millisecondsElapsed/MS_IN_SECOND;
	}
	
}
