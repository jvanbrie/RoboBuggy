package com.roboclub.robobuggy.utilities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.roboclub.robobuggy.main.MessageLevel;
import com.roboclub.robobuggy.main.RobobuggyLogicException;
import com.sun.javafx.binding.StringFormatter;

public class RobobuggyDateFormatter {

	public static String ROBOBUGGY_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
	
	public static Date getDatefromRobobuggyDateString(String date) {
		try {
			DateFormat format = new SimpleDateFormat(ROBOBUGGY_DATE_FORMAT, Locale.ENGLISH);
			return format.parse(date);
		} catch (ParseException e) {
			System.out.println("Unable to parse date");
			new RobobuggyLogicException("Couldn't parse date from " + date, MessageLevel.EXCEPTION);
			return new Date();
		}	
	}
	
	public static String getRobobuggyDateAsString(Date date) {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
	}
	
}
