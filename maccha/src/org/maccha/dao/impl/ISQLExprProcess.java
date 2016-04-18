package org.maccha.dao.impl;

public abstract interface ISQLExprProcess
{
  public abstract void setFilter(String paramString1, String paramString2, Object paramObject, String paramString3);

  public abstract String getSQLExpr();

  public abstract String getFilterExpr();
}
