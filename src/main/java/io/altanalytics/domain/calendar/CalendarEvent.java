package io.altanalytics.domain.calendar;

import java.util.Date;
import java.util.List;

public class CalendarEvent {

	private long eventId;
	private List<String> currencies;
	private String title;
	private String description;
	private Date eventDate;
	private Date createdDate;
	private long voteCount;
	private long positiveVoteCount;
	private long percentagePositiveVoteCount;
	
	public CalendarEvent(long eventId, List<String> currencies, String title, String description, Date eventDate,
			Date createdDate, long voteCount, long positiveVoteCount, long percentagePositiveVoteCount) {

		this.eventId = eventId;
		this.currencies = currencies;
		this.title = title;
		this.description = description;
		this.eventDate = eventDate;
		this.createdDate = createdDate;
		this.voteCount = voteCount;
		this.positiveVoteCount = positiveVoteCount;
		this.percentagePositiveVoteCount = percentagePositiveVoteCount; 
	}

	public long getEventId() {
		return eventId;
	}

	public List<String> getCurrencies() {
		return currencies;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public Date getEventDate() {
		return eventDate;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public long getVoteCount() {
		return voteCount;
	}

	public long getPositiveVoteCount() {
		return positiveVoteCount;
	}

	public long getPercentagePositiveVoteCount() {
		return percentagePositiveVoteCount;
	}

	@Override
	public String toString() {
		return "CalendarEvent [eventId=" + eventId + ", currencies=" + currencies + ", title=" + title
				+ ", description=" + description + ", eventDate=" + eventDate + ", createdDate=" + createdDate
				+ ", voteCount=" + voteCount + ", positiveVoteCount=" + positiveVoteCount
				+ ", percentagePositiveVoteCount=" + percentagePositiveVoteCount + "]";
	}
	
}
