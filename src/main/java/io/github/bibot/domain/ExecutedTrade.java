package io.github.bibot.domain;

import java.math.BigDecimal;
import java.util.Date;

public class ExecutedTrade {
	
	public enum Direction{BUY, SELL};
	
	public BigDecimal price;
	public BigDecimal quantity;
	public Direction direction;
	public Date datetime;
	
	public ExecutedTrade(BigDecimal price, BigDecimal quantity, Direction direction, Date datetime) {
		this.price = price;
		this.quantity = quantity;
		this.direction = direction;
		this.datetime = datetime;
	}

	@Override
	public String toString() {
		return "ExecutedTrade [price=" + price + ", quantity=" + quantity + ", direction=" + direction + ", datetime="
				+ datetime + "]";
	}
	
	
	
}
