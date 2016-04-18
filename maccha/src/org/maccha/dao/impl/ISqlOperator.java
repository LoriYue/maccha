package org.maccha.dao.impl;

public abstract interface ISqlOperator
{
  public static final String AND = " AND ";
  public static final String OR = " OR ";
  public static final String Eq = "=";
  public static final String GreaterThan = ">";
  public static final String LessThan = "<";
  public static final String In = "IN";
  public static final String NotIn = "NOT IN";
  public static final String Like = "LIKE";
  public static final String IsNULL = "IS NULL";
  public static final String GreaterThanEq = ">=";
  public static final String LessThanEq = "<=";
  public static final String NotEq = "<>";
  public static final String IsNotNULL = "IS NOT NULL";
  public static final String Between = "between {a} and {b}";
  public static final String HqlWhere = "HqlWhere";
}
