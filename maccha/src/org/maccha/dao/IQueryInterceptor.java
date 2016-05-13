package org.maccha.dao;

import org.hibernate.Query;
import org.hibernate.Session;

public interface IQueryInterceptor {
	
	public final static String EXECUTE_SQL_QUERY="";
	public final static String EXECUTE_HQL_QUERY="";
	public final static String ADD_FILTER="";
	
	/**
	 * 执行sql语句
	 * @param sql  
	 * @param returnType
	 * @param cls
	 * @return
	 */
	public Query excuteSQLQuery(Session _session,String _sql, int _returnType, Class _cls) ;
	/**
	 * 执行HSQL语句 Named
	 * @param hql
	 * @param returnType
	 * @return
	 */
	public Query excuteHQLQuery(Session _session,String _hql, int _returnType);
	/**
	 * 拦截 执行动态生成的HQL 
	 * @param session
	 * @param cls
	 * @param filter
	 * @return
	 */
	public Filter addFilter(Session _session,Class _cls,Filter _filter);

}
