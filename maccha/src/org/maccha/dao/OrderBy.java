package org.maccha.dao;

import java.util.Hashtable;
import java.util.Set;

public class OrderBy
{
  private static final String ASC = "ASC";
  private static final String DESC = "DESC";
  private Hashtable orderMap = null;

  public OrderBy() {
    this.orderMap = new Hashtable();
  }

  private void add(String name, String optioon) {
    this.orderMap.put(name, optioon);
  }

  public OrderBy ASC(String name)
  {
    add(name, "ASC");
    return this;
  }

  public OrderBy DESC(String name)
  {
    add(name, "DESC");
    return this;
  }

  public OrderBy order(String name, String opt)
  {
    add(name, opt);
    return this;
  }

  public static OrderBy getOrderBy()
  {
    OrderBy order = new OrderBy();
    return order;
  }

  public String getOrderByExpr(String alias)
  {
    String expr = "";

    Object[] names = this.orderMap.keySet().toArray();
    for (int i = names.length - 1; (names != null) && (i >= 0); i--) {
      String opt = (String)this.orderMap.get(names[i]);

      if (expr.length() <= 0)
        expr = expr + " order by " + alias + "." + names[i] + " " + opt;
      else
        expr = expr + "," + alias + "." + names[i] + " " + opt;
    }
    return expr;
  }
}
