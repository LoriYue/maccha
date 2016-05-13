package org.maccha.base.util;

import java.util.HashMap;
import java.util.Map;

public class ExistMap {
	public Map map = new HashMap();
	Integer FLAG = new Integer(1);

	public boolean exist(Object key) {
		if (map.containsKey(key))
			return true;
		else {
			map.put(key, FLAG);
			return false;
		}
	}

}