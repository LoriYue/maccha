package org.maccha.dao.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
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
import java.util.Set;
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
public class DaoServiceImpl extends HibernateDaoSupport
  implements IDaoService, IUpdateInterceptor
{
  private static Logger log = Logger.getLogger(DaoUtils.class);
  public static final String ORDER_COLUMN = "orderColumn";
  public static final int DEFAULT_BATCH_SIZE = 25;
  private IQueryService queryService;
  public static final String DB_UNKNOW = "unknow";
  public static final String DB_SQLSERVER = "sqlserver";
  public static final String DB_ORACLE = "oracle";
  public static final String DB_DB2 = "db2";
  public static final String DB_MYSQL = "mysql";
  private static String dbName = null;

  @Resource(name="sessionFactory")
  public void setSuperSessionFactory(SessionFactory sessionFactory)
  {
    super.setSessionFactory(sessionFactory);
  }

  public String getQueryNameType(String _queryName)
  {
    return DaoUtils.getQueryNameType(_queryName);
  }

  protected HibernateTemplate createHibernateTemplate(SessionFactory arg0)
  {
    return super.createHibernateTemplate(arg0);
  }

  protected void initDao() throws Exception
  {
    super.initDao();
  }

  protected Object clone() throws CloneNotSupportedException
  {
    return super.clone();
  }

  public boolean equals(Object obj)
  {
    return super.equals(obj);
  }

  protected void finalize() throws Throwable
  {
    super.finalize();
  }

  public int hashCode()
  {
    return super.hashCode();
  }

  public String toString()
  {
    return super.toString();
  }
  public List queryByQueryName2Entity(String _queryName, Map _queryArgMap) {
    return getQueryServiceByQueryName2Entity(_queryName, _queryArgMap, false).list();
  }

  public List queryByQueryName2Entity(String _queryName, String[] _queryArgNames, Object[] _queryArgValues) {
    Map _queryArgMap = toMap(_queryArgNames, _queryArgValues);
    return getQueryServiceByQueryName2Entity(_queryName, _queryArgMap, false).list();
  }
  public List queryByQueryName2Entity(String _queryName, String _queryArgName, Object _queryArgValue) {
    Map _queryArgMap = toMap(_queryArgName, _queryArgValue);
    return getQueryServiceByQueryName2Entity(_queryName, _queryArgMap, false).list();
  }

  public List queryByQueryName2Entity(String _queryName, Map _queryArgMap, Page _page) {
    return getQueryServiceByQueryName2Entity(_queryName, _queryArgMap).list(_page);
  }

  public List queryByQueryName2Entity(String _queryName, String[] _queryArgNames, Object[] _queryArgValues, Page _page) {
    Map _queryArgMap = toMap(_queryArgNames, _queryArgValues);
    return getQueryServiceByQueryName2Entity(_queryName, _queryArgMap).list(_page);
  }

  public List queryByQueryName2Entity(String _queryName, String _queryArgName, Object _queryArgValue, Page _page)
  {
    Map _queryArgMap = toMap(_queryArgName, _queryArgValue);

    return getQueryServiceByQueryName2Entity(_queryName, _queryArgMap).list(_page);
  }

  public List queryByQueryName2Entity(String _queryName, Map _queryArgMap, Page _page, String _orderClumn, String _orderSort) {
    return getQueryServiceByQueryName2Entity(_queryName, _queryArgMap).list(_page);
  }

  public List queryByQueryName2Map(String _queryName, Map _queryArgMap, Page _page, String _orderColumn, String _orderSort)
  {
    if ((StringUtils.isNotNull(_orderSort)) && (StringUtils.isNotNull(_orderColumn)))
    {
      _queryName = _queryName + "_" + _orderSort;
      _queryArgMap.put("orderColumn", _orderColumn);
    }
    IQueryService _queryService = getQueryServiceByQueryName2Map(_queryName, _queryArgMap);

    return _queryService.list(_page);
  }

  private Map toMap(String name, Object value)
  {
    return toMap(new String[] { name }, new Object[] { value });
  }

  private Map toMap(String[] names, Object[] values) {
    Map _queryArgMap = new HashMap();
    for (int i = 0; (names != null) && (i < names.length); i++) {
      _queryArgMap.put(names[i], values[i]);
    }
    return _queryArgMap;
  }

  private Filter getFilter(String filterName, Object filterValue)
  {
    return getFilter(new String[] { filterName }, new Object[] { filterValue });
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
      groupFilter.add(FieldFilter.eq(nameArray[i], map.get(nameArray[i])));
    }

    return groupFilter;
  }

  public List query(Class _modelClass, Select _select, Filter _filter, Page _page, String _orderColumn, String _orderSort, List _fetch)
  {
    IQueryService _queryService = getQueryService(_modelClass, _select, _filter);

    if (StringUtils.isNotNull(_orderColumn)) {
      OrderBy _orderBy = new OrderBy();
      if (StringUtils.isNotNull(_orderBy))
        _orderBy.order(_orderColumn, _orderSort);
      else {
        _orderBy.order(_orderColumn, "ASC");
      }
      _queryService.setOrderBy(_orderBy);
    }

    for (int i = 0; (_fetch != null) && (i < _fetch.size()); i++) {
      _queryService.setFetchMode((String)_fetch.get(i), 1);
    }
    return _queryService.list(_page);
  }

  public List query(Class _modelClass, Select _select, Filter _filter, Page _page, OrderBy _orderBy)
  {
    IQueryService queryService = getQueryService(_modelClass, _select, _filter);
    if (_orderBy != null) queryService.setOrderBy(_orderBy);
    return queryService.list(_page);
  }

  public List query(Class _modelClass, Select _select, Filter _filter, Page _page, String _orderColumn, String _orderSort)
  {
    IQueryService queryService = getQueryService(_modelClass, _select, _filter);

    if (StringUtils.isNotNull(_orderColumn)) {
      OrderBy _orderBy = new OrderBy();
      if (StringUtils.isNotNull(_orderBy))
        _orderBy.order(_orderColumn, _orderSort);
      else {
        _orderBy.order(_orderColumn, "ASC");
      }
      queryService.setOrderBy(_orderBy);
    }
    return queryService.list(_page);
  }

  public int countByHQLQueryName(String _queryName, Map _queryArgMap)
  {
    int _count = 0;
    IQueryService _queryService = getQueryServiceByHQLQueryName2Entity(_queryName, _queryArgMap);

    _count = _queryService.size();

    return _count;
  }

  public List queryByHQLQueryName2Map(String _queryName, Map _queryArgMap, Page _page, String _orderColumn, String _orderSort) {
    return queryByHQLQueryName2Map(_queryName, _queryArgMap, _page);
  }

  public List queryByHQLQueryName2Map(String _queryName, Map _queryArgMap, Page _page)
  {
    IQueryService _queryService = getQueryServiceByHQLQueryName2Map(_queryName, _queryArgMap);

    return _queryService.list(_page);
  }

  public List queryByHQLQueryName2Map(String _queryName, Map _queryArgMap)
  {
    IQueryService _queryService = getQueryServiceByHQLQueryName2Map(_queryName, _queryArgMap, true);
    return _queryService.list();
  }

  public int countByQueryName(String queryName, Map _queryArgMap) {
    int count = 0;
    IQueryService queryService = getQueryServiceByQueryName2Map(queryName, _queryArgMap);
    count = queryService.size();
    return count;
  }

  public List queryByQueryName2Map(String _queryName, Map _queryArgMap, Page _page)
  {
    IQueryService queryService = getQueryServiceByQueryName2Map(_queryName, _queryArgMap);
    return queryService.list(_page);
  }

  public List queryByQueryName2Map(String _queryName, String[] _queryArgNames, Object[] _queryArgValues, Page _page)
  {
    Map _queryArgMap = toMap(_queryArgNames, _queryArgValues);
    return queryByQueryName2Map(_queryName, _queryArgMap);
  }

  public List queryByQueryName2Map(String _queryName, String _queryArgName, Object _queryArgValue, Page _page)
  {
    Map _queryArgMap = toMap(_queryArgName, _queryArgValue);
    return queryByQueryName2Map(_queryName, _queryArgMap);
  }

  public List queryByQueryName2Map(String _queryName, Map _queryArgMap)
  {
    return getQueryServiceByQueryName2Map(_queryName, _queryArgMap, true).list();
  }

  public List queryByQueryName2Map(String _queryName, String[] _queryArgNames, Object[] _queryArgValues)
  {
    Map _queryArgMap = toMap(_queryArgNames, _queryArgValues);
    return queryByQueryName2Map(_queryName, _queryArgMap);
  }

  public List queryByQueryName2Map(String _queryName, String _queryArgName, Object _queryArgValue) {
    Map _queryArgMap = toMap(_queryArgName, _queryArgValue);
    return queryByQueryName2Map(_queryName, _queryArgMap);
  }

  public Map queryByQueryName2OneMap(String _queryName, Map _queryArgMap)
  {
    Page _page = new Page();
    _page.setRowCount(1);

    List _resultList = queryByQueryName2Map(_queryName, _queryArgMap, _page);

    if ((_resultList == null) || (_resultList.size() == 0)) {
      return null;
    }

    return (Map)_resultList.get(0);
  }

  public Map queryByQueryName2OneMap(String _queryName, String _queryArgName, Object _queryArgValue)
  {
    Map _queryArgMap = toMap(_queryArgName, _queryArgValue);
    return queryByQueryName2OneMap(_queryName, _queryArgMap);
  }

  public Map queryByQueryName2OneMap(String _queryName, String[] _queryArgNames, Object[] _queryArgValues)
  {
    Map _queryArgMap = toMap(_queryArgNames, _queryArgValues);
    return queryByQueryName2OneMap(_queryName, _queryArgMap);
  }

  public List queryByHQLQueryName2Entity(String _queryName, Map _queryArgMap, Page _page)
  {
    IQueryService _queryService = getQueryServiceByHQLQueryName2Entity(_queryName, _queryArgMap);

    return _queryService.list(_page);
  }

  public List queryByHQLQueryName2Entity(String _queryName, Map _queryArgMap, Page _page, String _orderColumn, String _orderSort) {
    return queryByHQLQueryName2Entity(_queryName, _queryArgMap, _page);
  }

  public List queryByHQLQueryName2Entity(String _queryName, Map _queryArgMap)
  {
    IQueryService _queryService = getQueryServiceByHQLQueryName2Entity(_queryName, _queryArgMap, false);
    return _queryService.list();
  }

  public static final IQueryService newQueryService()
  {
    return new QueryServiceImpl();
  }

  public Serializable save(Object objectToSave)
  {
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

  protected void checkWriteOperationAllowed(HibernateTemplate template, Session session) throws InvalidDataAccessApiUsageException
  {
    if ((template.isCheckWriteOperations()) && (template.getFlushMode() != 2) && (FlushMode.NEVER.equals(session.getFlushMode())))
    {
      throw new InvalidDataAccessApiUsageException("Write operations are not allowed in read-only mode (FlushMode.NEVER) - turn your Session into FlushMode.AUTO or remove 'readOnly' marker from transaction definition");
    }
  }

  public void save(List objectsToSave)
  {
    try
    {
      getHibernateTemplate().execute(new HibernateCallback(objectsToSave)
      {
        public Object doInHibernate(Session session) throws HibernateException
        {
          DaoServiceImpl.this.checkWriteOperationAllowed(DaoServiceImpl.this.getHibernateTemplate(), session);
          if (this.val$objectsToSave == null) {
            return null;
          }
          int max = this.val$objectsToSave.size();
          for (int i = 0; i < max; i++) {
            session.save(this.val$objectsToSave.get(i));
            if (((i != 0) && (i % 25 == 0)) || (i == max - 1)) {
              session.flush();
              session.clear();
            }
          }
          return null;
        } } );
    } catch (Throwable dae) {
      SysException.handleException(dae.getMessage(), dae);
    }
  }

  public int update(Object object)
  {
    try
    {
      getHibernateTemplate().update(object);
    } catch (Throwable dae) {
      SysException.handleException(dae.getMessage(), dae);
    }
    return 1;
  }

  public int update(Object object, boolean isIncludeNullValue) {
    try {
      if (!(object instanceof HibernateProxy)) {
        ClassUtils ClassUtils = null;
        Object tmpObj = load(object.getClass(), (String)ClassUtils.get(object, "id"));
        ObjectUtils.copyProperties(object, tmpObj, isIncludeNullValue);
        getHibernateTemplate().update(tmpObj);
      } else {
        getHibernateTemplate().update(object);
      }
    } catch (Throwable dae) {
      SysException.handleException(dae.getMessage(), dae);
    }
    return 1;
  }

  public int updateByQueryName(String queryName, Map _parameter)
  {
    Integer count = new Integer(0);
    StringBuffer _strSQLQuery = DaoUtils.getNamedSQLQuery(queryName);
    try {
      count = (Integer)getHibernateTemplate().execute(new HibernateCallback(_strSQLQuery, queryName, _parameter)
      {
        public Object doInHibernate(Session session) throws HibernateException {
          DaoServiceImpl.this.checkWriteOperationAllowed(DaoServiceImpl.this.getHibernateTemplate(), session);
          if (this.val$_strSQLQuery == null) {
            SysException.handleException(this.val$queryName + " sql-query 在XML中没有配置!");
          }

          SqlExpr.parseExpr(this.val$_strSQLQuery, this.val$_parameter);

          SqlExpr.parseJavaFuncExpr(this.val$_strSQLQuery, this.val$_parameter);

          SqlFunc.parseSqlFunc(this.val$_strSQLQuery, this.val$_parameter);

          Query _query = session.createSQLQuery(this.val$_strSQLQuery.toString());
          DaoUtils.setQueryParameters(_query, this.val$_parameter);
          int _count = _query.executeUpdate();
          return new Integer(_count);
        } } );
    } catch (Throwable dae) {
      SysException.handleException(dae.getMessage(), dae);
    }
    return count.intValue();
  }

  public int insertByQueryName(String queryName, Map _parameter)
  {
    Integer count = new Integer(0);
    StringBuffer _strSQLQuery = DaoUtils.getNamedSQLQuery(queryName);
    try {
      count = (Integer)getHibernateTemplate().execute(new HibernateCallback(_strSQLQuery, queryName, _parameter)
      {
        public Object doInHibernate(Session session) throws HibernateException {
          DaoServiceImpl.this.checkWriteOperationAllowed(DaoServiceImpl.this.getHibernateTemplate(), session);
          if (this.val$_strSQLQuery == null) {
            SysException.handleException(this.val$queryName + " sql-query 在XML中没有配置!");
          }

          SqlExpr.parseExpr(this.val$_strSQLQuery, this.val$_parameter);

          SqlExpr.parseJavaFuncExpr(this.val$_strSQLQuery, this.val$_parameter);

          SqlFunc.parseSqlFunc(this.val$_strSQLQuery, this.val$_parameter);

          SQLQuery _query = session.createSQLQuery(this.val$_strSQLQuery.toString());
          DaoUtils.setQueryParameters(_query, this.val$_parameter);
          int _count = _query.executeUpdate();
          return new Integer(_count);
        } } );
    } catch (Throwable dae) {
      SysException.handleException(dae.getMessage(), dae);
    }
    return count.intValue();
  }
  public int update(Object[] objectsToUpdate) {
    Integer count = new Integer(0);
    try {
      count = (Integer)getHibernateTemplate().execute(new HibernateCallback(objectsToUpdate)
      {
        public Object doInHibernate(Session session) throws HibernateException
        {
          DaoServiceImpl.this.checkWriteOperationAllowed(DaoServiceImpl.this.getHibernateTemplate(), session);

          if (this.val$objectsToUpdate == null) {
            return new Integer(0);
          }
          int max = this.val$objectsToUpdate.length;
          int _count = 0;
          for (int i = 0; i < max; i++) {
            session.update(this.val$objectsToUpdate[i]);
            if (((i != 0) && (i % 25 == 0)) || (i == max - 1)) {
              session.flush();
              session.clear();
            }
            _count++;
          }
          return new Integer(_count);
        } } );
    } catch (Throwable dae) {
      SysException.handleException(dae.getMessage(), dae);
    }
    return count.intValue();
  }

  Query getUpdateQuery(Class cls, UpdateSet updateSet, Filter filter, Session session)
  {
    String className = StringUtils.unqualify(cls.getName());

    if (!updateSet.isHasUpdateSet()) {
      SysException.handleException("没有提供更新的数据项,updateSet=" + updateSet);
    }

    String updateStr = "update " + className + " set 1=1";

    Object[] names = updateSet.getSet().keySet().toArray();
    Object[] values = updateSet.getSet().values().toArray();
    String[] parameterNames = new String[names.length];

    for (int i = 0; (names != null) && (i < names.length); i++) {
      parameterNames[i] = ("update" + (String)names[i]);
      updateStr = updateStr + "," + (String)names[i] + "=:" + parameterNames[i];
    }

    Object[] exprNames = updateSet.getExprSet()[0].keySet().toArray();

    Object[] exprs = updateSet.getExprSet()[0].values().toArray();

    Object[] exprValues = updateSet.getExprSet()[1].values().toArray();

    ArrayUtils temp_arrayUtils = new ArrayUtils();

    Vector temp_exprValues_vecor = new Vector();
    String[] exprParameterNames = null;
    Object[] exprParameterValues = null;

    for (int i = 0; (exprValues != null) && (i < exprValues.length); i++) {
      String exprParameterName = "update" + (String)exprNames[i];
      String[] exprVars = ((String)exprs[i]).split("\\?");
      String exprStr = "";
      for (int k = 0; (exprVars != null) && (k < exprVars.length); k++) {
        exprStr = exprStr + exprVars[k] + ":" + exprParameterName + k;
        temp_arrayUtils.addString(exprParameterName + k);
        temp_exprValues_vecor.add(((Object[])(Object[])exprValues[i])[k]);
      }
      updateStr = updateStr + ",set " + (String)exprNames[i] + "=" + exprStr;
    }

    exprParameterNames = temp_arrayUtils.getStringArray();
    exprParameterValues = temp_exprValues_vecor.toArray();
    temp_exprValues_vecor = null;
    temp_arrayUtils = null;

    if (((exprParameterValues != null) || (exprParameterNames != null)) && (
      (exprParameterValues == null) || (exprParameterNames == null) || (exprParameterValues.length != exprParameterNames.length)))
    {
      String tmp1 = "";
      String tmp2 = "";

      for (int i = 0; (exprs != null) && (i < exprs.length); i++) {
        tmp1 = tmp1 + exprs[i];
      }

      for (int i = 0; (exprParameterValues != null) && (i < exprParameterValues.length); i++) {
        tmp2 = tmp2 + exprParameterValues[i];
      }
      SysException.handleException("表达式更新数据中的变量同提供的变量值不一致(" + tmp1 + "," + tmp2 + ")");
    }

    String whereStr = "";
    if (filter != null) {
      whereStr = " where " + filter.getFilterExpr();
    }

    updateStr = updateStr.replaceAll("1\\=1\\,", " ");
    String hql = updateStr + whereStr;

    log.info("session = " + session + ",hql = " + hql);
    Query query = session.createQuery(hql);

    if (filter != null) {
      query = setQueryParameter(query, filter);
    }

    for (int i = 0; (parameterNames != null) && (i < parameterNames.length); i++) {
      query.setParameter(parameterNames[i], values[i]);
    }

    int i = 0;
    for (; (exprParameterNames != null) && (i < exprParameterNames.length); i++)
    {
      query.setParameter(exprParameterNames[i], exprParameterValues[i]);
    }

    return query;
  }

  Query getDeleteQuery(Class cls, Filter filter, Session session)
  {
    String className = StringUtils.unqualify(cls.getName());

    String selectStr = "delete from " + className;

    String whereStr = "";

    if (filter != null) {
      whereStr = filter.getFilterExpr();
      if (StringUtils.hasText(whereStr)) whereStr = " where " + whereStr;
    }
    String hql = selectStr + whereStr;

    Query query = session.createQuery(hql);

    if (filter != null) {
      query = setQueryParameter(query, filter);
    }

    return query;
  }

  private Query setQueryParameter(Query query, Filter filter)
  {
    String[] nameArray = ArrayUtils.toStringArray(filter.getParameterNames().toArray());
    Object[] valueArray = filter.getParameterValues().toArray();
    ISQLExprProcess iSQLExprProcess = filter.getSqlExprProcess();
    for (int i = 0; (nameArray != null) && (i < nameArray.length); i++) {
      nameArray[i] = nameArray[i].replaceAll("\\.", "_");
      if ((valueArray[i] instanceof Object[])) {
        log.info("^^^^setParameterList  " + nameArray[i] + "=" + Arrays.toString((Object[])(Object[])valueArray[i]));
        Object[] objectArray = (Object[])(Object[])valueArray[i];
        if ((objectArray != null) && (objectArray.length > 0))
          if ((iSQLExprProcess instanceof IsBetweenExprProcess)) {
            query.setParameter(nameArray[i] + "_0", valueArray[0]);
            query.setParameter(nameArray[i] + "_1", valueArray[1]);
          } else {
            query.setParameterList(nameArray[i], objectArray);
          }
      }
      else {
        System.out.println("^^^^setParameter  " + nameArray[i] + "=" + valueArray[i]);
        query.setParameter(nameArray[i], valueArray[i]);
      }
    }
    return query;
  }

  public int update(Class modeClass, Map updateValue, String[] filternames, Object[] filtervalues)
  {
    Filter groupFilter = getFilter(filternames, filtervalues);
    return update(modeClass, updateValue, groupFilter);
  }

  public int update(Class modeClass, Map updateValue, Filter filter)
  {
    return update(modeClass, UpdateSet.getUpdateSet().set(updateValue), filter);
  }

  public int update(Class modeClass, UpdateSet updateSet, Filter filter)
  {
    Integer count = new Integer(0);
    try {
      count = (Integer)getHibernateTemplate().execute(new HibernateCallback(modeClass, updateSet, filter)
      {
        public Object doInHibernate(Session session) throws HibernateException {
          Integer _count = new Integer(0);
          Query query = DaoServiceImpl.this.getUpdateQuery(this.val$modeClass, this.val$updateSet, this.val$filter, session);
          _count = new Integer(query.executeUpdate());
          return _count;
        } } );
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

  public int delete(Object object)
  {
    try {
      getHibernateTemplate().delete(object);
    } catch (Throwable dae) {
      SysException.handleException(dae.getMessage(), dae);
    }
    return 1;
  }

  public int delete(Object[] objects)
  {
    Integer count = new Integer(0);
    try {
      count = (Integer)getHibernateTemplate().execute(new HibernateCallback(objects)
      {
        public Object doInHibernate(Session session)
          throws HibernateException
        {
          DaoServiceImpl.this.checkWriteOperationAllowed(DaoServiceImpl.this.getHibernateTemplate(), session);

          if (this.val$objects == null) {
            return new Integer(0);
          }
          int max = this.val$objects.length;
          int _count = 0;
          for (int i = 0; i < max; i++) {
            session.refresh(this.val$objects[i]);
            session.delete(this.val$objects[i]);
            if (i % 25 == 0) {
              session.flush();
              session.clear();
            }
            _count++;
          }
          return new Integer(_count);
        } } );
    } catch (Throwable dae) {
      SysException.handleException(dae.getMessage(), dae);
    }
    return count.intValue();
  }

  public int delete(Class modeClass, String id) {
    Filter groupFilter = FieldFilter.eq("id", id);
    int count = delete(modeClass, groupFilter);
    return count;
  }

  public int delete(Class modeClass, String[] id)
  {
    Filter groupFilter = FieldFilter.in("id", id);
    int count = delete(modeClass, groupFilter);
    return count;
  }

  public int delete(Class modeClass, String[] filternames, Object[] filtervalues)
  {
    Filter groupFilter = getFilter(filternames, filtervalues);
    int count = delete(modeClass, groupFilter);
    return count;
  }

  public int delete(Class modeClass, Filter filter) {
    Integer count = new Integer(0);
    try {
      count = (Integer)getHibernateTemplate().execute(new HibernateCallback(modeClass, filter)
      {
        public Object doInHibernate(Session session) throws HibernateException
        {
          Integer _count = new Integer(0);
          Query query = DaoServiceImpl.this.getDeleteQuery(this.val$modeClass, this.val$filter, session);

          _count = new Integer(query.executeUpdate());
          return _count;
        } } );
    } catch (Throwable dae) {
      SysException.handleException(dae.getMessage(), dae);
    }
    return count.intValue();
  }

  public void flush() {
    try {
      getHibernateTemplate().flush();
    }
    catch (Throwable dae)
    {
      SysException.handleException(dae.getMessage(), dae);
    }
  }

  public Serializable getIdentifier(Object object)
  {
    Serializable s = null;
    s = (Serializable)getHibernateTemplate().execute(new HibernateCallback(object)
    {
      public Object doInHibernate(Session session)
        throws HibernateException
      {
        return session.getIdentifier(this.val$object);
      }
    });
    return s;
  }

  public Object getImplementation(Object object)
  {
    if (!(object instanceof HibernateProxy))
      return object;
    HibernateProxy hibernateProxy = (HibernateProxy)object;
    LazyInitializer lazyInit = hibernateProxy.getHibernateLazyInitializer();
    object = lazyInit.getImplementation();
    hibernateProxy = null;
    lazyInit = null;
    return object;
  }

  public void initialize(Object proxyObject)
  {
    Object[] proxyArray = { proxyObject };

    if ((proxyObject instanceof Object[])) {
      proxyArray = (Object[])(Object[])proxyObject;
    }
    if ((proxyObject instanceof Collection)) {
      Collection collObject = (Collection)proxyObject;
      proxyArray = collObject.toArray();
    }

    if ((proxyObject instanceof Iterator)) {
      Iterator itObject = (Iterator)proxyObject;
      proxyArray = ArrayUtils.toObjectArray(itObject);
    }

    for (int i = 0; (proxyArray != null) && (i < proxyArray.length); i++)
    {
      Object _proxyObject = proxyArray[i];

      if (!Hibernate.isInitialized(_proxyObject))
        Hibernate.initialize(_proxyObject);
    }
  }

  public void initialize(Object proxyObject, String path)
  {
    Hashtable map = new Hashtable();
    String[] propertyNames = StringUtils.split(path, "/");
    Level levelObj = new Level();
    levelObj.count = propertyNames.length;
    for (int i = 0; (propertyNames != null) && (i < propertyNames.length); i++) {
      map.put(propertyNames[i], "HAS");
    }
    initialize(proxyObject, levelObj, map);
  }

  private void initialize(Object proxyObject, Level levelObj, Map map)
  {
    levelObj.i += 1;

    Object[] proxyArray = { proxyObject };

    if ((proxyObject instanceof Object[])) {
      proxyArray = (Object[])(Object[])proxyObject;
    }
    if ((proxyObject instanceof Collection)) {
      Collection collObject = (Collection)proxyObject;
      proxyArray = collObject.toArray();
    }

    if ((proxyObject instanceof Iterator)) {
      Iterator itObject = (Iterator)proxyObject;
      proxyArray = ArrayUtils.toObjectArray(itObject);
    }

    for (int i = 0; (proxyArray != null) && (i < proxyArray.length); i++)
    {
      Object _proxyObject = proxyArray[i];

      if (!Hibernate.isInitialized(_proxyObject)) {
        Hibernate.initialize(_proxyObject);
      }

      Method[] methodArray = _proxyObject.getClass().getMethods();
      for (int j = 0; j < methodArray.length; j++)
      {
        String propertyName = "";

        String getMethodName = methodArray[j].getName();
        propertyName = StringUtils.uncapitalizeFirst(getMethodName.replaceAll("get", ""));

        if ("HAS".equals(map.get(propertyName))) {
          Object resultObject = null;
          try {
            resultObject = methodArray[j].invoke(_proxyObject, new Object[0]);
          }
          catch (Exception e)
          {
            resultObject = null;
          }

          if (resultObject == null) {
            continue;
          }
          if (!Hibernate.isInitialized(resultObject)) {
            Hibernate.initialize(resultObject);
          }

          if (!levelObj.isReturn()) {
            initialize(resultObject, levelObj, map);
            levelObj.i -= 1;
          }
        }
      }
    }
  }

  public Blob createBlob(byte[] bytes) {
    return Hibernate.createBlob(bytes);
  }

  public Blob createBlob(InputStream stream)
  {
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

  public Object load(Class _modeClass, Serializable _id)
  {
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
    return load(cls, groupFilter);
  }
  public Object load(Class cls, String[] filterNames, Object[] filterValues) {
    Filter groupFilter = getFilter(filterNames, filterValues);
    return load(cls, groupFilter);
  }

  public Object load(Class modeClass, String name, Object value)
  {
    return load(modeClass, new String[] { name }, new Object[] { value });
  }

  public Object load(Class modelClass, Filter filter) {
    IQueryService queryService = getQueryService(modelClass, filter, false);
    List list = queryService.list();
    if ((list == null) || (list.size() == 0)) {
      return null;
    }

    return list.get(0);
  }

  public List query(Class modelClass)
  {
    IQueryService queryService = getQueryService(modelClass, null, false);
    List list = null;
    try {
      list = queryService.list();
    } catch (Exception ex) {
    }
    return list;
  }

  public List query(Class modelClass, Filter filter)
  {
    IQueryService queryService = getQueryService(modelClass, filter, false);
    List list = null;
    try {
      list = queryService.list();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return list;
  }

  public List query(Class modelClass, Map queryArgMap)
  {
    Filter _filter = getFilter(queryArgMap);
    return query(modelClass, _filter);
  }

  public List query(Class modelClass, String[] queryNames, Object[] queryValues)
  {
    Filter _filter = getFilter(queryNames, queryValues);
    return query(modelClass, _filter);
  }

  public List query(Class modelClass, String queryName, Object queryValue)
  {
    Filter _filter = getFilter(queryName, queryValue);
    return query(modelClass, _filter);
  }

  public int count(Class modelClass)
  {
    Filter filter = null;
    return count(modelClass, filter);
  }

  public int count(Class modelClass, String[] filterNames, Object[] filterValues)
  {
    Filter groupFilter = getFilter(filterNames, filterValues);
    return count(modelClass, groupFilter);
  }

  public int count(Class modelClass, Map filterMap) {
    Filter groupFilter = getFilter(filterMap);
    return count(modelClass, groupFilter);
  }

  public int count(Class modelClass, Filter filter) {
    return getQueryService(modelClass, filter).size();
  }

  public List filter(Object collection, String[] names, Object[] values)
  {
    List list = null;
    try {
      Filter groupFilter = getFilter(names, values);

      String hql = " where " + groupFilter.getFilterExpr();

      list = getHibernateTemplate().executeFind(new HibernateCallback(collection, hql) {
        public Object doInHibernate(Session session) {
          Object obj = session.createFilter(this.val$collection, this.val$hql);
          return obj;
        } } );
    } catch (Exception dae) {
      return new ArrayList();
    }

    return list;
  }

  public List order(Object collection, OrderBy order) {
    List list = null;
    try {
      String orderStr = "";
      if (order != null) {
        orderStr = order.getOrderByExpr("this");
      }
      String hql = " " + orderStr;

      list = getHibernateTemplate().executeFind(new HibernateCallback(collection, hql) {
        public Object doInHibernate(Session session) {
          Object obj = session.createFilter(this.val$collection, this.val$hql);
          return obj;
        } } );
    } catch (Exception dae) {
      return new ArrayList();
    }

    return list;
  }

  public String queryNativteSQL(String sql) {
    Float arryObj = null;
    try {
      arryObj = (Float)getHibernateTemplate().execute(new HibernateCallback(sql)
      {
        public Object doInHibernate(Session session) throws HibernateException
        {
          Query query = session.createQuery(this.val$sql);
          List list = query.list();
          if (list.isEmpty()) {
            return null;
          }
          return (Float)list.get(0);
        } } );
    }
    catch (Throwable dae) {
      SysException.handleException(dae.getMessage(), dae);
    }
    if (arryObj != null) {
      return arryObj.toString();
    }
    return null;
  }
  public List executeNativteSQL(String sql) {
    List listObj = null;
    try {
      listObj = (List)getHibernateTemplate().execute(new HibernateCallback(sql)
      {
        public Object doInHibernate(Session session) throws HibernateException
        {
          Query query = session.createSQLQuery(this.val$sql).setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
          List list = query.list();
          return list;
        } } );
    } catch (Throwable dae) {
      SysException.handleException(dae.getMessage(), dae);
    }
    return listObj;
  }

  public Integer queryExecuteUpdateSQL(String sql) {
    Integer rowCount = null;
    try {
      rowCount = (Integer)getHibernateTemplate().execute(new HibernateCallback(sql)
      {
        public Object doInHibernate(Session session) throws HibernateException
        {
          SQLQuery query = session.createSQLQuery(this.val$sql);
          return Integer.valueOf(query.executeUpdate());
        } } );
    } catch (Throwable dae) {
      SysException.handleException(dae.getMessage(), dae);
    }
    return rowCount;
  }

  public IQueryService getQueryService(Class modelClass) {
    Filter groupFilter = null;
    return getQueryService(modelClass, groupFilter);
  }

  public IQueryService getQueryService(Class modelClass, String[] filterNames, Object[] filterValues)
  {
    Filter groupFilter = getFilter(filterNames, filterValues);
    return getQueryService(modelClass, groupFilter);
  }

  public IQueryService getQueryService(Class modelClass, String filterName, Object[] filterValues)
  {
    Filter filter = FieldFilter.in(filterName, filterValues);
    return getQueryService(modelClass, filter);
  }

  public IQueryService getQueryService(Class modelClass, String filterName, Object filterValue)
  {
    Filter groupFilter = getFilter(new String[] { filterName }, new Object[] { filterValue });

    return getQueryService(modelClass, groupFilter);
  }

  public IQueryService getQueryService(Class modelClass, Map filterMap) {
    Filter groupFilter = getFilter(filterMap);
    return getQueryService(modelClass, groupFilter);
  }

  public IQueryService getQueryService(Class modelClass, Filter filter)
  {
    return getQueryService(modelClass, filter, true);
  }

  public IQueryService getQueryService(Class modelClass, Filter filter, boolean isReturnCount)
  {
    Session session = getQueryServiceSession();
    this.queryService = newQueryService();
    this.queryService.set_session(session);
    this.queryService.setResultToMapModel(false);
    this.queryService.set_modelClass(modelClass);
    this.queryService.setReturnCount(isReturnCount);
    this.queryService.set_filter(filter);
    this.queryService.set_queryType("QUERY_TYPE_SIMPLE");

    return this.queryService;
  }

  public IQueryService getQueryService(Class startModelClass, Select select, String[] filterNames, Object[] filterValues)
  {
    Filter groupFilter = getFilter(filterNames, filterValues);
    return getQueryService(startModelClass, select, groupFilter);
  }

  public IQueryService getQueryService(Class startModelClass, Select select, Map filterMap)
  {
    Filter groupFilter = getFilter(filterMap);
    return getQueryService(startModelClass, select, groupFilter);
  }

  public IQueryService getQueryService(Class startModelClass, Select select, Filter filter)
  {
    return getQueryService(startModelClass, select, filter, true);
  }

  public IQueryService getQueryService(Class startModelClass, Select select, Filter filter, boolean isReturnCount)
  {
    Session session = getQueryServiceSession();
    this.queryService = newQueryService();
    this.queryService.set_session(session);
    this.queryService.set_modelClass(startModelClass);
    this.queryService.setReturnCount(isReturnCount);
    this.queryService.setResultToMapModel(false);
    this.queryService.set_filter(filter);
    this.queryService.setComplexQuerySelect(select);
    this.queryService.set_queryType("QUERY_TYPE_COMPLEX");
    return this.queryService;
  }

  public IQueryService getQueryServiceByQueryName2Entity(String queryName, Map queryArgMap)
  {
    return getQueryServiceByQueryName2Entity(queryName, queryArgMap, true);
  }

  public IQueryService getQueryServiceByQueryName2Entity(String queryName, Map queryArgMap, boolean isReturnCount)
  {
    Session session = getQueryServiceSession();
    this.queryService = newQueryService();
    this.queryService.set_session(session);
    this.queryService.setResultToMapModel(false);
    this.queryService.set_nameQuery_QueryName(queryName);
    this.queryService.set_nameQuery_Parameter(queryArgMap);
    this.queryService.setReturnCount(isReturnCount);
    this.queryService.set_queryType("QUERY_TYPE_SQL_NAMEQUERY");
    return this.queryService;
  }

  public IQueryService getQueryServiceByQueryName2Map(String queryName, Map queryArgMap)
  {
    return getQueryServiceByQueryName2Map(queryName, queryArgMap, true);
  }

  public IQueryService getQueryServiceByQueryName2Map(String queryName, Map queryArgMap, boolean isReturnCount)
  {
    Session session = getQueryServiceSession();
    this.queryService = newQueryService();
    this.queryService.set_session(session);
    this.queryService.setResultToMapModel(true);
    this.queryService.set_nameQuery_QueryName(queryName);
    this.queryService.set_nameQuery_Parameter(queryArgMap);
    this.queryService.set_queryType("QUERY_TYPE_SQL_NAMEQUERY");
    return this.queryService;
  }

  public List query(Class _modelClass, Map _queryArgMap, Page _page, String _orderColumn, String _orderSort) {
    Filter _filter = getFilter(_queryArgMap);
    return query(_modelClass, _filter, _page, _orderColumn, _orderSort);
  }

  public List query(Class _modelClass, String _queryArgName, Object _queryArgValue, Page _page, String _orderColumn, String _orderSort) {
    Filter _filter = getFilter(_queryArgName, _queryArgValue);
    return query(_modelClass, _filter, _page, _orderColumn, _orderSort);
  }

  public List query(Class _modelClass, String[] _queryArgNames, Object[] _queryArgValues, Page _page, String _orderColumn, String _orderSort) {
    Filter _filter = getFilter(_queryArgNames, _queryArgValues);
    return query(_modelClass, _filter, _page, _orderColumn, _orderSort);
  }

  public List query(Class _modelClass, Filter _filter, Page _page, String _orderColumn, String _orderSort)
  {
    IQueryService queryService = getQueryService(_modelClass, null, _filter, false);

    if (StringUtils.isNotNull(_orderColumn)) {
      OrderBy _orderBy = new OrderBy();
      if (StringUtils.isNotNull(_orderBy))
        _orderBy.order(_orderColumn, _orderSort);
      else {
        _orderBy.order(_orderColumn, "ASC");
      }
      queryService.setOrderBy(_orderBy);
    }
    return queryService.list(_page);
  }

  public int countEntity(Class _modelClass, Filter filter)
  {
    IQueryService queryService = getQueryService(_modelClass, filter);

    return queryService.size();
  }

  private Session getQueryServiceSession() {
    Session _session = null;
    try
    {
      _session = getSession();
      return _session;
    } catch (Exception dae) {
      SysException.handleException(dae.getMessage(), dae);
    }
    return _session;
  }

  public IQueryService getQueryServiceByHQLQueryName2Entity(String queryName, Map queryArgMap, boolean isReturnCount)
  {
    Session session = getQueryServiceSession();
    this.queryService = newQueryService();
    this.queryService.set_session(session);
    this.queryService.set_nameQuery_QueryName(queryName);
    this.queryService.set_nameQuery_Parameter(queryArgMap);
    this.queryService.setReturnCount(isReturnCount);
    this.queryService.set_queryType("QUERY_TYPE_HQL_NAMEQUERY");
    this.queryService.setResultToMapModel(false);
    return this.queryService;
  }

  public IQueryService getQueryServiceByHQLQueryName2Entity(String queryName, Map queryArgMap) {
    return getQueryServiceByHQLQueryName2Entity(queryName, queryArgMap, true);
  }

  public IQueryService getQueryServiceByHQLQueryName2Map(String queryName, Map queryArgMap, boolean isReturnCount) {
    Session session = getQueryServiceSession();
    this.queryService = newQueryService();
    this.queryService.set_queryType("QUERY_TYPE_HQL_NAMEQUERY");
    this.queryService.set_session(session);
    this.queryService.set_nameQuery_QueryName(queryName);
    this.queryService.set_nameQuery_Parameter(queryArgMap);
    this.queryService.setReturnCount(isReturnCount);
    this.queryService.setResultToMapModel(true);
    return this.queryService;
  }

  public IQueryService getQueryServiceByHQLQueryName2Map(String queryName, Map parameter) {
    return getQueryServiceByHQLQueryName2Map(queryName, parameter, true);
  }

  public List find(String hql)
  {
    List list = null;
    try {
      list = getHibernateTemplate().executeFind(new HibernateCallback(hql) {
        public Object doInHibernate(Session session) throws HibernateException {
          return session.createQuery(this.val$hql).list();
        } } );
    } catch (Throwable dae) {
      SysException.handleException(dae.getMessage(), dae);
    }
    return list;
  }

  public static String getDbName()
  {
    if (dbName != null) return dbName;

    Object _url = null;
    try
    {
      Object _dataSource = SpringManager.getComponent("dataSource");
      try
      {
        _url = BeanUtils.getProperty(_dataSource, "url");
      } catch (Exception ee) {
        _url = null;
      }
      try
      {
        if (StringUtils.isNull(_url)) {
          _url = BeanUtils.getProperty(_dataSource, "jdbcUrl");
        }
      }
      catch (Exception ee)
      {
      }
    }
    catch (Exception e)
    {
    }

    if (_url == null) {
      dbName = "sqlserver";
      return dbName;
    }

    if (_url.toString().indexOf("sqlserver") > 0) {
      dbName = "sqlserver";
      return dbName;
    }

    if (_url.toString().indexOf("oracle") > 0) {
      dbName = "oracle";
      return dbName;
    }
    if (_url.toString().indexOf("mysql") > 0) {
      dbName = "mysql";
      return dbName;
    }
    if (_url.toString().indexOf("db2") > 0) {
      dbName = "db2";
      return dbName;
    }
    dbName = "sqlserver";
    return dbName;
  }

  private static void setCallProcParameter(CallableStatement _pcs, String _sql, Map _argsMap)
  {
    if (_argsMap == null) return;

    _sql = _sql.replaceAll(" ", "");

    _sql = _sql.replaceAll("call", "call ");

    String _regexStr = "\\((([:\\w]*\\,*)*)\\)";

    Pattern _patt = Pattern.compile(_regexStr);
    Matcher _matc = null;

    String _callProcParaStr = _sql;
    try
    {
      _matc = _patt.matcher(_sql);

      if (_matc.find())
      {
        _callProcParaStr = _matc.group(1);

        System.out.println(_callProcParaStr);
      }
    }
    catch (Exception e)
    {
    }
    String[] _callProcParaArray = _callProcParaStr.split(",");

    for (int i = 0; (_callProcParaArray != null) && (i < _callProcParaArray.length); i++)
    {
      String _callProcParaName = _callProcParaArray[i].replaceAll(":", "");

      Object _callProcParaValue = _argsMap.get(_callProcParaName);
      try {
        _pcs.setObject(i + 1, _callProcParaValue);
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
  }

  private static List convertList(ResultSet _rs) throws SQLException
  {
    List _list = new ArrayList();
    if (_rs == null) {
      return null;
    }
    ResultSetMetaData _md = _rs.getMetaData();

    int columnCount = _md.getColumnCount();

    while (_rs.next())
    {
      Map _rowData = new HashMap();

      for (int i = 1; i <= columnCount; i++)
      {
        _rowData.put(_md.getColumnName(i), _rs.getObject(i));
      }

      _list.add(_rowData);
    }

    return _list;
  }

  public List call(String _queryName, Map _argsMap)
  {
    return call(_queryName, _argsMap, true);
  }

  public List call(String _queryName, Map _argsMap, boolean _isAutoCommit)
  {
    String _sql = DaoUtils.getNamedSQLQuery(_queryName).toString();
    String _backSql = _sql;
    CallableStatement _pcs = null;

    Transaction _tran = null;

    ResultSet _rs = null;

    List _resultList = null;
    try
    {
      if (_isAutoCommit) {
        _tran = getSession().beginTransaction();
      }

      String _regexStr = "(:[a-zA-Z0-9_]*)";
      Pattern _patt = Pattern.compile(_regexStr);
      Matcher _matc = null;
      try {
        _matc = _patt.matcher(_sql);
        if (_matc.find())
          _sql = _matc.replaceAll("?");
      }
      catch (Exception e) {
      }
      _pcs = getSession().connection().prepareCall(_sql);

      setCallProcParameter(_pcs, _backSql, _argsMap);

      _pcs.execute();

      _rs = _pcs.getResultSet();
      _resultList = convertList(_rs);
      if (_isAutoCommit)
        _tran.commit();
    }
    catch (Exception ee)
    {
      if (_rs != null)
        try {
          _rs.close();
        }
        catch (Exception ee) {
        }
      if (_pcs != null)
        try {
          _pcs.close();
        } catch (Exception ee) {
        }
      e.printStackTrace();
    }
    finally
    {
      if (_rs != null)
        try {
          _rs.close();
        }
        catch (Exception ee) {
        }
      if (_pcs != null)
        try {
          _pcs.close();
        }
        catch (Exception ee) {
        }
    }
    return _resultList;
  }

  class Level
  {
    public static final String HAS = "HAS";
    public int i = 0;

    public int count = 0;

    Level() {  }

    public boolean isReturn() { return this.i > this.count;
    }
  }
}
