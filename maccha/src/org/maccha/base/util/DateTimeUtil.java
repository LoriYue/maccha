package org.maccha.base.util;

import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtil
{
  private static final String format1 = "yyyy-MM-dd HH:mm:ss";
  private static final String format2 = "yyyy-MM-dd";
  private static final String format3 = "yyyyMMdd";
  private static final String YEAR = "yyyy";
  private static final String MONTH = "MM";
  private static final String DAY = "dd";
  private static final SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  private static final SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");

  private static final SimpleDateFormat dateFormat3 = new SimpleDateFormat("yyyyMMdd");

  private static final SimpleDateFormat dateFormatYear = new SimpleDateFormat("yyyy");

  private static final SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MM");

  private static final SimpleDateFormat dateFormatDay = new SimpleDateFormat("dd");

  public static long getTimeStamp(String time)
    throws ParseException
  {
    return dateFormat2.parse(time).getTime();
  }

  public static long getTimeStamp(String time, int format) throws ParseException
  {
    if (format == 1)
      return dateFormat1.parse(time).getTime();
    if (format == 2) {
      return dateFormat2.parse(time).getTime();
    }
    return dateFormat2.parse(time).getTime();
  }

  public static String getTime(long timeStamp)
    throws ParseException
  {
    return dateFormat2.format(new Date(timeStamp));
  }

  public static String getYear(long timeStamp) throws ParseException
  {
    return dateFormatYear.format(new Date(timeStamp));
  }

  public static String getMonth(long timeStamp) throws ParseException
  {
    return dateFormatMonth.format(new Date(timeStamp));
  }

  public static String getDay(long timeStamp) throws ParseException
  {
    return dateFormatDay.format(new Date(timeStamp));
  }

  public static String getTime(long timeStamp, int format)
    throws ParseException
  {
    if (format == 1)
      return dateFormat1.format(new Date(timeStamp));
    if (format == 2)
      return dateFormat2.format(new Date(timeStamp));
    if (format == 3) {
      return dateFormat3.format(new Date(timeStamp));
    }
    return dateFormat2.format(new Date(timeStamp));
  }

  public static long getTime(int days)
  {
    Calendar now = Calendar.getInstance();
    now.add(6, -days);
    return now.getTimeInMillis();
  }

  public static void main(String[] argv) {
    try {
      long time = getTimeStamp("2006-02-27", 2);
      System.out.println(time);
      System.out.println(getTime(time, 2));
      System.out.println(getDay(time));
      System.out.println(getMonth(time));
      System.out.println(getYear(time));
    }
    catch (ParseException e) {
      e.printStackTrace();
    }
  }
}
