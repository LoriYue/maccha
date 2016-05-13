package org.maccha.dao.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.NamedQueryDefinition;
import org.hibernate.engine.NamedSQLQueryDefinition;
import org.hibernate.engine.query.sql.NativeSQLQueryReturn;
import org.hibernate.engine.query.sql.NativeSQLQueryRootReturn;
import org.maccha.dao.IDaoService;
import org.maccha.dao.type.ObjectType;
import org.maccha.spring.SpringManager;
import org.maccha.base.util.StringUtils;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;

public class DaoUtils {
	private static Logger log = Logger.getLogger(DaoUtils.class);
	/**
	 * 生成HQL 对象别名
	 * @param className
	 * @return
	 */
	public static String getClassAlias(String className){
		String strClassName = StringUtils.unqualify(className);
		String alias = strClassName + "Obj";
		alias = StringUtils.uncapitalizeFirst(alias);	
		return alias ;
	}
	  /**
     * 转义like语句中的
     * <code>'_'</code><code>'%'</code>
     * 将<code>'?'</code>转成sql的<code>'/_'</code>
     * 将<code>'%'</code>转成sql的<code>'/%'</code>
     * <p>
     *   例如搜索<code>?aa*bb?c_d%f</code>将转化成<br/>
     *   <code>_aa%bb_c/_d/%f</code>
     * </p>
     * @param likeStr
     * @return
     * @author <a href="http://jdkcn.com">somebody</a>
     */
    public static String escapeSQLLike(String likeStr) {
        String str = StringUtils.replace(likeStr, "_", "/_");
        str = StringUtils.replace(str,"%","/%");
        str = StringUtils.replace(str,"?","_");
        str = StringUtils.replace(str,"*","%");
        return str;
    }
    public static void setQueryParameters(Query query, Map queryArgs) {
		String[] namedParameters = query.getNamedParameters();
		if (namedParameters != null) {
			String strParamName = null;
			for (int i = 0; i < namedParameters.length; i++) {
				strParamName = namedParameters[i];
				//System.out.println("the QyeryName SQL:[paramName:" + strParamName+ ",value:" + queryArgs.get(strParamName) + "]");
				Object obj = queryArgs.get(strParamName);
				if (obj instanceof Object[]) {
					log.info("^^^^setParameterList  " + strParamName + "=" + Arrays.toString((Object[]) obj) );
					query.setParameterList(strParamName, (Object[]) obj ,new ObjectType());
				} else if (obj instanceof Collection) {
					log.info("^^^^setParameterList  " + strParamName + "=" + Arrays.toString(((Collection) obj).toArray()) );
					query.setParameterList(strParamName, (Collection) obj ,new ObjectType());
				} else if (obj instanceof java.util.Date) {
					log.info("the QyeryName SQL:[paramName:" + strParamName+ ",value:" + queryArgs.get(strParamName) + "]");
					query.setDate(strParamName, (java.util.Date)obj);
				}else if (obj instanceof java.lang.Character) {
					log.info("the QyeryName SQL:[paramName:" + strParamName+ ",value:" + queryArgs.get(strParamName) + "]");
					query.setParameter(strParamName, obj.toString(),new ObjectType());
				}
				else {
					log.info("the QyeryName SQL:[paramName:" + strParamName+ ",value:" + queryArgs.get(strParamName) + "]");
					query.setParameter(strParamName, obj,new ObjectType());
				}
			}
		}
	}
	private static Configuration getConfiguration() {
		LocalSessionFactoryBean sessionFactoryBean = (LocalSessionFactoryBean) SpringManager.getComponent("&sessionFactory");
		Configuration conf = sessionFactoryBean.getConfiguration();
		return conf;
	}
	public static StringBuffer getSqlByQueryName(String _queryName){
		return SqlConfigResource.getSqlByQueryName(_queryName);
	}
	private static Map returnClassMap=new ConcurrentHashMap();
	/**
	 * 通过SQLQuery名称获得SQL查询的语句
	 * @param queryName
	 * @return
	 */
	public static StringBuffer getNamedSQLQuery(String _queryName){
		Map _mapSQLQuery = getConfiguration().getNamedSQLQueries();
		NamedSQLQueryDefinition _namedSQLQueryDefinition = (NamedSQLQueryDefinition) _mapSQLQuery.get(_queryName);
		StringBuffer strSQLQuery = null;
		if(_namedSQLQueryDefinition != null){
			strSQLQuery=new StringBuffer();
			strSQLQuery.append(_namedSQLQueryDefinition.getQueryString());
		}
		if(strSQLQuery==null||strSQLQuery.length()<=0){
			strSQLQuery=getSqlByQueryName(_queryName);
		}
		return strSQLQuery;
	}
	/**
	 * 通过Query名称获得HQL查询的语句
	 * @param queryName
	 * @return
	 */
	public static StringBuffer getNamedHQLQuery(String _queryName){
		Map _mapHQLQuery = getConfiguration().getNamedQueries();
		NamedQueryDefinition _namedQueryDefinition = (NamedQueryDefinition) _mapHQLQuery.get(_queryName);
		StringBuffer _strHQLQuery = null;
		if(_namedQueryDefinition!= null){
			_strHQLQuery=new StringBuffer();
			_strHQLQuery.append(_namedQueryDefinition.getQueryString());
		}
		if(_strHQLQuery==null||_strHQLQuery.length()<=0){
			_strHQLQuery=getSqlByQueryName(_queryName);
		}
		return _strHQLQuery;
	}
	/**
	 * 根据查询名称发回查询类型（类型包括：sql语句查询 hql语句查询 实体对象查询）
	 * @param _queryName
	 * @return String 范围 sqlQuery|hqlQuery|entityQuery
	 */
	public static String getQueryNameType(String _queryName){
		if(StringUtils.isNotNull(getSqlByQueryName(_queryName)))
				return IDaoService.QUERY_NAME_TYPE_SQL_QUERY;
		Map _mapSQLQuery = getConfiguration().getNamedSQLQueries();
		NamedSQLQueryDefinition _namedSQLQueryDefinition = (NamedSQLQueryDefinition) _mapSQLQuery.get(_queryName);
		if(_namedSQLQueryDefinition != null){
			return IDaoService.QUERY_NAME_TYPE_SQL_QUERY;
		}
		Map _mapHQLQuery = getConfiguration().getNamedQueries();
		NamedQueryDefinition _namedQueryDefinition = (NamedQueryDefinition) _mapHQLQuery.get(_queryName);
		if(_namedQueryDefinition != null){
			return IDaoService.QUERY_NAME_TYPE_HQL_QUERY;
		}
		_mapSQLQuery=null;
		_namedSQLQueryDefinition=null;
		_mapHQLQuery=null;
		_namedQueryDefinition=null;
		try{
			Class.forName(_queryName);
			return IDaoService.QUERY_NAME_TYPE_ENTITY_QUERY;
		}catch(Exception e){
			
		}
		return null;
	}
	/**
	 * 返回查询结果封装类
	 * @param _queryName
	 * @return
	 */
	public static Class getNamedSQLQueryReturnClass(String _queryName){
		Class _clsReturn=null;
		try {
			Map _mapSQLQuery = getConfiguration().getNamedSQLQueries();
			NamedSQLQueryDefinition _namedSQLQueryDefinition = (NamedSQLQueryDefinition) _mapSQLQuery.get(_queryName);
			if(_namedSQLQueryDefinition!=null){
				NativeSQLQueryReturn _arraySQLQueryReturn[] = _namedSQLQueryDefinition.getQueryReturns();
				if(_arraySQLQueryReturn != null &&_arraySQLQueryReturn.length>0){
					NativeSQLQueryRootReturn _returnClss = (NativeSQLQueryRootReturn) _arraySQLQueryReturn[0];
					if(_returnClss != null) _clsReturn = Class.forName(_returnClss.getReturnEntityName());
				}
			}else{
				String _returnClassName=(String)returnClassMap.get(_queryName);
				if(_returnClassName != null)_clsReturn =Class.forName(_returnClassName);
			}
		} catch (Throwable ta) {
			log.error("findByNamedSQLQuery [queryName:" + _queryName+ " is not exist from hbm xml file! ] Error:", ta);
		}
		return _clsReturn;
	}

}