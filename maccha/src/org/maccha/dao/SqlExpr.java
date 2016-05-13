package org.maccha.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.maccha.base.util.ClassUtils;
import org.maccha.base.util.StringUtils;

public class SqlExpr {
	/**
	 * 
	 * @param _strSQLQuery
	 * @param _paraMap
	 */
	public static void parseExpr(StringBuffer _strSQLQuery,Map _paraMap ){
		//##:projType##
		String _regexStr = "(##:(\\w*)##)";
		Pattern _patt = Pattern.compile(_regexStr);
		Matcher _matc =null;
		String _sqlExpr = null;
		String _sqlParam =null;
		try{
			_matc=_patt.matcher(_strSQLQuery);
			for(;_matc!=null&&_matc.find();){
				
				_sqlExpr = _matc.group(1);
				_sqlParam = _matc.group(2);
				expr2Sql(_sqlParam,_paraMap,_matc,_strSQLQuery);
				_matc.reset();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	private  static void expr2Sql(String _paraName,Map _paraMap,Matcher _matc,StringBuffer _strSQLQuery){
	    Object _paraValue=_paraMap.get(_paraName);
	    if(null ==_paraValue) _paraValue = "";
		String newStrSQLQuery=_matc.replaceFirst(_paraValue.toString());
		_strSQLQuery.replace(0,_strSQLQuery.length(), newStrSQLQuery);
	}
	public static void parseJavaFuncExpr(StringBuffer _strSQLQuery,Map _paraMap ){
		String _regexStr = "(##\\$((([a-zA-Z_][a-zA-Z0-9_]*).([a-zA-Z][A-Za-z0-9_]*))*)\\((([:\\w]*\\,*)*)\\)##)";
		Pattern _patt = Pattern.compile(_regexStr);
		Matcher _matc =null;
		String _funcExpr = null;
		String _className =null;
		String _methodName =null;
		String _paraNames =null;
		try{
			_matc=_patt.matcher(_strSQLQuery);
			for(;_matc!=null&&_matc.find();){
				_funcExpr = _matc.group(1);
				_className = _matc.group(2);
				_methodName = _matc.group(5);
				_paraNames = _matc.group(6);
				_className = _className.replaceAll("."+_methodName, "");
				System.out.println("className="+_className+" methodName="+_methodName+" paraNames="+_paraNames);
				javaFuncExpr2Sql(_className,_methodName,_paraNames,_paraMap,_matc,_strSQLQuery);
				_matc.reset();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private static void javaFuncExpr2Sql(String _className,String _methodName,String _paraNames,Map _paraMap,Matcher _matc,StringBuffer _strSQLQuery){
		String _value="";
		try{
		   Class _inst=Class.forName(_className);
		   List _paraValueList=new ArrayList();
		   if(StringUtils.isNotNull(_paraNames)){
			   String[] _paraNameArray=_paraNames.split(",");
			   for(int i=0;_paraNameArray!=null&&i<_paraNameArray.length;i++){
			     if(StringUtils.isNull(_paraNameArray[i]))continue;
			     _paraNameArray[i]=_paraNameArray[i].replaceAll(":", "");
			     Object _paraValue=_paraMap.get(_paraNameArray[i]);
			     if(_paraValue!=null)_paraValueList.add(_paraValue.toString());
			   }
		   }
		   System.out.println("paraValue:"+_paraValueList);
		   _value=ClassUtils.invokeStaticMethod(_inst, _methodName,_paraValueList.toArray())+"";
		}catch(Exception e){
			e.printStackTrace();
			_value="";
		}
		String newStrSQLQuery=_matc.replaceFirst(_value);
		_strSQLQuery.replace(0,_strSQLQuery.length(), newStrSQLQuery);
	}
}
