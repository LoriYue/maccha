package org.maccha.base.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import org.apache.log4j.Logger;

public class NumberFormatUtils
{
  public static Logger logger = Logger.getLogger(NumberFormatUtils.class.getName());

  public static String formatNumber(double d, String pattern, Locale locale)
  {
    String s = "";
    try {
      DecimalFormat nf = (DecimalFormat)NumberFormat.getInstance(locale);
      nf.applyPattern(pattern);
      s = nf.format(d);
    } catch (Exception e) {
      e.printStackTrace();
      logger.error(" formatNumber is error!");
    }
    return s;
  }

  public static String formatNumber(Object d, String pattern, Locale locale)
  {
    String s = "";
    try {
      DecimalFormat nf = (DecimalFormat)NumberFormat.getInstance(locale);
      nf.applyPattern(pattern);
      s = nf.format(d);
    } catch (Exception e) {
      e.printStackTrace();
      logger.error(" formatNumber is error!");
    }
    return s;
  }

  public static String formatNumber(Float d, String pattern)
  {
    return formatNumber(d, pattern, Locale.getDefault());
  }

  public static String formatNumber(Double d, String pattern)
  {
    return formatNumber(d, pattern, Locale.getDefault());
  }

  public static String formatNumber(double d, String pattern)
  {
    return formatNumber(d, pattern, Locale.getDefault());
  }

  public static String formatCurrency(double d, String pattern, Locale l)
  {
    String s = "";
    try {
      DecimalFormat nf = (DecimalFormat)NumberFormat.getCurrencyInstance(l);
      nf.applyPattern(pattern);
      s = nf.format(d);
    } catch (Exception e) {
      e.printStackTrace();
      logger.error(" formatNumber is error!");
    }
    return s;
  }

  public static String formatCurrency(double d, String pattern)
  {
    return formatCurrency(d, pattern, Locale.getDefault());
  }

  public static String formatCurrency(double d)
  {
    String s = "";
    try {
      DecimalFormat nf = (DecimalFormat)NumberFormat.getCurrencyInstance();
      s = nf.format(d);
    }
    catch (Exception e) {
      e.printStackTrace();
      logger.error(" formatNumber is error!");
    }
    return s;
  }

  public static String formatPercent(double d, String pattern, Locale l)
  {
    String s = "";
    try {
      DecimalFormat df = (DecimalFormat)NumberFormat.getPercentInstance(l);
      df.applyPattern(pattern);
      s = df.format(d);
    } catch (Exception e) {
      logger.error("formatPercent is error!", e);
    }
    return s;
  }

  public static String formatPercent(double d, String pattern)
  {
    return formatPercent(d, pattern, Locale.getDefault());
  }

  public static String formatPercent(double d)
  {
    String s = "";
    try {
      DecimalFormat df = (DecimalFormat)NumberFormat.getPercentInstance();
      s = df.format(d);
    } catch (Exception e) {
      logger.error("formatPercent is error!", e);
    }
    return s;
  }

  public static String numberFormat(BigDecimal bd, String format)
  {
    if ((bd == null) || ("0".equals(bd.toString()))) {
      return "";
    }

    DecimalFormat bf = new DecimalFormat(format);
    return bf.format(bd);
  }

}
