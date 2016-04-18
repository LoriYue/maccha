package org.maccha.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.maccha.dao.Filter;
import org.maccha.base.util.RandomGUIDUtil;
import org.maccha.base.util.StringUtils;

public class FieldFilter extends Filter
{
  public static Map hashSqlOpt = new ConcurrentHashMap();

  private static final Class COMMONEXPRPROCESS = CommonExprProcess.class;

  private static final Class INEXPRPROCESS = INExprProcess.class;

  private static final Class ISNOTNULLEXPRPROCESS = ISNotNULLExprProcess.class;

  private static final Class ISNULLEXPRPROCESS = ISNULLExprProcess.class;

  private static final Class ISWHEREEXPRPROCESS = IsWhereExprProcess.class;
  private static final Class ISBETWEENEXPRPROCESS = IsBetweenExprProcess.class;
  private String parameterName;
  private String columnName;
  private Object columnValue;
  private String sqlOpt;
  private ISQLExprProcess sqlExprProcess;
  public boolean isNoParameter = false;
  public String alias;

  public FieldFilter(String strColumnName, Object objValue, String strOpt)
  {
    this.sqlOpt = strOpt;
    Class clsExprProcess = (Class)hashSqlOpt.get(strOpt.trim());
    if (clsExprProcess == null)
      throw new IllegalArgumentException("SQL ������������ : " + strOpt);
    try {
      this.sqlExprProcess = ((ISQLExprProcess)clsExprProcess.newInstance());
    } catch (Exception ex) {
    }
    if (((this.sqlExprProcess instanceof ISNotNULLExprProcess)) || ((this.sqlExprProcess instanceof ISNULLExprProcess)) || ((this.sqlExprProcess instanceof IsWhereExprProcess)))
    {
      this.isNoParameter = true;
    }
    this.parameterName = (strColumnName.replaceAll("\\.", "_") + "_" + RandomGUIDUtil.randomUUID());
    this.sqlExprProcess.setFilter(strColumnName, this.parameterName, objValue, strOpt);
    this.columnValue = objValue;
    this.columnName = strColumnName;
  }

  public String getFilterExpr()
  {
    StringBuffer strBuff = new StringBuffer();
    if (StringUtils.isNotNull(this.alias)) {
      strBuff.append(this.alias).append(".").append(this.sqlExprProcess.getFilterExpr());
    }
    else {
      strBuff.append(this.sqlExprProcess.getFilterExpr());
    }
    return strBuff.toString();
  }

  String getParameterName()
  {
    return this.parameterName;
  }

  Object getParameterValue()
  {
    return this.columnValue;
  }
  public boolean isParameterEmpty() {
    return this.isNoParameter;
  }
  public boolean isEmpty() {
    return (this.parameterName == null) || (this.parameterName.trim().length() == 0);
  }

  public List getParameterValues()
  {
    List vecValue = new ArrayList();
    vecValue.add(this.columnValue);
    return vecValue;
  }

  public List getParameterNames()
  {
    List vecKey = new ArrayList();

    if (("IS NOT NULL".equals(this.sqlOpt)) || ("IS NULL".equals(this.sqlOpt)))
    {
      return vecKey;
    }

    vecKey.add(this.parameterName);
    return vecKey;
  }

  public void clear()
  {
  }

  public Filter add(Filter fldFilter)
  {
    return this;
  }

  public void setAlias(String alias)
  {
    this.alias = alias;
  }
  public ISQLExprProcess getSqlExprProcess() {
    return this.sqlExprProcess;
  }
  public void setSqlExprProcess(ISQLExprProcess sqlExprProcess) {
    this.sqlExprProcess = sqlExprProcess;
  }

  static
  {
    hashSqlOpt.put("=", COMMONEXPRPROCESS);
    hashSqlOpt.put(">", COMMONEXPRPROCESS);
    hashSqlOpt.put("<", COMMONEXPRPROCESS);
    hashSqlOpt.put(">=", COMMONEXPRPROCESS);
    hashSqlOpt.put("<=", COMMONEXPRPROCESS);
    hashSqlOpt.put("LIKE", COMMONEXPRPROCESS);
    hashSqlOpt.put("<>", COMMONEXPRPROCESS);
    hashSqlOpt.put("IN", INEXPRPROCESS);
    hashSqlOpt.put("NOT IN", INEXPRPROCESS);
    hashSqlOpt.put("IS NOT NULL", ISNOTNULLEXPRPROCESS);
    hashSqlOpt.put("IS NULL", ISNULLEXPRPROCESS);
    hashSqlOpt.put("HqlWhere", ISWHEREEXPRPROCESS);
    hashSqlOpt.put("between {a} and {b}", ISBETWEENEXPRPROCESS);
  }
}
