package org.maccha.dao.service.impl;

import java.io.InputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.Clob;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.maccha.dao.Filter;
import org.maccha.dao.IDaoService;
import org.maccha.dao.OrderBy;
import org.maccha.dao.Page;
import org.maccha.dao.Select;
import org.maccha.dao.UpdateSet;
import org.maccha.dao.service.IBaseBizService;
import org.maccha.base.exception.SysException;
import org.maccha.base.util.ClassUtils;
import org.maccha.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("org.simpro.dao.service.IBaseBizService")
public class BaseBizServiceImpl implements IBaseBizService
{

  @Autowired
  private IDaoService daoService;

  public String getQueryNameType(String _queryName)
  {
    return this.daoService.getQueryNameType(_queryName);
  }

  public List call(String _queryName, Map _argsMap, boolean _isAutoCommit)
  {
    return this.daoService.call(_queryName, _argsMap, _isAutoCommit);
  }

  public List call(String _queryName, Map _argsMap)
  {
    return this.daoService.call(_queryName, _argsMap);
  }

  public int insertByQueryName(String queryName, Map paramMap)
  {
    return this.daoService.insertByQueryName(queryName, paramMap);
  }

  public int updateByQueryName(String queryName, Map paramMap)
  {
    return this.daoService.updateByQueryName(queryName, paramMap);
  }

  public List query(Class _modelClass, Select _select, Filter _filter, Page _page, String _orderColumn, String _orderSort, List _fetch)
  {
    return this.daoService.query(_modelClass, _select, _filter, _page, _orderColumn, _orderSort, _fetch);
  }

  public List query(Class _modelClass, Select _select, Filter _filter, Page _page, String _orderColumn, String _orderSort)
  {
    return this.daoService.query(_modelClass, _select, _filter, _page, _orderColumn, _orderSort);
  }

  public List query(Class _modelClass, Select _select, Filter _filter, Page _page, OrderBy _orderBy)
  {
    return this.daoService.query(_modelClass, _select, _filter, _page, _orderBy);
  }

  public Serializable save(Object object)
  {
    return this.daoService.save(object);
  }

  public void saveOrUpdate(Object object)
  {
    this.daoService.saveOrUpdate(object);
  }

  public void saveOrUpdate(Collection collectionToSaveOrUpdate)
  {
    this.daoService.saveOrUpdate(collectionToSaveOrUpdate);
  }

  public void save(List objects)
  {
    this.daoService.save(objects);
  }

  public int update(Object object)
  {
    return this.daoService.update(object);
  }

  public int update(Object[] objects) {
    return this.daoService.update(objects);
  }

  public int update(Object object, boolean isIncludeNullValue) {
    return this.daoService.update(object, isIncludeNullValue);
  }

  public int update(Class modeClass, Map updateValue, String[] filternames, Object[] filtervalues)
  {
    return this.daoService.update(modeClass, updateValue, filternames, filtervalues);
  }

  public int update(Class cls, Map updateValueMap, Filter filter)
  {
    return this.daoService.update(cls, updateValueMap, filter);
  }

  public int update(Class cls, UpdateSet updateSet, Filter filter)
  {
    return this.daoService.update(cls, updateSet, filter);
  }

  public void merge(Object object)
  {
    this.daoService.merge(object);
  }

  public int delete(Object object)
  {
    return this.daoService.delete(object);
  }

  public int delete(Object[] objects)
  {
    return this.daoService.delete(objects);
  }

  public int delete(Class cls, String id)
  {
    return this.daoService.delete(cls, id);
  }

  public int delete(Class cls, String[] id)
  {
    return this.daoService.delete(cls, id);
  }

  public int delete(Class cls, Filter filter)
  {
    return this.daoService.delete(cls, filter);
  }

  public void flush()
  {
    this.daoService.flush();
  }

  public Serializable getIdentifier(Object object)
  {
    return this.daoService.getIdentifier(object);
  }

  public Object getImplementation(Object object)
  {
    return this.daoService.getImplementation(object);
  }

  public void initialize(Object proxy)
  {
    this.daoService.initialize(proxy);
  }

  public void initialize(Object proxyObject, String path)
  {
    this.daoService.initialize(proxyObject, path);
  }

  public Blob createBlob(byte[] bytes)
  {
    return this.daoService.createBlob(bytes);
  }

  public Blob createBlob(InputStream stream)
  {
    return this.daoService.createBlob(stream);
  }

  public Clob createClob(String string)
  {
    return this.daoService.createClob(string);
  }

  public Object load(Class modelClass, Serializable id)
  {
    return this.daoService.load(modelClass, id);
  }

  public Object load(Class modelClass, String[] names, Object[] values)
  {
    return this.daoService.load(modelClass, names, values);
  }

  public Object load(Class modelClass, String name, Object value) {
    return this.daoService.load(modelClass, name, value);
  }

  public Object load(Class modelClass, Map queryArgMap)
  {
    return this.daoService.load(modelClass, queryArgMap);
  }

  public Object load(Class modelClass, Filter filter)
  {
    return this.daoService.load(modelClass, filter);
  }

  public int count(Class modelClass)
  {
    return this.daoService.count(modelClass);
  }

  public int count(Class modelClass, String[] filternames, Object[] filtervalues)
  {
    return this.daoService.count(modelClass, filternames, filtervalues);
  }

  public int count(Class modelClass, Map filterMap)
  {
    return this.daoService.count(modelClass, filterMap);
  }

  public int count(Class modelClass, Filter filter)
  {
    return this.daoService.count(modelClass, filter);
  }

  public int countEntity(Class _modelClass, Filter _filter)
  {
    return this.daoService.countEntity(_modelClass, _filter);
  }

  public void deleteEntity(String entityName, String entityId) {
    try {
      this.daoService.delete(Class.forName(entityName), entityId);
    } catch (ClassNotFoundException ex) {
      SysException.handleException("", ex);
    }
  }

  public int countByQueryName(String queryName, Map _queryArgMap)
  {
    return this.daoService.countByQueryName(queryName, _queryArgMap);
  }

  public List executeNativteQuerySQL(String sql)
  {
    return this.daoService.executeNativteSQL(sql);
  }

  public Integer executeNativteUpdateSQL(String sql)
  {
    return this.daoService.queryExecuteUpdateSQL(sql);
  }

  public List queryByQueryName2Map(String _queryName, Map _queryArgMap) {
    return this.daoService.queryByQueryName2Map(_queryName, _queryArgMap);
  }

  public List queryByQueryName2Map(String _queryName, String _queryArgName, Object _queryArgValue)
  {
    return this.daoService.queryByQueryName2Map(_queryName, _queryArgName, _queryArgValue);
  }

  public List queryByQueryName2Map(String _queryName, String[] _queryArgNames, Object[] _queryArgValues)
  {
    return this.daoService.queryByQueryName2Map(_queryName, _queryArgNames, _queryArgValues);
  }

  public List queryByQueryName2Map(String _queryName, Map _queryArgMap, Page _page)
  {
    return this.daoService.queryByQueryName2Map(_queryName, _queryArgMap, _page);
  }

  public List queryByQueryName2Map(String _queryName, String _queryArgName, Object _queryArgValue, Page _page)
  {
    return this.daoService.queryByQueryName2Map(_queryName, _queryArgName, _queryArgValue, _page);
  }

  public List queryByQueryName2Map(String _queryName, String[] _queryArgNames, Object[] _queryArgValues, Page _page)
  {
    return this.daoService.queryByQueryName2Map(_queryName, _queryArgNames, _queryArgValues, _page);
  }

  public List queryByQueryName2Map(String _queryName, Map _queryArgMap, Page _page, String _orderClumn, String _orderSort)
  {
    return this.daoService.queryByQueryName2Map(_queryName, _queryArgMap, _page, _orderClumn, _orderSort);
  }

  public Map queryByQueryName2OneMap(String _queryName, Map _queryArgMap) {
    return this.daoService.queryByQueryName2OneMap(_queryName, _queryArgMap);
  }

  public Map queryByQueryName2OneMap(String _queryName, String _queryArgName, Object _queryArgValue)
  {
    return this.daoService.queryByQueryName2OneMap(_queryName, _queryArgName, _queryArgValue);
  }

  public Map queryByQueryName2OneMap(String _queryName, String[] _queryArgNames, Object[] _queryArgValues)
  {
    return this.daoService.queryByQueryName2OneMap(_queryName, _queryArgNames, _queryArgValues);
  }

  public int countByHQLQueryName(String _queryName, Map _queryArgMap)
  {
    return this.daoService.countByHQLQueryName(_queryName, _queryArgMap);
  }

  public List queryByHQLQueryName2Map(String _queryName, Map _queryArgMap, Page _page)
  {
    return this.daoService.queryByHQLQueryName2Map(_queryName, _queryArgMap, _page);
  }

  public List queryByHQLQueryName2Map(String _queryName, Map _queryArgMap)
  {
    return this.daoService.queryByHQLQueryName2Map(_queryName, _queryArgMap);
  }

  public List queryByHQLQueryName2Map(String _queryName, Map _queryArgMap, Page _page, String _orderColumn, String _orderSort)
  {
    return this.daoService.queryByHQLQueryName2Map(_queryName, _queryArgMap, _page);
  }

  public List filter(Object collection, String[] filterNames, Object[] filterValues)
  {
    return this.daoService.filter(collection, filterNames, filterValues);
  }

  public List query(Class _modelClass, Filter _filter, Page _page, String _orderColumn, String _orderSort)
  {
    return this.daoService.query(_modelClass, _filter, _page, _orderColumn, _orderSort);
  }

  protected Set parseString2Set(Class entityClass, String ids)
  {
    if (StringUtils.isNull(ids)) {
      return null;
    }
    Set hashSet = new HashSet();

    String[] idArray = ids.split(",");
    for (int i = 0; (idArray != null) && (i < idArray.length); i++)
      try {
        Object obj = entityClass.newInstance();
        ClassUtils.set(obj, "id", idArray[i]);
        hashSet.add(obj);
      }
      catch (Exception e) {
      }
    return hashSet;
  }

  public List query(Class _modelClass, Map queryArgMap, Page _page, String _orderColumn, String _orderSort)
  {
    return this.daoService.query(_modelClass, queryArgMap, _page, _orderColumn, _orderSort);
  }

  public List query(Class _modelClass, String _queryArgName, Object _queryArgValue, Page _page, String _orderColumn, String _orderSort)
  {
    return this.daoService.query(_modelClass, _queryArgName, _queryArgValue, _page, _orderColumn, _orderSort);
  }

  public List query(Class _modelClass, String[] _queryArgNames, Object[] _queryArgValues, Page _page, String _orderColumn, String _orderSort)
  {
    return this.daoService.query(_modelClass, _queryArgNames, _queryArgValues, _page, _orderColumn, _orderSort);
  }

  public List order(Object collection, OrderBy order)
  {
    return this.daoService.order(collection, order);
  }

  public List query(Class modelClass, Filter filter)
  {
    return this.daoService.query(modelClass, filter);
  }

  public List query(Class modelClass, Map queryArgMap)
  {
    return this.daoService.query(modelClass, queryArgMap);
  }

  public List query(Class modelClass, String queryName, Object queryValue)
  {
    return this.daoService.query(modelClass, queryName, queryValue);
  }

  public List query(Class modelClass, String[] queryNames, Object[] queryValues)
  {
    return this.daoService.query(modelClass, queryNames, queryValues);
  }

  public List query(Class modelClass)
  {
    return this.daoService.query(modelClass);
  }

  public List queryByQueryName2Entity(String _queryName, Map _queryArgMap, Page _page, String _orderClumn, String _orderSort)
  {
    return this.daoService.queryByQueryName2Entity(_queryName, _queryArgMap, _page, _orderClumn, _orderSort);
  }

  public List queryByQueryName2Entity(String _queryName, Map _queryArgMap, Page _page) {
    return this.daoService.queryByQueryName2Entity(_queryName, _queryArgMap, _page);
  }

  public List queryByQueryName2Entity(String _queryName, String[] _queryArgNames, Object[] _queryArgValues, Page _page)
  {
    return this.daoService.queryByQueryName2Entity(_queryName, _queryArgNames, _queryArgValues, _page);
  }

  public List queryByQueryName2Entity(String _queryName, String _queryArgName, Object _queryArgValue, Page _page)
  {
    return this.daoService.queryByQueryName2Entity(_queryName, _queryArgName, _queryArgValue, _page);
  }

  public List queryByQueryName2Entity(String _queryName, Map _queryArgMap)
  {
    return this.daoService.queryByQueryName2Entity(_queryName, _queryArgMap);
  }

  public List queryByQueryName2Entity(String _queryName, String _queryArgName, Object _queryArgValue)
  {
    return this.daoService.queryByQueryName2Entity(_queryName, _queryArgName, _queryArgValue);
  }

  public List queryByQueryName2Entity(String _queryName, String[] _queryArgNames, Object[] _queryArgValues)
  {
    return this.daoService.queryByQueryName2Entity(_queryName, _queryArgNames, _queryArgValues);
  }

  public List queryByHQLQueryName2Entity(String _queryName, Map _queryArgMap, Page _page, String _orderColumn, String _orderSort)
  {
    return this.daoService.queryByQueryName2Entity(_queryName, _queryArgMap, _page, _orderColumn, _orderSort);
  }

  public List queryByHQLQueryName2Entity(String _queryName, Map _queryArgMap, Page _page)
  {
    return this.daoService.queryByQueryName2Entity(_queryName, _queryArgMap, _page);
  }

  public List queryByHQLQueryName2Entity(String _queryName, Map _queryArgMap)
  {
    return this.daoService.queryByQueryName2Entity(_queryName, _queryArgMap);
  }

  public int delete(Class _modelClass, Map _filterMap)
  {
    return 0;
  }

  public int delete(Class _modelClass, String _filterPropName, Object _filterPropValue)
  {
    return this.daoService.delete(_modelClass, new String[] { _filterPropName }, new Object[] { _filterPropValue });
  }

  public int delete(Class _modelClass, String[] _filterPropNames, Object[] _filterPropValues)
  {
    return this.daoService.delete(_modelClass, _filterPropNames, _filterPropValues);
  }

  public int update(Class _modelClass, Map _updateValueMap, Map _filterMap)
  {
    return 0;
  }

  public int update(Class _modelClass, Map _updateValueMap, String _filterPropName, Object _filterPropValue)
  {
    return 0;
  }
}
