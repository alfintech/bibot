package io.altanalytics.persistence;

import java.io.IOException;
import java.util.List;

import io.altanalytics.domain.calendar.CalendarEvent;
import io.altanalytics.domain.currency.Analytic;
import io.altanalytics.domain.currency.IntervalPrice;
import io.altanalytics.domain.market.MarketCap;

public interface Publisher {

	void publishMarketData(List<IntervalPrice> prices) throws IOException;

	void publishAnalytics(List<Analytic> analytics) throws IOException;

	void publishEvents(List<CalendarEvent> events) throws IOException;

	void publishMarketCap(MarketCap marketCap) throws IOException;

}