package io.github.bibot.backtest;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import io.github.bibot.domain.price.Price;
import io.github.bibot.priceanalysis.BigDecimalOperations;

public class RandomPriceSimulator implements Iterator<Price>, Iterable<Price>  {

	private BigDecimal lastPrice;
	private Calendar time;
	private BigDecimal priceMoveScalingFactor;
	private List<ResistanceLevels> floors;
	private Random generator;

	public RandomPriceSimulator(BigDecimal startingPrice, List<ResistanceLevels> floors, BigDecimal priceMoveScalingFactor, Date startingTime, long seed) {
		this.lastPrice = startingPrice;
		this.time = Calendar.getInstance();
		this.time.setTime(startingTime);
		this.floors = floors;
		this.priceMoveScalingFactor = priceMoveScalingFactor;
		this.generator = new Random(seed);
	}

	@Override
	public boolean hasNext() {
		return true;
	}

	@Override
	public Price next() {
		BigDecimal randomPriceMove = determineRandomScaledPriceMove();

		if(priceMoveUpwards(randomPriceMove)) {
			lastPrice = lastPrice.add(randomPriceMove);				
		} else {
			lastPrice = lastPrice.subtract(randomPriceMove);
		}

		time.add(Calendar.SECOND, 1);
		return new Price(lastPrice, time.getTime());
	}

	private boolean priceMoveUpwards(BigDecimal randomPriceMove) {

		BigDecimal upwardProbability = determineUpwardProbability();

		BigDecimal randomNumber = new BigDecimal(generator.nextDouble());
		randomNumber = randomNumber.add(upwardProbability);

		if(randomNumber.doubleValue() > 0.5) {
			return true;
		}

		return false;
	}

	private BigDecimal determineUpwardProbability() {

		BigDecimal cumulativeProbability = BigDecimal.ZERO;

		for(ResistanceLevels floor : floors) {
			if(BigDecimalOperations.lessThan(lastPrice, floor.level)) {
				cumulativeProbability = cumulativeProbability.add(floor.probabilityOfBreachLevel);
			}
		}

		return cumulativeProbability;

	}

	private BigDecimal determineRandomScaledPriceMove() {

		BigDecimal random = new BigDecimal(generator.nextDouble());
		BigDecimal randomScaledToPrice = lastPrice.multiply(random).multiply(priceMoveScalingFactor);
		return randomScaledToPrice.round(MathContext.DECIMAL32);
	}

	@Override
	public Iterator<Price> iterator() {
		return this;
	}

	public static class ResistanceLevels {

		public BigDecimal level;
		public BigDecimal probabilityOfBreachLevel;

		public ResistanceLevels(BigDecimal level, BigDecimal probabilityOfBreachLevel) {
			this.level = level;
			this.probabilityOfBreachLevel = probabilityOfBreachLevel;
		}
	}

}
