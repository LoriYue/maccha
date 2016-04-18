package org.maccha.base.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

public class MapUtils {
	private Map map = new HashMap();

	public MapUtils(Map map) {
		this.map = map;
	}
	/**
	 * 获取int类型的值
	 * @param name key
	 * @return value
	 * @throws Exception 
	 */
	public int getInt(String name) throws Exception {
		return Integer.parseInt(getString(name));
	}
	/**
	 * 获取long类型的值
	 * @param name key
	 * @return value
	 * @throws Exception 
	 */
	public long getLong(String name) throws Exception {
		return Long.parseLong(getString(name));
	}
	/**
	 * 获取String类型的值
	 * @param name key
	 * @return value
	 */
	public String getString(String name) {
		String tmpstr = this.map.get(name)+"";
		if (tmpstr == null) {
			tmpstr = "";
		}
		return tmpstr.trim();
	}
	/**
	 * 设置String类型的value
	 * @param name key
	 * @param value
	 */
	public void setString(String name, String value) {
		if ((value == null) || ("null".equals(value))) {
			value = "";
		}
		setObject(name, value);
	}
	/**
	 * 设置Object类型的value
	 * @param name key
	 * @param value
	 */
	public void setObject(String name, Object obj) {
		if ((name == null) || (obj == null)) {
			return;
		}
		this.map.put(name, obj);
	}
	/**
	 * 获取Object类型的值
	 * @param name key
	 * @return value
	 */
	public Object getObject(String name) {
		return this.map.get(name);
	}
	/**
	 * 获取boolean类型的值
	 * @param name key
	 * @return value
	 */
	public boolean getBoolean(String name) {
		return Boolean.valueOf(getString(name)).booleanValue();
	}
	/**
	 * 获取所有key
	 * @return 返回Object类型的数组
	 */
	public Object[] getNames() {
		return this.map.keySet().toArray();
	}
	/**
	 * 获取所有value
	 * @return 返回Object类型的数组
	 */
	public Object[] getValues() {
		return this.map.values().toArray();
	}

	public static Map ModelToMap(Object obj) {
		Map _map = new Hashtable();
		return _map;
	}

	public static String MapToXml(Map _map) {
		String xml = "";
		return xml;
	}

	public static Map XmlToMap(String xml) {
		Map _map = new Hashtable();
		return _map;
	}

	public static Map[] toMapArray(Map map) {
		Map[] mapArray = new Hashtable[map.size()];
		Object[] keyArray = map.keySet().toArray();
		for (int i = 0; (keyArray != null) && (i < keyArray.length); i++) {
			Object key = keyArray[i];
			if (key == null) {
				continue;
			}
			Object value = map.get(key);
			Hashtable att = new Hashtable();
			att.put("name", key);
			att.put("value", value);
			mapArray[i] = att;
		}
		return mapArray;
	}

	public static Map mapArrayToMap(Map[] maps, String name_key,
			String value_key) {
		Hashtable map = new Hashtable();
		Object key = null;
		Object value = null;
		for (int i = 0; (maps != null) && (i < maps.length); i++) {
			key = maps[i].get(name_key);
			value = maps[i].get(value_key);
			if (key == null) {
				continue;
			}
			if (value == null) {
				value = "";
			}
			map.put(key, value);
		}
		maps = null;
		name_key = null;
		value_key = null;
		key = null;
		value = null;
		return map;
	}

	public void addObject(Object name, Object value) {
		String PREFIX = "ADDOBJECT000000000";
		name = "ADDOBJECT000000000" + name;

		if ((name == null) || (value == null)) {
			return;
		}
		HashSet set = null;
		try {
			set = (HashSet) this.map.get(name);
		} catch (Exception e) {
		}
		if (set == null) {
			set = new HashSet();
		}
		set.add(value);
		this.map.put(name, set);
	}

	public Set getSet(Object name) {
		String PREFIX = "ADDOBJECT000000000";
		name = "ADDOBJECT000000000" + name;

		Object obj = this.map.get(name);
		if (obj == null) {
			obj = new HashSet();
		}
		return (Set) obj;
	}

	public void addNum(String name, String val) {
		String PREFIX = "ADDNUM000000000";
		name = "ADDNUM000000000" + name;

		String str = getString(name);
		if ((str != null) && (str.trim().length() > 0)) {
			str = MathUtils.sum(str, val);
			setString(name, str);
		} else {
			setString(name, val);
		}
	}

	public String getNum(String name) {
		String PREFIX = "ADDNUM000000000";
		return getString(name);
	}

	public boolean isHas(String name) {
		return isHas("check" + name, "CHACK_FLAG");
	}

	private boolean isHas(String name, String val) {
		String str = getString(name);
		if ((str != null) && (str.trim().length() > 0)) {
			return true;
		}
		setString(name, val);
		return false;
	}

	public void addString(String name, String val, String tag) {
		String PREFIX = "ADDSTRING000000000";
		name = "ADDSTRING000000000" + name;
		String str = getString(name);
		if ((str != null) && (str.trim().length() > 0)) {
			str = str + tag + val;
			setString(name, str);
		} else {
			setString(name, val);
		}
	}

	public static Map getMap(Map[] props, String name_key, String value_key) {
		Map prop = new Hashtable();
		for (int i = 0; (props != null) && (i < props.length); i++) {
			Object key = props[i].get(name_key);
			if (key == null) {
				key = props[i].get(name_key.toUpperCase());
			}
			String value = (String) props[i].get(value_key);
			if (value == null) {
				value = (String) props[i].get(value_key.toUpperCase());
			}
			if (key == null) {
				continue;
			}
			if (value == null) {
				value = "";
			}
			prop.put(key, value);
		}
		return prop;
	}

	public static String[] getStringArray(Map[] tmp, String key) {
		String[] list = new String[tmp.length];
		for (int i = 0; i < list.length; i++) {
			String value = (String) tmp[i].get(key);
			if (value == null) {
				continue;
			}
			list[i] = value;
		}
		return list;
	}

	public static Map toMap(String name, Object value) {
		return toMap(new String[] { name }, new Object[] { value });
	}

	public static Map toMap(String[] names, Object[] values) {
		Map _queryArgMap = new HashMap();
		for (int i = 0; (names != null) && (i < names.length); i++) {
			_queryArgMap.put(names[i], values[i]);
		}
		return _queryArgMap;
	}
}
