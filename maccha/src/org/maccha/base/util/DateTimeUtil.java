package org.maccha.base.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtil {
	private static final String format1 = "yyyy-MM-dd HH:mm:ss";
	private static final String format2 = "yyyy-MM-dd";
	private static final String format3 = "yyyyMMdd";
	private static final String YEAR = "yyyy";
	private static final String MONTH = "MM";
	private static final String DAY = "dd";
	private static final SimpleDateFormat dateFormat1 = new SimpleDateFormat(format1);
	private static final SimpleDateFormat dateFormat2 = new SimpleDateFormat(format2);
	private static final SimpleDateFormat dateFormat3 = new SimpleDateFormat(format3);
	private static final SimpleDateFormat dateFormatYear = new SimpleDateFormat(YEAR);
	private static final SimpleDateFormat dateFormatMonth = new SimpleDateFormat(MONTH);
	private static final SimpleDateFormat dateFormatDay = new SimpleDateFormat(DAY);
  /**
   * 获取指定日期时间戳
   * @param time
   * @return
   * @throws ParseException
   */
	public static long getTimeStamp(String time) throws ParseException {
		return dateFormat2.parse(time).getTime();
	}
  /**
   * 按指定格式获取指定时间戳
   * @param time
   * @param format 格式 1 yyyy-MM-dd HH:mm:ss 2 yyyy-MM-dd
   * @return
   * @throws ParseException
   */
	public static long getTimeStamp(String time, int format) throws ParseException {
		if (format == 1) {
			return dateFormat1.parse(time).getTime();
		} else if (format == 2) {
			return dateFormat2.parse(time).getTime();
		} else {
			return dateFormat2.parse(time).getTime();
		}
	}
  /**
   * 根据时间戳获取yyyy-MM-dd格式的字符串
   * @param timeStamp
   * @return
   * @throws ParseException
   */
	public static String getTime(long timeStamp) throws ParseException {
		return dateFormat2.format(new Date(timeStamp));
	}
  /**
   * 根据时间戳获取年份的字符串
   * @param timeStamp
   * @return
   * @throws ParseException
   */
	public static String getYear(long timeStamp) throws ParseException {
		return dateFormatYear.format(new Date(timeStamp));
	}
  /**
   * 根据时间戳获取月份的字符串
   * @param timeStamp
   * @return
   * @throws ParseException
   */
	public static String getMonth(long timeStamp) throws ParseException {
		return dateFormatMonth.format(new Date(timeStamp));
	}
  /**
   * 根据时间戳获取天数的字符串
   * @param timeStamp
   * @return
   * @throws ParseException
   */
	public static String getDay(long timeStamp) throws ParseException {
		return dateFormatDay.format(new Date(timeStamp));
	}
  /**
   * 根据时间戳获取指定格式的时间字符串
   * @param timeStamp
   * @param format 格式 1 yyyy-MM-dd HH:mm:ss 2 yyyy-MM-dd 3 yyyyMMdd
   * @return
   * @throws ParseException
   */
	public static String getTime(long timeStamp, int format) throws ParseException {
		if (format == 1) {
			return dateFormat1.format(new Date(timeStamp));
		} else if (format == 2) {
			return dateFormat2.format(new Date(timeStamp));
		} else if (format == 3) {
			return dateFormat3.format(new Date(timeStamp));
		} else {
			return dateFormat2.format(new Date(timeStamp));
		}
	}

	public static long getTime(int days) {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.DAY_OF_YEAR,-(days));
		return now.getTimeInMillis();
	}

}
