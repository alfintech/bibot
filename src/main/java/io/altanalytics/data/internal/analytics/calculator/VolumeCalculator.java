package io.altanalytics.data.internal.analytics.calculator;

import java.math.BigDecimal;
import java.util.List;

import io.altanalytics.domain.currency.IntervalPrice;
import io.altanalytics.util.BigDecimalUtil;

public class VolumeCalculator {

	private static final long MS_IN_DAY = 24 * 60 * 60 * 1000;
	
	public static BigDecimal cumulative(List<IntervalPrice> marketDataSeries) {
		
		BigDecimal volume = new BigDecimal(0);
		
		for(IntervalPrice marketData : marketDataSeries) {
			volume = BigDecimalUtil.add(volume, marketData.getIntervalVolume());
		}
		
		return volume;
	}

	public static BigDecimal average(BigDecimal dayValue, int interval) {

		BigDecimal intervalDayFraction =  BigDecimalUtil.divide(new BigDecimal(interval), new BigDecimal(MS_IN_DAY));
		return BigDecimalUtil.multiply(dayValue, intervalDayFraction);
	}
	
}
