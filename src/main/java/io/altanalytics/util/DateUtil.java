package io.altanalytics.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {


	public static Date intervalStart(long interval) {
		Calendar currentTime = Calendar.getInstance();
		long msFromIntervalStart = currentTime.getTimeInMillis() % interval;
		currentTime.setTimeInMillis(currentTime.getTimeInMillis() - msFromIntervalStart);
		return currentTime.getTime();
	}
	
	public static Date intervalEnd(int interval) {
		return new Date(intervalStart(interval).getTime() + interval);
	}

	public static Date shiftToFuture(Date date, long shiftMs) {
		return new Date(date.getTime()+shiftMs);
	}
	
	public static Date shiftToPast(Date date, long shiftMs) {
		return new Date(date.getTime()-shiftMs);
	}
	
	public static Date now() {
		Calendar currentTime = Calendar.getInstance();
		return currentTime.getTime();
	}
}
