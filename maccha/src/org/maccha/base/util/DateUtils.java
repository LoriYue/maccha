package org.maccha.base.util;

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

public class DateUtils  {
	public static final String YEAR_MONTH_DAY_PATTERN_MIDLINE = "yyyy-MM-dd";
	public static final String YEAR_MONTH_DAY_PATTERN_DOT = "yyyy.MM.dd";
	public static final String YEAR_MONTH_DAY_PATTERN_BLANK = "yyyyMMdd";
	public static final String YEAR_MONTH_DAY_PATTERN_SOLIDUS = "yyyy/MM/dd";
	public static final String HOUR_MINUTE_SECOND_PATTERN = "HH:mm:ss";
	public static final String YMDHMS_PATTERN = "yyyy-MM-dd HH:mm:ss";
  /**
   * 按yyyy-MM-dd格式获得系统当前时间
   * @return
   */
	public static String getCurrentDate() {
		return format(new Date(System.currentTimeMillis()), YEAR_MONTH_DAY_PATTERN_MIDLINE);
	}
  /**
   * 按指定格式获得系统当前时间
   * @param format 格式 
   * 	DateUtils.YEAR_MONTH_DAY_PATTERN_MIDLINE 即 yyyy-MM-dd
   * 	DateUtils.YEAR_MONTH_DAY_PATTERN_DOT 即 yyyy.MM.dd
   * 	DateUtils.YEAR_MONTH_DAY_PATTERN_BLANK 即 yyyyMMdd
   * 	DateUtils.YEAR_MONTH_DAY_PATTERN_SOLIDUS 即 yyyy/MM/dd
   * 	DateUtils.HOUR_MINUTE_SECOND_PATTERN 即 HH:mm:ss
   * 	DateUtils.YMDHMS_PATTERN 即 yyyy-MM-dd HH:mm:ss
   * @return
   */
	public static String getCurrentDate(String format) {
		return format(new Date(System.currentTimeMillis()), format);
	}
  /**
   * 将当前日期增加指定年数，并按yyyy-MM-dd格式返回日期。如果传入负数，则为减。
   * @param ammount 要增加年的数目
   * @return
   */
	public static String addYearFromCurrentDate(int ammount) {
	  	Calendar c = Calendar.getInstance();
		c.getFirstDayOfWeek();
		c.setTime(new Date(System.currentTimeMillis()));
		c.add(Calendar.YEAR, ammount);
		return format(c.getTime(), YEAR_MONTH_DAY_PATTERN_MIDLINE);
	}
  /**
   * 将当前日期增加指定月数，并按yyyy-MM-dd格式返回日期。如果传入负数，则为减。
   * @param ammount 要增加月的数目
   * @return
   */
	public static String addMonthFromCurrentDate(int ammount) {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(System.currentTimeMillis()));
		c.add(Calendar.MONTH, ammount);
		return format(c.getTime(), YEAR_MONTH_DAY_PATTERN_MIDLINE);
	}
  /**
   * 将当前日期增加指定天数，并按yyyy-MM-dd格式返回日期。如果传入负数，则为减。
   * @param ammount 要增加天的数目
   * @return
   */
	public static String addDayFromCurrentDate(int ammount) {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(System.currentTimeMillis()));
		c.add(Calendar.DATE, ammount);
		return format(c.getTime(), YEAR_MONTH_DAY_PATTERN_MIDLINE);
	}
  /**
	 * 获得系统当前时间Date对象
	 * 
	 * @return Date
	 */
	public static Date currentDate() {
		return new Date(System.currentTimeMillis());
	}
  /**
	 * 获得系统当前时间Timestamp对象
	 * 
	 * @return Date
	 */
	public static Timestamp currentTimestamp() {
		return new Timestamp(System.currentTimeMillis());
	}
  /**
	 * 从数据库服务器获取当前时间。
	 * 
	 * @return 返回当前时间
	 * @throws SQLException
	 *             获取数据库时间时发生错误
	 */
	public static Date currentDate(Connection conn) throws SQLException {
	  	Date result = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = conn.prepareStatement("select sysdate from dual");
			rs = pst.executeQuery();
			if (rs.next()) {
				Timestamp ts = rs.getTimestamp(1);
				if (ts != null) {
					result = new Date(ts.getTime());
				}
			}
		} finally {
			if (rs != null) {
				try {
					rs.close();
					rs = null;
				} catch (SQLException sqle) {
					// ignore it
				}
			}
			if (pst != null) {
				try {
					pst.close();
					pst = null;
				} catch (SQLException sqle) { // ignore it}
				}
			}
		}
		return result;
	}
  /**
	 * 从数据库服务器获取当前时间并根据传入的patter转换成字符串形式。
	 * 
	 * @param pattern
	 *            日期pattern
	 * @return 返回当前时间根据传入pattern转换后的字符串
	 * @throws SQLException
	 *             获取数据库时间时发生错误
	 */
	public static String currentDateString(Connection conn, String pattern) throws SQLException {
		return format(currentDate(conn), pattern);
	}
  /**
	 * 从数据库服务器获取当前时间并转换成默认字符串形式（yyyy-MM-dd）。
	 * 
	 * @return 返回当前时间的默认字符串形式（yyyy-MM-dd）
	 * @throws SQLException
	 *             获取数据库时间时发生错误
	 */
	public static String currentDateDefaultString(Connection conn) throws SQLException {
		return format(currentDate(conn), YEAR_MONTH_DAY_PATTERN_MIDLINE);
	}
  /**
	 * 获取给定日期对象的年
	 * 
	 * @param date
	 *            日期对象
	 * @return 年
	 */
	public static int getYear(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.YEAR);
	}
  /**
	 * 获取给定日期对象的月
	 * 
	 * @param date
	 *            日期对象
	 * @return 返回自然月数（即1-12）
	 */
	public static int getMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.MONTH) + 1;
	}
  /**
	 * 获取给定日期对象的天
	 * 
	 * @param date
	 *            日期对象
	 * @return 天
	 */
	public static int getDay(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.DATE);
	}
  /**
	 * 获取给定日期对象的时
	 * 
	 * @param date
	 *            日期对象
	 * @return 时
	 */
	public static int getHour(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.HOUR);
	}
  /**
	 * 获取给定日期对象的分
	 * 
	 * @param date
	 *            日期对象
	 * @return 分
	 */
	public static int getMinute(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.MINUTE);
	}
  /**
	 * 获取给定日期对象的秒
	 * 
	 * @param date
	 *            日期对象
	 * @return 秒
	 */
	public static int getSecond(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.SECOND);
	}
  /**
	 * 获取传入日期的年和月的Integer形式（yyyyMM）。
	 * 
	 * @param date
	 *            要转换的日期对象
	 * @return 转换后的Integer对象
	 */
	public static Integer getYearMonth(Date date) {
		return new Integer(format(date, "yyyyMM"));
	}
  /**
	 * 将年月的整数形式（yyyyMM）转换为日期对象返回。
	 * 
	 * @param yearMonth
	 *            年月的整数形式（yyyyMM）
	 * @return 日期类型
	 * @throws ParseException
	 */
	public static Date parseYearMonth(Integer yearMonth) throws ParseException {
		return parse(String.valueOf(yearMonth), "yyyyMM");
	}
  /**
	 * 将年月的字符串（yyyyMM）转换为日期对象返回。
	 * 
	 * @param yearMonth
	 *            年月的字符串（yyyyMM）
	 * @return 日期类型
	 * @throws ParseException
	 */
	public static Date parseYearMonth(String yearMonth) throws ParseException {
		return parse(yearMonth, "yyyyMM");
	}
  /**
	 * 将某个日期增加指定年数，并返回结果。如果传入负数，则为减。
	 * 
	 * @param date
	 *            要操作的日期对象
	 * @param ammount
	 *            要增加年的数目
	 * @return 结果日期对象
	 */
	public static Date addYear(Date date, int ammount) {
		Calendar c = Calendar.getInstance();
		c.getFirstDayOfWeek();
		c.setTime(date);
		c.add(Calendar.YEAR, ammount);
		return c.getTime();
	}
  /**
	 * 将某个日期增加指定月数，并返回结果。如果传入负数，则为减。
	 * 
	 * @param date
	 *            要操作的日期对象
	 * @param ammount
	 *            要增加月的数目
	 * @return 结果日期对象
	 */
	public static Date addMonth(Date date, int ammount) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, ammount);
		return c.getTime();
	}
  /**
	 * 将某个日期增加指定天数，并返回结果。如果传入负数，则为减。
	 * 
	 * @param date
	 *            要操作的日期对象
	 * @param ammount
	 *            要增加天的数目
	 * @return 结果日期对象
	 */
	public static Date addDay(Date date, int ammount) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, ammount);
		return c.getTime();
	}
  /**
	 * 将给定整数形式的年月增加指定月数，并返回结果。如果传入负数，则为减。
	 * 
	 * @param yearMonth
	 *            要操作的年月
	 * @param ammount
	 *            要增加的月数
	 * @return 结果年月
	 * @throws ParseException
	 */
	public static Integer addMonth(Integer yearMonth, int ammount) throws ParseException {
		return getYearMonth(addMonth(parseYearMonth(yearMonth), ammount));
	}
  /**
	 * 返回给定的beforeDate比afterDate早的年数。如果beforeDate晚于afterDate，则 返回负数。
	 * 
	 * @param beforeDate
	 *            要比较的早的日期
	 * @param afterDate
	 *            要比较的晚的日期
	 * @return beforeDate比afterDate早的年数，负数表示晚。
	 */
	public static int beforeYears(Date beforeDate, Date afterDate) {
		Calendar beforeCalendar = Calendar.getInstance();
		beforeCalendar.setTime(beforeDate);
		beforeCalendar.set(Calendar.MONTH, 1);
		beforeCalendar.set(Calendar.DATE, 1);
		beforeCalendar.set(Calendar.HOUR, 0);
		beforeCalendar.set(Calendar.SECOND, 0);
		beforeCalendar.set(Calendar.MINUTE, 0);
		Calendar afterCalendar = Calendar.getInstance();
		afterCalendar.setTime(afterDate);
		afterCalendar.set(Calendar.MONTH, 1);
		afterCalendar.set(Calendar.DATE, 1);
		afterCalendar.set(Calendar.HOUR, 0);
		afterCalendar.set(Calendar.SECOND, 0);
		afterCalendar.set(Calendar.MINUTE, 0);
		boolean positive = true;
		if (beforeDate.after(afterDate))
			positive = false;
		int beforeYears = 0;
		while (true) {
			boolean yearEqual = beforeCalendar.get(Calendar.YEAR) == afterCalendar.get(Calendar.YEAR);
			if (yearEqual) {
				break;
			} else {
				if (positive) {
					beforeYears++;
					beforeCalendar.add(Calendar.YEAR, 1);
				} else {
					beforeYears--;
					beforeCalendar.add(Calendar.YEAR, -1);
				}
			}
		}
		return beforeYears;
	}
  /**
	 * 返回给定的beforeDate比afterDate早的月数。如果beforeDate晚于afterDate，则 返回负数。
	 * 
	 * @param beforeDate
	 *            要比较的早的日期
	 * @param afterDate
	 *            要比较的晚的日期
	 * @return beforeDate比afterDate早的月数，负数表示晚。
	 */
	public static int beforeMonths(Date beforeDate, Date afterDate) {
		Calendar beforeCalendar = Calendar.getInstance();
		beforeCalendar.setTime(beforeDate);
		beforeCalendar.set(Calendar.DATE, 1);
		beforeCalendar.set(Calendar.HOUR, 0);
		beforeCalendar.set(Calendar.SECOND, 0);
		beforeCalendar.set(Calendar.MINUTE, 0);
		Calendar afterCalendar = Calendar.getInstance();
		afterCalendar.setTime(afterDate);
		afterCalendar.set(Calendar.DATE, 1);
		afterCalendar.set(Calendar.HOUR, 0);
		afterCalendar.set(Calendar.SECOND, 0);
		afterCalendar.set(Calendar.MINUTE, 0);
		boolean positive = true;
		if (beforeDate.after(afterDate))
			positive = false;
		int beforeMonths = 0;
		while (true) {
			boolean yearEqual = beforeCalendar.get(Calendar.YEAR) == afterCalendar.get(Calendar.YEAR);
			boolean monthEqual = beforeCalendar.get(Calendar.MONTH) == afterCalendar.get(Calendar.MONTH);
			if ((yearEqual) && (monthEqual)) {
				break;
			}
			if (positive) {
				beforeMonths++;
				beforeCalendar.add(Calendar.MONTH, 1);
			} else {
				beforeMonths--;
				beforeCalendar.add(Calendar.MONTH, -1);
			}
		}
		return beforeMonths;
	}
  /**
	 * 返回给定的beforeDate比afterDate早的天数。如果beforeDate晚于afterDate，则 返回负数。
	 * 
	 * @param beforeDate
	 *            要比较的早的日期
	 * @param afterDate
	 *            要比较的晚的日期
	 * @return beforeDate比afterDate早的天数，负数表示晚。
	 */
	public static int beforeDays(Date beforeDate, Date afterDate) {
		Calendar beforeCalendar = Calendar.getInstance();
		beforeCalendar.setTime(beforeDate);
		beforeCalendar.set(Calendar.HOUR, 0);
		beforeCalendar.set(Calendar.SECOND, 0);
		beforeCalendar.set(Calendar.MINUTE, 0);
		Calendar afterCalendar = Calendar.getInstance();
		afterCalendar.setTime(afterDate);
		afterCalendar.set(Calendar.HOUR, 0);
		afterCalendar.set(Calendar.SECOND, 0);
		afterCalendar.set(Calendar.MINUTE, 0);
		boolean positive = true;
		if (beforeDate.after(afterDate))
			positive = false;
		int beforeDays = 0;
		while (true) {
			boolean yearEqual = beforeCalendar.get(Calendar.YEAR) == afterCalendar.get(Calendar.YEAR);
			boolean monthEqual = beforeCalendar.get(Calendar.MONTH) == afterCalendar.get(Calendar.MONTH);
			boolean dayEqual = beforeCalendar.get(Calendar.DATE) == afterCalendar.get(Calendar.DATE);
			if ((yearEqual) && (monthEqual) && (dayEqual)) {
				break;
			}
			if (positive) {
				beforeDays++;
				beforeCalendar.add(Calendar.DATE, 1);
			} else {
				beforeDays--;
				beforeCalendar.add(Calendar.DATE, -1);
			}
		}
		return beforeDays;
	}
  /**
	 * 获取beforeDate和afterDate之间相差的完整年数，精确到天。负数表示晚。
	 * 
	 * @param beforeDate
	 *            要比较的早的日期
	 * @param afterDate
	 *            要比较的晚的日期
	 * @return beforeDate比afterDate早的完整年数，负数表示晚。
	 */
	public static int beforeRoundYears(Date beforeDate, Date afterDate) {
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
  /**
	 * 获取beforeDate和afterDate之间相差的完整年数，精确到月。负数表示晚。
	 * 
	 * @param beforeDate
	 *            要比较的早的日期
	 * @param afterDate
	 *            要比较的晚的日期
	 * @return beforeDate比afterDate早的完整年数，负数表示晚。
	 */
	public static int beforeRoundAges(Date beforeDate, Date afterDate) {
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
  /**
	 * 获取beforeDate和afterDate之间相差的完整月数，精确到天。负数表示晚。
	 * 
	 * @param beforeDate
	 *            要比较的早的日期
	 * @param afterDate
	 *            要比较的晚的日期
	 * @return beforeDate比afterDate早的完整月数，负数表示晚。
	 */
	public static int beforeRoundMonths(Date beforeDate, Date afterDate) {
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
  /**
	 * 根据传入的年、月、日构造日期对象
	 * 
	 * @param year
	 *            年(1900年后的年份)
	 * @param month
	 *            月（0-11）
	 * @param date
	 *            日
	 * @return 返回根据传入的年、月、日构造的日期对象
	 */
	public static Date getDate(int year, int month, int date) {
		Calendar c = Calendar.getInstance();
		c.set(year + 1900, month, date);
		return c.getTime();
	}
  /**
	 * 根据传入的日期格式化pattern将传入的日期格式化成字符串。
	 * 
	 * @param date
	 *            要格式化的日期对象
	 * @param pattern
	 *            日期格式化pattern
	 * @return 格式化后的日期字符串
	 */
	public static String format(Date date, String pattern) {
		DateFormat df = new SimpleDateFormat(pattern);
		return df.format(date);
	}
  /**
	 * 将传入的日期按照默认形势转换成字符串（yyyy-MM-dd）
	 * 
	 * @param date
	 *            要格式化的日期对象
	 * @return 格式化后的日期字符串
	 */
	public static String format(Date date) {
		return format(date, YEAR_MONTH_DAY_PATTERN_MIDLINE);
	}
  /**
	 * 根据传入的日期格式化patter将传入的字符串转换成日期对象
	 * 
	 * @param dateStr
	 *            要转换的字符串
	 * @param pattern
	 *            日期格式化pattern
	 * @return 转换后的日期对象
	 * @throws ParseException
	 *             如果传入的字符串格式不合法
	 */
	public static Date parse(String dateStr, String pattern) throws ParseException {
		DateFormat df = new SimpleDateFormat(pattern);
		return df.parse(dateStr);
	}
  /**
	 * 将传入的字符串按照默认格式转换为日期对象（yyyy-MM-dd）
	 * 
	 * @param dateStr
	 *            要转换的字符串
	 * @return 转换后的日期对象
	 * @throws ParseException
	 *             如果传入的字符串格式不符合默认格式（如果传入的字符串格式不合法）
	 */
	public static Date parse(Date date, String pattern) throws ParseException {
		return parse(date.toString(), pattern);
	}
  /**
	 * 将传入的字符串按照默认格式转换为日期对象（yyyy-MM-dd）
	 * 
	 * @param dateStr
	 *            要转换的字符串
	 * @return 转换后的日期对象
	 * @throws ParseException
	 *             如果传入的字符串格式不符合默认格式（如果传入的字符串格式不合法）
	 */
	public static Date parse(String dateStr) throws ParseException {
		return parse(dateStr, YEAR_MONTH_DAY_PATTERN_MIDLINE);
	}
  /**
	 * 要进行合法性验证的年月数值
	 * 
	 * @param yearMonth(年份1800-3000，月份1-12)
	 *            验证年月数值
	 * @return 年月是否合法
	 */
	public static boolean isYearMonth(Integer yearMonth) {
		String yearMonthStr = yearMonth.toString();
		return isYearMonth(yearMonthStr);
	}
  /**
	 * 要进行合法性验证的年月字符串
	 * 
	 * @param yearMonthStr(年份1800-3000，月份1-12)
	 *            验证年月字符串
	 * @return 年月是否合法
	 */
	public static boolean isYearMonth(String yearMonthStr) {
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
  /**
	 * 获取一个月的最大天数
	 * 
	 * @param date
	 *            要计算月份
	 * @return int 一个月的最大天数
	 */
	public static int getDayOfMonth(Date date) {
		Calendar calendarDate = Calendar.getInstance();
		calendarDate.setTime(date);
		return calendarDate.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
  /**
	 * 获取从from到to的年月Integer形式值的列表
	 * 
	 * @param from
	 *            从
	 * @param to
	 *            到
	 * @return 年月Integer形式值列表
	 * @throws ParseException
	 */
	public static List getYearMonths(Integer from, Integer to) throws ParseException {
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
  /**
	 * 获得当前年季度 @ String date 获得时间的参数，为提供扩展作用
	 * @return String 当前年季度(年份-季度)
	 */
	public static String getYQ(String ymd) throws ParseException {
		int[] dates = splitYMD(ymd);
		if ((dates[1] >= 1) && (dates[1] <= 3))
			return dates[0] + "-" + "1";
		if ((dates[1] >= 4) && (dates[1] <= 6))
			return dates[0] + "-" + "2";
		if ((dates[1] >= 7) && (dates[1] <= 9))
			return dates[0] + "-" + "3";
		return dates[0] + "-" + "4";
	}
  /**
	 * 获得指定日期所属季度 @ String ymd 指定日期
	 * @return String 指定日期的季度
	 */
	public static String getQ(String dateStr) throws ParseException {
		return StringUtils.splitToIntArray(getYQ(dateStr), "-")[1] + "";
	}
  /**
	 * 分割年月日成数组
	 * 
	 * @param ymd
	 * @return
	 * @throws ParseException
	 */
	public static int[] splitYMD(String ymd) throws ParseException {
		Date date = parse(ymd, "yyyy-MM-dd");
		String _ymd = format(date, "yyyy-MM-dd");
		int[] intArray = StringUtils.splitToIntArray(_ymd, "-");
		return intArray;
	}
  /**
	 * 清除时间字符串的格式信息
	 * 
	 * @param ymd
	 * @return
	 */
	public static String clearFormat(String ymd) throws ParseException {
		Date date = parse(ymd, "yyyy-MM-dd");
		String _ymd = format(date, "yyyyMMdd");
		return _ymd;
	}
  /**
	 * 指定两个日期比较，目标日期比源日期迟返回true，反之，返回false
	 * 
	 * @param ymd1
	 *            源日期
	 * @param ymd2
	 *            目标日期
	 */
	public static boolean isLast(String ymd1, String ymd2) throws ParseException {
		int ymd1Int = MathUtils.parseInt(clearFormat(ymd1));
		int ymd2Int = MathUtils.parseInt(clearFormat(ymd2));
		return ymd2Int > ymd1Int;
	}
  /**
	 * @param symd
	 * @param eymd
	 * @return
	 * @throws ParseException
	 */
	public static int getBetweenDays(String symd, String eymd) throws ParseException {
		int[] ymdL = splitYMD(symd);
		GregorianCalendar gcStart = new GregorianCalendar(ymdL[0], ymdL[1], ymdL[2]);
		ymdL = splitYMD(eymd);
		GregorianCalendar gcEnd = new GregorianCalendar(ymdL[0], ymdL[1], ymdL[2]);
		long longStart = gcStart.getTimeInMillis();
		long longEnd = gcEnd.getTimeInMillis();
		int days = (int)((longEnd - longStart) / 86400000L);
		return days;
	}
  /**
	* 取得当前日期是多少周
	* 
	* @param date
	* @return
	*/
	public static int getWeekOfYear(Date date) {
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setMinimalDaysInFirstWeek(7);
		c.setTime (date);
		return c.get(Calendar.WEEK_OF_YEAR);
	}
	/**
	* 得到某一年周的总数
	* 
	* @param year
	* @return
	*/
	public static int getMaxWeekNumOfYear(int year) {
		Calendar c = new GregorianCalendar();
		c.set(year, Calendar.DECEMBER, 31, 23, 59, 59);
		return getWeekOfYear(c.getTime());
	}
	/**
	* 得到某年某周的第一天
	* 
	* @param year 
	* @param week
	* @return
	*/
	public static Date getFirstDayOfWeek(int year, int week) {
		Calendar c = new GregorianCalendar();
		c.set(Calendar.YEAR, year);
		c.set (Calendar.MONTH, Calendar.JANUARY);
		c.set(Calendar.DATE, 1);
		Calendar cal = (GregorianCalendar) c.clone();
		cal.add(Calendar.DATE, week * 7);
		return getFirstDayOfWeek(cal.getTime());
	}
	/**
	* 得到某年某周的最后一天
	* 
	* @param year
	* @param week
	* @return
	*/
	public static Date getLastDayOfWeek(int year, int week) {
		Calendar c = new GregorianCalendar(); 
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, Calendar.JANUARY);
		c.set(Calendar.DATE, 1);
		Calendar cal = (GregorianCalendar) c.clone();
		cal.add(Calendar.DATE , week * 7);
		return getLastDayOfWeek(cal.getTime());
	}
	/**
	* 取得指定日期所在周的第一天
	* 
	* @param date
	* @return
	*/
	public static Date getFirstDayOfWeek(Date date) {
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
		return c.getTime ();
	}
	/**
	 * 获得 上xxx周的第一天
	 * @param prevNum
	 * @return
	 */
	public static Date getFirstDayOfPrevWeek(int prevNum) {
		Date currentDate = currentDate();
		int weekNum = getWeekOfYear(currentDate);
		return getFirstDayOfWeek(getYear(currentDate), weekNum - prevNum);
	}
	/**
	 * 获得 上xxx周的最后一天
	 * @param prevNum
	 * @return
	 */
	public static Date getLastDayOfPrevWeek(int prevNum) {
		Date currentDate = currentDate();
		int weekNum = getWeekOfYear(currentDate);
		return getLastDayOfWeek(getYear(currentDate), weekNum - prevNum);
	}
	/**
	 * 获得 下xxx周的第一天
	 * @param prevNum
	 * @return
	 */
	public static Date getFirstDayOfNextWeek(int nextNum) {
		Date currentDate = currentDate();
		int weekNum = getWeekOfYear(currentDate);
		return getFirstDayOfWeek(getYear(currentDate), weekNum + nextNum);
	}
	/**
	 * 获得 下xxx周的最后一天
	 * @param prevNum
	 * @return
	 */
	public static Date getLastDayOfNextWeek(int nextNum) {
		Date currentDate = currentDate();
		int weekNum = getWeekOfYear(currentDate);
		return getLastDayOfWeek(getYear(currentDate), weekNum + nextNum);
	}
	/**
	* 取得指定日期所在周的最后一天
	* 
	* @param date
	* @return
	*/
	public static Date getLastDayOfWeek(Date date) {
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday
		return c.getTime();
	}
	/**
	 * 获取指定日期所在月的第一天
	 * @param date
	 * @return
	 */
	public static Date getFirstDayOfMonth(Date date){
		Calendar c = new GregorianCalendar();
		c.setTime(date);
		c.set(Calendar.DATE,1);
		return c.getTime();
	}
	/**
	 * 获取指定日期所在月的最后一天
	 * @param date
	 * @return
	 */
	public static Date getLastDayOfMonth(Date date){
		Calendar c = new GregorianCalendar();
		c.setTime(date);
		c.set(Calendar.DATE,1);
		c.roll(Calendar.DATE, -1);
		return c.getTime();
	}	
	/**
	 * 获取某年某月的最后一天
	 * @param year
	 * @param month
	 * @return
	 */
	public static Date getLastDayOfMonth(int year,int month){
		Calendar c = new GregorianCalendar();
		c.set(year,month-1,1);
		c.set(Calendar.DATE,1);
		c.roll(Calendar.DATE, -1);
		return c.getTime();
	}
}