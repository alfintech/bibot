package io.github.bibot.priceanalysis;

import java.util.Calendar;
import java.util.Date;

import io.github.bibot.domain.price.Price;
import io.github.bibot.domain.price.PriceTimeSeries;

public class WindowAnalysis {

	public static PriceTimeSeries getTimeWindow(PriceTimeSeries prices, int secondsDuration) {

		Date lastTime = prices.getSeries().getLast().datetime;
		Calendar windowStartTime = Calendar.getInstance();
		windowStartTime.setTime(lastTime);
		windowStartTime.add(Calendar.SECOND, Math.negateExact(secondsDuration));
				
		PriceTimeSeries pricesWindow = new PriceTimeSeries();

		for(Price price : prices.getSeries()) {
			if(price.datetime.after(windowStartTime.getTime())) {
				pricesWindow.addPrice(price);
			}
		}

		return pricesWindow;
	}
}
