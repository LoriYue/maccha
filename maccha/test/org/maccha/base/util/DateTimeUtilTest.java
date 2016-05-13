package org.maccha.base.util;

import java.text.ParseException;
import java.util.Calendar;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DateTimeUtilTest extends TestCase {
	private Calendar calendar;
	private String date;
	private String time;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		calendar = Calendar.getInstance();
		date = calendar.get(Calendar.YEAR) + "-" + prefixTimeNumber((calendar.get(Calendar.MONTH) + 1)) + "-" + prefixTimeNumber(calendar.get(Calendar.DAY_OF_MONTH));
		time = date + " " + prefixTimeNumber(calendar.get(Calendar.HOUR_OF_DAY)) + ":" + prefixTimeNumber(calendar.get(Calendar.MINUTE)) + ":" + prefixTimeNumber(calendar.get(Calendar.SECOND));
	}
	private String prefixTimeNumber(int number) {
		return number > 9 ? number + "" : "0" + number;
	}
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetTimeStampString() throws ParseException {
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		assertEquals(calendar.getTimeInMillis(), DateTimeUtil.getTimeStamp(calendar.get(Calendar.YEAR) 
				+ "-" + prefixTimeNumber((calendar.get(Calendar.MONTH) + 1)) + "-" + prefixTimeNumber(calendar.get(Calendar.DAY_OF_MONTH))));
	}

	@Test
	public void testGetTimeStampStringInt() throws ParseException {
		calendar.set(Calendar.MILLISECOND, 0);
		assertEquals(calendar.getTimeInMillis(), DateTimeUtil.getTimeStamp(time, 1));
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		assertEquals(calendar.getTimeInMillis(), DateTimeUtil.getTimeStamp(time, 2));
	}

	@Test
	public void testGetTimeLong() throws ParseException {
		assertEquals(date, DateTimeUtil.getTime(calendar.getTimeInMillis()));
	}

	@Test
	public void testGetYear() throws ParseException {
		assertEquals(calendar.get(Calendar.YEAR) + "", DateTimeUtil.getYear(calendar.getTimeInMillis()));
	}

	@Test
	public void testGetMonth() throws ParseException {
		assertEquals(prefixTimeNumber((calendar.get(Calendar.MONTH) + 1)) + "", DateTimeUtil.getMonth(calendar.getTimeInMillis()));
	}

	@Test
	public void testGetDay() throws ParseException {
		assertEquals(prefixTimeNumber(calendar.get(Calendar.DAY_OF_MONTH)) + "", DateTimeUtil.getDay(calendar.getTimeInMillis()));
	}

	@Test
	public void testGetTimeLongInt() throws ParseException {
		assertEquals(time, DateTimeUtil.getTime(calendar.getTimeInMillis(), 1));
	}

	@Test
	public void testGetTimeInt() {
		fail("Not yet test");
	}

}
