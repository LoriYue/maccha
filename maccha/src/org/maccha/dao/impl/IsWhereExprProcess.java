package org.maccha.dao.impl;

public class IsWhereExprProcess
implements ISQLExprProcess
{
String sqlOpt = "IS NULL";
String colName = null;
Object colValue = null;

public void setFilter(String strColName, String strParamName, Object objColValue, String strSqlOpt)
{
  this.colName = strColName;
  this.colValue = objColValue;
}

public String getSQLExpr()
{
  return this.sqlOpt;
}

public String getFilterExpr()
{
  StringBuffer buffExpr = new StringBuffer();
  buffExpr.append(this.colName);
  return buffExpr.toString();
}
}
