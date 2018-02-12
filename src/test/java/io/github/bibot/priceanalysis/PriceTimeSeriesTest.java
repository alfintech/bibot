package io.github.bibot.priceanalysis;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import io.github.bibot.domain.price.Price;
import io.github.bibot.domain.price.PriceTimeSeries;
import io.github.bibot.priceanalysis.PriceAnalysis;
import io.github.bibot.priceanalysis.WindowAnalysis;

public class PriceTimeSeriesTest {
	
	@Test
	public void testPositivePercentageChange() {
		
		PriceTimeSeries prices = new PriceTimeSeries();
		Date arbitaryTime = Calendar.getInstance().getTime();
		
		prices.addPrice(new Price(new BigDecimal(100), arbitaryTime));
		prices.addPrice(new Price(new BigDecimal(200), arbitaryTime));
		prices.addPrice(new Price(new BigDecimal(300), arbitaryTime));
		prices.addPrice(new Price(new BigDecimal(400), arbitaryTime));
		
		BigDecimal percentageChange = PriceAnalysis.getPercentageChange(prices);
		
		Assert.assertEquals(300d, percentageChange.doubleValue(), 0);
	}
	
	@Test
	public void testNoPercentageChange() {
		
		PriceTimeSeries prices = new PriceTimeSeries();
		Date arbitaryTime = Calendar.getInstance().getTime();
		
		prices.addPrice(new Price(new BigDecimal(100), arbitaryTime));
		prices.addPrice(new Price(new BigDecimal(200), arbitaryTime));
		prices.addPrice(new Price(new BigDecimal(300), arbitaryTime));
		prices.addPrice(new Price(new BigDecimal(100), arbitaryTime));
		
		BigDecimal percentageChange = PriceAnalysis.getPercentageChange(prices);
		
		Assert.assertEquals(0d, percentageChange.doubleValue(), 0);
	}
	
	@Test
	public void testNegativePercentageChange() {
		
		PriceTimeSeries prices = new PriceTimeSeries();
		Date arbitaryTime = Calendar.getInstance().getTime();
		
		prices.addPrice(new Price(new BigDecimal(400), arbitaryTime));
		prices.addPrice(new Price(new BigDecimal(300), arbitaryTime));
		prices.addPrice(new Price(new BigDecimal(200), arbitaryTime));
		prices.addPrice(new Price(new BigDecimal(100), arbitaryTime));
		
		BigDecimal percentageChange = PriceAnalysis.getPercentageChange(prices);
		
		Assert.assertEquals(-75d, percentageChange.doubleValue(), 0);
	}
	
	@Test
	public void testAverage() {
		
		PriceTimeSeries prices = new PriceTimeSeries();
		Date arbitaryTime = Calendar.getInstance().getTime();
		
		prices.addPrice(new Price(new BigDecimal(100), arbitaryTime));
		prices.addPrice(new Price(new BigDecimal(200), arbitaryTime));
		prices.addPrice(new Price(new BigDecimal(300), arbitaryTime));
		prices.addPrice(new Price(new BigDecimal(400), arbitaryTime));
		
		BigDecimal average = PriceAnalysis.getAverage(prices);
		
		Assert.assertEquals(250d, average.doubleValue(), 0);
	}
	
	@Test
	public void testTimeWindow() {
		
		PriceTimeSeries prices = new PriceTimeSeries();
		Date arbitaryTime = Calendar.getInstance().getTime();
		
		prices.addPrice(new Price(new BigDecimal(100), arbitaryTime));
		prices.addPrice(new Price(new BigDecimal(200), arbitaryTime));
		prices.addPrice(new Price(new BigDecimal(300), arbitaryTime));
		prices.addPrice(new Price(new BigDecimal(400), arbitaryTime));
		
		PriceTimeSeries priceWindow = WindowAnalysis.getTimeWindow(prices, 3);

		Assert.assertEquals(3, priceWindow.getSeries().size());
		Assert.assertEquals(200d, priceWindow.getSeries().getFirst().price.doubleValue(), 0);
		Assert.assertEquals(400d, priceWindow.getSeries().getLast().price.doubleValue(), 0);
	}
}
