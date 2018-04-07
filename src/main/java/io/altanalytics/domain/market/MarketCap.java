package io.altanalytics.domain.market;

import java.math.BigDecimal;
import java.util.Date;

public class MarketCap {

	private BigDecimal marketCap;
	private BigDecimal dayVolume;
	private BigDecimal btcDominance;
	private Long activeCurrencies;
	private Date date;
	
	public MarketCap(BigDecimal marketCap, BigDecimal dayVolume, BigDecimal btcDominance, Long activeCurrencies, Date date) {
		this.marketCap = marketCap;
		this.dayVolume = dayVolume;
		this.btcDominance = btcDominance;
		this.activeCurrencies = activeCurrencies;
		this.date = date;
	}

	public BigDecimal getMarketCap() {
		return marketCap;
	}

	public BigDecimal getDayVolume() {
		return dayVolume;
	}

	public BigDecimal getBtcDominance() {
		return btcDominance;
	}

	public Long getActiveCurrencies() {
		return activeCurrencies;
	}
	
	public Date getDate() {
		return date;
	}

	@Override
	public String toString() {
		return "MarketCap [marketCap=" + marketCap + ", dayVolume=" + dayVolume + ", btcDominance=" + btcDominance
				+ ", activeCurrencies=" + activeCurrencies + ", date=" + date + "]";
	}

}	
