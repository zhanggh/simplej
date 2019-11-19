package com.haven.simplej.time;

import com.haven.simplej.time.enums.DateStyle;
import com.haven.simplej.time.enums.Week;
import com.vip.vjtools.vjkit.time.DateUtil;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by haven.zhang on 2019/1/3.
 */
public class DateUtils extends DateUtil {


	private static ThreadLocal<SimpleDateFormat> dateFormatThreadLocal = ThreadLocal.withInitial(() -> new SimpleDateFormat());

	public static Date StringToDate(String date) {
		DateStyle dateStyle = null;
		return StringToDate(date, dateStyle);
	}

	public static Date StringToDate(String date, String parttern) {
		Date myDate = null;
		if (date != null)
			try {
				myDate = getDateFormat(parttern).parse(date);
			} catch (Exception localException) {
			}
		return myDate;
	}

	public static Date StringToDate(String date, DateStyle dateStyle) {
		Date myDate = null;
		if (dateStyle == null) {
			for (DateStyle style : DateStyle.values()) {
				Date dateTmp = StringToDate(date, style.getValue());
				if (dateTmp != null) {
					return dateTmp;
				}
			}
		} else {
			myDate = StringToDate(date, dateStyle.getValue());
		}
		return myDate;
	}

	public static String dateToString(Date date, String parttern) {
		String dateString = null;
		if (date != null)
			try {
				dateString = getDateFormat(parttern).format(date);
			} catch (Exception localException) {
			}
		return dateString;
	}

	public static String dateToString(Date date, DateStyle dateStyle) {
		String dateString = null;
		if (dateStyle != null) {
			dateString = dateToString(date, dateStyle.getValue());
		}
		return dateString;
	}

	public static String StringToString(String date, String parttern) {
		return StringToString(date, null, parttern);
	}

	public static String StringToString(String date, DateStyle dateStyle) {
		return StringToString(date, null, dateStyle);
	}

	public static String StringToString(String date, String olddParttern, String newParttern) {
		String dateString = null;
		if (olddParttern == null) {
			DateStyle style = getDateStyle(date);
			if (style != null) {
				Date myDate = StringToDate(date, style.getValue());
				dateString = dateToString(myDate, newParttern);
			}
		} else {
			Date myDate = StringToDate(date, olddParttern);
			dateString = dateToString(myDate, newParttern);
		}
		return dateString;
	}

	public static String StringToString(String date, DateStyle olddDteStyle, DateStyle newDateStyle) {
		String dateString = null;
		if (olddDteStyle == null) {
			DateStyle style = getDateStyle(date);
			dateString = StringToString(date, style.getValue(), newDateStyle.getValue());
		} else {
			dateString = StringToString(date, olddDteStyle.getValue(), newDateStyle.getValue());
		}
		return dateString;
	}


	public static int getYear(String date) {
		return getYear(StringToDate(date));
	}

	public static int getYear(Date date) {
		return getInteger(date, 1);
	}

	public static int getMonth(String date) {
		return getMonth(StringToDate(date));
	}

	public static int getMonth(Date date) {
		return getInteger(date, 2);
	}

	public static int getDay(String date) {
		return getDay(StringToDate(date));
	}

	public static int getDay(Date date) {
		return getInteger(date, 5);
	}

	public static int getHour(String date) {
		return getHour(StringToDate(date));
	}

	public static int getHour(Date date) {
		return getInteger(date, 11);
	}

	public static int getMinute(String date) {
		return getMinute(StringToDate(date));
	}

	public static int getMinute(Date date) {
		return getInteger(date, 12);
	}

	public static int getSecond(String date) {
		return getSecond(StringToDate(date));
	}

	public static int getSecond(Date date) {
		return getInteger(date, 13);
	}

	public static String getDate(String date) {
		return StringToString(date, DateStyle.YYYY_MM_DD);
	}

	public static String getDate(Date date) {
		return dateToString(date, DateStyle.YYYY_MM_DD);
	}

	public static String getDate(Date date, String dateFormat) {
		return dateToString(date, dateFormat);
	}

	public static String getTime(String date) {
		return StringToString(date, DateStyle.HH_MM_SS);
	}

	public static String getTime(Date date) {
		return dateToString(date, DateStyle.HH_MM_SS);
	}

	public static Week getWeek(String date) {
		Week week = null;
		DateStyle dateStyle = getDateStyle(date);
		if (dateStyle != null) {
			Date myDate = StringToDate(date, dateStyle);
			week = getWeek(myDate);
		}
		return week;
	}

	public static Week getWeek(Date date) {
		Week week = null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int weekNumber = calendar.get(7) - 1;
		switch (weekNumber) {
			case 0:
				week = Week.SUNDAY;
				break;
			case 1:
				week = Week.MONDAY;
				break;
			case 2:
				week = Week.TUESDAY;
				break;
			case 3:
				week = Week.WEDNESDAY;
				break;
			case 4:
				week = Week.THURSDAY;
				break;
			case 5:
				week = Week.FRIDAY;
				break;
			case 6:
				week = Week.SATURDAY;
		}

		return week;
	}

	/**
	 * 获取间隔天数
	 * @param end
	 * @param begin
	 * @return
	 */
	public static int getIntervalDays(String begin, String end) {
		return getIntervalDays(StringToDate(begin), StringToDate(end));
	}

	/**
	 * 获取间隔天数
	 * @param end
	 * @param begin
	 * @return
	 */
	public static int getIntervalDays(Date end, Date begin) {
		long time = Math.abs(end.getTime() - begin.getTime());
		return (int) time / 86400000;
	}

	/**
	 * 获取间隔天数
	 * @param end
	 * @param begin
	 * @return
	 */
	public static int getIntervalDays(Timestamp end, Timestamp begin) {
		long time = Math.abs(end.getTime() - begin.getTime());
		return (int) time / 86400000;
	}

	public static String addYear(String date, int yearAmount) {
		return addInteger(date, 1, yearAmount);
	}

	public static Date addYear(Date date, int yearAmount) {
		return addInteger(date, 1, yearAmount);
	}

	public static String addMonth(String date, int yearAmount) {
		return addInteger(date, 2, yearAmount);
	}

	public static Date addMonth(Date date, int yearAmount) {
		return addInteger(date, 2, yearAmount);
	}

	public static String addDay(String date, int dayAmount) {
		return addInteger(date, 5, dayAmount);
	}

	public static Date addDay(Date date, int dayAmount) {
		return addInteger(date, 5, dayAmount);
	}

	public static String addHour(String date, int hourAmount) {
		return addInteger(date, 11, hourAmount);
	}

	public static Date addHour(Date date, int hourAmount) {
		return addInteger(date, 11, hourAmount);
	}

	public static String addMinute(String date, int hourAmount) {
		return addInteger(date, 12, hourAmount);
	}

	public static Date addMinute(Date date, int hourAmount) {
		return addInteger(date, 12, hourAmount);
	}

	public static String addSecond(String date, int hourAmount) {
		return addInteger(date, 13, hourAmount);
	}

	public static Date addSecond(Date date, int hourAmount) {
		return addInteger(date, 13, hourAmount);
	}


	public static boolean isDate(String date) {
		boolean isDate = false;
		if ((date != null) && (StringToDate(date) != null)) {
			isDate = true;
		}

		return isDate;
	}

	public static DateStyle getDateStyle(String date) {
		DateStyle dateStyle = null;
		Map map = new HashMap();
		for (DateStyle style : DateStyle.values()) {
			Date dateTmp = StringToDate(date, style.getValue());
			if (dateTmp != null) {
				 return style;
			}
		}
		return null;
	}

	public static SimpleDateFormat getDateFormat(String parttern) throws RuntimeException {
		dateFormatThreadLocal.get().applyPattern(parttern);//@todo 低效的做法，待优化
		return dateFormatThreadLocal.get();
	}

	public static int getInteger(Date date, int dateType) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(dateType);
	}

	public static String addInteger(String date, int dateType, int amount) {
		String dateString = null;
		DateStyle dateStyle = getDateStyle(date);
		if (dateStyle != null) {
			Date myDate = StringToDate(date, dateStyle);
			myDate = addInteger(myDate, dateType, amount);
			dateString = dateToString(myDate, dateStyle);
		}
		return dateString;
	}

	public static Date addInteger(Date date, int dateType, int amount) {
		Date myDate = null;
		if (date != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(dateType, amount);
			myDate = calendar.getTime();
		}
		return myDate;
	}

	public static Timestamp getTimestamp(Date date){
		return new Timestamp(date.getTime());
	}

	/**
	 * 获取当前日期
	 * @param dateFormat 日期输出格式，如:yyyyMMdd
	 * @return
	 */
	public static String getCurrentDate(String dateFormat){

		return getDate(new Date(),dateFormat);
	}
}
