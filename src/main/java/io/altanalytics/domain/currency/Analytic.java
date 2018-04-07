package io.altanalytics.domain.currency;

import java.math.BigDecimal;
import java.util.Date;

public class Analytic {

	private BigDecimal intervalVolume;
	private BigDecimal dayAverage;
	private BigDecimal percentageVolume;
	private BigDecimal percentageAllTimeHigh;
	private CurrencyPair currencyPair;
	private Date date;
	
	public Analytic(CurrencyPair currencyPair, BigDecimal intervalVolume, BigDecimal dayAverage, BigDecimal percentageVolume, BigDecimal percentageAllTimeHigh, Date date) {
		this.intervalVolume = intervalVolume;
		this.dayAverage = dayAverage;
		this.currencyPair = currencyPair;
		this.percentageVolume = percentageVolume;
		this.percentageAllTimeHigh = percentageAllTimeHigh;
		this.date = date;
	}

	public BigDecimal getIntervalVolume() {
		return intervalVolume;
	}

	public BigDecimal getDayAverage() {
		return dayAverage;
	}

	public BigDecimal getPercentageVolume() {
		return percentageVolume;
	}

	public BigDecimal getPercentageAllTimeHigh() {
		return percentageAllTimeHigh;
	}

	public CurrencyPair getCurrencyPair() {
		return currencyPair;
	}
	
	public Date getDate() {
		return date;
	}

	@Override
	public String toString() {
		return "Analytic [intervalVolume=" + intervalVolume + ", dayAverage=" + dayAverage
				+ ", percentageVolume=" + percentageVolume + ", percentageAllTimeHigh=" + percentageAllTimeHigh
				+ ", currencyPair=" + currencyPair + ", date=" + date + "]";
	}

}
