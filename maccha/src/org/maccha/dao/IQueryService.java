package org.maccha.dao;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;

public interface IQueryService {
	/*
	 * 简单查询
	 */
	public final String QUERY_TYPE_SIMPLE = "QUERY_TYPE_SIMPLE";
	/**
	 * 复合查询
	 */
	public final String QUERY_TYPE_COMPLEX = "QUERY_TYPE_COMPLEX";
	/**
	 * HBM文件sql名称查询
	 */
	public final String QUERY_TYPE_SQL_NAMEQUERY = "QUERY_TYPE_SQL_NAMEQUERY";
	/**
	 * HBM文件hql名称查询
	 */
	public final String QUERY_TYPE_HQL_NAMEQUERY = "QUERY_TYPE_HQL_NAMEQUERY";
	public final int OBJECT_RETURN = 0;
	public final int MAP_RETURN = 1;
	public final int INTEGER_RETURN = 2;
	public void setFetchMode(String fetch,int fetchMode);
	/**
	 * 设置排序
	 * 
	 * @param order
	 */
	public void setOrderBy(OrderBy order);
	/**
	 * 
	 * 获得符合条件的数据总行数
	 */
	public int size();
	/**
	 * 一次获得所有行和列的数据
	 * 
	 * @return
	 */
	public List list();
	public Object getEntity();
	/**
	 * 获得指定范围行，所有列的数据
	 * 
	 * @return
	 */
	public List list(int start, int end);
	/**
	 * 获得指定范围行，所有列的数据
	 * @param page
	 * @return
	 */
	public List list(Page page);
	/**
	 * 获得前面指定行数数据
	 * 
	 * @param count
	 * @return
	 */
	public List beginList(int count);
	/**
	 * 获得最后指定行数数据
	 * 
	 * @param count
	 * @return
	 */
	public List lastList(int count);
	/**
	 * 一次获得所有行数据，但分次获得列的数据
	 * 
	 * @return
	 */
	public Iterator iterator();
	/**
	 * 获得指定范围行，但分次获得列的数据
	 * 
	 * @return
	 */
	public Iterator iterator(int start, int end);
	public Select getComplexQuerySelect() ;
	public void setComplexQuerySelect(Select _complexQuerySelect);
	public Filter get_filter() ;
	public void set_filter(Filter _filter);
	public boolean isResultToMapModel();
	public void setResultToMapModel(boolean _isResultToMapModel);
	public boolean isReturnCount() ;
	public void setReturnCount(boolean _isReturnCount);
	public Class get_modelClass();
	public void set_modelClass(Class class1);
	public Map get_nameQuery_Parameter();
	public void set_nameQuery_Parameter(Map query_Parameter);
	public String get_nameQuery_QueryName();
	public void set_nameQuery_QueryName(String query_QueryName) ;
	//public OrderBy getOrderBy();
	public String get_queryType() ;
	public void set_queryType(String type) ;
	public Session get_session();
	public void set_session(Session _session);
	public List getFetchGroup();
	public void setFetchGroup(List fetchGroup);
	 
}
