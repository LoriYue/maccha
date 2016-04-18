package org.maccha.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.maccha.dao.impl.FieldFilter;
import org.maccha.dao.impl.GroupFilter;
import org.maccha.dao.impl.ISQLExprProcess;
import org.maccha.base.util.ArrayUtils;

public abstract class Filter
{
  public abstract String getFilterExpr();

  public abstract List getParameterNames();

  public abstract List getParameterValues();

  public abstract Filter add(Filter paramFilter);

  public abstract void setAlias(String paramString);

  public abstract void clear();

  public abstract boolean isEmpty();

  public abstract boolean isParameterEmpty();

  public abstract ISQLExprProcess getSqlExprProcess();

  public static Filter getOrGroupFilter()
  {
    return new GroupFilter(" OR ");
  }

  public static Filter getAndGroupFilter()
  {
    return new GroupFilter(" AND ");
  }

  public static Filter eq(String name, Object value)
  {
    return new FieldFilter(name, value, "=");
  }

  public static Filter gte(String name, Object value)
  {
    return new FieldFilter(name, value, ">=");
  }

  public static Filter lte(String name, Object value) {
    return new FieldFilter(name, value, "<=");
  }

  public static Filter gt(String name, Object value)
  {
    return new FieldFilter(name, value, ">");
  }

  public static Filter lt(String name, Object value)
  {
    return new FieldFilter(name, value, "<");
  }

  public static Filter in(String name, Object[] valueArray)
  {
    List list = new ArrayList();
    for (int i = 0; (valueArray != null) && (i < valueArray.length); i++) {
      if (valueArray[i] != null)
        list.add(valueArray[i]);
    }
    if (list.size() <= 0) {
      list = null;
      return null;
    }
    return new FieldFilter(name, list.toArray(), "IN");
  }

  public static Filter notIn(String name, Object[] valueArray)
  {
    List list = new ArrayList();
    for (int i = 0; (valueArray != null) && (i < valueArray.length); i++) {
      if (valueArray[i] == null)
        continue;
      list.add(valueArray[i]);
    }
    if (list.size() <= 0) {
      list = null;
      return null;
    }
    list = null;

    return new FieldFilter(name, valueArray, "NOT IN");
  }

  public static Filter like(String name, String likeExpr)
  {
    return new FieldFilter(name, likeExpr, "LIKE");
  }

  public static Filter isNull(String name)
  {
    return new FieldFilter(name, null, "IS NULL");
  }

  public static Filter ltEq(String name, Object value)
  {
    return new FieldFilter(name, value, "<=");
  }

  public static Filter gtEq(String name, Object value)
  {
    return new FieldFilter(name, value, ">=");
  }

  public static Filter notEq(String name, Object value)
  {
    return new FieldFilter(name, value, "<>");
  }

  public static Filter notNull(String name)
  {
    return new FieldFilter(name, null, "IS NOT NULL");
  }

  public static Filter between(String name, Object a, Object b)
  {
    return new FieldFilter(name, new Object[] { a, b }, "between {a} and {b}");
  }

  public static Filter getFilter(Map map) {
    Filter groupFilter = getAndGroupFilter();
    String[] nameArray = ArrayUtils.toStringArray(map.keySet().toArray());
    for (int i = 0; i < nameArray.length; i++) {
      groupFilter.add(FieldFilter.eq(nameArray[i], map.get(nameArray[i])));
    }

    return groupFilter;
  }

  public static Filter hqlWhere(String hqlCondition)
  {
    return new FieldFilter(hqlCondition, null, "HqlWhere");
  }
  public static Filter getFilter(String name, String opt, Object value) {
    return new FieldFilter(name, value, opt);
  }
  public String toString() {
    return getFilterExpr();
  }
}
