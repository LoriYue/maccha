package org.maccha.dao.service;

import java.io.InputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.Clob;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.maccha.dao.Filter;
import org.maccha.dao.OrderBy;
import org.maccha.dao.Page;
import org.maccha.dao.Select;

public abstract interface IBaseBizService
{
  public static final String BASEBIZSERVICE = IBaseBizService.class.getName();

  public abstract Serializable save(Object paramObject);

  public abstract void saveOrUpdate(Object paramObject);

  public abstract void saveOrUpdate(Collection paramCollection);

  public abstract void save(List paramList);

  public abstract int updateByQueryName(String paramString, Map paramMap);

  public abstract int insertByQueryName(String paramString, Map paramMap);

  public abstract int update(Object paramObject);

  public abstract int update(Object[] paramArrayOfObject);

  public abstract int update(Object paramObject, boolean paramBoolean);

  public abstract int update(Class paramClass, Map paramMap, String paramString, Object paramObject);

  public abstract int update(Class paramClass, Map paramMap, String[] paramArrayOfString, Object[] paramArrayOfObject);

  public abstract int update(Class paramClass, Map paramMap1, Map paramMap2);

  public abstract int update(Class paramClass, Map paramMap, Filter paramFilter);

  public abstract int delete(Object paramObject);

  public abstract int delete(Object[] paramArrayOfObject);

  public abstract int delete(Class paramClass, String paramString);

  public abstract int delete(Class paramClass, String[] paramArrayOfString);

  public abstract int delete(Class paramClass, String paramString, Object paramObject);

  public abstract int delete(Class paramClass, String[] paramArrayOfString, Object[] paramArrayOfObject);

  public abstract int delete(Class paramClass, Map paramMap);

  public abstract int delete(Class paramClass, Filter paramFilter);

  public abstract int count(Class paramClass);

  public abstract int count(Class paramClass, String[] paramArrayOfString, Object[] paramArrayOfObject);

  public abstract int count(Class paramClass, Map paramMap);

  public abstract int count(Class paramClass, Filter paramFilter);

  public abstract int countByQueryName(String paramString, Map paramMap);

  public abstract int countByHQLQueryName(String paramString, Map paramMap);

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

  public abstract List query(Class paramClass, Select paramSelect, Filter paramFilter, Page paramPage, OrderBy paramOrderBy);

  public abstract List query(Class paramClass, Filter paramFilter, Page paramPage, String paramString1, String paramString2);

  public abstract List query(Class paramClass, Select paramSelect, Filter paramFilter, Page paramPage, String paramString1, String paramString2);

  public abstract List query(Class paramClass, Select paramSelect, Filter paramFilter, Page paramPage, String paramString1, String paramString2, List paramList);

  public abstract void merge(Object paramObject);

  public abstract void flush();

  public abstract Serializable getIdentifier(Object paramObject);

  public abstract Object getImplementation(Object paramObject);

  public abstract void initialize(Object paramObject);

  public abstract void initialize(Object paramObject, String paramString);

  public abstract Blob createBlob(byte[] paramArrayOfByte);

  public abstract Blob createBlob(InputStream paramInputStream);

  public abstract Clob createClob(String paramString);

  public abstract Map queryByQueryName2OneMap(String paramString, Map paramMap);

  public abstract Map queryByQueryName2OneMap(String paramString1, String paramString2, Object paramObject);

  public abstract Map queryByQueryName2OneMap(String paramString, String[] paramArrayOfString, Object[] paramArrayOfObject);

  public abstract List queryByQueryName2Map(String paramString, Map paramMap);

  public abstract List queryByQueryName2Map(String paramString, String[] paramArrayOfString, Object[] paramArrayOfObject);

  public abstract List queryByQueryName2Map(String paramString1, String paramString2, Object paramObject);

  public abstract List queryByQueryName2Map(String paramString, Map paramMap, Page paramPage);

  public abstract List queryByQueryName2Map(String paramString, String[] paramArrayOfString, Object[] paramArrayOfObject, Page paramPage);

  public abstract List queryByQueryName2Map(String paramString1, String paramString2, Object paramObject, Page paramPage);

  public abstract List queryByQueryName2Map(String paramString1, Map paramMap, Page paramPage, String paramString2, String paramString3);

  public abstract List queryByQueryName2Entity(String paramString, Map paramMap);

  public abstract List queryByQueryName2Entity(String paramString, String[] paramArrayOfString, Object[] paramArrayOfObject);

  public abstract List queryByQueryName2Entity(String paramString1, String paramString2, Object paramObject);

  public abstract List queryByQueryName2Entity(String paramString, Map paramMap, Page paramPage);

  public abstract List queryByQueryName2Entity(String paramString1, Map paramMap, Page paramPage, String paramString2, String paramString3);

  public abstract List queryByQueryName2Entity(String paramString, String[] paramArrayOfString, Object[] paramArrayOfObject, Page paramPage);

  public abstract List queryByQueryName2Entity(String paramString1, String paramString2, Object paramObject, Page paramPage);

  public abstract List queryByHQLQueryName2Map(String paramString, Map paramMap);

  public abstract List queryByHQLQueryName2Map(String paramString, Map paramMap, Page paramPage);

  public abstract List queryByHQLQueryName2Map(String paramString1, Map paramMap, Page paramPage, String paramString2, String paramString3);

  public abstract List queryByHQLQueryName2Entity(String paramString, Map paramMap);

  public abstract List queryByHQLQueryName2Entity(String paramString, Map paramMap, Page paramPage);

  public abstract List queryByHQLQueryName2Entity(String paramString1, Map paramMap, Page paramPage, String paramString2, String paramString3);

  public abstract List executeNativteQuerySQL(String paramString);

  public abstract Integer executeNativteUpdateSQL(String paramString);

  public abstract List filter(Object paramObject, String[] paramArrayOfString, Object[] paramArrayOfObject);

  public abstract List order(Object paramObject, OrderBy paramOrderBy);

  public abstract List call(String paramString, Map paramMap);

  public abstract List call(String paramString, Map paramMap, boolean paramBoolean);

  public abstract String getQueryNameType(String paramString);
}
