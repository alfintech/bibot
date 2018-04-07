package io.altanalytics.domain.currency;

import java.math.BigDecimal;
import java.util.Date;

public class Order {

	public BigDecimal price;
	public BigDecimal quantity;
	public Date datetime;
	
	public Order(BigDecimal price, BigDecimal quantity, Date datetime) {
		this.price = price;
		this.quantity = quantity;
		this.datetime = datetime;
	}
	
}
