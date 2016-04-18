package org.maccha.base.util;

import java.util.HashMap;
import java.util.Map;

public class ExistMap
{
  public Map map = new HashMap();

  Integer FLAG = new Integer(1);

  public boolean exist(Object key) {
    if (this.map.containsKey(key)) {
      return true;
    }
    this.map.put(key, this.FLAG);
    return false;
  }
}