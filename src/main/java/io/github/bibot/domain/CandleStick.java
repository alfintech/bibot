package io.github.bibot.domain;

import java.math.BigDecimal;
import java.util.Date;

public class CandleStick {

     private Date openTime;
     private Date closeTime;
     private BigDecimal open;
     private BigDecimal low;
     private BigDecimal high;
     private BigDecimal close;
     private BigDecimal volume;

    public CandleStick(Date openTime, Date closeTime, BigDecimal open, BigDecimal low, BigDecimal high, BigDecimal close, BigDecimal volume) {
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.open = open;
        this.low = low;
        this.high = high;
        this.close = close;
        this.volume = volume;
    }

    public Date getOpenTime() {
        return openTime;
    }

    public Date getCloseTime() {
        return closeTime;
    }

    public BigDecimal getOpen() {
        return open;
    }

    public BigDecimal getLow() {
        return low;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public BigDecimal getClose() {
        return close;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setOpenTime(Date openTime) {
        this.openTime = openTime;
    }

    public void setCloseTime(Date closeTime) {
        this.closeTime = closeTime;
    }

    public void setOpen(BigDecimal open) {
        this.open = open;
    }

    public void setLow(BigDecimal low) {
        this.low = low;
    }

    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    public void setClose(BigDecimal close) {
        this.close = close;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }
}
