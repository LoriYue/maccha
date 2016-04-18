package org.maccha.base.util;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class DateUtils
{
  public static final String YEAR_MONTH_DAY_PATTERN_MIDLINE = "yyyy-MM-dd";
  public static final String YEAR_MONTH_DAY_PATTERN_DOT = "yyyy.MM.dd";
  public static final String YEAR_MONTH_DAY_PATTERN_BLANK = "yyyyMMdd";
  public static final String YEAR_MONTH_DAY_PATTERN_SOLIDUS = "yyyy/MM/dd";
  public static final String HOUR_MINUTE_SECOND_PATTERN = "HH:mm:ss";
  public static final String YMDHMS_PATTERN = "yyyy-MM-dd HH:mm:ss";

  public static String getCurrentDate() {
    return format(new Date(System.currentTimeMillis()), "yyyy-MM-dd");
  }

  public static String getCurrentDate(String format) {
    return format(new Date(System.currentTimeMillis()), format);
  }

  public static String addYearFromCurrentDate(int ammount) {
    Calendar c = Calendar.getInstance();
    c.getFirstDayOfWeek();
    c.setTime(new Date(System.currentTimeMillis()));
    c.add(1, ammount);
    return format(c.getTime(), "yyyy-MM-dd");
  }

  public static String addMonthFromCurrentDate(int ammount) {
    Calendar c = Calendar.getInstance();
    c.setTime(new Date(System.currentTimeMillis()));
    c.add(2, ammount);
    return format(c.getTime(), "yyyy-MM-dd");
  }

  public static String addDayFromCurrentDate(int ammount) {
    Calendar c = Calendar.getInstance();
    c.setTime(new Date(System.currentTimeMillis()));
    c.add(5, ammount);
    return format(c.getTime(), "yyyy-MM-dd");
  }

  public static Date currentDate() {
    return new Date(System.currentTimeMillis());
  }

  public static Timestamp currentTimestamp() {
    return new Timestamp(System.currentTimeMillis());
  }

  public static Date currentDate(Connection conn) throws SQLException {
    Date result = null;

    PreparedStatement pst = null;
    ResultSet rs = null;
    try {
      pst = conn.prepareStatement("select sysdate from dual");
      rs = pst.executeQuery();
      if (rs.next()) {
        Timestamp ts = rs.getTimestamp(1);
        if (ts != null)
          result = new Date(ts.getTime());
      }
    }
    finally {
      if (rs != null)
        try {
          rs.close();
          rs = null;
        }
        catch (SQLException sqle)
        {
        }
      if (pst != null)
        try {
          pst.close();
          pst = null;
        }
        catch (SQLException sqle)
        {
        }
    }
    return result;
  }

  public static String currentDateString(Connection conn, String pattern)
    throws SQLException
  {
    return format(currentDate(conn), pattern);
  }

  public static String currentDateDefaultString(Connection conn)
    throws SQLException
  {
    return format(currentDate(conn), "yyyy-MM-dd");
  }

  public static int getYear(Date date)
  {
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    return c.get(1);
  }

  public static int getMonth(Date date)
  {
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    return c.get(2) + 1;
  }

  public static int getDay(Date date)
  {
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    return c.get(5);
  }

  public static int getHour(Date date)
  {
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    return c.get(10);
  }

  public static int getMinute(Date date)
  {
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    return c.get(12);
  }

  public static int getSecond(Date date)
  {
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    return c.get(13);
  }

  public static Integer getYearMonth(Date date)
  {
    return new Integer(format(date, "yyyyMM"));
  }

  public static Date parseYearMonth(Integer yearMonth)
    throws ParseException
  {
    return parse(String.valueOf(yearMonth), "yyyyMM");
  }

  public static Date parseYearMonth(String yearMonth)
    throws ParseException
  {
    return parse(yearMonth, "yyyyMM");
  }

  public static Date addYear(Date date, int ammount)
  {
    Calendar c = Calendar.getInstance();
    c.getFirstDayOfWeek();
    c.setTime(date);
    c.add(1, ammount);
    return c.getTime();
  }

  public static Date addMonth(Date date, int ammount)
  {
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    c.add(2, ammount);
    return c.getTime();
  }

  public static Date addDay(Date date, int ammount)
  {
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    c.add(5, ammount);
    return c.getTime();
  }

  public static Integer addMonth(Integer yearMonth, int ammount)
    throws ParseException
  {
    return getYearMonth(addMonth(parseYearMonth(yearMonth), ammount));
  }

  public static int beforeYears(Date beforeDate, Date afterDate)
  {
    Calendar beforeCalendar = Calendar.getInstance();
    beforeCalendar.setTime(beforeDate);
    beforeCalendar.set(2, 1);
    beforeCalendar.set(5, 1);
    beforeCalendar.set(10, 0);
    beforeCalendar.set(13, 0);
    beforeCalendar.set(12, 0);
    Calendar afterCalendar = Calendar.getInstance();
    afterCalendar.setTime(afterDate);
    afterCalendar.set(2, 1);
    afterCalendar.set(5, 1);
    afterCalendar.set(10, 0);
    afterCalendar.set(13, 0);
    afterCalendar.set(12, 0);
    boolean positive = true;
    if (beforeDate.after(afterDate))
      positive = false;
    int beforeYears = 0;
    while (true) {
      boolean yearEqual = beforeCalendar.get(1) == afterCalendar.get(1);

      if (yearEqual) {
        break;
      }
      if (positive) {
        beforeYears++;
        beforeCalendar.add(1, 1);
      } else {
        beforeYears--;
        beforeCalendar.add(1, -1);
      }
    }

    return beforeYears;
  }

  public static int beforeMonths(Date beforeDate, Date afterDate)
  {
    Calendar beforeCalendar = Calendar.getInstance();
    beforeCalendar.setTime(beforeDate);
    beforeCalendar.set(5, 1);
    beforeCalendar.set(10, 0);
    beforeCalendar.set(13, 0);
    beforeCalendar.set(12, 0);
    Calendar afterCalendar = Calendar.getInstance();
    afterCalendar.setTime(afterDate);
    afterCalendar.set(5, 1);
    afterCalendar.set(10, 0);
    afterCalendar.set(13, 0);
    afterCalendar.set(12, 0);
    boolean positive = true;
    if (beforeDate.after(afterDate))
      positive = false;
    int beforeMonths = 0;
    while (true) {
      boolean yearEqual = beforeCalendar.get(1) == afterCalendar.get(1);

      boolean monthEqual = beforeCalendar.get(2) == afterCalendar.get(2);

      if ((yearEqual) && (monthEqual)) {
        break;
      }
      if (positive) {
        beforeMonths++;
        beforeCalendar.add(2, 1);
      } else {
        beforeMonths--;
        beforeCalendar.add(2, -1);
      }
    }

    return beforeMonths;
  }

  public static int beforeDays(Date beforeDate, Date afterDate)
  {
    Calendar beforeCalendar = Calendar.getInstance();
    beforeCalendar.setTime(beforeDate);
    beforeCalendar.set(10, 0);
    beforeCalendar.set(13, 0);
    beforeCalendar.set(12, 0);
    Calendar afterCalendar = Calendar.getInstance();
    afterCalendar.setTime(afterDate);
    afterCalendar.set(10, 0);
    afterCalendar.set(13, 0);
    afterCalendar.set(12, 0);
    boolean positive = true;
    if (beforeDate.after(afterDate))
      positive = false;
    int beforeDays = 0;
    while (true) {
      boolean yearEqual = beforeCalendar.get(1) == afterCalendar.get(1);

      boolean monthEqual = beforeCalendar.get(2) == afterCalendar.get(2);

      boolean dayEqual = beforeCalendar.get(5) == afterCalendar.get(5);

      if ((yearEqual) && (monthEqual) && (dayEqual)) {
        break;
      }
      if (positive) {
        beforeDays++;
        beforeCalendar.add(5, 1);
      } else {
        beforeDays--;
        beforeCalendar.add(5, -1);
      }
    }

    return beforeDays;
  }

  public static int beforeRoundYears(Date beforeDate, Date afterDate)
  {
    Date bDate = beforeDate;
    Date aDate = afterDate;
    boolean positive = true;
    if (beforeDate.after(afterDate)) {
      positive = false;
      bDate = afterDate;
      aDate = beforeDate;
    }
    int beforeYears = beforeYears(bDate, aDate);

    int bMonth = getMonth(bDate);
    int aMonth = getMonth(aDate);
    if (aMonth < bMonth) {
      beforeYears--;
    } else if (aMonth == bMonth) {
      int bDay = getDay(bDate);
      int aDay = getDay(aDate);
      if (aDay < bDay) {
        beforeYears--;
      }
    }

    if (positive) {
      return beforeYears;
    }
    return new BigDecimal(beforeYears).negate().intValue();
  }

  public static int beforeRoundAges(Date beforeDate, Date afterDate)
  {
    Date bDate = beforeDate;
    Date aDate = afterDate;
    boolean positive = true;
    if (beforeDate.after(afterDate)) {
      positive = false;
      bDate = afterDate;
      aDate = beforeDate;
    }
    int beforeYears = beforeYears(bDate, aDate);

    int bMonth = getMonth(bDate);
    int aMonth = getMonth(aDate);
    if (aMonth < bMonth) {
      beforeYears--;
    }

    if (positive) {
      return beforeYears;
    }
    return new BigDecimal(beforeYears).negate().intValue();
  }

  public static int beforeRoundMonths(Date beforeDate, Date afterDate)
  {
    Date bDate = beforeDate;
    Date aDate = afterDate;
    boolean positive = true;
    if (beforeDate.after(afterDate)) {
      positive = false;
      bDate = afterDate;
      aDate = beforeDate;
    }
    int beforeMonths = beforeMonths(bDate, aDate);

    int bDay = getDay(bDate);
    int aDay = getDay(aDate);
    if (aDay < bDay) {
      beforeMonths--;
    }

    if (positive) {
      return beforeMonths;
    }
    return new BigDecimal(beforeMonths).negate().intValue();
  }

  public static Date getDate(int year, int month, int date)
  {
    Calendar c = Calendar.getInstance();
    c.set(year + 1900, month, date);
    return c.getTime();
  }

  public static String format(Date date, String pattern)
  {
    DateFormat df = new SimpleDateFormat(pattern);
    return df.format(date);
  }

  public static String format(Date date)
  {
    return format(date, "yyyy-MM-dd");
  }

  public static Date parse(String dateStr, String pattern)
    throws ParseException
  {
    DateFormat df = new SimpleDateFormat(pattern);

    return df.parse(dateStr);
  }

  public static Date parse(Date date, String pattern) throws ParseException
  {
    return parse(date.toString(), pattern);
  }

  public static Date parse(String dateStr)
    throws ParseException
  {
    return parse(dateStr, "yyyy-MM-dd");
  }

  public static boolean isYearMonth(Integer yearMonth)
  {
    String yearMonthStr = yearMonth.toString();
    return isYearMonth(yearMonthStr);
  }

  public static boolean isYearMonth(String yearMonthStr)
  {
    if (yearMonthStr.length() != 6) {
      return false;
    }
    String yearStr = yearMonthStr.substring(0, 4);
    String monthStr = yearMonthStr.substring(4, 6);
    try {
      int year = Integer.parseInt(yearStr);
      int month = Integer.parseInt(monthStr);
      if ((year < 1800) || (year > 3000)) {
        return false;
      }

      return (month >= 1) && (month <= 12);
    }
    catch (Exception e)
    {
    }
    return false;
  }

  public static int getDayOfMonth(Date date)
  {
    Calendar calendarDate = Calendar.getInstance();
    calendarDate.setTime(date);
    return calendarDate.getActualMaximum(5);
  }

  public static List getYearMonths(Integer from, Integer to)
    throws ParseException
  {
    List yearMonths = new ArrayList();
    Date fromDate = parseYearMonth(from);
    Date toDate = parseYearMonth(to);
    if (fromDate.after(toDate)) {
      throw new IllegalArgumentException("'from' date should before 'to' date!");
    }
    Date tempDate = fromDate;
    while (tempDate.before(toDate)) {
      yearMonths.add(getYearMonth(tempDate));
      tempDate = addMonth(tempDate, 1);
    }
    if (!from.equals(to)) {
      yearMonths.add(to);
    }

    return yearMonths;
  }

  public static String getYQ(String ymd)
    throws ParseException
  {
    int[] dates = splitYMD(ymd);
    if ((dates[1] >= 1) && (dates[1] <= 3))
      return dates[0] + "-" + "1";
    if ((dates[1] >= 4) && (dates[1] <= 6))
      return dates[0] + "-" + "2";
    if ((dates[1] >= 7) && (dates[1] <= 9))
      return dates[0] + "-" + "3";
    return dates[0] + "-" + "4";
  }

  public static String getQ(String dateStr)
    throws ParseException
  {
    return StringUtils.splitToIntArray(getYQ(dateStr), ".")[1] + "";
  }

  public static int[] splitYMD(String ymd)
    throws ParseException
  {
    Date date = parse(ymd, "yyyy-MM-dd");
    String _ymd = format(date, "yyyy-MM-dd");

    int[] intArray = StringUtils.splitToIntArray(_ymd, "-");
    return intArray;
  }

  public static String clearFormat(String ymd)
    throws ParseException
  {
    Date date = parse(ymd, "yyyy-MM-dd");
    String _ymd = format(date, "yyyyMMdd");
    return _ymd;
  }

  public static boolean isLast(String ymd1, String ymd2)
    throws ParseException
  {
    int ymd1Int = MathUtils.parseInt(clearFormat(ymd1));
    int ymd2Int = MathUtils.parseInt(clearFormat(ymd2));

    return ymd2Int > ymd1Int;
  }

  public static int getBetweenDays(String symd, String eymd)
    throws ParseException
  {
    int[] ymdL = splitYMD(symd);
    GregorianCalendar gcStart = new GregorianCalendar(ymdL[0], ymdL[1], ymdL[2]);

    ymdL = splitYMD(eymd);
    GregorianCalendar gcEnd = new GregorianCalendar(ymdL[0], ymdL[1], ymdL[2]);

    long longStart = gcStart.getTimeInMillis();
    long longEnd = gcEnd.getTimeInMillis();

    int days = (int)((longEnd - longStart) / 86400000L);
    return days;
  }

  public static int getWeekOfYear(Date date)
  {
    Calendar c = new GregorianCalendar();
    c.setFirstDayOfWeek(2);
    c.setMinimalDaysInFirstWeek(7);
    c.setTime(date);

    return c.get(3);
  }

  public static int getMaxWeekNumOfYear(int year)
  {
    Calendar c = new GregorianCalendar();
    c.set(year, 11, 31, 23, 59, 59);

    return getWeekOfYear(c.getTime());
  }

  public static Date getFirstDayOfWeek(int year, int week)
  {
    Calendar c = new GregorianCalendar();
    c.set(1, year);
    c.set(2, 0);
    c.set(5, 1);

    Calendar cal = (GregorianCalendar)c.clone();
    cal.add(5, week * 7);

    return getFirstDayOfWeek(cal.getTime());
  }

  public static Date getLastDayOfWeek(int year, int week)
  {
    Calendar c = new GregorianCalendar();
    c.set(1, year);
    c.set(2, 0);
    c.set(5, 1);

    Calendar cal = (GregorianCalendar)c.clone();
    cal.add(5, week * 7);

    return getLastDayOfWeek(cal.getTime());
  }

  public static Date getFirstDayOfWeek(Date date)
  {
    Calendar c = new GregorianCalendar();
    c.setFirstDayOfWeek(2);
    c.setTime(date);
    c.set(7, c.getFirstDayOfWeek());
    return c.getTime();
  }

  public static Date getFirstDayOfPrevWeek(int prevNum)
  {
    Date currentDate = currentDate();
    int weekNum = getWeekOfYear(currentDate);
    return getFirstDayOfWeek(getYear(currentDate), weekNum - prevNum);
  }

  public static Date getLastDayOfPrevWeek(int prevNum)
  {
    Date currentDate = currentDate();
    int weekNum = getWeekOfYear(currentDate);
    return getLastDayOfWeek(getYear(currentDate), weekNum - prevNum);
  }

  public static Date getFirstDayOfNextWeek(int nextNum)
  {
    Date currentDate = currentDate();
    int weekNum = getWeekOfYear(currentDate);
    return getFirstDayOfWeek(getYear(currentDate), weekNum + nextNum);
  }

  public static Date getLastDayOfNextWeek(int nextNum)
  {
    Date currentDate = currentDate();
    int weekNum = getWeekOfYear(currentDate);
    return getLastDayOfWeek(getYear(currentDate), weekNum + nextNum);
  }

  public static Date getLastDayOfWeek(Date date)
  {
    Calendar c = new GregorianCalendar();
    c.setFirstDayOfWeek(2);
    c.setTime(date);
    c.set(7, c.getFirstDayOfWeek() + 6);
    return c.getTime();
  }
  public static Date getFirstDayOfMonth(Date date) {
    Calendar c = new GregorianCalendar();
    c.setTime(date);
    c.set(5, 1);
    return c.getTime();
  }
  public static Date getLastDayOfMonth(Date date) {
    Calendar c = new GregorianCalendar();
    c.setTime(date);
    c.set(5, 1);
    c.roll(5, -1);
    return c.getTime();
  }
  public static Date getLastDayOfMonth(int year, int month) {
    Calendar c = new GregorianCalendar();
    c.set(year, month - 1, 1);
    c.set(5, 1);
    c.roll(5, -1);
    return c.getTime();
  }

  public static void main(String[] agrs) {
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    Calendar day1 = Calendar.getInstance();
    day1.set(1980, 0, 24);
    Date remindDate = day1.getTime();
    System.out.println(df.format(remindDate));
  }
}