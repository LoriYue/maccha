package org.maccha.base.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.maccha.dao.SqlFunc;

public class MathUtils
{
  public static int parseInt(String value)
  {
    return Integer.parseInt(value);
  }

  public static int parseInt() {
    return 234;
  }

  public static void main(String[] args)
  {
    Map _paraMap = new HashMap();

    StringBuffer _strSQLQuery = new StringBuffer("select ##@top(2)## ##@top(29)## ##@concat('[a]',aa.b,'cc')##,##@china('北京')## from simpro_bizframe_org_department aa where ##@rownumEq(1)## and ##@rownumEq(12)## (:isDeleted is null or depa_is_deleted=:isDeleted) and depa_id=##$org.simpro.util.MathUtils.parseInt()## and job_id in(##$org.simpro.util.MathUtils.parseInt()##) order by ##:jobId##");

    System.out.println("aaaa\\nbb");
    parseSqlFunc(_strSQLQuery, _paraMap);

    System.out.println(_strSQLQuery);
  }

  private static void parseSqlFunc(StringBuffer _strSQLQuery, Map _paraMap)
  {
    String _regexStr = "(##@([\\+\\w]*)\\(?(([[^\\x00-\\xff]:.\\[\\]\\'\\/\\w]*\\,*)*)\\)?##)";

    Pattern _patt = Pattern.compile(_regexStr);
    Matcher _matc = null;
    String _sqlExpr = null;
    String _sqlFunc = null;
    String _sqlParam = null;
    try
    {
      _matc = _patt.matcher(_strSQLQuery);
      while (_matc.find()) {
        _sqlExpr = _matc.group(1);
        _sqlFunc = _matc.group(2);
        _sqlParam = _matc.group(3);
        System.out.println("sqlExpr=" + _sqlExpr);
        System.out.println("_sqlFunc=" + _sqlFunc);
        System.out.println("_sqlParam=" + _sqlParam);
        sqlFunc2Sql(_sqlFunc, _sqlParam, _paraMap, _matc, _strSQLQuery);
        _matc.reset();
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void sqlFunc2Sql(String _sqlFunc, String _sqlParam, Map _paraMap, Matcher _matc, StringBuffer _strSQLQuery)
  {
    if (StringUtils.isNull(_sqlFunc)) return;

    String _newSqlFunc = SqlFunc.parseSqlFunc(_sqlFunc, _sqlParam, _paraMap);

    String _newStrSQLQuery = _matc.replaceFirst(_newSqlFunc.toString());

    _strSQLQuery.replace(0, _strSQLQuery.length(), _newStrSQLQuery);
  }

  private static void parseExpr(StringBuffer _strSQLQuery, Map _paraMap)
  {
    String _regexStr = "(##:(\\w*)##)";

    Pattern _patt = Pattern.compile(_regexStr);
    Matcher _matc = null;
    String _sqlExpr = null;
    String _sqlParam = null;
    try {
      _matc = _patt.matcher(_strSQLQuery);
      while (_matc.find())
      {
        _sqlExpr = _matc.group(1);
        _sqlParam = _matc.group(2);
        System.out.println("sqlExpr=" + _sqlExpr);
        System.out.println("sqlParam=" + _sqlParam);
        expr2Sql(_sqlParam, _paraMap, _matc, _strSQLQuery);
      }
    }
    catch (Exception e)
    {
    }
  }

  private static void expr2Sql(String _paraName, Map _paraMap, Matcher _matc, StringBuffer _strSQLQuery)
  {
    Object _paraValue = _paraMap.get(_paraName);

    if (StringUtils.isNull(_paraValue)) return;

    String newStrSQLQuery = _matc.replaceAll(_paraValue.toString());

    _strSQLQuery.replace(0, _strSQLQuery.length(), newStrSQLQuery);

    System.out.println(_strSQLQuery);
  }

  private void parseJavaFuncExpr(StringBuffer _strSQLQuery, Map _paraMap)
  {
    String _regexStr = "(##\\$((([a-zA-Z_][a-zA-Z0-9_]*).([a-zA-Z][A-Za-z0-9_]*))*)\\((([:\\w]*\\,*)*)\\)##)";

    Pattern _patt = Pattern.compile(_regexStr);

    Matcher _matc = null;
    String _funcExpr = null;
    String _className = null;
    String _methodName = null;
    String _paraNames = null;
    try
    {
      _matc = _patt.matcher(_strSQLQuery);

      while (_matc.find())
      {
        _funcExpr = _matc.group(1);
        _className = _matc.group(2);
        _methodName = _matc.group(5);
        _paraNames = _matc.group(6);

        _className = _className.replaceAll("." + _methodName, "");

        System.out.println("funcExpr=" + _funcExpr);
        System.out.println("className=" + _className + " methodName=" + _methodName + " paraNames=" + _paraNames);

        javaFuncExpr2Sql(_className, _methodName, _paraNames, _paraMap, _matc, _strSQLQuery);
      }
    }
    catch (Exception e)
    {
    }
  }

  private void javaFuncExpr2Sql(String _className, String _methodName, String _paraNames, Map _paraMap, Matcher _matc, StringBuffer _strSQLQuery)
  {
    String _value = "";
    try
    {
      Object _inst = Class.forName(_className).newInstance();
      List _paraValueList = new ArrayList();

      if (StringUtils.isNotNull(_paraNames)) {
        String[] _paraNameArray = _paraNames.split(",");

        for (int i = 0; (_paraNameArray != null) && (i < _paraNameArray.length); i++) {
          if (!StringUtils.isNull(_paraNameArray[i])) {
            _paraNameArray[i] = _paraNameArray[i].replaceAll(":", "");
            Object _paraValue = _paraMap.get(_paraNameArray[i]);
            System.out.println(_paraNameArray[i] + "=" + _paraValue);
            if (_paraValue != null)
              _paraValueList.add(_paraValue.toString());
          }
        }
      }
      System.out.println("paraValue:" + _paraValueList);

      _value = ClassUtils.invoke(_inst, _methodName, _paraValueList.toArray()) + "";
    }
    catch (Exception e)
    {
      e.printStackTrace();
      _value = "";
    }

    String newStrSQLQuery = _matc.replaceAll(_value);

    _strSQLQuery.replace(0, _strSQLQuery.length(), newStrSQLQuery);

    System.out.println("after funcExpr2Sql:" + _strSQLQuery);
  }

  public static double parseDouble(String value, int dot)
  {
    double dvalue = Double.parseDouble(value);
    dvalue = Math.round(dvalue * dot) / dot;
    return dvalue;
  }

  public static double parseDouble(String value)
  {
    return parseDouble(value, 100);
  }

  public static float parseFloat(String value, int dot)
  {
    float fvalue = Float.parseFloat(value);
    fvalue = Math.round(fvalue * dot) / dot;
    return fvalue;
  }

  public static float parseFloat(String value)
  {
    return parseFloat(value, 100);
  }

  public static String sum(String augend, String addend)
  {
    String str_augend = "";
    String str_addend = "";
    if (augend.length() >= 2)
      str_augend = augend.substring(0, 2);
    if (addend.length() >= 2)
      str_addend = addend.substring(0, 2);
    if ("--".equals(str_augend))
      augend = augend.substring(2);
    if ("--".equals(str_addend))
      addend = addend.substring(2);
    double d_augend = parseDouble(augend);
    double d_addend = parseDouble(addend);
    double sum = d_augend + d_addend;
    return parseDouble(Double.toString(sum)) + "";
  }

  public static String mul(String faciend, String multiplier, int dot)
  {
    double d_faciend = Double.parseDouble(faciend);
    double d_multiplier = Double.parseDouble(multiplier);
    double d_amass = d_faciend * d_multiplier;
    d_amass = Math.round(d_amass * dot) / dot;
    return d_amass + "";
  }

  public static String mul(String faciend, String multiplier)
  {
    return mul(faciend, multiplier, 100);
  }

  public static String rate(String dividend, String divisor, int dot)
  {
    double d_dividend = Double.parseDouble(dividend);
    double d_divisor = Double.parseDouble(divisor);
    double d_quotient = d_dividend / d_divisor;
    d_quotient = Math.round(d_quotient * dot) / dot;
    return d_quotient + "";
  }

  public static String rate(String dividend, String divisor)
  {
    return rate(dividend, divisor, 100);
  }

  public static String percent(String a, String b)
  {
    double d_a = 0.0D;
    double d_b = 0.0D;
    double d_rate = 0.0D;
    String rate = "";
    try
    {
      if ((b == null) || (b == "0") || (b.trim().length() <= 0))
        return "-";
    }
    catch (Exception e)
    {
    }
    try
    {
      d_a = Double.parseDouble(a);
    }
    catch (Exception e) {
      d_a = 0.0D;
    }
    try {
      d_b = Double.parseDouble(b);
    }
    catch (Exception e) {
      d_b = 0.0D;
    }
    try {
      d_rate = d_a / d_b;
      rate = Math.round(d_rate * 10000.0D) / 100L + "%";
    }
    catch (Exception e) {
      rate = "0%";
    }
    if (rate.equals("0%"))
      rate = "-";
    return rate;
  }
}