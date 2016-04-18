package org.maccha.base.util;

import com.google.common.base.Joiner;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.josql.Query;
import org.josql.QueryResults;
import org.maccha.dao.Filter;
import org.maccha.dao.OrderBy;
import org.maccha.dao.impl.ISQLExprProcess;
import org.maccha.dao.impl.IsBetweenExprProcess;
import org.maccha.dao.util.DaoUtils;

public class JoSQLUtil
{
  public static Logger logger = Logger.getLogger(JoSQLUtil.class.getName());

  public static Object load(List allList, Class modelClass, Serializable id)
  {
    List _list = query(allList, modelClass, Filter.eq("id", id), null);
    if (_list.isEmpty()) return null;
    return _list.get(0);
  }

  public static List query(List allList, Class _modelClass, Filter _filter)
  {
    return query(allList, _modelClass, _filter, null);
  }

  public static List query(List allList, Class _modelClass, Filter _filter, OrderBy order)
  {
    List _list = new ArrayList();
    try {
      Query _query = new Query();
      String className = _modelClass.getName();
      String alias = DaoUtils.getClassAlias(_modelClass.getName());

      StringBuffer selectBuffer = new StringBuffer();
      selectBuffer.append("Select * from ").append(className).append(" ");

      String whereStr = "";
      String orderStr = "";
      if (_filter != null) {
        whereStr = _filter.getFilterExpr();
        if (StringUtils.hasText(whereStr)) whereStr = " where " + whereStr;
      }
      if (order != null) {
        orderStr = order.getOrderByExpr(alias);
      }

      selectBuffer.append(whereStr).append(orderStr);

      if (_filter != null) {
        _query = setQueryParameter(_query, selectBuffer, _filter);
      }
      System.out.println(selectBuffer.toString());
      _query.parse(selectBuffer.toString());
      QueryResults clClients = _query.execute(allList);
      _list = clClients.getWhereResults();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return _list;
  }
  private static Query setQueryParameter(Query query, StringBuffer sql, Filter filter) {
    String[] nameArray = ArrayUtils.toStringArray(filter.getParameterNames().toArray());
    Object[] valueArray = filter.getParameterValues().toArray();
    ISQLExprProcess iSQLExprProcess = filter.getSqlExprProcess();
    for (int i = 0; (nameArray != null) && (i < nameArray.length); i++) {
      if (nameArray[i].indexOf(".") != -1) {
        nameArray[i] = nameArray[i].replaceAll("\\.", "_");
      }
      if ((valueArray[i] instanceof Object[]))
      {
        logger.info("^^^^setParameterList  " + nameArray[i] + "=" + Arrays.toString((Object[])(Object[])valueArray[i]));
        Object[] objectArray = (Object[])(Object[])valueArray[i];
        if ((objectArray != null) && (objectArray.length > 0))
          if ((iSQLExprProcess instanceof IsBetweenExprProcess)) {
            query.setVariable(nameArray[i] + "_0", valueArray[0]);
            query.setVariable(nameArray[i] + "_1", valueArray[1]);
          } else {
            int intStart = sql.indexOf(":" + nameArray[i]);
            int intEnd = intStart + (":" + nameArray[i]).length();
            sql.replace(intStart, intEnd, "'" + Joiner.on("','").join(objectArray) + "'");
          }
      }
      else
      {
        logger.info("^^^^setParameter  " + nameArray[i] + "=" + valueArray[i]);
        query.setVariable(nameArray[i], valueArray[i]);
      }
    }

    return query;
  }

  public static List query(List srcList, Class modeClass, String selectPropName, Filter filter)
  {
    List listResult = query(srcList, modeClass, filter, null);
    List listReturnResult = new ArrayList();
    Object objPropValue = null;
    for (int i = 0; i < listResult.size(); i++) {
      Object obj = listResult.get(i);
      try {
        objPropValue = PropertyUtils.getProperty(obj, selectPropName);
      } catch (Exception ex) {
        objPropValue = null;
      }
      listReturnResult.add(objPropValue);
    }
    return listReturnResult;
  }

  public static List query(List srcList, Class modeClass, String selectPropName)
  {
    return query(srcList, modeClass, selectPropName, null);
  }
}
