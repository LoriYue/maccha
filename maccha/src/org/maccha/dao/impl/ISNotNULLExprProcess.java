package org.maccha.dao.impl;

import java.io.PrintStream;

public class ISNotNULLExprProcess extends ISNULLExprProcess
  implements ISQLExprProcess
{
  String sqlOpt = "IS NOT NULL";

  public String getSQLExpr()
  {
    return this.sqlOpt;
  }

  public String getFilterExpr()
  {
    StringBuffer buffExpr = new StringBuffer();
    buffExpr.append(this.colName).append(" ").append(this.sqlOpt);

    return buffExpr.toString();
  }
}
