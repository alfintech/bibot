package io.github.bibot.domain.price;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Price implements Comparable<Price> {

	private static final DateFormat DF = new SimpleDateFormat("ddMMyyyy HH:mm:ss");
	
	public BigDecimal price;
	public Date datetime;
	
	public Price(BigDecimal price, Date datetime) {
		this.price = price;
		this.datetime = datetime;
	}

	@Override
	public int compareTo(Price other) {
		return this.datetime.compareTo(other.datetime);
	}

	@Override
	public String toString() {
		return price + "\t" + DF.format(datetime);
	}
	
	
	
}
