package org.maccha.dao.impl;

public class CommonExprProcess implements ISQLExprProcess
{
private String sqlOpt = null;
private String paramName = null;
private String colName = null;
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
  StringBuffer buffExpr = new StringBuffer();
  buffExpr.append(this.colName).append(" ").append(this.sqlOpt).append(" :").append(this.paramName).append(" ");
  return buffExpr.toString();
}
}
