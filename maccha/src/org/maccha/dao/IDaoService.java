package org.maccha.dao;

import java.io.InputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.Clob;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract interface IDaoService
{
  public static final String DAOSERVICE = IDaoService.class.getName();
  public static final String QUERY_NAME_TYPE_SQL_QUERY = "sqlQuery";
  public static final String QUERY_NAME_TYPE_HQL_QUERY = "hqlQuery";
  public static final String QUERY_NAME_TYPE_ENTITY_QUERY = "entityQuery";

  public abstract Serializable save(Object paramObject);

  public abstract void saveOrUpdate(Object paramObject);

  public abstract void saveOrUpdate(Collection paramCollection);

  public abstract void save(List paramList);

  public abstract int update(Object paramObject);

  public abstract int update(Object[] paramArrayOfObject);

  public abstract int update(Object paramObject, boolean paramBoolean);

  public abstract String queryNativteSQL(String paramString);

  public abstract List executeNativteSQL(String paramString);

  public abstract Integer queryExecuteUpdateSQL(String paramString);

  public abstract int update(Class paramClass, Map paramMap, String[] paramArrayOfString, Object[] paramArrayOfObject);

  public abstract int update(Class paramClass, Map paramMap, Filter paramFilter);

  public abstract int update(Class paramClass, UpdateSet paramUpdateSet, Filter paramFilter);

  public abstract int updateByQueryName(String paramString, Map paramMap);

  public abstract int insertByQueryName(String paramString, Map paramMap);

  public abstract void merge(Object paramObject);

  public abstract int delete(Object paramObject);

  public abstract int delete(Object[] paramArrayOfObject);

  public abstract int delete(Class paramClass, String[] paramArrayOfString, Object[] paramArrayOfObject);

  public abstract int delete(Class paramClass, String paramString);

  public abstract int delete(Class paramClass, String[] paramArrayOfString);

  public abstract int delete(Class paramClass, Filter paramFilter);

  public abstract void flush();

  public abstract Serializable getIdentifier(Object paramObject);

  public abstract Object getImplementation(Object paramObject);

  public abstract void initialize(Object paramObject);

  public abstract void initialize(Object paramObject, String paramString);

  public abstract Blob createBlob(byte[] paramArrayOfByte);

  public abstract Blob createBlob(InputStream paramInputStream);

  public abstract Clob createClob(String paramString);

  public abstract Object load(Class paramClass, Serializable paramSerializable);

  public abstract Object load(Class paramClass, Map paramMap);

  public abstract Object load(Class paramClass, String[] paramArrayOfString, Object[] paramArrayOfObject);

  public abstract Object load(Class paramClass, String paramString, Object paramObject);

  public abstract Object load(Class paramClass, Filter paramFilter);

  public abstract List query(Class paramClass);

  public abstract List query(Class paramClass, Filter paramFilter);

  public abstract List query(Class paramClass, Map paramMap);

  public abstract List query(Class paramClass, String[] paramArrayOfString, Object[] paramArrayOfObject);

  public abstract List query(Class paramClass, String paramString, Object paramObject);

  public abstract List query(Class paramClass, String paramString1, Object paramObject, Page paramPage, String paramString2, String paramString3);

  public abstract List query(Class paramClass, String[] paramArrayOfString, Object[] paramArrayOfObject, Page paramPage, String paramString1, String paramString2);

  public abstract List query(Class paramClass, Map paramMap, Page paramPage, String paramString1, String paramString2);

  public abstract List query(Class paramClass, Filter paramFilter, Page paramPage, String paramString1, String paramString2);

  public abstract List query(Class paramClass, Select paramSelect, Filter paramFilter, Page paramPage, String paramString1, String paramString2);

  public abstract List query(Class paramClass, Select paramSelect, Filter paramFilter, Page paramPage, String paramString1, String paramString2, List paramList);

  public abstract List query(Class paramClass, Select paramSelect, Filter paramFilter, Page paramPage, OrderBy paramOrderBy);

  public abstract int count(Class paramClass);

  public abstract int count(Class paramClass, String[] paramArrayOfString, Object[] paramArrayOfObject);

  public abstract int count(Class paramClass, Map paramMap);

  public abstract int count(Class paramClass, Filter paramFilter);

  public abstract IQueryService getQueryService(Class paramClass);

  public abstract IQueryService getQueryService(Class paramClass, String[] paramArrayOfString, Object[] paramArrayOfObject);

  public abstract IQueryService getQueryService(Class paramClass, String paramString, Object paramObject);

  public abstract IQueryService getQueryService(Class paramClass, String paramString, Object[] paramArrayOfObject);

  public abstract IQueryService getQueryService(Class paramClass, Map paramMap);

  public abstract IQueryService getQueryService(Class paramClass, Filter paramFilter);

  public abstract IQueryService getQueryService(Class paramClass, Select paramSelect, String[] paramArrayOfString, Object[] paramArrayOfObject);

  public abstract IQueryService getQueryService(Class paramClass, Select paramSelect, Map paramMap);

  public abstract IQueryService getQueryService(Class paramClass, Select paramSelect, Filter paramFilter);

  public abstract IQueryService getQueryServiceByQueryName2Entity(String paramString, Map paramMap);

  public abstract IQueryService getQueryServiceByQueryName2Entity(String paramString, Map paramMap, boolean paramBoolean);

  public abstract IQueryService getQueryServiceByQueryName2Map(String paramString, Map paramMap);

  public abstract IQueryService getQueryServiceByQueryName2Map(String paramString, Map paramMap, boolean paramBoolean);

  public abstract int countByQueryName(String paramString, Map paramMap);

  public abstract Map queryByQueryName2OneMap(String paramString, Map paramMap);

  public abstract Map queryByQueryName2OneMap(String paramString1, String paramString2, Object paramObject);

  public abstract Map queryByQueryName2OneMap(String paramString, String[] paramArrayOfString, Object[] paramArrayOfObject);

  public abstract List queryByQueryName2Map(String paramString, Map paramMap);

  public abstract List queryByQueryName2Map(String paramString, String[] paramArrayOfString, Object[] paramArrayOfObject);

  public abstract List queryByQueryName2Map(String paramString1, String paramString2, Object paramObject);

  public abstract List queryByQueryName2Map(String paramString, Map paramMap, Page paramPage);

  public abstract List queryByQueryName2Map(String paramString1, Map paramMap, Page paramPage, String paramString2, String paramString3);

  public abstract List queryByQueryName2Map(String paramString, String[] paramArrayOfString, Object[] paramArrayOfObject, Page paramPage);

  public abstract List queryByQueryName2Map(String paramString1, String paramString2, Object paramObject, Page paramPage);

  public abstract List queryByQueryName2Entity(String paramString, Map paramMap);

  public abstract List queryByQueryName2Entity(String paramString, String[] paramArrayOfString, Object[] paramArrayOfObject);

  public abstract List queryByQueryName2Entity(String paramString1, String paramString2, Object paramObject);

  public abstract List queryByQueryName2Entity(String paramString, Map paramMap, Page paramPage);

  public abstract List queryByQueryName2Entity(String paramString1, Map paramMap, Page paramPage, String paramString2, String paramString3);

  public abstract List queryByQueryName2Entity(String paramString, String[] paramArrayOfString, Object[] paramArrayOfObject, Page paramPage);

  public abstract List queryByQueryName2Entity(String paramString1, String paramString2, Object paramObject, Page paramPage);

  public abstract int countByHQLQueryName(String paramString, Map paramMap);

  public abstract List queryByHQLQueryName2Entity(String paramString, Map paramMap);

  public abstract List queryByHQLQueryName2Entity(String paramString, Map paramMap, Page paramPage);

  public abstract List queryByHQLQueryName2Entity(String paramString1, Map paramMap, Page paramPage, String paramString2, String paramString3);

  public abstract List queryByHQLQueryName2Map(String paramString, Map paramMap);

  public abstract List queryByHQLQueryName2Map(String paramString, Map paramMap, Page paramPage);

  public abstract List queryByHQLQueryName2Map(String paramString1, Map paramMap, Page paramPage, String paramString2, String paramString3);

  public abstract IQueryService getQueryServiceByHQLQueryName2Entity(String paramString, Map paramMap);

  public abstract IQueryService getQueryServiceByHQLQueryName2Entity(String paramString, Map paramMap, boolean paramBoolean);

  public abstract IQueryService getQueryServiceByHQLQueryName2Map(String paramString, Map paramMap);

  public abstract IQueryService getQueryServiceByHQLQueryName2Map(String paramString, Map paramMap, boolean paramBoolean);

  public abstract IQueryService getQueryService(Class paramClass, Select paramSelect, Filter paramFilter, boolean paramBoolean);

  public abstract IQueryService getQueryService(Class paramClass, Filter paramFilter, boolean paramBoolean);

  public abstract int countEntity(Class paramClass, Filter paramFilter);

  public abstract List filter(Object paramObject, String[] paramArrayOfString, Object[] paramArrayOfObject);

  public abstract List order(Object paramObject, OrderBy paramOrderBy);

  public abstract List find(String paramString);

  public abstract List call(String paramString, Map paramMap);

  public abstract List call(String paramString, Map paramMap, boolean paramBoolean);

  public abstract String getQueryNameType(String paramString);
}
