package org.maccha.base.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.maccha.dao.SqlFunc;

/**
 * <p>Title: MathUtils</p>
 * <p>Description: 处理算术通用工具类</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: 北京东方信软信息技术有限公司</p>
 * @author not attributable
 * @version 1.0
 */

public class MathUtils {
  /**
   *解析整数
   *@param value 解析字符串
   **/
	public static int parseInt(String value){
		return Integer.parseInt(value);
	}
  
	public static int parseInt(){
		return 234;
	}
	
	private static void parseSqlFunc(StringBuffer _strSQLQuery,Map _paraMap ){
		String _regexStr = "(##@([\\+\\w]*)\\(?(([[^\\x00-\\xff]:.\\[\\]\\'\\/\\w]*\\,*)*)\\)?##)";
		Pattern _patt = Pattern.compile(_regexStr);
		Matcher _matc =null;
		String _sqlExpr = null;
		String _sqlFunc=null;
		String _sqlParam =null;
		try{
			_matc=_patt.matcher(_strSQLQuery);
			for(;_matc.find();){
				_sqlExpr = _matc.group(1);
				_sqlFunc = _matc.group(2);
				_sqlParam =_matc.group(3);
				System.out.println("sqlExpr="+_sqlExpr);
				System.out.println("_sqlFunc="+_sqlFunc);
				System.out.println("_sqlParam="+_sqlParam);
				sqlFunc2Sql(_sqlFunc,_sqlParam,_paraMap,_matc,_strSQLQuery);
				_matc.reset();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private static void sqlFunc2Sql(String _sqlFunc,String _sqlParam,Map _paraMap,Matcher _matc,StringBuffer _strSQLQuery){
		if(StringUtils.isNull(_sqlFunc))return;
		String _newSqlFunc=SqlFunc.parseSqlFunc(_sqlFunc,_sqlParam,_paraMap);
		String _newStrSQLQuery=_matc.replaceFirst(_newSqlFunc.toString());
		_strSQLQuery.replace(0,_strSQLQuery.length(), _newStrSQLQuery);
	}
	
	private static void parseExpr(StringBuffer _strSQLQuery,Map _paraMap ){
		String _regexStr = "(##:(\\w*)##)";
		Pattern _patt = Pattern.compile(_regexStr);
		Matcher _matc =null;
		String _sqlExpr = null;
		String _sqlParam =null;
		try{
			_matc=_patt.matcher(_strSQLQuery);
			for(;_matc.find();){
				_sqlExpr = _matc.group(1);
				_sqlParam = _matc.group(2);
				System.out.println("sqlExpr="+_sqlExpr);
				System.out.println("sqlParam="+_sqlParam);
				expr2Sql(_sqlParam,_paraMap,_matc,_strSQLQuery);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private static void expr2Sql(String _paraName,Map _paraMap,Matcher _matc,StringBuffer _strSQLQuery){
		Object _paraValue=_paraMap.get(_paraName);
		if(StringUtils.isNull(_paraValue))return;
		String newStrSQLQuery=_matc.replaceAll(_paraValue.toString());
		_strSQLQuery.replace(0,_strSQLQuery.length(), newStrSQLQuery);
		System.out.println(_strSQLQuery);
	}
		
	private  void parseJavaFuncExpr(StringBuffer _strSQLQuery,Map _paraMap ){
		String _regexStr = "(##\\$((([a-zA-Z_][a-zA-Z0-9_]*).([a-zA-Z][A-Za-z0-9_]*))*)\\((([:\\w]*\\,*)*)\\)##)";
		Pattern _patt = Pattern.compile(_regexStr);
		Matcher _matc =null;
		String _funcExpr = null;
		String _className =null;
		String _methodName =null;
		String _paraNames =null;
		try{
			_matc=_patt.matcher(_strSQLQuery);
			for(;_matc.find();){
				_funcExpr = _matc.group(1);
				_className = _matc.group(2);
				_methodName = _matc.group(5);
				_paraNames = _matc.group(6);
				_className = _className.replaceAll("."+_methodName, "");
				System.out.println("funcExpr="+_funcExpr);
				System.out.println("className="+_className+" methodName="+_methodName+" paraNames="+_paraNames);
				javaFuncExpr2Sql(_className,_methodName,_paraNames,_paraMap,_matc,_strSQLQuery);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
		
	private void javaFuncExpr2Sql(String _className,String _methodName,String _paraNames,Map _paraMap,Matcher _matc,StringBuffer _strSQLQuery){
		String _value="";
		try{
		   Object _inst=Class.forName(_className).newInstance();
		   List _paraValueList=new ArrayList();
		   if(StringUtils.isNotNull(_paraNames)){
			   String[] _paraNameArray=_paraNames.split(",");
			   for(int i=0;_paraNameArray!=null&&i<_paraNameArray.length;i++){
			     if(StringUtils.isNull(_paraNameArray[i]))continue;
			     _paraNameArray[i]=_paraNameArray[i].replaceAll(":", "");
			     Object _paraValue=_paraMap.get(_paraNameArray[i]);
			     System.out.println(_paraNameArray[i]+"="+_paraValue);
			     if(_paraValue!=null)
			     _paraValueList.add(_paraValue.toString());
			   }
		   }
		   System.out.println("paraValue:"+_paraValueList);
		   _value=ClassUtils.invoke(_inst, _methodName,_paraValueList.toArray())+"";
		}catch(Exception e){
			e.printStackTrace();
			_value="";
		}
		String newStrSQLQuery=_matc.replaceAll(_value);
		_strSQLQuery.replace(0,_strSQLQuery.length(), newStrSQLQuery);
		System.out.println("after funcExpr2Sql:"+_strSQLQuery);
	}
	
  /**
   *解析双精度
   *@param value 解析字符串
   *@param dot 保留小数的位数 取值范围10,100,1000,10000
           分别表示 保留1,2,3,4位小数
   **/
	public static double parseDouble(String value, int dot) {
		double dvalue = Double.parseDouble(value);
		dvalue = Math.round(dvalue * dot) / (double) dot;
		return dvalue;
	}

  /**
   *解析双精度(保留两位小数)
   *@param value 解析字符串
   **/
	public static double parseDouble(String value) {
		return parseDouble(value, 100);
	}

  /**
   *解析浮点数
   *@param value 解析字符串
   *@param dot 保留小数的位数 取值范围10,100,1000,10000
           分别表示 保留1,2,3,4位小数
   **/
	public static float parseFloat(String value, int dot) {
		float fvalue = Float.parseFloat(value);
		fvalue = Math.round(fvalue * dot) / (float) dot;
		return fvalue;
	}

  /**
   *解析浮点数(保留两位小数)
   *@param value 解析字符串
   **/
	public static float parseFloat(String value) {
		return parseFloat(value, 100);
	}

  /**
   *两数相加
   *@param augend 被加数
   *@param addend 加数
   **/
	public static String sum(String augend, String addend) {
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

  /**
   *两数相乘
   *@param faciend       被乘数
   *@param multiplier    乘数
   *@param dot 保留小数的位数 取值范围10,100,1000,10000
          分别表示 保留1,2,3,4位小数
   **/
	public static String mul(String faciend, String multiplier, int dot) {
		double d_faciend = Double.parseDouble(faciend);
		double d_multiplier = Double.parseDouble(multiplier);
		double d_amass = d_faciend * d_multiplier;
		d_amass = Math.round(d_amass * dot) / (double) dot;
		return d_amass + "";
	}

  /**
   *两数相乘(保留两位小数)
   *@param faciend 被乘数
   *@param multiplier 乘数
   **/
	public static String mul(String faciend, String multiplier) {
		return mul(faciend, multiplier, 100);
	}

  /**
   *两数相除
   *@param dividend 被除数
   *@param divisor  除数       quotient  商数
   *@param dot 保留小数的位数 取值范围10,100,1000,10000
          分别表示 保留1,2,3,4位小数
   **/
	public static String rate(String dividend, String divisor, int dot) {
		double d_dividend = Double.parseDouble(dividend);
		double d_divisor = Double.parseDouble(divisor);
		double d_quotient = d_dividend / d_divisor;
		d_quotient = Math.round(d_quotient * dot) / (double) dot;
		return d_quotient + "";
	}

  /**
   *两数相除(保留两位小数)
   *@param dividend 被除数
   *@param divisor 除数
   **/
	public static String rate(String dividend, String divisor) {
		return rate(dividend, divisor, 100);
	}

  /**
   *两数的百分数计算
   *@param a 分子 字符串
   *@param b 分母 字符串
   **/

	public static String percent(String a, String b) {
		double d_a = 0;
		double d_b = 0;
		double d_rate = 0;
		String rate = "";
		try {
			if (b == null || b == "0" || b.trim().length() <= 0)
				return "-";
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	    try {
	      d_a = Double.parseDouble(a);
	    } catch (Exception e) {
	      d_a = 0;
	    }
	    try {
	      d_b = Double.parseDouble(b);
	    } catch (Exception e) {
	      d_b = 0;
	    }
	    try {
	      d_rate = (d_a / d_b);
	      rate = (Math.round(d_rate * 10000)) / 100 + "%";
	    } catch (Exception e) {
	      rate = "0%";
	    }
	    if (rate.equals("0%"))
	      rate = "-";
	    return rate;
	  }
}