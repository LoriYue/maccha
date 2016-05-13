package org.maccha.dao.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;
import org.maccha.dao.FetchMode;
import org.maccha.dao.Filter;
import org.maccha.dao.IDaoService;
import org.maccha.dao.IQueryService;
import org.maccha.dao.IUpdateInterceptor;
import org.maccha.dao.OrderBy;
import org.maccha.dao.Page;
import org.maccha.dao.Select;
import org.maccha.dao.SqlExpr;
import org.maccha.dao.SqlFunc;
import org.maccha.dao.UpdateSet;
import org.maccha.dao.util.DaoUtils;
import org.maccha.base.exception.SysException;
import org.maccha.spring.SpringManager;
import org.maccha.base.util.ArrayUtils;
import org.maccha.base.util.BeanUtils;
import org.maccha.base.util.ClassUtils;
import org.maccha.base.util.ObjectUtils;
import org.maccha.base.util.StringUtils;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

@Repository("daoService")
public class DaoServiceImpl extends HibernateDaoSupport implements IDaoService,IUpdateInterceptor {
	private static Logger log = Logger.getLogger(DaoUtils.class);

	// 不能直接使用 setSessionFactory 是因为在HibernateDaoSupport中被定义为final
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	/**
	 * 根据查询名称发回查询类型（类型包括：sql语句查询 hql语句查询 实体对象查询）
	 * @param _queryName
	 * @return String 范围 sqlQuery|hqlQuery|entityQuery
	 */
	public String getQueryNameType(String _queryName) {
		// TODO 自动生成方法存根
		return DaoUtils.getQueryNameType(_queryName);
	}

	@Override
	protected HibernateTemplate createHibernateTemplate(SessionFactory arg0) {
		// TODO 自动生成方法存根
		return super.createHibernateTemplate(arg0);
	}
	@Override
	protected void initDao() throws Exception {
		// TODO 自动生成方法存根
		super.initDao();
	}
	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO 自动生成方法存根
		return super.clone();
	}
	@Override
	public boolean equals(Object obj) {
		// TODO 自动生成方法存根
		return super.equals(obj);
	}
	@Override
	protected void finalize() throws Throwable {
		// TODO 自动生成方法存根
		super.finalize();
	}
	@Override
	public int hashCode() {
		// TODO 自动生成方法存根
		return super.hashCode();
	}
	@Override
	public String toString() {
		// TODO 自动生成方法存根
		return super.toString();
	}
	public List queryByQueryName2Entity(String _queryName, Map _queryArgMap) {
		return getQueryServiceByQueryName2Entity(_queryName,_queryArgMap,false).list();
	}
	
	public List queryByQueryName2Entity(String _queryName, String[] _queryArgNames, Object[] _queryArgValues) {
		Map _queryArgMap=toMap(_queryArgNames,_queryArgValues);
		return getQueryServiceByQueryName2Entity(_queryName,_queryArgMap,false).list();
	}
	public List queryByQueryName2Entity(String _queryName, String _queryArgName, Object _queryArgValue) {
		Map _queryArgMap=toMap(_queryArgName,_queryArgValue);
		return getQueryServiceByQueryName2Entity(_queryName,_queryArgMap,false).list();
	}
	
	public List queryByQueryName2Entity(String _queryName, Map _queryArgMap, Page _page) {
		return getQueryServiceByQueryName2Entity(_queryName,_queryArgMap).list(_page);
	}
	
	public List queryByQueryName2Entity(String _queryName, String[] _queryArgNames, Object[] _queryArgValues, Page _page) {
		Map _queryArgMap=toMap(_queryArgNames,_queryArgValues);
		return getQueryServiceByQueryName2Entity(_queryName,_queryArgMap).list(_page);
	}
	
	public List queryByQueryName2Entity(String _queryName, String _queryArgName, Object _queryArgValue, Page _page) {
		
		Map _queryArgMap=toMap(_queryArgName,_queryArgValue);

		return getQueryServiceByQueryName2Entity(_queryName,_queryArgMap).list(_page);
	}
	
	public List queryByQueryName2Entity(String _queryName, Map _queryArgMap, Page _page, String _orderClumn, String _orderSort) {
		return getQueryServiceByQueryName2Entity(_queryName,_queryArgMap).list(_page);
	}
	/**
	 * 通过QueryName查询
	 * @param _queryName：查询名称
	 * @param _queryArgMap：查询参数(此处不分次序，系统按名称读取变量)
	 * @param _page：查询分页对象，包括开始行号，返回行数 *
	 * @param _orderColumn：所要排序的列
	 * @param _orderSort：排序方式(asc,desc)等
	 * @return
    */
	public List queryByQueryName2Map(String _queryName, Map _queryArgMap, Page _page,String _orderColumn, String _orderSort) {
		// 如果排序字段和排序方式都不为空则添加排序方式参数，并将queryName拼接orderSort
		if (StringUtils.isNotNull(_orderSort)
				&& StringUtils.isNotNull(_orderColumn)) {
			_queryName = _queryName + "_" + _orderSort;
			_queryArgMap.put(ORDER_COLUMN, _orderColumn);
		}
		IQueryService _queryService = this.getQueryServiceByQueryName2Map(_queryName,_queryArgMap);
		return _queryService.list(_page);

	}
	
	private Map toMap(String name,Object value){
		return toMap(new String[]{name},new Object[]{value});
	}
	
	private Map toMap(String[] names,Object[] values){
		Map _queryArgMap=new HashMap();
		for(int i=0;names!=null&&i<names.length;i++){
		_queryArgMap.put(names[i], values[i]);
		}
		return _queryArgMap;
	}
	
	private Filter getFilter(String filterName, Object filterValue) {
		return getFilter(new String[]{filterName}, new Object[]{filterValue});
	}
	
	private Filter getFilter(String[] filterNames, Object[] filterValues) {
		Filter groupFilter = Filter.getAndGroupFilter();
		for (int i = 0; i < filterNames.length; i++) {
			groupFilter.add(FieldFilter.eq(filterNames[i], filterValues[i]));
		}
		return groupFilter;
	}
	
	private Filter getFilter(Map map) {
		Filter groupFilter = Filter.getAndGroupFilter();
		String[] nameArray = ArrayUtils.toStringArray(map.keySet().toArray());
		for (int i = 0; i < nameArray.length; i++) {
			groupFilter
					.add(FieldFilter.eq(nameArray[i], map.get(nameArray[i])));
		}
		return groupFilter;
	}
	
	public List query(Class _modelClass, Select _select, Filter _filter,Page _page, String _orderColumn, String _orderSort, List _fetch) {
		IQueryService _queryService = this.getQueryService(_modelClass,_select, _filter);
		if (StringUtils.isNotNull(_orderColumn)) {
			OrderBy _orderBy = new OrderBy();
			if (StringUtils.isNotNull(_orderBy)) {
				_orderBy.order(_orderColumn, _orderSort);
			} else {
				_orderBy.order(_orderColumn, "ASC");
			}
			_queryService.setOrderBy(_orderBy);
		}
		for (int i = 0; _fetch != null&&i < _fetch.size(); i++){
			_queryService.setFetchMode((String) _fetch.get(i),FetchMode.LEFT_JOIN);
		}
		return _queryService.list(_page);
	}
	
	public List query(Class _modelClass, Select _select, Filter _filter,Page _page, OrderBy _orderBy) {
		IQueryService queryService =getQueryService(_modelClass,_select, _filter);
		if(_orderBy != null)queryService.setOrderBy(_orderBy);
		return queryService.list(_page);
	}
	
	public List query(Class _modelClass, Select _select, Filter _filter,Page _page, String _orderColumn,String _orderSort) {
		IQueryService queryService =getQueryService(_modelClass,_select, _filter);
		if (StringUtils.isNotNull(_orderColumn)) {
			OrderBy _orderBy = new OrderBy();
			if (StringUtils.isNotNull(_orderBy)) {
				_orderBy.order(_orderColumn, _orderSort);
			} else {
				_orderBy.order(_orderColumn, "ASC");
			}
			queryService.setOrderBy(_orderBy);
		}
		return queryService.list(_page);
	}
	
	public int countByHQLQueryName(String _queryName, Map _queryArgMap) {
		int _count = 0;
		IQueryService _queryService = this.getQueryServiceByHQLQueryName2Entity(_queryName, _queryArgMap);
		_count = _queryService.size();
		return _count;
	}
	public List queryByHQLQueryName2Map(String _queryName, Map _queryArgMap, Page _page, String _orderColumn, String _orderSort) {
		// TODO 自动生成方法存根
		return queryByHQLQueryName2Map(_queryName,_queryArgMap,_page);
	}
	
	public List queryByHQLQueryName2Map(String _queryName, Map _queryArgMap, Page _page) {
		IQueryService _queryService = this.getQueryServiceByHQLQueryName2Map(_queryName,_queryArgMap);
		return _queryService.list(_page);
	}
	
	public List queryByHQLQueryName2Map(String _queryName, Map _queryArgMap) {
		IQueryService _queryService = this.getQueryServiceByHQLQueryName2Map(_queryName,_queryArgMap,true);
		return _queryService.list();
	}
	
	public int countByQueryName(String queryName, Map _queryArgMap) {
		int count = 0;
		IQueryService queryService = this.getQueryServiceByQueryName2Map(queryName,_queryArgMap);
		count = queryService.size();
		return count;
	}
	
	public List queryByQueryName2Map(String _queryName, Map _queryArgMap, Page _page) {
		IQueryService queryService = this.getQueryServiceByQueryName2Map(_queryName, _queryArgMap);
		return queryService.list(_page);
	}
	
	public List queryByQueryName2Map(String _queryName, String[] _queryArgNames, Object[] _queryArgValues, Page _page) {
		// TODO 自动生成方法存根
		Map _queryArgMap=toMap(_queryArgNames,_queryArgValues);
		return queryByQueryName2Map(_queryName,_queryArgMap);
	}
	
	public List queryByQueryName2Map(String _queryName, String _queryArgName, Object _queryArgValue, Page _page) {
		// TODO 自动生成方法存根
		Map _queryArgMap=toMap(_queryArgName,_queryArgValue);
		return queryByQueryName2Map(_queryName,_queryArgMap);
	}
	
	public List queryByQueryName2Map(String _queryName, Map _queryArgMap) {
		return this.getQueryServiceByQueryName2Map(_queryName, _queryArgMap,true).list();
	}
	
	public List queryByQueryName2Map(String _queryName, String[] _queryArgNames, Object[] _queryArgValues) {
		// TODO 自动生成方法存根
		Map _queryArgMap=toMap(_queryArgNames,_queryArgValues);
		return queryByQueryName2Map(_queryName,_queryArgMap);
	}

	public List queryByQueryName2Map(String _queryName, String _queryArgName, Object _queryArgValue) {
		Map _queryArgMap=toMap(_queryArgName,_queryArgValue);
		return queryByQueryName2Map(_queryName,_queryArgMap);
	}
	
	public Map queryByQueryName2OneMap(String _queryName, Map _queryArgMap) {
		// TODO 自动生成方法存根
		Page _page=new Page();
		_page.setRowCount(1);
		List _resultList=queryByQueryName2Map(_queryName,_queryArgMap,_page);
		if(_resultList==null||_resultList.size()==0){
			return null;
		}
		return (Map)_resultList.get(0);
	}

	public Map queryByQueryName2OneMap(String _queryName, String _queryArgName, Object _queryArgValue) {
		Map _queryArgMap=toMap(_queryArgName,_queryArgValue);
		return queryByQueryName2OneMap(_queryName,_queryArgMap);
	}

	public Map queryByQueryName2OneMap(String _queryName, String[] _queryArgNames, Object[] _queryArgValues) {
		Map _queryArgMap=toMap(_queryArgNames,_queryArgValues);
		return queryByQueryName2OneMap(_queryName,_queryArgMap);
	}

	public List queryByHQLQueryName2Entity(String _queryName, Map _queryArgMap, Page _page) {
		IQueryService _queryService = this.getQueryServiceByHQLQueryName2Entity(_queryName,_queryArgMap);
		return _queryService.list(_page);
	}
	public List queryByHQLQueryName2Entity(String _queryName, Map _queryArgMap, Page _page, String _orderColumn, String _orderSort) {
		// TODO 自动生成方法存根
		return queryByHQLQueryName2Entity(_queryName,_queryArgMap,_page);
	}
	
	public List queryByHQLQueryName2Entity(String _queryName, Map _queryArgMap) {
		IQueryService _queryService = this.getQueryServiceByHQLQueryName2Entity(_queryName,_queryArgMap,false);
		return _queryService.list();
	}
	
	public final static String ORDER_COLUMN = "orderColumn";
	public final static int DEFAULT_BATCH_SIZE = 25;
    private IQueryService queryService;
	public DaoServiceImpl() {
		
	}
	
	public final static IQueryService newQueryService(){
		   return new QueryServiceImpl();	
	}
	
	public Serializable save(Object objectToSave) {
		Serializable obj = null;
		try {
			obj = getHibernateTemplate().save(objectToSave);
		} catch (Throwable dae) {
			SysException.handleException(dae.getMessage(), dae);
		}
		return obj;
	}

	public void saveOrUpdate(Object objectToSaveOrUpdate) {
		try {
			getHibernateTemplate().saveOrUpdate(objectToSaveOrUpdate);
		} catch (Throwable dae) {
			SysException.handleException(dae.getMessage(), dae);
		}
	}

	public void saveOrUpdate(Collection collectionToSaveOrUpdate) {
		try {
			getHibernateTemplate().saveOrUpdateAll(collectionToSaveOrUpdate);
		} catch (Throwable dae) {
			SysException.handleException(dae.getMessage(), dae);
		}
	}

	protected void checkWriteOperationAllowed(HibernateTemplate template,
			Session session) throws InvalidDataAccessApiUsageException {
		if (template.isCheckWriteOperations()
				&& template.getFlushMode() != HibernateTemplate.FLUSH_EAGER
				&& FlushMode.NEVER.equals(session.getFlushMode())) {
			throw new InvalidDataAccessApiUsageException(
					"Write operations are not allowed in read-only mode (FlushMode.NEVER) - turn your Session "
							+ "into FlushMode.AUTO or remove 'readOnly' marker from transaction definition");
		}
	}
	
	public void save(final List objectsToSave) {
		try {
			getHibernateTemplate().execute(new HibernateCallback() {
				public Object doInHibernate(Session session) throws HibernateException {
					checkWriteOperationAllowed(getHibernateTemplate(), session);
					if (objectsToSave == null) {
						return null;
					}
					int max = objectsToSave.size();
					for (int i = 0; i < max; i++) {
						session.save(objectsToSave.get(i));
						if ((i != 0 && i % DEFAULT_BATCH_SIZE == 0) || i == max - 1) {
							session.flush();
							session.clear();
						}
					}
					return null;
				}
			});
		} catch (Throwable dae) {
			SysException.handleException(dae.getMessage(), dae);
		}
	}
	
	public int update(Object object) {
		try {
			getHibernateTemplate().update(object);
		} catch (Throwable dae) {
			SysException.handleException(dae.getMessage(), dae);
		}
		return 1;
	}
	
	public int update(Object object,boolean isIncludeNullValue) {
		try {
			if (!(object instanceof HibernateProxy)){
				ClassUtils ClassUtils=null;
				Object tmpObj = this.load(object.getClass(),(String)ClassUtils.get(object, "id"));
				ObjectUtils.copyProperties(object, tmpObj,isIncludeNullValue);
				getHibernateTemplate().update(tmpObj);
			}else{
			    getHibernateTemplate().update(object);
			}
		} catch (Throwable dae) {
			SysException.handleException(dae.getMessage(), dae);
		}
		return 1;
	}
	
	/**
	 * 执行queryname对应插入语句
	 * @param queryName queryname
	 * @param paramMap 参数
	 * @param whereMap 条件参数
	 * @return
	 */
	public int updateByQueryName(final String queryName,final Map _parameter){
		Integer count = new Integer(0);
		final StringBuffer _strSQLQuery =DaoUtils.getNamedSQLQuery(queryName);
		try {
			count = (Integer) getHibernateTemplate().execute(
					new HibernateCallback() {
						public Object doInHibernate(Session session)throws HibernateException {
							checkWriteOperationAllowed(getHibernateTemplate(),session);
							if (_strSQLQuery == null) {
								SysException.handleException(queryName + " sql-query 在XML中没有配置!");
							}
							//##:sqlContentPara##
							//实现sql语句动态通过程序输入
							SqlExpr.parseExpr(_strSQLQuery,_parameter);
							//$className.method(:para)
							SqlExpr.parseJavaFuncExpr(_strSQLQuery,_parameter);
							//##@sqlFuncExpr##
							SqlFunc.parseSqlFunc(_strSQLQuery,_parameter);
							Query _query = session.createSQLQuery(_strSQLQuery.toString());
							DaoUtils.setQueryParameters(_query,_parameter);
							int _count = _query.executeUpdate();
							return new Integer(_count);
						}
					});
		} catch (Throwable dae) {
			SysException.handleException(dae.getMessage(), dae);
		}
		return count.intValue();
	}
	
	/**
	 * 执行queryname对应插入语句
	 * @param queryName queryname
	 * @param paramMap 参数
	 * @param whereMap 条件参数
	 * @return
	 */
	public int insertByQueryName(final String queryName,final Map _parameter) {
		Integer count = new Integer(0);
		final StringBuffer _strSQLQuery =DaoUtils.getNamedSQLQuery(queryName);
		try {
			count = (Integer) getHibernateTemplate().execute(
					new HibernateCallback() {
						public Object doInHibernate(Session session)throws HibernateException {
							checkWriteOperationAllowed(getHibernateTemplate(),session);
							if (_strSQLQuery == null) {
								SysException.handleException(queryName + " sql-query 在XML中没有配置!");
							}
							//##:sqlContentPara##
							//实现sql语句动态通过程序输入
							SqlExpr.parseExpr(_strSQLQuery,_parameter);
							//$className.method(:para)
							SqlExpr.parseJavaFuncExpr(_strSQLQuery,_parameter);
							//##@sqlFuncExpr##
							SqlFunc.parseSqlFunc(_strSQLQuery,_parameter);
							SQLQuery _query = session.createSQLQuery(_strSQLQuery.toString());
							DaoUtils.setQueryParameters(_query,_parameter);
							int _count = _query.executeUpdate();
							return new Integer(_count);
						}
					});
		} catch (Throwable dae) {
			SysException.handleException(dae.getMessage(), dae);
		}
		return count.intValue();
	}
	public int update(final Object[] objectsToUpdate) {
		Integer count = new Integer(0);
		try {
			count = (Integer) getHibernateTemplate().execute(
					new HibernateCallback() {
						public Object doInHibernate(Session session)
								throws HibernateException {
							checkWriteOperationAllowed(getHibernateTemplate(),
									session);
							if (objectsToUpdate == null) {
								return new Integer(0);
							}
							int max = objectsToUpdate.length;
							int _count = 0;
							for (int i = 0; i < max; i++) {
								session.update(objectsToUpdate[i]);
								if ((i != 0 && i % DEFAULT_BATCH_SIZE == 0)|| i == max - 1) {
									session.flush();
									session.clear();
								}
								_count++;
							}
							return new Integer(_count);
						}
					});
		} catch (Throwable dae) {
			SysException.handleException(dae.getMessage(), dae);
		}
		return count.intValue();
	}

	Query getUpdateQuery(Class cls, UpdateSet updateSet, Filter filter,
			Session session) {
		String className = StringUtils.unqualify(cls.getName());
		if (!updateSet.isHasUpdateSet()) {
			SysException.handleException("没有提供更新的数据项,updateSet=" + updateSet);
		}
		String updateStr = "update " + className + " set 1=1";
		Object[] names = updateSet.getSet().keySet().toArray();
		Object[] values = updateSet.getSet().values().toArray();
		String[] parameterNames = new String[names.length];
		for (int i = 0; names != null && i < names.length; i++) {
			parameterNames[i] = "update" + (String) names[i];
			updateStr += "," + (String) names[i] + "=:" + parameterNames[i];
		}
		Object[] exprNames = updateSet.getExprSet()[0].keySet().toArray();
		Object[] exprs = updateSet.getExprSet()[0].values().toArray();
		Object[] exprValues = updateSet.getExprSet()[1].values().toArray();
		ArrayUtils temp_arrayUtils = new ArrayUtils();
		Vector temp_exprValues_vecor = new Vector();
		String[] exprParameterNames = null;
		Object[] exprParameterValues = null;
		for (int i = 0; exprValues != null && i < exprValues.length; i++) {
			String exprParameterName = "update" + (String) exprNames[i]; // "name+?/?"
			String[] exprVars = ((String) exprs[i]).split("\\?");
			String exprStr = "";
			for (int k = 0; exprVars != null && k < exprVars.length; k++) {
				exprStr += exprVars[k] + ":" + exprParameterName + k;
				temp_arrayUtils.addString(exprParameterName + k);
				temp_exprValues_vecor.add(((Object[]) exprValues[i])[k]);
			}
			updateStr += ",set " + (String) exprNames[i] + "=" + exprStr;
		}
		exprParameterNames = temp_arrayUtils.getStringArray();
		exprParameterValues = temp_exprValues_vecor.toArray();
		temp_exprValues_vecor = null;
		temp_arrayUtils = null;
		if (!(exprParameterValues == null && exprParameterNames == null)) {
			if (exprParameterValues == null || exprParameterNames == null
					|| exprParameterValues.length != exprParameterNames.length) {

				String tmp1 = "";
				String tmp2 = "";
				for (int i = 0; exprs != null && i < exprs.length; i++) {
					tmp1 += exprs[i];
				}
				for (int i = 0; exprParameterValues != null && i < exprParameterValues.length; i++) {
					tmp2 += exprParameterValues[i];
				}
				SysException.handleException("表达式更新数据中的变量同提供的变量值不一致(" + tmp1 + "," + tmp2 + ")");
			}
		}
		String whereStr = "";
		if (filter != null) {
			whereStr = " where " + filter.getFilterExpr();
		}
		updateStr = updateStr.replaceAll("1\\=1\\,", " ");
		String hql = updateStr + whereStr;
		log.info("session = " + session +",hql = " + hql);
		Query query = session.createQuery(hql);
		if (filter != null) {
			query = setQueryParameter(query, filter);
		}
		for (int i = 0; parameterNames != null && i < parameterNames.length; i++) {
			query.setParameter(parameterNames[i], values[i]);
		}
		for (int i = 0; exprParameterNames != null && i < exprParameterNames.length; i++) {
			query.setParameter(exprParameterNames[i], exprParameterValues[i]);

		}
		return query;
	}

	Query getDeleteQuery(Class cls, Filter filter, Session session) {
		String className = StringUtils.unqualify(cls.getName());
		String selectStr = "delete from " + className;
		String whereStr = "";
		if (filter != null) {
			whereStr = filter.getFilterExpr();
			if (StringUtils.hasText(whereStr))whereStr = " where " + whereStr;
		}
		String hql = selectStr + whereStr;
		Query query = session.createQuery(hql);
		if (filter != null) {
			query = setQueryParameter(query, filter);
		}
		return query;
	}

	private Query setQueryParameter(Query query, Filter filter) {
		String[] nameArray = ArrayUtils.toStringArray(filter.getParameterNames().toArray());
		Object[] valueArray = filter.getParameterValues().toArray();
		ISQLExprProcess iSQLExprProcess = filter.getSqlExprProcess();
		for (int i = 0; nameArray != null && i < nameArray.length; i++) {
			nameArray[i] = nameArray[i].replaceAll("\\.", "_");
			if (valueArray[i] instanceof Object[]) {
				log.info("^^^^setParameterList  " + nameArray[i] + "=" + Arrays.toString((Object[])valueArray[i]) );
				Object[] objectArray = (Object[]) valueArray[i];
				if (objectArray != null && objectArray.length > 0) {
					if (iSQLExprProcess instanceof IsBetweenExprProcess) {
						query.setParameter(nameArray[i] + "_0", valueArray[0]);
						query.setParameter(nameArray[i] + "_1", valueArray[1]);
					} else {
						query.setParameterList(nameArray[i], objectArray);
					}
				}
			} else {
				System.out.println("^^^^setParameter  " + nameArray[i] + "="+ valueArray[i]);
				query.setParameter(nameArray[i], valueArray[i]);
			}
		}
		return query;
	}

	public int update(final Class modeClass, Map updateValue,
			String[] filternames, Object[] filtervalues) {
		final Filter groupFilter = getFilter(filternames, filtervalues);
		return update(modeClass, updateValue, groupFilter);
	}

	public int update(final Class modeClass, final Map updateValue, final Filter filter) {
		return update(modeClass, UpdateSet.getUpdateSet().set(updateValue), filter);
	}

	public int update(final Class modeClass, final UpdateSet updateSet, final Filter filter) {
		Integer count = new Integer(0);
		try {
			count = (Integer) getHibernateTemplate().execute(
					new HibernateCallback() {
						public Object doInHibernate(Session session) throws HibernateException {
							Integer _count = new Integer(0);
							Query query = getUpdateQuery(modeClass, updateSet,filter, session);
							_count = new Integer(query.executeUpdate());
							return _count;
						}
					});
		} catch (Throwable dae) {
			SysException.handleException(dae.getMessage(), dae);
		}
		return count.intValue();
	}

	public void merge(Object object) {
		try {
			getHibernateTemplate().merge(object);
		} catch (Throwable dae) {
			SysException.handleException(dae.getMessage(), dae);
		}

	}

	public int delete(Object object) {
		try {
			getHibernateTemplate().delete(object);
		} catch (Throwable dae) {
			SysException.handleException(dae.getMessage(), dae);
		}
		return 1;

	}

	public int delete(final Object[] objects) {
		Integer count = new Integer(0);
		try {
			count = (Integer) getHibernateTemplate().execute(
					new HibernateCallback() {
						public Object doInHibernate(Session session) throws HibernateException {
							checkWriteOperationAllowed(getHibernateTemplate(), session);
							if (objects == null) {
								return new Integer(0);
							}
							int max = objects.length;
							int _count = 0;
							for (int i = 0; i < max; i++) {
								session.refresh(objects[i]);
								session.delete(objects[i]);
								if (i % DEFAULT_BATCH_SIZE == 0) {
									session.flush();
									session.clear();
								}
								_count++;
							}
							return new Integer(_count);
						}
					});
		} catch (Throwable dae) {
			SysException.handleException(dae.getMessage(), dae);
		}
		return count.intValue();
	}

	public int delete(Class modeClass, String id) {
		final Filter groupFilter = FieldFilter.eq("id", id);
		int count = delete(modeClass, groupFilter);
		return count;
	}

	public int delete(Class modeClass, String[] id) {

		final Filter groupFilter = FieldFilter.in("id", id);
		int count = delete(modeClass, groupFilter);
		return count;

	}

	public int delete(final Class modeClass, String[] filternames,
			Object[] filtervalues) {
		final Filter groupFilter = getFilter(filternames, filtervalues);
		int count = delete(modeClass, groupFilter);
		return count;
	}

	public int delete(final Class modeClass, final Filter filter) {
		Integer count = new Integer(0);
		try {
			count = (Integer) getHibernateTemplate().execute(
					new HibernateCallback() {
						public Object doInHibernate(Session session) throws HibernateException {
							Integer _count = new Integer(0);
							Query query = getDeleteQuery(modeClass, filter, session);
							_count = new Integer(query.executeUpdate());
							return _count;
						}
					});
		} catch (Throwable dae) {
			SysException.handleException(dae.getMessage(), dae);
		}
		return count.intValue();
	}

	public void flush() {
		try {
			getHibernateTemplate().flush();
		} catch (Throwable dae) {
			SysException.handleException(dae.getMessage(), dae);
		}
	}

	public Serializable getIdentifier(final Object object) {
		Serializable s = null;
		s = (Serializable) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session) throws HibernateException {
						return session.getIdentifier(object);
					}
				});
		return s;
	}

	/**
	 * 获得实现的实体对象，当还为延迟的代理对象时自动进行对象加载，当为代理对象时自动转换成实现的实体对象
	 * 
	 * @param object
	 * @return
	 */
	public Object getImplementation(Object object) {
		if (!(object instanceof HibernateProxy))
			return object;
		HibernateProxy hibernateProxy = (HibernateProxy) object;
		LazyInitializer lazyInit = hibernateProxy.getHibernateLazyInitializer();
		object = lazyInit.getImplementation();
		hibernateProxy = null;
		lazyInit = null;
		return object;
	}

	/**
	 * 强制对当前对象读取数,支持单对象,数组,叠代(Iterator),集合等实体
	 * 
	 * @param proxy
	 *            Object
	 */
	public void initialize(Object proxyObject) {
		Object[] proxyArray = new Object[] { proxyObject };
		if (proxyObject instanceof Object[])
			proxyArray = (Object[]) proxyObject;
		if (proxyObject instanceof Collection) {
			Collection collObject = (Collection) proxyObject;
			proxyArray = collObject.toArray();
		}
		if (proxyObject instanceof Iterator) {
			Iterator itObject = (Iterator) proxyObject;
			proxyArray = ArrayUtils.toObjectArray(itObject);
		}
		for (int i = 0; proxyArray != null && i < proxyArray.length; i++) {
			Object _proxyObject = proxyArray[i];
			if (!Hibernate.isInitialized(_proxyObject))
				Hibernate.initialize(_proxyObject);
		}
	}

	/**
	 * 强制对象读取数,支持单对象,数组,叠代(Iterator),集合等实体, 对象读取策略根据对象的关系路径
	 * path格式:根模型属性名(符合属性,支持多对一和一对多的Set类型)/子模型属性名/增子属性名/... 学生/课程(一对多Set类型)/老师
	 * 
	 * @param proxy
	 *            Object
	 * @param path
	 *            String
	 * @throws Exception
	 */
	public void initialize(Object proxyObject, String path) {
		Hashtable map = new Hashtable();
		String[] propertyNames = StringUtils.split(path, "/");
		Level levelObj = new Level();
		levelObj.count = propertyNames.length;
		for (int i = 0; propertyNames != null && i < propertyNames.length; i++) {
			map.put(propertyNames[i], Level.HAS);
		}
		initialize(proxyObject, levelObj, map);
	}

	class Level {
		static public final String HAS = "HAS";
		public int i = 0;
		public int count = 0;
		public boolean isReturn() {
			return i > count;
		}
	};

	private void initialize(Object proxyObject, Level levelObj, Map map) {
		levelObj.i++;
		Object[] proxyArray = new Object[] { proxyObject };
		if (proxyObject instanceof Object[])
			proxyArray = (Object[]) proxyObject;
		if (proxyObject instanceof Collection) {
			Collection collObject = (Collection) proxyObject;
			proxyArray = collObject.toArray();
		}
		if (proxyObject instanceof Iterator) {
			Iterator itObject = (Iterator) proxyObject;
			proxyArray = ArrayUtils.toObjectArray(itObject);
		}
		for (int i = 0; proxyArray != null && i < proxyArray.length; i++) {
			Object _proxyObject = proxyArray[i];
			if (!Hibernate.isInitialized(_proxyObject)) {
				Hibernate.initialize(_proxyObject);
			}
			// 获得对象所有的方法
			Method[] methodArray = _proxyObject.getClass().getMethods();
			for (int j = 0; j < methodArray.length; j++) {
				// 对象方法
				String propertyName = "";
				String getMethodName = methodArray[j].getName();
				propertyName = StringUtils.uncapitalizeFirst(getMethodName.replaceAll("get", ""));
				// 验证域是否为强制读取数据的目标
				if (!Level.HAS.equals(map.get(propertyName)))continue;
				Object resultObject = null;
				try {
					resultObject = methodArray[j].invoke(_proxyObject, new Object[] {});
				} catch (Exception e) {
					resultObject = null;
				}
				if (resultObject == null)
					continue;
				if (!Hibernate.isInitialized(resultObject)) {
					Hibernate.initialize(resultObject);
				}
				if (!levelObj.isReturn()) {
					initialize(resultObject, levelObj, map);
					levelObj.i--;
				}
			}
		}
	}

	public Blob createBlob(byte[] bytes) {
		return Hibernate.createBlob(bytes);
		
	}

	public Blob createBlob(InputStream stream) {
		Blob blob = null;
		try {
			try {
				blob = Hibernate.createBlob(stream);
			} catch (IOException ex1) {
			}
		} catch (Throwable dae) {
			SysException.handleException(dae.getMessage(), dae);
		}
		return blob;
	}

	public Clob createClob(String string) {
		return Hibernate.createClob(string);
	}

	public Object load(Class _modeClass, Serializable _id) {
		Object obj = null;
		try {
			obj = getHibernateTemplate().get(_modeClass, _id);
		} catch (Exception dae) {
			return null;
		}
		return obj;
	}

	public Object load(Class cls, Map filterMap) {
		Filter groupFilter = getFilter(filterMap);
		return load(cls,groupFilter);
	}
	public Object load(Class cls, String[] filterNames, Object[] filterValues) {
		Filter groupFilter = getFilter(filterNames, filterValues);
		return load(cls,groupFilter);
	}
	
	public Object load(Class modeClass, String name, Object value) {
		// TODO 自动生成方法存根
		return this.load(modeClass,new String[]{name}, new Object[]{value});
	}
	
	public Object load(Class modelClass, Filter filter) {
		IQueryService queryService = getQueryService(modelClass, filter, false);
		List list= queryService.list();
		if(list==null||list.size()==0){
		   return null;
		}
		return list.get(0);
	}
	
	/**
	 * 通过实体类查询对象列表
	 * @param modelClass
	 * @return
	 */
	public List query(Class modelClass){
		IQueryService queryService = getQueryService(modelClass,null, false);
		List list = null;
		try{
			list = queryService.list();
		}catch(Exception ex){
		}
		return list;
	}
	
	/**
	 * 通过实体类查询对象列表
	 * @param modeClass
	 * @param filter
	 * @return
	 */
	public List query(Class modelClass, Filter filter){
		IQueryService queryService = getQueryService(modelClass, filter, false);
		List list = null;
		try{
			list = queryService.list();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return list;
	}
	/**
	 * 通过实体类查询对象列表
	 * @param modelClass
	 * @param queryArgMap
	 * @return
	 */
	public List query(Class modelClass, Map queryArgMap){
		Filter _filter=getFilter(queryArgMap);
		return this.query(modelClass, _filter);
	}
	/**
	 * 通过实体类查询对象列表
	 * @param modelClass
	 * @param queryNames
	 * @param queryValues
	 * @return
	 */
	public List query(Class modelClass, String[] queryNames,Object[] queryValues){
		Filter _filter=getFilter(queryNames,queryValues);
		return this.query(modelClass, _filter);
	}
	/**
	 * 通过实体类查询对象列表
	 * @param modelClass
	 * @param queryName
	 * @param queryValue
	 * @return
	 */
	public List query(Class modelClass, String queryName,Object queryValue){
		Filter _filter=getFilter(queryName,queryValue);
		return this.query(modelClass, _filter);
	}
	
	public int count(Class modelClass) {
		Filter filter = null;
		return count(modelClass, filter);
	}

	public int count(Class modelClass, String[] filterNames, Object[] filterValues) {
		final Filter groupFilter = getFilter(filterNames, filterValues);
		return count(modelClass, groupFilter);
	}

	public int count(Class modelClass, Map filterMap) {
		final Filter groupFilter = getFilter(filterMap);
		return count(modelClass, groupFilter);
	}

	public int count(final Class modelClass, final Filter filter) {
		return this.getQueryService(modelClass, filter).size();
	}
	
	public List filter(final Object collection, String[] names, Object[] values) {
		List list = null;
		try {
			Filter groupFilter = getFilter(names, values);
			final String hql = " where " + groupFilter.getFilterExpr();
			list = getHibernateTemplate().executeFind(new HibernateCallback() {
				public Object doInHibernate(Session session) {
					Object obj = session.createFilter(collection, hql);
					return obj;
				}
			});
		} catch (Exception dae) {
			return new ArrayList();
		}
		return list;
	}

	public List order(final Object collection, OrderBy order) {
		List list = null;
		try {
			String orderStr = "";
			if (order != null) {
				orderStr = order.getOrderByExpr("this");
			}
			final String hql = " " + orderStr;
			list = getHibernateTemplate().executeFind(new HibernateCallback() {
				public Object doInHibernate(Session session) {
					Object obj = session.createFilter(collection, hql);
					return obj;
				}
			});
		} catch (Exception dae) {
			return new ArrayList();
		}
		return list;
	}

	public String queryNativteSQL(final String sql) {
		Float arryObj = null;
		try {
			arryObj = (Float) getHibernateTemplate().execute(
					new HibernateCallback() {
						public Object doInHibernate(Session session) throws HibernateException {
							Query query = session.createQuery(sql);
							List list = query.list();
							if (list.isEmpty()) {
								return null;
							} else {
								return (Float) list.get(0);
							}
						}
					});
		} catch (Throwable dae) {
			SysException.handleException(dae.getMessage(), dae);
		}
		if (arryObj != null) {
			return arryObj.toString();
		} else
			return null;
	}
	public List executeNativteSQL(final String sql) {
		List listObj = null;
		try {
			listObj = (List)getHibernateTemplate().execute(
					new HibernateCallback() {
						public Object doInHibernate(Session session) throws HibernateException {
							Query query = session.createSQLQuery(sql).setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
							List list = query.list();
							return list;
						}
					});
		} catch (Throwable dae) {
			SysException.handleException(dae.getMessage(), dae);
		}
		return listObj;
	}
	
	public Integer queryExecuteUpdateSQL(final String sql) {
		Integer rowCount = null;
		try {
			rowCount = (Integer) getHibernateTemplate().execute(
					new HibernateCallback() {
						public Object doInHibernate(Session session) throws HibernateException {
							SQLQuery query = session.createSQLQuery(sql);
							return query.executeUpdate();
						}
					});
		} catch (Throwable dae) {
			SysException.handleException(dae.getMessage(), dae);
		}
		return rowCount;
	}

	public IQueryService getQueryService(final Class modelClass) {
		final Filter groupFilter = null;
		return getQueryService(modelClass, groupFilter);
	}

	public IQueryService getQueryService(final Class modelClass, String[] filterNames, Object[] filterValues) {
		final Filter groupFilter = getFilter(filterNames, filterValues);
		return getQueryService(modelClass, groupFilter);
	}

	public IQueryService getQueryService(Class modelClass, String filterName, Object[] filterValues) {
		Filter filter = FieldFilter.in(filterName, filterValues);
		return getQueryService(modelClass, filter);

	}

	public IQueryService getQueryService(Class modelClass, String filterName, Object filterValue) {
		final Filter groupFilter = getFilter(new String[] { filterName }, new Object[] { filterValue });
		return getQueryService(modelClass, groupFilter);
	}

	public IQueryService getQueryService(Class modelClass, Map filterMap) {
		Filter groupFilter = getFilter(filterMap);
		return getQueryService(modelClass, groupFilter);
	}

	public IQueryService getQueryService(final Class modelClass, final Filter filter) {
		return getQueryService(modelClass, filter, true);
	}

	public IQueryService getQueryService(final Class modelClass, final Filter filter, boolean isReturnCount) {
		Session session = getQueryServiceSession();
		this.queryService=newQueryService();
		this.queryService.set_session(session);
		this.queryService.setResultToMapModel(false);
		this.queryService.set_modelClass(modelClass);
		this.queryService.setReturnCount(isReturnCount);
		this.queryService.set_filter(filter);		
		this.queryService.set_queryType(IQueryService.QUERY_TYPE_SIMPLE);
		return this.queryService;
	}

	public IQueryService getQueryService(final Class startModelClass,
			final Select select, String[] filterNames, Object[] filterValues) {
		Filter groupFilter = getFilter(filterNames, filterValues);
		return getQueryService(startModelClass, select, groupFilter);
	}

	public IQueryService getQueryService(Class startModelClass, Select select, Map filterMap) {
		Filter groupFilter = getFilter(filterMap);
		return getQueryService(startModelClass, select, groupFilter);
	}

	public IQueryService getQueryService(final Class startModelClass, final Select select, final Filter filter) {
		return getQueryService(startModelClass, select, filter, true);
	}

	public IQueryService getQueryService(final Class startModelClass, final Select select, final Filter filter, final boolean isReturnCount) {
		Session session = getQueryServiceSession();
		this.queryService=newQueryService();
		this.queryService.set_session(session);
		this.queryService.set_modelClass(startModelClass);		
		this.queryService.setReturnCount(isReturnCount);
		this.queryService.setResultToMapModel(false);
		this.queryService.set_filter(filter);
		this.queryService.setComplexQuerySelect(select);
		this.queryService.set_queryType(IQueryService.QUERY_TYPE_COMPLEX);		
		return this.queryService;
	}
	
	/**
	 * 获取查询服务接口(结果以List<Entity>格式返回)
	 * @param queryName
	 * @param queryArgMap
	 * @return
	 */                   
	public IQueryService getQueryServiceByQueryName2Entity(final String queryName,final Map queryArgMap) {
		return getQueryServiceByQueryName2Entity(queryName, queryArgMap, true);
	}
	
	/**
	 * 获取查询服务接口(结果以List<Entity>格式返回)
	 * @param queryName
	 * @param queryArgMap
	 * @param isReturnCount 是否查询记录总条数
	 * @return
	 */
	public IQueryService getQueryServiceByQueryName2Entity(final String queryName,final Map queryArgMap, final boolean isReturnCount) {
		Session session = getQueryServiceSession();
		this.queryService=newQueryService();
		this.queryService.set_session(session);
		this.queryService.setResultToMapModel(false);
		this.queryService.set_nameQuery_QueryName(queryName);
		this.queryService.set_nameQuery_Parameter(queryArgMap);
		this.queryService.setReturnCount(isReturnCount);
		this.queryService.set_queryType(IQueryService.QUERY_TYPE_SQL_NAMEQUERY);
		return this.queryService;
	}
	
	/**
	 * 获取查询服务接口(结果以List<Map>格式返回)
	 * @param queryName
	 * @param queryArgMap
	 * @return
	 */
	public IQueryService getQueryServiceByQueryName2Map(String queryName,Map queryArgMap){
		return getQueryServiceByQueryName2Map(queryName, queryArgMap, true);		
	}
	
	/**
	 * 获取查询服务接口(结果以List<Entity>格式返回)
	 * @param queryName
	 * @param queryArgMap
	 * @param isReturnCount 是否查询记录总条数
	 * @return
	 */
	public IQueryService getQueryServiceByQueryName2Map(String queryName,Map queryArgMap, final boolean isReturnCount){
		Session session = getQueryServiceSession();
		this.queryService=newQueryService();
		this.queryService.set_session(session);
		this.queryService.setResultToMapModel(true);
		this.queryService.set_nameQuery_QueryName(queryName);
		this.queryService.set_nameQuery_Parameter(queryArgMap);
		this.queryService.set_queryType(IQueryService.QUERY_TYPE_SQL_NAMEQUERY);
		return this.queryService;		
	}
	
	public List query(Class _modelClass, Map _queryArgMap, Page _page, String _orderColumn, String _orderSort) {
		Filter _filter=this.getFilter(_queryArgMap);
		return query(_modelClass,_filter,_page,_orderColumn,_orderSort);
	}
	
	public List query(Class _modelClass, String _queryArgName, Object _queryArgValue, Page _page, String _orderColumn, String _orderSort) {
		Filter _filter=this.getFilter(_queryArgName, _queryArgValue);
		return query(_modelClass,_filter,_page,_orderColumn,_orderSort);
	}
	
	public List query(Class _modelClass, String[] _queryArgNames, Object[] _queryArgValues, Page _page, String _orderColumn, String _orderSort) {
		Filter _filter=this.getFilter(_queryArgNames, _queryArgValues);
		return query(_modelClass,_filter,_page,_orderColumn,_orderSort);
	}
	
	/**
	 * 查询单个实体对象
	 * @param _modelClass
	 * @param _filter：查询条件对象
	 * @param _page：查询分页对象，包括开始行号，返回行数
	 * @param _orderColumn：所要排序的列
	 * @param _orderSort：排序方式(asc,desc)等
	 * @return
	 */
	public List query(Class _modelClass, Filter _filter, Page _page,String _orderColumn, String _orderSort) {
		IQueryService queryService = this.getQueryService(_modelClass, null, _filter, false);
		if (StringUtils.isNotNull(_orderColumn)) {
			OrderBy _orderBy = new OrderBy();
			if (StringUtils.isNotNull(_orderBy)) {
				_orderBy.order(_orderColumn, _orderSort);
			} else {
				_orderBy.order(_orderColumn, "ASC");
			}
			queryService.setOrderBy(_orderBy);
		}
		return queryService.list(_page);
	}

	/**
	 * 查询实体对象记录总数
	 * @param _modelClass
	 * @param _filer
	 * @return
	 */
	public int countEntity(Class _modelClass, Filter filter) {
		IQueryService queryService = this.getQueryService(_modelClass, filter) ;
		return queryService.size();
	}

	private Session getQueryServiceSession() {
		Session _session=null ;
		try {
			_session = this.getSession();
			return _session;
		} catch (Exception dae) {
			SysException.handleException(dae.getMessage(), dae);
		}
		return _session;
	}
	                      
	public IQueryService getQueryServiceByHQLQueryName2Entity(String queryName, Map queryArgMap, boolean isReturnCount) {
		Session session = getQueryServiceSession();
		this.queryService=newQueryService();
		this.queryService.set_session(session);
		this.queryService.set_nameQuery_QueryName (queryName);
		this.queryService.set_nameQuery_Parameter (queryArgMap);
		this.queryService.setReturnCount (isReturnCount);
		this.queryService.set_queryType(IQueryService.QUERY_TYPE_HQL_NAMEQUERY);
		this.queryService.setResultToMapModel(false);
		return this.queryService;
	}

	public IQueryService getQueryServiceByHQLQueryName2Entity(String queryName, Map queryArgMap) {
		return getQueryServiceByHQLQueryName2Entity(queryName, queryArgMap,true);
	}

	public IQueryService getQueryServiceByHQLQueryName2Map(String queryName, Map queryArgMap, boolean isReturnCount) {
		Session session = getQueryServiceSession();
		this.queryService=newQueryService();
		this.queryService.set_queryType(IQueryService.QUERY_TYPE_HQL_NAMEQUERY);
		this.queryService.set_session( session);
		this.queryService.set_nameQuery_QueryName ( queryName);
		this.queryService.set_nameQuery_Parameter ( queryArgMap);
		this.queryService.setReturnCount (isReturnCount);
		this.queryService.setResultToMapModel(true);
		return this.queryService;
	}

	public IQueryService getQueryServiceByHQLQueryName2Map(String queryName, Map parameter) {
		return getQueryServiceByHQLQueryName2Map(queryName,parameter,true);
	}
	
	public List find(final String hql) { 
		List list = null; 
		try { 
			list = getHibernateTemplate().executeFind(new HibernateCallback() { 
				public Object doInHibernate(Session session) throws HibernateException { 
					return session.createQuery(hql).list(); 
				} 
			});
		} catch (Throwable dae) {
		 	SysException.handleException(dae.getMessage(), dae); 
		} 
		return list; 
	}
	 
	public final static String DB_UNKNOW="unknow";
	public final static String DB_SQLSERVER="sqlserver";
	public final static String DB_ORACLE="oracle";
	public final static String DB_DB2="db2";
	public final static String DB_MYSQL="mysql";
	private static String dbName=null;
	public static String getDbName() {
		if(dbName!=null) return dbName;
		Object _url=null;
		try{
		  Object _dataSource=SpringManager.getComponent("dataSource");
		  try{
		      _url=BeanUtils.getProperty(_dataSource,"url");
			}catch(Exception ee){
				_url=null;
			}
		  try{
			  if(StringUtils.isNull(_url))
		      _url=BeanUtils.getProperty(_dataSource,"jdbcUrl");
			}catch(Exception ee){
				
			}
		}catch(Exception e){
			
		}
		if(_url==null){
			dbName=DB_SQLSERVER;
			return dbName;
		}
		if(_url.toString().indexOf(DB_SQLSERVER)>0){
			dbName=DB_SQLSERVER;
			return dbName;
		}
		if(_url.toString().indexOf(DB_ORACLE)>0){
			dbName=DB_ORACLE;
			return dbName;
		}
		if(_url.toString().indexOf(DB_MYSQL)>0){
			dbName=DB_MYSQL;
			return dbName;
		}
		if(_url.toString().indexOf(DB_DB2)>0){
			dbName=DB_DB2;
			return dbName;
		}
		dbName=DB_SQLSERVER;
		return dbName;
	}
	
	private static void setCallProcParameter(CallableStatement _pcs,String _sql,Map _argsMap){
        if(_argsMap==null)return;
        //获得存储过程参数串 {call xxxProc(:para1,:para2)}---> :para1,:para2
        _sql=_sql.replaceAll(" ","");
	   	_sql=_sql.replaceAll("call","call ");
	   	String _regexStr = "\\((([:\\w]*\\,*)*)\\)";
	   	Pattern _patt = Pattern.compile(_regexStr);
	   	Matcher _matc =null;
	   	String _callProcParaStr=_sql;
	   	try{
	   		_matc=_patt.matcher(_sql);
	   		if(_matc.find()){
	   			_callProcParaStr=_matc.group(1);
	   			System.out.println(_callProcParaStr);
	   		}
	   	}catch(Exception e){
		   
	   	}
		String[] _callProcParaArray=_callProcParaStr.split(",");
		for(int i=0;_callProcParaArray!=null&&i<_callProcParaArray.length;i++){
			String _callProcParaName=_callProcParaArray[i].replaceAll(":","");
			Object _callProcParaValue=_argsMap.get(_callProcParaName);
			try{
				_pcs.setObject(i+1,_callProcParaValue);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	private static List convertList(ResultSet _rs) throws SQLException { 
		List _list = new ArrayList(); 
        if(_rs==null){
        	return null; 
        }
		ResultSetMetaData _md = _rs.getMetaData(); 
		int columnCount = _md.getColumnCount(); //Map rowData; 
		while (_rs.next()) { //rowData = new HashMap(columnCount); 
		Map _rowData = new HashMap(); 
		for (int i = 1; i <= columnCount; i++) { 
			_rowData.put(_md.getColumnName(i), _rs.getObject(i)); 
		}
		_list.add(_rowData); 
		} 
		return _list; 
	}
	
	public List call(final String _queryName,Map _argsMap){
		return call(_queryName,_argsMap,true);
	}
	
	public List call(final String _queryName,Map _argsMap,boolean _isAutoCommit){
		String _sql=DaoUtils.getNamedSQLQuery(_queryName).toString();
		String _backSql=_sql;
		CallableStatement _pcs=null;
		Transaction _tran = null;
		ResultSet   _rs=null;
		List _resultList=null;
		try{
		   if(_isAutoCommit)
			 _tran = getSession().beginTransaction();
           //存储过程调用串 参数格式转换为序号参数形式 {call xxxProc(:para1,:para2)}---> {call xxxProc(?,?)}
		   String _regexStr = "(:[a-zA-Z0-9_]*)";
		   Pattern _patt = Pattern.compile(_regexStr);
		   Matcher _matc =null;
		   try{
		     _matc=_patt.matcher(_sql);
		     if(_matc.find()){
		    	 _sql= _matc.replaceAll("?");
		     }
		   }catch(Exception e){
			   
		   }
		   _pcs=getSession().connection().prepareCall(_sql);
		   setCallProcParameter(_pcs,_backSql,_argsMap);
		   _pcs.execute();
		   _rs=_pcs.getResultSet();
		   _resultList=convertList(_rs);
		   if(_isAutoCommit)
			   _tran.commit();
		}catch(Exception e){
		  if(_rs!=null){
			  try{
			    _rs.close();
			  } catch(Exception ee){
				  
			  }
		  }
		  if(_pcs!=null){
			  try{
			    _pcs.close();
			  }catch(Exception ee){
				  
			  }
		  }
		  e.printStackTrace();  
		}finally{
		  if(_rs!=null){
			  try{
			    _rs.close();
			  }catch(Exception ee){
				  
			  }
		  }
		  if(_pcs!=null){
			  try{
			    _pcs.close();
			  }catch(Exception ee){
				  
			  }
		  }
		}
		return _resultList;
	}
}
