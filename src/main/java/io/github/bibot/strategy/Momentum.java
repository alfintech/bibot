package io.github.bibot.strategy;

import java.math.BigDecimal;

import io.github.bibot.domain.price.Price;
import io.github.bibot.domain.price.PriceTimeSeries;
import io.github.bibot.priceanalysis.BigDecimalOperations;
import io.github.bibot.priceanalysis.PriceAnalysis;
import io.github.bibot.priceanalysis.WindowAnalysis;

/**
 * Pump rider looks to join a rising coin waiting for n subsequent rises
 * in order to ride for a predefined number of rises.
 */
public class Momentum extends Strategy {
	
	private BigDecimal buyTriggerMovingSlope; 
	private BigDecimal sellTriggerMovingSlope;
	private int windowOfAnalysis;
	private PriceTimeSeries prices;

	public Momentum(BigDecimal buyTriggerPercentageChange, BigDecimal sellTriggerPercentageChange, int windowOfAnalysis) {
		super();
		this.buyTriggerMovingSlope = buyTriggerPercentageChange;
		this.sellTriggerMovingSlope = sellTriggerPercentageChange;
		this.windowOfAnalysis = windowOfAnalysis;
		this.prices = new PriceTimeSeries();
	}

	public boolean isBuy(Price price) {

		prices.addPrice(price);
		
		if(prices.getSeriesDuration() < windowOfAnalysis) {
			return false;
		} else {
			PriceTimeSeries window = WindowAnalysis.getTimeWindow(prices, windowOfAnalysis); 
			BigDecimal percentageChange = PriceAnalysis.getPercentageChange(window);
			if(BigDecimalOperations.greaterThan(percentageChange, buyTriggerMovingSlope)) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isSell(Price price) {

		prices.addPrice(price);
		
		if(prices.getSeriesDuration() < windowOfAnalysis) {
			return false;
		} else {
			PriceTimeSeries window = WindowAnalysis.getTimeWindow(prices, windowOfAnalysis); 
			BigDecimal percentageChange = PriceAnalysis.getPercentageChange(window);
			
			if(BigDecimalOperations.lessThan(percentageChange, sellTriggerMovingSlope)) {
				return true;				
			}
		}
		
		return false;
	}

}
