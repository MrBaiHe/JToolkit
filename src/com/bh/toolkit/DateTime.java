package com.bh.toolkit;

import java.util.Calendar;
import java.util.Date;

public class DateTime {
	public int year;
	public int month;
	public int day;
	public int hour;
	public int minute;
	public int second;
	public int millisecond;

	public static DateTime get(Date date) {
		DateTime dateTime = new DateTime();

		Calendar cal = Calendar.getInstance();
		if (date != null) {
			cal.setTime(date);
		}
		dateTime.year = cal.get(Calendar.YEAR);
		dateTime.month = cal.get(Calendar.MONTH) + 1;
		dateTime.day = cal.get(Calendar.DAY_OF_MONTH);
		dateTime.hour = cal.get(Calendar.HOUR_OF_DAY);
		dateTime.minute = cal.get(Calendar.MINUTE);
		dateTime.second = cal.get(Calendar.SECOND);
		dateTime.millisecond = cal.get(Calendar.MILLISECOND);

		return dateTime;
	}

	public static DateTime get() {
		return get(null);
	}

	public static String toString_DateTime(String separator1,
			String separator2, String separator3) {
		return String
				.format("%s%s%s", toString_Date(separator1),
						separator2 != null ? separator2 : "",
						toString_Time(separator3));
	}

	public static String toString_DateTime(DateTime dt) {
		return String.format("%s%s", toString_Date(null, dt),
				toString_Time(null, dt));
	}

	public static String toString_Date(String separator) {
		return toString_Date(separator, get());
	}

	public static String toString_Date(String separator, DateTime dt) {
		return String.format("%04d%s%02d%s%02d", dt.year,
				(separator != null ? separator : ""), dt.month,
				(separator != null ? separator : ""), dt.day);
	}

	public static String toString_Time(String separator) {
		return toString_Time(separator, get());
	}

	public static String toString_Time(String separator, DateTime dt) {
		return String.format("%02d%s%02d%s%02d", dt.hour,
				(separator != null ? separator : ""), dt.minute,
				(separator != null ? separator : ""), dt.second);
	}
}