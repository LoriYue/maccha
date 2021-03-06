package org.maccha.dao.impl;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import org.maccha.dao.Filter;
import org.maccha.base.util.StringUtils;

public class GroupFilter extends Filter {
	private List filterList = null;
	private String opt;
	private List vecKeys = null;
	private List vecValues = null;
	public String alias;
	
	/**
	 * 构造函数，一个Filters 只能有一种 关系运算符合 AND 活在 OR,在构造时要指定。
	 * @param strOpt
	 */
	public GroupFilter(String strOpt) {
	    this.opt = strOpt;
	    this.filterList = new ArrayList();
	    this.vecValues = new ArrayList();
	    this.vecKeys = new ArrayList();
	}

	/**
	 * 将 FieldFilter 放入 Fielters 中，FieldFilter的格式：column=value
	 * @param fldFilter
	 */
	public Filter add(Filter filter) {
	    if (filter == null || filter.isEmpty()) return this;
	    this.filterList.add(filter);
	    //add parameter
	    if (!filter.isParameterEmpty()){
	        this.vecKeys.addAll(filter.getParameterNames());
	        this.vecValues.addAll(filter.getParameterValues());
	    }
	    return this ;
	}
	
	public List getParameterNames() {
	    return vecKeys;
	}
	public boolean isEmpty(){
		return this.filterList.isEmpty() ;
	}
	public boolean isParameterEmpty(){
		return this.vecKeys.isEmpty() ;
	}
	/**
	 * 返回where 子句
	 */
	public String getFilterExpr() {
	    StringBuffer buffExpr = new StringBuffer(" ");
	    Filter filter = null;
	    int intLength = this.filterList.size();
	    for (int i = 0; i < intLength; i++) {
	        filter = (Filter) filterList.get(i);
	        if (StringUtils.isNotNull(this.alias)) {
	            filter.setAlias(this.alias);
	        }
	        if (i == intLength - 1) {
	            if (filter instanceof GroupFilter) {                	
	                buffExpr.append("(" + filter.getFilterExpr() + ")");
	            } else {
	                buffExpr.append(filter.getFilterExpr());
	            }
	        } else {
	            if (filter instanceof GroupFilter) {
	                buffExpr.append("(" + filter.getFilterExpr() + ")" + this.opt);
	            } else {
	                buffExpr.append(filter.getFilterExpr() + this.opt);
	            }
	        }
	    }
	    return buffExpr.toString();
	}
	
	public List getParameterValues() {
	    return this.vecValues;
	}
	
	public ArrayList parserStrCols(String filterExpr) {
	    ArrayList arryCols = new ArrayList();
	    String charStr = "(=?ORAND)<>INLIKE<=>=ISNOTNULL";
	    String[] arr = filterExpr.split(" ");
	    int index = 0;
	    for (int i = 0; i < arr.length; i++) {
	        index = charStr.indexOf(arr[i].trim());
	        if (index == -1)
	            arryCols.add(arr[i].trim());
	    }
	    return arryCols;
	}
	
	public String processSQL(ArrayList arryCols, String sql) {
	    String strCol;
	    for (int i = 0; i < arryCols.size(); i++) {
	        strCol = (String) arryCols.get(i);
	        String[] cols = strCol.split("##");
	        sql = sql.replaceFirst(strCol, cols[0]);
	    }
	    return sql;
	}
	
	public void clear() {
	    if (vecKeys != null) {
	        vecKeys.clear();
	    }
	    if (vecValues != null) {
	        vecValues.clear();
	    }
	}
	
	public void setAlias(String alias) {
	    this.alias = alias;
	}
	public ISQLExprProcess getSqlExprProcess(){
		return null ;
	}

}
