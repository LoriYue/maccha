package org.maccha.dao;

import java.io.PrintStream;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import org.maccha.dao.impl.DaoServiceImpl;
import org.maccha.spring.SpringManager;
import org.maccha.base.util.ConfigUtils;
import org.maccha.base.util.StringUtils;

public final class SqlFunc {
	private static Logger logger = Logger.getLogger(SqlFunc.class);
	public static void parseSqlFunc(StringBuffer _strSQLQuery,Map _paraMap ){
		//##:projType##
		String _regexStr = "(##@([\\+\\w]*)\\(?(([[^\\x00-\\xff]:.\\'\\[\\]\\/\\w]*\\,*)*)\\)?##)";
		Pattern _patt = Pattern.compile(_regexStr);
		Matcher _matc =null;
		String _sqlExpr = null;
		String _sqlFunc=null;
		String _sqlParam =null;
		try{
			_matc=_patt.matcher(_strSQLQuery);
			for(;_matc!=null&&_matc.find();){
				_sqlExpr = _matc.group(1);
				_sqlFunc = _matc.group(2);
				_sqlParam =_matc.group(3);
				sqlFunc2Sql(_sqlFunc,_sqlParam,_paraMap,_matc,_strSQLQuery);
				_matc.reset();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void sqlFunc2Sql(String _sqlFunc,String _sqlParam,Map _paraMap,Matcher _matc,StringBuffer _strSQLQuery){
		if(StringUtils.isNull(_sqlFunc))return;
		String _newSqlFunc=SqlFunc.parseSqlFunc(_sqlFunc,_sqlParam,_paraMap);
		String _newStrSQLQuery=_matc.replaceFirst(_newSqlFunc.toString());
		_strSQLQuery.replace(0,_strSQLQuery.length(), _newStrSQLQuery);
	}
	
	public static String parseSqlFunc(String _sqlFunc,String _paraSql,Map _paraMap){
		//数据权限控制，根据config配置解析对象配置，解析sql.xml时,按照数据权限进行拼接过滤 
		if("dataFilter".equalsIgnoreCase(_sqlFunc)){
			SqlFuncParser _dataFilterSqlFuncParser = SpringManager.getComponent("dataFilterSqlFuncParser");
			logger.info("数据权限“dataFilterSqlFuncParser”,实现对象为:"+_dataFilterSqlFuncParser);
			if(_dataFilterSqlFuncParser == null ){
				logger.error("数据权限“dataFilterSqlFuncParser” 对象加载失败,请检查配置!");
				return "1=1";
			}
			String _filterSql= _dataFilterSqlFuncParser.parse(_paraSql);
			if(StringUtils.isNotNull(_filterSql))return _filterSql;
			else return "1=2";
		}
		//老的数据权限控制，已经作废 jhf 2014-12-16
		if("dataRight".equalsIgnoreCase(_sqlFunc)){
			String _dataRightSqlFuncParserClassName=ConfigUtils.getValue("dataRightSqlFuncParser");
			SqlFuncParser _dataRightSqlFuncParser=null;
			try{
				_dataRightSqlFuncParser=(SqlFuncParser)Class.forName(_dataRightSqlFuncParserClassName).newInstance();
			   String _filterSql= _dataRightSqlFuncParser.parse(_paraSql);
			   if(StringUtils.isNotNull(_filterSql))
				   return _filterSql;
			}catch(Exception e){}
			return "1=1";
		}
		if("charIndex".equalsIgnoreCase(_sqlFunc))
			return charIndex(_paraSql);
		if("in".equalsIgnoreCase(_sqlFunc))
			return in(_paraSql,_paraMap);
		if("notIn".equalsIgnoreCase(_sqlFunc))
			return notIn(_paraSql,_paraMap);
		if("isInclude".equalsIgnoreCase(_sqlFunc))
			return isInclude(_paraSql);
		if("isNull".equalsIgnoreCase(_sqlFunc))
			return isNull(_paraSql);
		if("+".equalsIgnoreCase(_sqlFunc)){
			return addOpt(_paraSql);
		}
		if("top".equalsIgnoreCase(_sqlFunc))
			return top(_paraSql);
		if("rownumEq".equalsIgnoreCase(_sqlFunc))
			return rownumEq(_paraSql);
		if("getDictName".equalsIgnoreCase(_sqlFunc))
			return getDictName(_paraSql);
		if("concat".equalsIgnoreCase(_sqlFunc))
			return concat(_paraSql);
		if("zh_cn".equalsIgnoreCase(_sqlFunc))
			return zh_cn(_paraSql);
		return null;
	}

	private static String getDictName(String _paraSql){
	    String _subSql="select d.dict_name from bizframe_dict_dictionary d ";
	    String _group="";
	    String _clunmName="";
	    _subSql+=" where d.dict_group="+_group+" \n";
	    _subSql+=" and d.dict_code="+_clunmName+"\n";
		return _subSql;
	}
	
	private static String top(String _paraSql){
		String _dbName=DaoServiceImpl.getDbName();
		if(DaoServiceImpl.DB_SQLSERVER.equals(_dbName)){
			return "top "+_paraSql;
		}
		if(DaoServiceImpl.DB_MYSQL.equals(_dbName)){
			return "";
		}
		if(DaoServiceImpl.DB_ORACLE.equals(_dbName))
			return "";
		return null;
	}
	private static String zh_cn(String _paraSql){
		String _dbName=DaoServiceImpl.getDbName();
		if(DaoServiceImpl.DB_SQLSERVER.equals(_dbName)){
			return _paraSql;
		}
		if(DaoServiceImpl.DB_MYSQL.equals(_dbName)){
			return "_gbk"+_paraSql;
		}
		if(DaoServiceImpl.DB_ORACLE.equals(_dbName))
			return _paraSql;
		return null;
	}
	private static String rownumEq(String _paraSql){
		String _dbName=DaoServiceImpl.getDbName();
		if(DaoServiceImpl.DB_SQLSERVER.equals(_dbName)){
			return "1=1";
		}
		if(DaoServiceImpl.DB_MYSQL.equals(_dbName)){
			return "1=1";
		}
		if(DaoServiceImpl.DB_ORACLE.equals(_dbName))
			return " rownum="+_paraSql;
		return null;
	}

	
	private static String addOpt(String _paraSql){
		String _dbName=DaoServiceImpl.getDbName();
		if(DaoServiceImpl.DB_SQLSERVER.equals(_dbName)){
			return "+";
		}
		if(DaoServiceImpl.DB_MYSQL.equals(_dbName)){
			return "+";
		}
		if(DaoServiceImpl.DB_ORACLE.equals(_dbName))
			return "||";
		return null;
	}
	private static String concat(String _paraSql){
		String _dbName=DaoServiceImpl.getDbName();
		System.out.println(":::::::::::::::::::::::::::::::::::_paraSql = " + _paraSql);
		if(DaoServiceImpl.DB_SQLSERVER.equals(_dbName)){
			StringBuffer _paraSqlBuff=new StringBuffer();
			String[] _arrayParam= _paraSql.split(",") ;
			for(int i=0 ;i<_arrayParam.length;i++){
				if(i==_arrayParam.length-1){
					_paraSqlBuff.append(_arrayParam[i]);
				}else{
					_paraSqlBuff.append(_arrayParam[i]).append("+");
				}
			}
			return _paraSqlBuff.toString();
		}
		if(DaoServiceImpl.DB_MYSQL.equals(_dbName)){
			return "concat("+_paraSql+")";
		}
		if(DaoServiceImpl.DB_ORACLE.equals(_dbName)){
			StringBuffer _paraSqlBuff=new StringBuffer();
			String[] _arrayParam= _paraSql.split(",") ;
			for(int i=0 ;i<_arrayParam.length;i++){
				if(i==_arrayParam.length-1){
					_paraSqlBuff.append(_arrayParam[i]);
				}else{
					_paraSqlBuff.append(_arrayParam[i]).append("||");
				}
			}
			return _paraSqlBuff.toString();
		}
		return null;
	}
	
	private static String charIndex(String _paraSql){
		String _dbName=DaoServiceImpl.getDbName();
		if(DaoServiceImpl.DB_SQLSERVER.equals(_dbName)){
			return "CHARINDEX("+_paraSql.split(",")[1]+","+_paraSql.split(",")[0]+")";
		}
		if(DaoServiceImpl.DB_MYSQL.equals(_dbName)){
			return "Instr("+_paraSql+")";
		}
		if(DaoServiceImpl.DB_ORACLE.equals(_dbName))
			return "Instr("+_paraSql+")";
		
		return null;
	}
	
	
	private static String isInclude(String _paraSql){
		String _dbName=DaoServiceImpl.getDbName();
		if(DaoServiceImpl.DB_SQLSERVER.equals(_dbName)){
			return "CHARINDEX("+_paraSql.split(",")[1]+","+_paraSql.split(",")[0]+")>0";
		}
		if(DaoServiceImpl.DB_MYSQL.equals(_dbName)){
			return "Instr("+_paraSql+")>0";
		}
		if(DaoServiceImpl.DB_ORACLE.equals(_dbName))
			return "Instr("+_paraSql+")>0";
		return null;
	}
	
	private static String in(String _paraSql,Map _paraMap){
		String _columnName=_paraSql.split(",")[0];
		String _inValues=_paraMap.get((_paraSql.split(",")[1]+"").replaceAll(":",""))+"";
		_inValues=_inValues.replaceAll(",", "','");
		if(StringUtils.isNotNull(_inValues))
			_inValues="'"+_inValues+"'";
		if(StringUtils.isNull(_inValues))
			return "1=1";
		return _columnName+" in("+_inValues+")";
	}
	
	private static String notIn(String _paraSql,Map _paraMap){
		String _columnName=_paraSql.split(",")[0];
		String _inValues=_paraMap.get((_paraSql.split(",")[1]+"").replaceAll(":",""))+"";
		_inValues=_inValues.replaceAll(",", "','");
		if(StringUtils.isNotNull(_inValues))
			_inValues="'"+_inValues+"'";
		if(StringUtils.isNull(_inValues))
			return "1=1";
		return _columnName+" not in("+_inValues+")";
	}
	
	private static String isNull(String _paraSql){
		String _dbName=DaoServiceImpl.getDbName();
		if(DaoServiceImpl.DB_SQLSERVER.equals(_dbName))
			return "isNull("+_paraSql+")";		
		if(DaoServiceImpl.DB_MYSQL.equals(_dbName))
			return "IFNull("+_paraSql+")";	
		if(DaoServiceImpl.DB_ORACLE.equals(_dbName))
			return "nvl("+_paraSql+")";
		return null;
	}
}
