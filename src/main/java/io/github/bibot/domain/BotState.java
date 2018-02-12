package io.github.bibot.domain;

import java.math.BigDecimal;

import io.github.bibot.domain.price.Price;
import io.github.bibot.priceanalysis.BigDecimalOperations;

public class BotState {
	
	public BigDecimal openQuantity = BigDecimal.ZERO;
	public Price orderPrice;
	
	public void clear() {
		this.orderPrice = null;
		this.openQuantity = BigDecimal.ZERO;
	}
	
	public boolean isOpenPosition() {
		return BigDecimalOperations.greaterThan(openQuantity, BigDecimal.ZERO);
	}

}
