package com.repup.pricecrawler.utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class RepupDateUtility {

	public final static String FLAG_BOOKING_YEAR_MONTH = "YEAR-MONTH";

	public final static String FLAG_BOOKING_DAY = "DAY";

	// SAMPLE DATE FORMAT 09302015
	public static String getMMTStartDate() {

		Date date = new Date();
		DateTime dt = new DateTime(date).plusDays(2);
		DateTimeFormatter dtfOut = DateTimeFormat.forPattern("MMddyyyy");
		return dtfOut.print(dt);

	}

	public static String getMMTEndDate() {

		Date date = new Date();
		DateTime dt = new DateTime(date);
		DateTime endDate = dt.plusDays(4);
		DateTimeFormatter dtfOut = DateTimeFormat.forPattern("MMddyyyy");
		return dtfOut.print(endDate);

	}

	public static String getGoibiboEndDate() {

		Date date = new Date();
		DateTime dt = new DateTime(date);
		DateTime endDate = dt.plusDays(8);
		DateTimeFormatter dtfOut = DateTimeFormat.forPattern("yyyyMMdd");
		return dtfOut.print(endDate);

	}

	public static String getGoibiboStartDate() {

		Date date = new Date();
		DateTime dt = new DateTime(date);
		DateTime startDate = dt.plusDays(7);
		DateTimeFormatter dtfOut = DateTimeFormat.forPattern("yyyyMMdd");
		return dtfOut.print(startDate);

	}

	public static String getExpediaEndDate() {

		Date date = new Date();
		DateTime dt = new DateTime(date);
		DateTime endDate = dt.plusDays(1);
		DateTimeFormatter dtfOut = DateTimeFormat.forPattern("dd/MM/yyyy");
		return dtfOut.print(endDate);

	}

	public static String getBookingCheckInData(String flag) {
		String data = "";

		if (flag.equals(FLAG_BOOKING_YEAR_MONTH)) {
			DateTime dt = new DateTime();
			int month = dt.monthOfYear().get();
			int year = dt.year().get();
			data = year + "-" + month;
		} else if (flag.equals(FLAG_BOOKING_DAY)) {
			DateTime dt = new DateTime();
			data = String.valueOf(dt.dayOfMonth().get());
		}

		return String.valueOf(data);
	}
	public static String getBookingCheckOutData(String flag) {
		String data = "";

		if (flag.equals(FLAG_BOOKING_YEAR_MONTH)) {
			DateTime dt = new DateTime().plusDays(1);
			int month = dt.monthOfYear().get();
			int year = dt.year().get();
			data = year + "-" + month;
		} else if (flag.equals(FLAG_BOOKING_DAY)) {
			DateTime dt = new DateTime().plusDays(1);
			data = String.valueOf(dt.dayOfMonth().get());
		}

		return String.valueOf(data);
	}

	public static String getExpediaStartDate() {

		Date date = new Date();
		DateTime dt = new DateTime(date);
		DateTimeFormatter dtfOut = DateTimeFormat.forPattern("dd/MM/yyyy");
		return dtfOut.print(dt);

	}

	public static Date getNullDate() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date reviewDate = null;
		try {
			reviewDate = df.parse("0000-00-00");
		} catch (Exception ex) {
			ex.printStackTrace();
			reviewDate = new Date();
		}

		return reviewDate;
	}

}
