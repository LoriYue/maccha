package org.maccha.dao.impl;

import java.io.PrintStream;
import java.util.Vector;

public class INExprProcess implements ISQLExprProcess
{
  private String sqlOpt = null;
  private String colName = null;
  private String paramName = null;
  private Object colValue = null;

  public void setFilter(String strColName, String strParamName, Object objColValue, String strSqlOpt)
  {
    this.paramName = strParamName;
    this.colName = strColName;
    this.colValue = objColValue;
    this.sqlOpt = strSqlOpt;
  }

  public String getSQLExpr()
  {
    return this.sqlOpt;
  }

  public String getFilterExpr()
  {
    StringBuffer buffSQLExpr = new StringBuffer();
    buffSQLExpr.append(this.colName).append(" ").append(this.sqlOpt).append(" (");
    buffSQLExpr.append(":").append(this.paramName);
    buffSQLExpr.append(")");
    return buffSQLExpr.toString();
  }
}
