package org.maccha.base.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

public class DateUtilsTest extends TestCase{
	private Calendar calendar;
	private String date;
	private String time;
	@Before
	public void setUp() throws Exception {
		calendar = Calendar.getInstance();
		date = calendar.get(Calendar.YEAR) + "-" + prefixTimeNumber((calendar.get(Calendar.MONTH) + 1)) + "-" + prefixTimeNumber(calendar.get(Calendar.DAY_OF_MONTH));
		time = date + " " + prefixTimeNumber(calendar.get(Calendar.HOUR_OF_DAY)) + ":" + prefixTimeNumber(calendar.get(Calendar.MINUTE)) + ":" + prefixTimeNumber(calendar.get(Calendar.SECOND));
	}
	private String prefixTimeNumber(int number) {
		return number > 9 ? number + "" : "0" + number;
	}
	@Test
	public void testGetCurrentDate() {
		assertEquals(date, DateUtils.getCurrentDate());
	}

	@Test
	public void testGetCurrentDateString() {
		assertEquals(date, DateUtils.getCurrentDate(DateUtils.YEAR_MONTH_DAY_PATTERN_MIDLINE));
	}

	@Test
	public void testAddYearFromCurrentDate() {
		int amount = 1;
		assertEquals((calendar.get(Calendar.YEAR) + amount) + "-" + prefixTimeNumber((calendar.get(Calendar.MONTH) + 1)) + "-" + prefixTimeNumber(calendar.get(Calendar.DAY_OF_MONTH))
				, DateUtils.addYearFromCurrentDate(amount));
	}

	@Test
	public void testAddMonthFromCurrentDate() {
		int amount = 1;
		assertEquals(calendar.get(Calendar.YEAR) + "-" + prefixTimeNumber((calendar.get(Calendar.MONTH) + 1 + amount)) + "-" + prefixTimeNumber(calendar.get(Calendar.DAY_OF_MONTH))
				, DateUtils.addMonthFromCurrentDate(amount));
	}

	@Test
	public void testAddDayFromCurrentDate() {
		int amount = 1;
		assertEquals(calendar.get(Calendar.YEAR) + "-" + prefixTimeNumber((calendar.get(Calendar.MONTH) + 1)) + "-" + prefixTimeNumber((calendar.get(Calendar.DAY_OF_MONTH) + amount))
				, DateUtils.addDayFromCurrentDate(amount));
	}

	@Test
	public void testCurrentDate() {
		this.run();
	}

	@Test
	public void testCurrentTimestamp() {
		this.run();
	}

	@Test
	public void testCurrentDateConnection() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException, ParseException {
		String url = "jdbc:oracle:thin:@123.59.52.182:1521:orcl";
        Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
        Connection conn = DriverManager.getConnection(url, "wes1", "wes1");
        assertEquals(date, DateTimeUtil.getTime(DateUtils.currentDate(conn).getTime()));
        conn.close();
	}

	@Test
	public void testCurrentDateString() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException, ParseException {
		String url = "jdbc:oracle:thin:@123.59.52.182:1521:orcl";
        Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
        Connection conn = DriverManager.getConnection(url, "wes1", "wes1");
        assertEquals(date, DateUtils.currentDateString(conn, DateUtils.YEAR_MONTH_DAY_PATTERN_MIDLINE));
        conn.close();
	}

	@Test
	public void testCurrentDateDefaultString() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException, ParseException {
		String url = "jdbc:oracle:thin:@123.59.52.182:1521:orcl";
        Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
        Connection conn = DriverManager.getConnection(url, "wes1", "wes1");
        assertEquals(date, DateUtils.currentDateDefaultString(conn));
        conn.close();
	}

	@Test
	public void testGetYear() {
		assertEquals(calendar.get(Calendar.YEAR), DateUtils.getYear(calendar.getTime()));
	}

	@Test
	public void testGetMonth() {
		assertEquals(calendar.get(Calendar.MONTH) + 1, DateUtils.getMonth(calendar.getTime()));
	}

	@Test
	public void testGetDay() {
		assertEquals(calendar.get(Calendar.DATE), DateUtils.getDay(calendar.getTime()));
	}

	@Test
	public void testGetHour() {
		assertEquals(calendar.get(Calendar.HOUR), DateUtils.getHour(calendar.getTime()));
	}

	@Test
	public void testGetMinute() {
		assertEquals(calendar.get(Calendar.MINUTE), DateUtils.getMinute(calendar.getTime()));
	}

	@Test
	public void testGetSecond() {
		assertEquals(calendar.get(Calendar.SECOND), DateUtils.getSecond(calendar.getTime()));
	}

	@Test
	public void testGetYearMonth() {
		assertEquals(calendar.get(Calendar.YEAR) + prefixTimeNumber((calendar.get(Calendar.MONTH) + 1 )) + "", DateUtils.getYearMonth(calendar.getTime()).toString());
	}

	@Test
	public void testParseYearMonthInteger() throws ParseException {
		String yyyymm = "201605";
		Integer i = Integer.parseInt(yyyymm);
		Date date = DateUtils.parseYearMonth(i);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
		assertEquals(yyyymm, dateFormat.format(date));
	}

	@Test
	public void testParseYearMonthString() throws ParseException {
		String yyyymm = "201605";
		Date date = DateUtils.parseYearMonth(yyyymm);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
		assertEquals(yyyymm, dateFormat.format(date));
	}

	@Test
	public void testAddYear() {
		assertEquals(calendar.get(Calendar.YEAR) + 1, DateUtils.getYear(DateUtils.addYear(calendar.getTime(), 1)));
	}

	@Test
	public void testAddMonthDateInt() {
		assertEquals(calendar.get(Calendar.MONTH) + 1 + 2, DateUtils.getMonth(DateUtils.addMonth(calendar.getTime(), 2)));
	}

	@Test
	public void testAddDay() {
		assertEquals(calendar.get(Calendar.DATE) + 1, DateUtils.getDay(DateUtils.addDay(calendar.getTime(), 1)));
	}

	@Test
	public void testAddMonthIntegerInt() throws ParseException {
		String yyyymm = "201605";
		Integer i = Integer.parseInt(yyyymm);
		assertEquals(i + 2 + "", DateUtils.addMonth(i, 2).toString());
	}

	@Test
	public void testBeforeYears() {
		Date d1 = DateUtils.getDate(2016, 5, 11);
		Date d2 = DateUtils.getDate(1989, 10, 26);
		assertEquals(27, DateUtils.beforeYears(d2, d1));
	}

	@Test
	public void testBeforeMonths() {
		Date d1 = DateUtils.getDate(2016, 5, 11);
		Date d2 = DateUtils.getDate(2015, 10, 26);
		assertEquals(7, DateUtils.beforeMonths(d2, d1));
	}

	@Test
	public void testBeforeDays() {
		Date d1 = DateUtils.getDate(2016, 5, 11);
		Date d2 = DateUtils.getDate(2016, 3, 26);
		assertEquals(46, DateUtils.beforeDays(d2, d1));
	}

	@Test
	public void testBeforeRoundYears() {
		Date d1 = DateUtils.getDate(2016, 10, 11);
		Date d2 = DateUtils.getDate(1989, 10, 26);
		assertEquals(26, DateUtils.beforeRoundYears(d2, d1));
	}

	@Test
	public void testBeforeRoundAges() {
		Date d1 = DateUtils.getDate(2016, 5, 26);
		Date d2 = DateUtils.getDate(1989, 10, 26);
		assertEquals(26, DateUtils.beforeRoundAges(d2, d1));
	}

	@Test
	public void beforeRoundMonths() {
		Date d1 = DateUtils.getDate(2016, 5, 11);
		Date d2 = DateUtils.getDate(2015, 10, 26);
		assertEquals(6, DateUtils.beforeMonths(d2, d1));
	}

	@Test
	public void testGetDate() {
		assertEquals(calendar.getTime(), DateUtils.getDate(calendar.get(Calendar.YEAR) - 1900, calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE)));
	}

	@Test
	public void testFormatDateString() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
		assertEquals(dateFormat.format(calendar.getTime()), DateUtils.format(calendar.getTime(), "yyyyMM"));
	}

	@Test
	public void testFormatDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		assertEquals(dateFormat.format(calendar.getTime()), DateUtils.format(calendar.getTime()));
	}

	@Test
	public void testParseStringString() throws ParseException {
		assertEquals(DateUtils.format(calendar.getTime()), DateUtils.format(DateUtils.parse(DateUtils.format(calendar.getTime()), DateUtils.YEAR_MONTH_DAY_PATTERN_MIDLINE)));
	}

	@Test
	public void testParseDateString() throws ParseException {
		assertEquals(DateUtils.format(calendar.getTime()), DateUtils.format(DateUtils.parse(DateUtils.format(calendar.getTime()))));
	}

	@Test
	public void testParseString() throws ParseException {
		assertEquals(DateUtils.format(calendar.getTime()), DateUtils.format(DateUtils.parse(date)));
	}

	@Test
	public void testIsYearMonthInteger() {
		String yyyymm = "201605";
		Integer i = Integer.parseInt(yyyymm);
		assertTrue(DateUtils.isYearMonth(i));
	}

	@Test
	public void testIsYearMonthString() {
		String yyyymm = "201605";
		assertTrue(DateUtils.isYearMonth(yyyymm));
		assertFalse(DateUtils.isYearMonth("120012"));
	}

	@Test
	public void testGetDayOfMonth() {
		assertEquals(calendar.getActualMaximum(Calendar.DAY_OF_MONTH), DateUtils.getDayOfMonth(calendar.getTime()));
	}

	@Test
	public void testGetYearMonths() throws ParseException {
		Integer from = 198910;
		Integer to = 199105;
		List list = DateUtils.getYearMonths(from, to);
		assertEquals(20, list.size());
		assertTrue(list.contains(new Integer(199105)));
		assertTrue(list.contains(new Integer(198910)));
		assertTrue(list.contains(new Integer(199010)));
	}

	@Test
	public void testGetYQ() throws ParseException {
		String ymd = "2016-5-11 15:00:53";
		assertEquals("2016-2", DateUtils.getYQ(ymd));
	}

	@Test
	public void testGetQ() throws ParseException {
		String ymd = "2016-5-11 15:00:53";
		assertEquals("2", DateUtils.getQ(ymd));
	}

	@Test
	public void testSplitYMD() throws ParseException {
		String ymd = "2016-5-11 15:00:53";
		int[] arr = DateUtils.splitYMD(ymd);
		assertEquals(3, arr.length);
		assertTrue(arr[0] == 2016);
		assertTrue(arr[1] == 5);
		assertTrue(arr[2] == 11);
	}

	@Test
	public void testClearFormat() throws ParseException {
		String ymd = "2016-5-11 15:00:53";
		assertEquals("20160511", DateUtils.clearFormat(ymd));
	}

	@Test
	public void testIsLast() throws ParseException {
		String ymd1 = "2016-5-11 15:00:53";
		String ymd2 = "2016-5-11 15:19:07";
		assertFalse(DateUtils.isLast(ymd1, ymd2));
	}

	@Test
	public void testGetBetweenDays() throws ParseException {
		String ymd1 = "2016-5-11 15:00:53";
		String ymd2 = "2016-6-11 15:19:07";
		assertEquals(30, DateUtils.getBetweenDays(ymd1, ymd2));
	}

	@Test
	public void testGetWeekOfYear() {
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.setMinimalDaysInFirstWeek(7);
		assertEquals(calendar.get(Calendar.WEEK_OF_YEAR), DateUtils.getWeekOfYear(calendar.getTime()));
	}

	@Test
	public void testGetMaxWeekNumOfYear() {
		assertEquals(52, DateUtils.getMaxWeekNumOfYear(2016));
	}

	@Test
	public void testGetFirstDayOfWeekIntInt() {
		Date d = DateUtils.getFirstDayOfWeek(2016, 25);
		assertEquals("2016-06-20", DateUtils.format(d));
	}

	@Test
	public void testGetLastDayOfWeekIntInt() {
		Date d = DateUtils.getLastDayOfWeek(2016, 25);
		assertEquals("2016-06-26", DateUtils.format(d));
	}

	@Test
	public void testGetFirstDayOfWeekDate() {
		Date d1 = DateUtils.getDate(116, 4, 11);
		Date d2 = DateUtils.getFirstDayOfWeek(d1);
		assertEquals("2016-05-09", DateUtils.format(d2));
	}

	@Test
	public void testGetFirstDayOfPrevWeek() {
		this.run();
	}

	@Test
	public void testGetLastDayOfPrevWeek() {
		this.run();
	}

	@Test
	public void testGetFirstDayOfNextWeek() {
		this.run();
	}

	@Test
	public void testGetLastDayOfNextWeek() {
		this.run();
	}

	@Test
	public void testGetLastDayOfWeekDate() {
		Date d1 = DateUtils.getDate(116, 4, 11);
		Date d2 = DateUtils.getLastDayOfWeek(d1);
		assertEquals("2016-05-15", DateUtils.format(d2));
	}

	@Test
	public void testGetFirstDayOfMonth() {
		Date d1 = DateUtils.getDate(116, 4, 11);
		Date d2 = DateUtils.getFirstDayOfMonth(d1);
		assertEquals("2016-05-01", DateUtils.format(d2));
	}

	@Test
	public void testGetLastDayOfMonthDate() {
		Date d1 = DateUtils.getDate(116, 4, 11);
		Date d2 = DateUtils.getLastDayOfMonth(d1);
		assertEquals("2016-05-31", DateUtils.format(d2));
	}

	@Test
	public void testGetLastDayOfMonthIntInt() {
		Date d = DateUtils.getLastDayOfMonth(2016, 5);
		assertEquals("2016-05-31", DateUtils.format(d));
	}

}
