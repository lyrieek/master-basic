package pers.th.util.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatter {

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


	public static String toDateString(Date date) {
		if (date == null) {
			date = new Date();
		}
		return DATE_FORMAT.format(date);
	}

	public static String toDateTimeString(Date date) {
		if (date == null) {
			date = new Date();
		}
		return DATE_TIME_FORMAT.format(date);
	}

	public static String formatDate(Date date, String format) {
		try {
			return new SimpleDateFormat(format).format(date);
		} catch (Exception e) {
			return date.toString();
		}
	}

	public static Date parseDateTime(String date) {
		try {
			if (date.length() == 10) {
				return DATE_FORMAT.parse(date);
			}
			if (date.length() == 19) {
				return DATE_TIME_FORMAT.parse(date);
			}
			return null;
		} catch (ParseException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
}
