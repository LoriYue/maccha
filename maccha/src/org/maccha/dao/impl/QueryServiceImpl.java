package org.maccha.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.type.IntegerType;
import org.maccha.dao.Filter;
import org.maccha.dao.IQueryInterceptor;
import org.maccha.dao.IQueryService;
import org.maccha.dao.OrderBy;
import org.maccha.dao.Page;
import org.maccha.dao.Select;
import org.maccha.dao.SqlExpr;
import org.maccha.dao.SqlFunc;
import org.maccha.dao.type.ObjectType;
import org.maccha.dao.util.DaoUtils;
import org.maccha.base.exception.SysException;
import org.maccha.base.util.ArrayUtils;
import org.maccha.base.util.ClassUtils;
import org.maccha.base.util.ObjectUtils;
import org.maccha.base.util.StringUtils;

public class QueryServiceImpl implements IQueryService, IQueryInterceptor {
	private static Logger log = Logger.getLogger(QueryServiceImpl.class);

	public Object getEntity() {
		List list=this.list();
		if(list==null||list.size()<=0)return null;
		return this.list().get(0);
	}

	// 查询类型(SIMPLE_QUERY COMPLEX_QUERY NAME_QUERY)

	private String queryType = null;
	private Session session = null;
	private Class modelClass = null;
	private Filter filter = null;
	private OrderBy orderBy = null;
	private boolean isReturnCount = true;
	private boolean isResultToMapModel = true;
	private int returnType=this.MAP_RETURN;
	private List<String> resultPrevExprList=new ArrayList<String>();
	private Select complexQuerySelect = null;
    public QueryServiceImpl(){
    }
	/**
	 * 简单查询服务（SIMPLE_QUERY）
	 * 
	 * @param session
	 * @param modelClass 对象模型
	 * @param filter 过滤器
	 */
	public QueryServiceImpl(Session _session, Class _modelClass, Filter _filter, boolean _isReturnCount) {
		this.queryType = QUERY_TYPE_SIMPLE;
		this.session = _session;
		this.modelClass = _modelClass;
		this.filter = _filter;
		this.isReturnCount = _isReturnCount;
	}

	/**
	 * 复合查询
	 * 
	 * @param session
	 * @param modelClass
	 * @param filter
	 */
	public QueryServiceImpl(Session _session, Class modelClass, Select _complexQuerySelect, Filter _filter, boolean _isReturnCount) {
		this.queryType = QUERY_TYPE_COMPLEX;
		this.session = _session;
		this.modelClass = modelClass;
		this.filter = _filter;
		this.complexQuerySelect = _complexQuerySelect;
		this.isReturnCount = _isReturnCount;
	}

	private String _nameQuery_QueryName = null;

	private Map _nameQuery_Parameter = null;

	/**
	 * HBM文件sql名称查询
	 * @param session
	 * @param queryName 名称
	 * @param parameter 参数
	 */
	public QueryServiceImpl(Session session, String queryName, Map parameter, boolean isReturnCount) {
		this(session,queryName,parameter,isReturnCount,false,false) ;
	}
	/**
	 * HBM文件sql名称查询
	 * 
	 * @param session
	 * @param queryName 名称
	 * @param parameter 参数
	 */
	public QueryServiceImpl(Session session, String queryName, Map parameter, boolean isReturnCount, boolean isResultToEntityMapModel) {
		this(session,queryName,parameter,isReturnCount,isResultToEntityMapModel,false) ;
	}

	/**
	 * HBM文件sql名称查询
	 * 
	 * @param session
	 * @param queryName 名称
	 * @param parameter 参数
	 */
	public QueryServiceImpl(Session _session, String _queryName, Map _parameter, boolean _isReturnCount, boolean _isResultToMapModel, boolean _isHQL) {
		if(_isHQL)this.queryType = QUERY_TYPE_HQL_NAMEQUERY; 
		else this.queryType = QUERY_TYPE_SQL_NAMEQUERY;
		this.session = _session;
		this._nameQuery_QueryName = _queryName;
		this._nameQuery_Parameter = _parameter;
		this.isReturnCount = _isReturnCount;
		this.isResultToMapModel = _isResultToMapModel;
	}

	private List fetchGroup = new ArrayList();

	public void setFetchMode(String fetch, int fetchMode) {
		this.fetchGroup.add(fetch);
	}

	public List beginList(int count) {
		return list(0, count);
	}

	public Iterator iterator() {
		excuteQuery();
		return result_dataQuery.iterate();
	}
	public Iterator iterator(int start, int end) {
		excuteQuery();
		result_dataQuery.setFirstResult(start);
		result_dataQuery.setMaxResults(end);
		return result_dataQuery.iterate();
	}
	
	public List lastList(int count) {
		int start = size() - count;
		if (start < 0)start = 0;
		result_dataQuery.setFirstResult(start);
		result_dataQuery.setMaxResults(count);
		List _resultList=result_dataQuery.list();
		parseResultPrevExpr2Value(_resultList);
		return _resultList;
	}

	public List list() {
		excuteQuery();
		List _resultList=result_dataQuery.list();
		parseResultPrevExpr2Value(_resultList);
		return _resultList;
	}

	public List list(int start, int end) {
		excuteQuery();
		result_dataQuery.setFirstResult(start);
		result_dataQuery.setMaxResults(end);
		List _resultList=result_dataQuery.list();
		parseResultPrevExpr2Value(_resultList);
		return _resultList;
	}

	public void parseResultPrevExpr2Value(List _list) {
		if(this.resultPrevExprList.size()<0)return;
		if(isResultToMapModel()){
			for (int i = 0; i < _list.size(); i++) {
				Map _rowMap = (Map) _list.get(i);
				Object[] _keyArray=_rowMap.keySet().toArray();
				for(int j = 0; _keyArray!=null&&j <_keyArray.length; j++){
					Object _key=_keyArray[j];
					Object _valueObj=_rowMap.get(_key);
					if(_valueObj==null)continue;
					if(!(_valueObj instanceof String))continue;
					for(int k=0;k<resultPrevExprList.size();k++){
						String _prevExpr=resultPrevExprList.get(k);
						if(_valueObj.toString().indexOf(_prevExpr)<0)continue;
						String _regexStr =_prevExpr+"\\[([.=\\w\\u4e00-\\u9fa5]*)\\]";
						Pattern _patt = Pattern.compile(_regexStr);
						Matcher _matc =null;
						try{
							_matc=_patt.matcher(_valueObj.toString());
							if(!_matc.find())continue;
							String _paraValuesStr = _matc.group(1);
							String _className=(String)resultPrevExprJavaFuncMap.get(_prevExpr+"_className");
							String _methodName=(String)resultPrevExprJavaFuncMap.get(_prevExpr+"_methodName");
							String _paraNames=(String)resultPrevExprJavaFuncMap.get(_prevExpr+"_paraNames");
							Map _paraMap=(Map)resultPrevExprJavaFuncMap.get(_prevExpr+"_paraMap");
							String[] _paraValues=_paraValuesStr.split("=");
							Map _tempParaMap=StringUtils.splitToMap(_paraValuesStr,",");
							if(_tempParaMap!=null)_paraMap.putAll(_tempParaMap);
							_valueObj=parseResultExpr(_className,_methodName,_paraNames,_paraMap,_matc);
							_matc.reset();
							_rowMap.put(_key,_valueObj);
					    }catch(Throwable t){}
					}
				}
			}
		}
		if(this.returnType==this.OBJECT_RETURN&&_list.size()>0){
			List<String> _fieldList=ObjectUtils.getFields(_list.get(0));
			for(int i = 0;_fieldList!=null&&_fieldList.size()>0&&i <_list.size();i++) {
				Object _rowObj = _list.get(i);
				for(int j = 0; _fieldList!=null&&j <_fieldList.size(); j++){
					String _fieldName=_fieldList.get(j);
					Object _valueObj=null;
					try{
						_valueObj=ClassUtils.get(_rowObj,_fieldList.get(j));
					}catch (Throwable t) {
						_valueObj=null;
						SysException.handleWarn(t);
					}
					if(_valueObj==null)continue;
					if(!(_valueObj instanceof String))continue;
					for(int k=0;k<resultPrevExprList.size();k++){
						String _prevExpr=resultPrevExprList.get(k);
						if(_valueObj.toString().indexOf(_prevExpr)<0)continue;
						String _regexStr =_prevExpr+"\\[([.=\\w\\u4e00-\\u9fa5]*)\\]";
						Pattern _patt = Pattern.compile(_regexStr);
						Matcher _matc =null;
						try{
							_matc=_patt.matcher(_valueObj.toString());
							if(!_matc.find())continue;
							String _paraValuesStr = _matc.group(1);
							String _className=(String)resultPrevExprJavaFuncMap.get(_prevExpr+"_className");
							String _methodName=(String)resultPrevExprJavaFuncMap.get(_prevExpr+"_methodName");
							String _paraNames=(String)resultPrevExprJavaFuncMap.get(_prevExpr+"_paraNames");
							Map _paraMap=(Map)resultPrevExprJavaFuncMap.get(_prevExpr+"_paraMap");
							String[] _paraValues=_paraValuesStr.split("=");
							Map _tempParaMap=StringUtils.splitToMap(_paraValuesStr,",");
							if(_tempParaMap!=null)_paraMap.putAll(_tempParaMap);
							_valueObj=parseResultExpr(_className,_methodName,_paraNames,_paraMap,_matc);
							_matc.reset();
							ClassUtils.set(_rowObj,_fieldName,_valueObj);
						}catch (Throwable t){
							SysException.handleWarn(t);
						}
					}
				}
			}
		}
	}
	
	private  String parseResultExpr(String _className,String _methodName,String _paraNames,Map _paraMap,Matcher _matc){
		Object _objValue=null;
		try{
			Object _inst=Class.forName(_className).newInstance();
			List _paraValueList=new ArrayList();
			if(_inst!=null&&StringUtils.isNotNull(_paraNames)){
				   String[] _paraNameArray=_paraNames.split(",");
				   for(int i=0;_paraNameArray!=null&&i<_paraNameArray.length;i++){
				     if(StringUtils.isNull(_paraNameArray[i]))continue;
				     _paraNameArray[i]=_paraNameArray[i].replaceAll(":", "");
				     Object _paraValue=_paraMap.get(_paraNameArray[i]);
				     if(_paraValue!=null)_paraValueList.add(_paraValue);
				   }
		    }
			log.info(_className+"."+_methodName+">"+_paraNames+":"+_paraValueList);
			if(_inst!=null)_objValue=ClassUtils.invoke(_inst,_methodName,_paraValueList.toArray());
			log.info(_objValue);
		}catch(Throwable t){
			_objValue=null;
		}
		String _valueStr="";
		if(_objValue!=null)_valueStr=_objValue.toString();
		return _matc.replaceFirst(_valueStr);
    }
	
	public List list(Page page) {
		if (page == null) return list();
		if (this.isReturnCount) page.setTotalRowCount(size());
		else excuteQuery();
		result_dataQuery.setFirstResult(page.getStartRow());
		result_dataQuery.setMaxResults(page.getRowCount());
		List _resultList=result_dataQuery.list();
		parseResultPrevExpr2Value(_resultList);
		if (this.complexQuerySelect == null || this.complexQuerySelect.getMode() != Select.CLASSMODEL_MODE) return _resultList;
		String[] propertyNames = this.complexQuerySelect.getPropertyNames();
		Class[] propertyTypes = this.complexQuerySelect.getPropertyTypes();
		List _tempList = new ArrayList();
		for (int i = 0; _resultList!=null&&i < _resultList.size(); i++) {
			try {
				Object modelObj = null;
				modelObj = this.modelClass.newInstance();
				Object[] rowObjs = (Object[]) _resultList.get(i);
				for (int j = 0; j < propertyNames.length; j++)
					ClassUtils.set(modelObj, propertyNames[j], propertyTypes[j], rowObjs[j]);
				_tempList.add(modelObj);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return _tempList;
	}

	public void setOrderBy(OrderBy _orderBy) {
		this.orderBy = _orderBy;
	}

	public OrderBy getOrderBy() {
		return this.orderBy;
	}
	public int size() {
		excuteQuery();
		List list = result_totalQuery.list();
		int count = 0;
		if (list != null && list.size() >= 1) {
			Object objCount = list.get(0);
			if (objCount instanceof Integer) {
				count = ((Integer) list.get(0)).intValue();
			} else count = ((Long) list.get(0)).intValue();
		}
		return count;
	}

	private Query result_totalQuery = null;

	private Query result_dataQuery = null;

	private void excuteQuery() {
		Query queryArray[] = null;
		if (QUERY_TYPE_SIMPLE.equals(this.queryType)) {
			queryArray = excuteSimpleQuery(this.session, this.modelClass, this.filter, this.orderBy);
		}
		if (QUERY_TYPE_SQL_NAMEQUERY.equals(this.queryType)) {
			queryArray = excuteSQLNameQuery(this.session, this._nameQuery_QueryName, this._nameQuery_Parameter, this.orderBy);
		}
		if (QUERY_TYPE_COMPLEX.equals(this.queryType)) {
			queryArray = excuteComplexQuery(this.session, this.modelClass, this.complexQuerySelect, this.filter, this.orderBy);
		}
		if (QUERY_TYPE_HQL_NAMEQUERY.equals(this.queryType)) {
			queryArray = excuteHQLNameQuery(this.session, this._nameQuery_QueryName, this._nameQuery_Parameter, this.orderBy);
		}
		if (queryArray != null) {
			result_totalQuery = queryArray[0];
			result_dataQuery = queryArray[1];
		}
	}
	
	private Query[] excuteSimpleQuery(Session _session, Class modelClass, Filter filter, OrderBy order) {
		_session.createCriteria(modelClass).setFetchMode("", FetchMode.EAGER);
		Query _queryArray[] = new Query[2];
		if(this.isReturnCount)_queryArray[0] = getTotalRowsQuery(modelClass,  filter, _session);
		else _queryArray[0] = null ;
		_queryArray[1] = getSimpleSelectQuery(modelClass, filter, order,_session);
		return _queryArray;
	}
	private Query[] excuteComplexQuery(Session _session, Class startModelClass, Select select, Filter filter, OrderBy order) {
		Query _queryArray[] = new Query[2];
		if(this.isReturnCount) _queryArray[0] = getTotalRowsQuery(startModelClass, select, filter,_session);
		else _queryArray[0] = null ;
		_queryArray[1] = getComplexSelectQuery(startModelClass, select, filter,order, _session);
		return _queryArray;
	}
	
	private static String[] parseRowCountSql(StringBuffer _strSQLQuery,Map _paraMap ){
		//##:projType##
		String _regexStr = "(##returnRowCountSql([\\S\\w\\s]*)##)";
		Pattern _patt = Pattern.compile(_regexStr);
		Matcher _matc =null;
		String _sqlExpr = null;
		String _rowCountSql=null;
		String[] _resultSqlArray=new String[]{null,_strSQLQuery.toString()};
		try{
			//_matc=_patt.matcher(_strSQLQuery);
			_matc=_patt.matcher(_strSQLQuery);
			if(_matc!=null&&_matc.find()){
				_sqlExpr = _matc.group(1);
				_resultSqlArray[0] = _matc.group(2);
				_resultSqlArray[1]=_matc.replaceAll("");
				return _resultSqlArray;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return _resultSqlArray;
	}
	
	private Map resultPrevExprJavaFuncMap=new HashMap();
	
	private  List<String> parseResultPrevExprList(StringBuffer _strSQLQuery,Map _paraMap ){
        List<String> _resultPrevExprList=new ArrayList<String>();
		String _regexStr = "(##\\!((([a-zA-Z_][a-zA-Z0-9_]*).([a-zA-Z][A-Za-z0-9_]*))*)\\((([.:\\w]*\\,*)*)\\)##)";
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
				String _resultPrevExpr=parseResultPrevExpr(_funcExpr,_paraNames,_paraMap,_matc,_strSQLQuery);
				resultPrevExprJavaFuncMap.put(_resultPrevExpr+"_className", _className);
				resultPrevExprJavaFuncMap.put(_resultPrevExpr+"_methodName", _methodName);
				resultPrevExprJavaFuncMap.put(_resultPrevExpr+"_paraNames", _paraNames);
				resultPrevExprJavaFuncMap.put(_resultPrevExpr+"_paraMap", _paraMap);
				if(StringUtils.isNotNull(_resultPrevExpr)) _resultPrevExprList.add(_resultPrevExpr);
				_matc.reset();
			}
		}catch(Exception e){
			//e.printStackTrace();
		}
		return _resultPrevExprList;
	}
	
	private  String _getAddOptExpr(){
		if(DaoServiceImpl.DB_SQLSERVER.equals(DaoServiceImpl.getDbName()))
			return "+";
		if(DaoServiceImpl.DB_ORACLE.equals(DaoServiceImpl.getDbName()))
			return "||";
		return "+";
    }
	
	private  String parseResultPrevExpr(String _funcExpr,String _paraNames,Map _paraMap,Matcher _matc,StringBuffer _strSQLQuery){
		   String _valuesStr="";
		   if(StringUtils.isNotNull(_paraNames)){
			   String[] _paraNameArray=_paraNames.split(",");
			   for(int i=0;_paraNameArray!=null&&i<_paraNameArray.length;i++){
			     if(StringUtils.isNull(_paraNameArray[i]))continue;
			     if(_paraNameArray[i].indexOf(":")<0){
			    	 if(i==0)
			    		 _valuesStr+="'"+_paraNameArray[i]+"='"+_getAddOptExpr()+_paraNameArray[i];
			    	 else
			    		 _valuesStr+="',"+_paraNameArray[i]+"='"+_getAddOptExpr()+_paraNameArray[i]; 
			    	 log.info("=========================== "+_valuesStr);
			     }else{
			    	 _paraNameArray[i]=_paraNameArray[i].replaceAll(":", "");
			    	 Object _paraValue=_paraMap.get(_paraNameArray[i]);
			    	 if(StringUtils.isNull(_paraValue))continue;
			    	 if(i==0)
			    		 _valuesStr+="'"+_paraNameArray[i]+"="+_paraValue+"'";
			    	 else
			    		 _valuesStr+="',"+_paraNameArray[i]+"="+_paraValue+"'";
			    	 log.info("=========================== "+_valuesStr);
			     }
			   }
		}
		String _resultPrevExpr=_funcExpr.replaceAll("#", "zzzzzz");
		_resultPrevExpr=_resultPrevExpr.replaceAll("!", "z");
		_resultPrevExpr=_resultPrevExpr.replaceAll("\\(", "AA");
		_resultPrevExpr=_resultPrevExpr.replaceAll("\\)", "AA"); 
		_funcExpr=_resultPrevExpr;
		log.info("_resultPrevExpr1 =========================== "+_resultPrevExpr);
		if(StringUtils.isNotNull(_valuesStr))
			_resultPrevExpr="'"+_resultPrevExpr+"['"+_getAddOptExpr()+_valuesStr+_getAddOptExpr()+"']'";
		else
			_resultPrevExpr="'"+_resultPrevExpr+"[]'";
		log.info("_resultPrevExpr2 =========================== "+_resultPrevExpr);
		String newStrSQLQuery=_matc.replaceFirst(_resultPrevExpr);
		log.info("newStrSQLQuery =========================== "+newStrSQLQuery);
		_strSQLQuery.replace(0,_strSQLQuery.length(), newStrSQLQuery);
		return _funcExpr;
    }
	private Query[] excuteSQLNameQuery(Session _session, String _queryName, Map _parameter, OrderBy _order) {
		Query _queryArray[] = new Query[2];	
		StringBuffer _strSQLQuery =DaoUtils.getNamedSQLQuery(_queryName);
		//##:sqlContentPara##
		//实现sql语句动态通过程序输入
		SqlExpr.parseExpr(_strSQLQuery,_parameter);
		//$className.method(:para)
		SqlExpr.parseJavaFuncExpr(_strSQLQuery,_parameter);
		//##@sqlFuncExpr##
		SqlFunc.parseSqlFunc(_strSQLQuery,_parameter);
		//##!className.method(columnName|:para)##
		//用于结果二次处理
		resultPrevExprList=parseResultPrevExprList(_strSQLQuery,_parameter);
		//##returnRowCountSql ##
		String[] _resultSqlArray=parseRowCountSql(_strSQLQuery,_parameter);
		if(this.isReturnCount){
			StringBuffer _buffCountSql = new StringBuffer();
			if(_resultSqlArray[0]!=null&&_resultSqlArray[0].length()>0){
				_buffCountSql.append(_resultSqlArray[0]);
			}else{
				_buffCountSql.append("select count(1) countnum from (").append(_resultSqlArray[1]).append(") t");
			}
			_queryArray[0] =excuteSQLQuery(_session,_buffCountSql.toString(),this.INTEGER_RETURN, null);
			DaoUtils.setQueryParameters(_queryArray[0],_parameter);
		}else _queryArray[0] = null ;
		
		if (this.isResultToMapModel) {
			_queryArray[1] = excuteSQLQuery(_session,_resultSqlArray[1], this.MAP_RETURN,null);
		}else{
			Class _clsReturn = DaoUtils.getNamedSQLQueryReturnClass(_queryName);
			_queryArray[1] = excuteSQLQuery(_session,_resultSqlArray[1], this.OBJECT_RETURN, _clsReturn);
		}
		DaoUtils.setQueryParameters(_queryArray[1], _parameter);
		return _queryArray;
	}

	private Query[] excuteHQLNameQuery(Session _session, String queryName, Map parameter, OrderBy order) {
		Query _queryArray[] = new Query[2];
		String strHql = DaoUtils.getNamedHQLQuery(queryName).toString();
		if(this.isReturnCount){
			String countHQL = " select count (*) " + removeSelect(removeOrders(strHql));
			_queryArray[0] = excuteHQLQuery(_session,countHQL, this.INTEGER_RETURN);
			DaoUtils.setQueryParameters(_queryArray[0], parameter);
		}else _queryArray[0] = null ;
		
		if (this.isResultToMapModel) {
			_queryArray[1] = excuteHQLQuery(_session,strHql, this.MAP_RETURN);
		}else{
			_queryArray[1] = excuteHQLQuery(_session,strHql, this.OBJECT_RETURN);			
		}
		DaoUtils.setQueryParameters(_queryArray[1], parameter);
		return _queryArray;
	}

	private static String removeSelect(String sql) {
		int beginPos = sql.toLowerCase().indexOf("from");
		return sql.substring(beginPos);
	}

	private static String removeOrders(String sql) {
		Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*",Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(sql);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return sb.toString();
	}
	/**
	 * 简单查询语句获取查询接口对象
	 * 
	 * @param cls Class
	 * @param filter Filter
	 * @param session Session
	 * @return Query
	 */
	private Query getSimpleSelectQuery(Class cls, Filter filter, OrderBy order,Session _session) {
		String className = StringUtils.unqualify(cls.getName());
		String alias = className + "Obj";
		alias = StringUtils.uncapitalizeFirst(alias);
		StringBuffer selectBuffer = new StringBuffer();
		selectBuffer.append("from ").append(className).append(" ").append( alias ).append(this.getFetchGroup(alias));
		String whereStr = "";
		String orderStr = "";
		if (filter != null) {
			filter.setAlias(alias);
			whereStr = filter.getFilterExpr();
			if (StringUtils.hasText(whereStr)) whereStr = " where " + whereStr;
		}
		if (order != null) {
			orderStr = order.getOrderByExpr(alias);
		}
		selectBuffer.append(whereStr).append(orderStr);
		Query query = _session.createQuery(selectBuffer.toString());
		if (filter != null) {
			query = setQueryParameter(query, filter);
		}
		return query;
	}

	/**
	 * 复合查询语句获取查询接口对象
	 * 
	 * @param cls Class
	 * @param selectNames String[]
	 * @param filter Filter
	 * @param session Session
	 * @return Query
	 */
	private Query getComplexSelectQuery(Class cls, Select select, Filter filter, OrderBy order, Session _session) {
		filter=addFilter(session, cls, filter);
//		log.info("QueryCoreService::::filter==============="+filter);
		String className = StringUtils.unqualify(cls.getName());
		String alias = DaoUtils.getClassAlias(cls.getName());
		
		StringBuffer selectBuffer = new StringBuffer();
		if (select != null) {
			select.setClassModel(cls);
			selectBuffer.append(select.getSelectExpr(alias));
		}
		selectBuffer.append(" from ").append(className).append(" ").append(alias).append(" ").append(this.getFetchGroup(alias));
		String whereStr = "";
		if (filter != null) {
			filter.setAlias(alias);
			whereStr = filter.getFilterExpr();
			if (StringUtils.hasText(whereStr))whereStr = " where " + whereStr;
		}
		String orderStr = "";
		if (order != null) {
			orderStr = order.getOrderByExpr(alias);
		}
		selectBuffer.append(whereStr).append(orderStr);
		Query query =_session.createQuery(selectBuffer.toString());
		if (filter != null) {
			query = setQueryParameter(query, filter);
		}
		return query;
	}
	
	private Query getTotalRowsQuery(Class cls,  Filter filter,Session _session) {
		filter=addFilter(session, cls, filter);
		String className = StringUtils.unqualify(cls.getName());
		String alias = className + "Obj";
		alias = StringUtils.uncapitalizeFirst(alias);
		StringBuffer selectBuffer = new StringBuffer();
		selectBuffer.append("select count(*) from ").append(className).append( " " ).append(alias).append(this.getFetchGroup(alias));
		String whereStr = "";
		if (filter != null) {
			filter.setAlias(alias);
			whereStr = filter.getFilterExpr();
			if (StringUtils.hasText(whereStr)) whereStr = " where " + whereStr;
		}
		selectBuffer.append(whereStr);
		Query query =_session.createQuery(selectBuffer.toString());
		log.info("DaoServiceImpl.getTotalRowsQuery is =" + selectBuffer.toString());
		if (filter == null) {
			return query;
		}
		query = setQueryParameter(query, filter);
		return query;
	}
	
	private Query getTotalRowsQuery(Class cls, Select select, Filter filter,Session _session) {
		filter=addFilter(session, cls, filter);
		String className = StringUtils.unqualify(cls.getName());
		String alias = className + "Obj";
		alias = StringUtils.uncapitalizeFirst(alias);
		StringBuffer selectBuffer = new StringBuffer();
		selectBuffer.append("select count(*) from ").append(className).append( " " ).append(alias).append(this.getFetchGroup(alias));
		if (select != null) {
			select.setClassModel(cls);
			String _selectStr = select.getTotalSelectExpr(alias);

			if (_selectStr!= null && _selectStr.trim().length() > 0) {
				selectBuffer.append(_selectStr).append(" from ").append(className).append(" ").append( alias).append(this.getFetchGroup(alias));
			}
		}
		String whereStr = "";
		if (filter != null) {
			filter.setAlias(alias);
			whereStr = filter.getFilterExpr();
			if (StringUtils.hasText(whereStr)) whereStr = " where " + whereStr;
		}
		selectBuffer.append(whereStr);
		Query query =_session.createQuery(selectBuffer.toString());
		log.info("DaoServiceImpl.getTotalRowsQuery is =" + selectBuffer.toString());

		if (filter == null) {
			return query;
		}
		query = setQueryParameter(query, filter);
		return query;
	}
	private Query setQueryParameter(Query query, Filter filter) {
		String[] nameArray = ArrayUtils.toStringArray(filter.getParameterNames().toArray());
		Object[] valueArray = filter.getParameterValues().toArray();
		ISQLExprProcess iSQLExprProcess = filter.getSqlExprProcess();
		for (int i = 0; nameArray != null && i < nameArray.length; i++) {
			if(nameArray[i].indexOf(".") != -1){
				nameArray[i] = nameArray[i].replaceAll("\\.", "_");
			}
			if (valueArray[i] instanceof Object[]) {
				log.info("^^^^setParameterList  " + nameArray[i] + "=" + Arrays.toString((Object[])valueArray[i]) );
				Object[] objectArray = (Object[]) valueArray[i];
				if (objectArray != null && objectArray.length > 0) {
					if (iSQLExprProcess instanceof IsBetweenExprProcess) {
						query.setParameter(nameArray[i] + "_0", valueArray[0]);
						query.setParameter(nameArray[i] + "_1", valueArray[1]);
					} else {
						query.setParameterList(nameArray[i], objectArray,new ObjectType());
					}
				}
			} else {
				log.info("^^^^setParameter  " + nameArray[i] + "=" + valueArray[i]);
				query.setParameter(nameArray[i], valueArray[i],new ObjectType());
			}
		}
		return query;
	}

	private String getFetchGroup(String alias) {
		StringBuffer buff = new StringBuffer();
		for (int i = 0; i < this.fetchGroup.size(); i++) {
			buff.append(" left join ").append(alias).append(".").append(this.fetchGroup.get(i));
		}
		return buff.toString();
	}

	public Select getComplexQuerySelect() {
		return this.complexQuerySelect;
	}

	public void setComplexQuerySelect(Select _complexQuerySelect) {
		this.complexQuerySelect = _complexQuerySelect;
	}

	public Filter get_filter() {
		return this.filter;
	}

	public void set_filter(Filter _filter) {
		this.filter = _filter;
	}

	public boolean isResultToMapModel() {
		return this.isResultToMapModel;
	}

	public void setResultToMapModel(boolean _isResultToMapModel) {
		this.isResultToMapModel = _isResultToMapModel;
	}

	public boolean isReturnCount() {
		return this.isReturnCount;
	}

	public void setReturnCount(boolean _isReturnCount) {
		this.isReturnCount = _isReturnCount;
	}

	public Class get_modelClass() {
		return this.modelClass;
	}

	public void set_modelClass(Class _modelClass) {
		this.modelClass = _modelClass;
	}

	public Map get_nameQuery_Parameter() {
		return _nameQuery_Parameter;
	}

	public void set_nameQuery_Parameter(Map query_Parameter) {
		_nameQuery_Parameter = query_Parameter;
	}

	public String get_nameQuery_QueryName() {
		return _nameQuery_QueryName;
	}

	public void set_nameQuery_QueryName(String query_QueryName) {
		_nameQuery_QueryName = query_QueryName;
	}

	public String get_queryType() {
		return this.queryType;
	}

	public void set_queryType(String _queryType) {
		this.queryType = _queryType;
	}

	public Session get_session() {
		return this.session;
	}

	public void set_session(Session _session) {
		this.session = _session;
	}

	public List getFetchGroup() {
		return fetchGroup;
	}

	public void setFetchGroup(List fetchGroup) {
		this.fetchGroup = fetchGroup;
	}
	
	public Query excuteSQLQuery(Session session,String sql, int _returnType, Class cls) {
		Query query = null;
		this.returnType=_returnType;
		switch (_returnType) {
		case MAP_RETURN:// 返回MAP
			query = session.createSQLQuery(sql).setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			break;
		case INTEGER_RETURN:// 返回COUNT
			query = session.createSQLQuery(sql).addScalar("countnum",new IntegerType());
			break;
		default:
			query = session.createSQLQuery(sql).addEntity(cls);
			break;
		}
		return query;
	}

	public Query excuteHQLQuery(Session session,String hql, int _returnType) {
		Query query = null;
		this.returnType=_returnType;
		switch (_returnType) {
		case MAP_RETURN:// 返回MAP
			query = session.createQuery(hql).setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			break;
		case INTEGER_RETURN:// 返回INTEGER
			query = session.createQuery(hql);
			break;
		default:
			query = session.createQuery(hql);
			break;
		}
		return query;
	}
	public Filter addFilter(Session session,Class cls,Filter filter){
		return filter;
	}
}
