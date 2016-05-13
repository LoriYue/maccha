package org.maccha.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.maccha.dao.impl.FieldFilter;
import org.maccha.dao.impl.GroupFilter;
import org.maccha.dao.impl.ISQLExprProcess;
import org.maccha.dao.impl.ISqlOperator;
import org.maccha.base.util.ArrayUtils;

public abstract class Filter {	
	/**
	 * 返回 where 字符串
	 */
	abstract public String getFilterExpr();
	/**
	 * 返回参数值列表
	 */
	abstract public List getParameterNames();

	/**
	 * 返回参数列表
	 */
	abstract public List getParameterValues();

	/**
	 * 将 FieldFilter 放入 Fielters 中，FieldFilter的格式：column=value
	 * 
	 * @param fldFilter
	 */
	abstract public Filter add(Filter filter);

	/**
	 * 设置别名
	 * 
	 * @param alias
	 */
	abstract public void setAlias(String alias);

	/**
	 * 清空过滤条件
	 */
	abstract public void clear();
	abstract public boolean isEmpty();
	/**
	 * 参数是否为空
	 * @return
	 */
	abstract public boolean isParameterEmpty();
	abstract public ISQLExprProcess getSqlExprProcess() ;
	/**
	 * 获得或（or）条件过滤组 ( a='b' or b='c' or c!='d' )
	 * 
	 * @return Filter
	 */
	public static Filter getOrGroupFilter() {
		return new GroupFilter(ISqlOperator.OR);
	}

	/**
	 * 获得与（and）条件过滤组 ( a='b'and b='c' and c!='d' )
	 * 
	 * @return Filter
	 */
	public static Filter getAndGroupFilter() {
		return new GroupFilter(ISqlOperator.AND);
	}

	/**
	 * 相等过滤 name=value
	 * 
	 * @param name
	 *            String
	 * @param value
	 *            Object
	 * @return Filter
	 */
	public static Filter eq(String name, Object value) {
		return new FieldFilter(name, value, ISqlOperator.Eq);
	}

	
	public static Filter gte(String name, Object value) {
		return new FieldFilter(name, value, ISqlOperator.GreaterThanEq);
	}
	
	public static Filter lte(String name, Object value) {
		return new FieldFilter(name, value, ISqlOperator.LessThanEq);
	}
	
	/**
	 * 大于过滤 name>value
	 * 
	 * @param name
	 *            String
	 * @param value
	 *            Object
	 * @return Filter
	 */
	public static Filter gt(String name, Object value) {
		return new FieldFilter(name, value, ISqlOperator.GreaterThan);
	}

	/**
	 * 小于过滤 name<value
	 * 
	 * @param name
	 *            String
	 * @param value
	 *            Object
	 * @return Filter
	 */
	public static Filter lt(String name, Object value) {
		return new FieldFilter(name, value, ISqlOperator.LessThan);
	}

	/**
	 * in (valueArray[0],valueArray[1]...)
	 * 
	 * @param name
	 *            String
	 * @param valueArray
	 *            Object[]
	 * @return Filter
	 */
	public static Filter in(String name, Object[] valueArray) {
		List list = new ArrayList();
		for (int i = 0; valueArray != null && i < valueArray.length; i++) {
			if (valueArray[i] == null)continue;
			list.add(valueArray[i]);
		}
		if (list.size() <= 0) {
			list = null;
			return null;
		}
		return new FieldFilter(name, list.toArray(), ISqlOperator.In);
	}

	/**
	 * 不包含于某集合 not in (valueArray[0],valueArray[1]...)
	 * 
	 * @param name
	 *            String
	 * @param valueArray
	 *            Object[]
	 * @return Filter
	 */
	public static Filter notIn(String name, Object[] valueArray) {

		List list = new ArrayList();
		for (int i = 0; valueArray != null && i < valueArray.length; i++) {
			if (valueArray[i] == null)
				continue;
			list.add(valueArray[i]);
		}
		if (list.size() <= 0) {
			list = null;
			return null;
		}
		list = null;

		return new FieldFilter(name, valueArray, ISqlOperator.NotIn);
	}

	/**
	 * 与一字符串相似，规则使用Ansi SQL 92，即用“%”和“_”做通配符 name like 'likeExpr'
	 * 
	 * @param name
	 *            String
	 * @param likeExpr
	 *            String like表达式
	 * @return Filter
	 */
	public static Filter like(String name, String likeExpr) {
		return new FieldFilter(name, likeExpr, ISqlOperator.Like);
	}

	/**
	 * 空过滤 name is null
	 * 
	 * @param name
	 *            String
	 * @return Filter
	 */
	public static Filter isNull(String name) {
		return new FieldFilter(name, null, ISqlOperator.IsNULL);
	}

	/**
	 * 小于等于对象过滤 name<value
	 * 
	 * @param name
	 *            String
	 * @param value
	 *            Object
	 * @return Filter
	 */
	public static Filter ltEq(String name, Object value) {
		return new FieldFilter(name, value, ISqlOperator.LessThanEq);
	}

	/**
	 * 大于等对象过滤 name=value
	 * 
	 * @param name
	 *            String
	 * @param value
	 *            Object
	 * @return Filter
	 */
	public static Filter gtEq(String name, Object value) {
		return new FieldFilter(name, value, ISqlOperator.GreaterThanEq);
	}

	/**
	 * 不等过滤 name!=value
	 * 
	 * @param name
	 *            String
	 * @param value
	 *            Object
	 * @return Filter
	 */
	public static Filter notEq(String name, Object value) {
		return new FieldFilter(name, value, ISqlOperator.NotEq);
	}

	/**
	 * 非空过滤 name is not null
	 * 
	 * @param name
	 *            String
	 * @return Filter
	 */
	public static Filter notNull(String name) {
		return new FieldFilter(name, null, ISqlOperator.IsNotNULL);
	}

	/**
	 * 两对象范围过滤 name between a and b
	 * 
	 * @param name
	 *            String
	 * @param a
	 *            Object
	 * @param b
	 *            Object
	 * @return Filter
	 */
	public static Filter between(String name, Object a, Object b) {
		return new FieldFilter(name, new Object[]{a,b}, ISqlOperator.Between);
	}

	public static Filter getFilter(Map map) {
		Filter groupFilter = Filter.getAndGroupFilter();
		String[] nameArray = ArrayUtils.toStringArray(map.keySet().toArray());
		for (int i = 0; i < nameArray.length; i++) {
			groupFilter
					.add(FieldFilter.eq(nameArray[i], map.get(nameArray[i])));
		}
		return groupFilter;
	}
	
	/**
	 * hql where字符串
	 * @param hqlCondition String
	 * @return Filter
	 */
	public static Filter hqlWhere(String hqlCondition) {
		return new FieldFilter(hqlCondition, null, ISqlOperator.HqlWhere);
	}	
	public static Filter getFilter(String name, String opt, Object value) {
		return new FieldFilter(name, value, opt);
	}
	public String toString(){
		return getFilterExpr() ;
	}
}
