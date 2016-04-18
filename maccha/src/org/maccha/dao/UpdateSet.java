package org.maccha.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateSet
{
  UpdateSet updateSet = null;
  private Map map = new HashMap();
  private Map exprMap = new HashMap();
  private Map exprValueMap = new HashMap();

  public static UpdateSet getUpdateSet() {
    return new UpdateSet();
  }

  public UpdateSet set(Map map) {
    this.map.putAll(map);
    return this;
  }

  public UpdateSet set(String name, Object value)
  {
    this.map.put(name, value);
    return this;
  }

  public UpdateSet exprSet(String name, String expr, Object[] values)
  {
    this.exprMap.put(name, expr);
    this.exprValueMap.put(name, values);
    return this;
  }

  public UpdateSet exprSet(String name, String expr)
  {
    Object[] values = getExprValues(expr);
    expr = getExpr(expr);
    exprSet(name, expr, values);
    return this;
  }

  public String getExpr(String complexExpr)
  {
    String reg = "([\\*\\-\\+\\/\\(\\)])(\\d+|\\'\\w+\\')";
    return complexExpr.replaceAll(reg, "$1?");
  }

  public Object[] getExprValues(String complexExpr)
  {
    String reg = "([\\*\\-\\+\\/\\(\\)])(\\d+|\\'\\w+\\')";
    Vector exprValues_vector = new Vector();
    Pattern p = Pattern.compile(reg);
    Matcher m = p.matcher(complexExpr);
    boolean result = m.find();
    for (; result; result = m.find()) {
      String strValue = m.group(2);
      Object objValue = null;
      if (strValue.indexOf("'") > -1)
        objValue = new String(strValue.replaceAll("\\'", ""));
      else
        objValue = new Integer(strValue);
      exprValues_vector.add(objValue);
    }
    return exprValues_vector.toArray();
  }

  public String getUpdateExprStr() {
    return "";
  }


  public boolean isHasUpdateSet()
  {
    return (this.exprMap.isEmpty()) || (this.map.isEmpty());
  }

  public Map[] getExprSet() {
    return new Map[] { this.exprMap, this.exprValueMap };
  }

  public Map getSet()
  {
    return this.map;
  }
}
