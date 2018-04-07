package io.altanalytics.data.external.coinmarketcal.recorder;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import io.altanalytics.data.external.coinmarketcal.client.CoinMarketCalClient;
import io.altanalytics.domain.calendar.CalendarEvent;
import io.altanalytics.persistence.Publisher;

@EnableScheduling
@Component
public class CalendarEventsRecorder {

	@Value("${recorder.calendar.active}")
	private boolean active;

	@Autowired
	public CoinMarketCalClient eventsClient;
	
	@Autowired
	protected Publisher publisher;

	@Scheduled(cron = "${recorder.calendar.schedule}")
	public void tick() throws Exception {
		if(active) {
			List<CalendarEvent> calendarEvents = eventsClient.fetch();
			publisher.publishEvents(calendarEvents);
		}
	}

}
