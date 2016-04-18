package org.maccha.dao;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;

public abstract interface IQueryService
{
  public static final String QUERY_TYPE_SIMPLE = "QUERY_TYPE_SIMPLE";
  public static final String QUERY_TYPE_COMPLEX = "QUERY_TYPE_COMPLEX";
  public static final String QUERY_TYPE_SQL_NAMEQUERY = "QUERY_TYPE_SQL_NAMEQUERY";
  public static final String QUERY_TYPE_HQL_NAMEQUERY = "QUERY_TYPE_HQL_NAMEQUERY";
  public static final int OBJECT_RETURN = 0;
  public static final int MAP_RETURN = 1;
  public static final int INTEGER_RETURN = 2;

  public abstract void setFetchMode(String paramString, int paramInt);

  public abstract void setOrderBy(OrderBy paramOrderBy);

  public abstract int size();

  public abstract List list();

  public abstract Object getEntity();

  public abstract List list(int paramInt1, int paramInt2);

  public abstract List list(Page paramPage);

  public abstract List beginList(int paramInt);

  public abstract List lastList(int paramInt);

  public abstract Iterator iterator();

  public abstract Iterator iterator(int paramInt1, int paramInt2);

  public abstract Select getComplexQuerySelect();

  public abstract void setComplexQuerySelect(Select paramSelect);

  public abstract Filter get_filter();

  public abstract void set_filter(Filter paramFilter);

  public abstract boolean isResultToMapModel();

  public abstract void setResultToMapModel(boolean paramBoolean);

  public abstract boolean isReturnCount();

  public abstract void setReturnCount(boolean paramBoolean);

  public abstract Class get_modelClass();

  public abstract void set_modelClass(Class paramClass);

  public abstract Map get_nameQuery_Parameter();

  public abstract void set_nameQuery_Parameter(Map paramMap);

  public abstract String get_nameQuery_QueryName();

  public abstract void set_nameQuery_QueryName(String paramString);

  public abstract String get_queryType();

  public abstract void set_queryType(String paramString);

  public abstract Session get_session();

  public abstract void set_session(Session paramSession);

  public abstract List getFetchGroup();

  public abstract void setFetchGroup(List paramList);
}
