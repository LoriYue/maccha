package org.maccha.dao.impl;

public class IsBetweenExprProcess
implements ISQLExprProcess
{
String sqlOpt = null;
String colName = null;
Object colValue = null;
private String paramName = null;

public void setFilter(String strColName, String strParamName, Object objColValue, String strSqlOpt)
{
  this.colName = strColName;
  this.colValue = objColValue;
  this.paramName = strParamName;
  this.sqlOpt = strSqlOpt;
}

public String getSQLExpr()
{
  return this.sqlOpt;
}

public String getFilterExpr()
{
  StringBuffer buffExpr = new StringBuffer();
  if ((this.colValue instanceof Object[]))
  {
    buffExpr.append(this.colName).append(" ").append(this.sqlOpt.replace("{a}", ":" + this.paramName + "_1").replace("{b}", ":" + this.paramName + "_2"));
  }
  else buffExpr.append("1=1");

  return buffExpr.toString();
}
}
