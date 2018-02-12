package io.github.bibot.priceanalysis;

import java.math.BigDecimal;

import io.github.bibot.domain.price.Price;
import io.github.bibot.domain.price.PriceTimeSeries;

public class PriceAnalysis {

	public static BigDecimal getPercentageChange(PriceTimeSeries prices) {

		Price priceAtEndOfWindow = prices.getSeries().getLast();
		Price priceAtStartOfWindow = prices.getSeries().getFirst();

		BigDecimal priceDelta = BigDecimalOperations.subtract(priceAtEndOfWindow.price, priceAtStartOfWindow.price);

		return BigDecimalOperations.multiply(BigDecimalOperations.divide(priceDelta, priceAtStartOfWindow.price), new BigDecimal(100));
	}

	public static BigDecimal getAverage(PriceTimeSeries prices) {

		long count = prices.getSeries().stream().count();
		BigDecimal total = new BigDecimal(prices.getSeries().stream().mapToDouble(price -> price.price.doubleValue()).sum());

		return BigDecimalOperations.divide(total, new BigDecimal(count));
	}

}
