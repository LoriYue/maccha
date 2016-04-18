package org.maccha.dao;

import org.hibernate.Query;
import org.hibernate.Session;

public abstract interface IQueryInterceptor
{
  public static final String EXECUTE_SQL_QUERY = "";
  public static final String EXECUTE_HQL_QUERY = "";
  public static final String ADD_FILTER = "";

  public abstract Query excuteSQLQuery(Session paramSession, String paramString, int paramInt, Class paramClass);

  public abstract Query excuteHQLQuery(Session paramSession, String paramString, int paramInt);

  public abstract Filter addFilter(Session paramSession, Class paramClass, Filter paramFilter);
}
