package org.maccha.base.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

public class CollectionsUtils {
	/**
	 * 把List类型转换为Set类型，可用于去除List中的重复元素
	 * @param list
	 * @return
	 */
	public static Set listToSet(List list) {
		Set set = new HashSet();
		if (list == null)
			list = new ArrayList();
		Iterator itr = list.iterator();
		while (itr.hasNext()) {
			set.add(itr.next());
		}
		return set;
	}
	/**
	 * 将Set 转换为 List
	 * 
	 * @param set
	 * @return List
	 */
	public static List changeSetToList(Set set) {
		Iterator setIter = set.iterator();
		List list = new ArrayList();
		while (setIter.hasNext()) {
			list.add(setIter.next());
		}
		return list;
	}
	/**
	 * 将数组转换为List
	 * @param arry
	 * @return
	 */
	public static List arrayToList(Object[] arry) {
		ArrayList list = new ArrayList();
		addAll(list, arry);
		return list;
	}
	/**
	 * 计算两个Collection对象的并集
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static Collection union(Collection a, Collection b) {
		return CollectionUtils.union(a, b);
	}
	/**
	 * 计算两个Collection对象的交集
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static Collection intersection(Collection a, Collection b) {
		return CollectionUtils.intersection(a, b);
	}
	/**
	 * 计算两个Collection对象的交集的补集
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static Collection disjunction(Collection a, Collection b) {
		return CollectionUtils.disjunction(a, b);
	}
	/**
	 * 从集合a中减去a和b的交集
	 * @param a
	 * @param b
	 * @return
	 */
	public static Collection subtract(Collection a, Collection b) {
		return CollectionUtils.subtract(a, b);
	}
	/**
	 * 计算两个collection对象值是否相等
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean isEqualCollection(Collection a, Collection b) {
		return CollectionUtils.isEqualCollection(a, b);
	}
	/**
	 * 将iterator中的对象全部添加到collection中
	 * @param collection
	 * @param iterator
	 */
	public static void addAll(Collection collection, Iterator iterator) {
		CollectionUtils.addAll(collection, iterator);
	}
	/**
	 * 将enumeration中的元素全部添加到Collection中
	 * @param collection
	 * @param elements
	 */
	public static void addAll(Collection collection, Enumeration enumeration) {
		CollectionUtils.addAll(collection, enumeration);
	}
	/**
	 * 将Object[]中的元素全部添加到Collection中
	 * @param collection
	 * @param elements
	 */
	public static void addAll(Collection collection, Object[] elements) {
		CollectionUtils.addAll(collection, elements);
	}
	/**
	 * 检索list
	 * @param _list list中的元素必须是可转换为map的类型
	 * @param _names 查询参数名数组
	 * @param _values 查询参数值数组
	 * @return 返回的是List<Map>类型
	 */
	public static List query(List _list, String[] _names, Object[] _values) {
		Map _queryArgMap = MapUtils.toMap(_names, _values);
		return query(_list, _queryArgMap);
	}
	/**
	 * 检索list
	 * @param _list list中的元素必须是可转换为map的类型
	 * @param _name 查询参数名
	 * @param _value 查询参数值
	 * @return 返回的是List<Map>类型
	 */
	public static List query(List _list, String _name, Object _value) {
		Map _queryArgMap = MapUtils.toMap(_name, _value);
		return query(_list, _queryArgMap);
	}
	/**
	 * 检索list
	 * @param _list list中的元素必须是可转换为map的类型
	 * @param _queryArgMap 查询参数键值对
	 * @return 返回的是List<Map>类型
	 */
	public static List query(List _list, Map _queryArgMap) {
		List _newList = new ArrayList();
		if (_queryArgMap == null)
			return _list;
		for (int i = 0; (_list != null) && (i < _list.size()); i++) {
			Map map = (Map) _list.get(i);
			Object[] _keyArray = _queryArgMap.keySet().toArray();
			boolean _flag = true;
			for (int j = 0; (_keyArray != null) && (j < _keyArray.length); j++) {
				Object _value1 = map.get(_keyArray[j]);
				Object _value2 = _queryArgMap.get(_keyArray[j]);
				if ((_value1 != null) || (_value2 != null))
					if (_value1 == null) {
						_flag = false;
					} else if (_value2 == null) {
						_flag = false;
					} else {
						if (_value1.equals(_value2))
							continue;
						_flag = false;
					}
			}
			if (!_flag)
				continue;
			_newList.add(map);
		}
		return _newList;
	}
}
