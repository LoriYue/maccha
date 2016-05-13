package org.maccha.dao;

import java.util.Hashtable;

public class OrderBy {
	// 升序
	private static final String ASC = "ASC";
	// 降序
	private static final String DESC = "DESC";
	private Hashtable orderMap = null;
	public OrderBy() {
		orderMap = new Hashtable();
	}

	private void add(String name, String optioon) {
		orderMap.put(name, optioon);
	}

	/**
	 * 升序
	 * 
	 * @param name
	 *            属性名
	 * @return
	 */
	public OrderBy ASC(String name) {
		this.add(name, ASC);
		return this;
	}

	/**
	 * 降序
	 * 
	 * @param name
	 *            属性名
	 * @return
	 */
	public OrderBy DESC(String name) {
		this.add(name, DESC);
		return this;
	}

	/**
	 * 设定排序属性，及排序方式：ASC 或 DESC
	 * @param name
	 * @param opt
	 * @return
	 */
	public OrderBy order(String name,String opt) {
		this.add(name, opt);
		return this;
	}

	public static OrderBy getOrderBy() {
		OrderBy order = new OrderBy();
		return order;
	}

	/**
	 * 获得指定别名的排序表达式
	 * @param alias 对象别名
	 * @return
	 */
	public String getOrderByExpr(String alias) {
		String expr = "";
		Object[] names = orderMap.keySet().toArray();
		for (int i = names.length-1; names != null && i >= 0; i--) {
			String opt = (String) orderMap.get(names[i]);

			if (expr.length() <= 0)
				expr += " order by "+alias + "." + names[i] + " " + opt;
			else
				expr += "," + alias + "." + names[i] + " " + opt;
		}
		return expr;

	}
}
