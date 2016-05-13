package org.maccha.dao.service.impl;

import java.io.InputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.Clob;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.maccha.dao.Filter;
import org.maccha.dao.IDaoService;
import org.maccha.dao.OrderBy;
import org.maccha.dao.Page;
import org.maccha.dao.Select;
import org.maccha.dao.UpdateSet;
import org.maccha.dao.service.IBaseBizService;
import org.maccha.base.exception.SysException;
import org.maccha.base.util.ClassUtils;
import org.maccha.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("org.simpro.dao.service.IBaseBizService")
public class BaseBizServiceImpl implements IBaseBizService {
	@Autowired
	private IDaoService daoService;
	
	public String getQueryNameType(String _queryName) {
		// TODO 自动生成方法存根
		return daoService.getQueryNameType(_queryName);
	}

	public List call(String _queryName, Map _argsMap, boolean _isAutoCommit) {
		// TODO 自动生成方法存根
		return daoService.call(_queryName, _argsMap,_isAutoCommit);
	}

	public List call(String _queryName, Map _argsMap) {
		// TODO 自动生成方法存根
		return daoService.call(_queryName, _argsMap);
	}

	public int insertByQueryName(String queryName, Map paramMap) {
		// TODO 自动生成方法存根
		return daoService.insertByQueryName(queryName, paramMap);
	}

	public int updateByQueryName(String queryName, Map paramMap) {
		// TODO 自动生成方法存根
		return daoService.updateByQueryName(queryName, paramMap);
	}

	public List query(Class _modelClass, Select _select, Filter _filter, Page _page, String _orderColumn, String _orderSort, List _fetch) {
		// TODO 自动生成方法存根
		return daoService.query(_modelClass, _select, _filter, _page, _orderColumn, _orderSort,_fetch);
	}

	public List query(Class _modelClass, Select _select, Filter _filter, Page _page, String _orderColumn, String _orderSort) {
		// TODO 自动生成方法存根
		return daoService.query(_modelClass, _select, _filter, _page, _orderColumn, _orderSort);
	}
	/**
	 * 通过entityName,fetch查询单个实体对象
	 * @param _entityName
	 * @param _select
	 * @param _filter
	 * @param _page
	 * @param _orderBy
	 * @return
	 */
	public List query(Class _modelClass, Select _select, Filter _filter,Page _page, OrderBy _orderBy){
		return daoService.query(_modelClass, _select, _filter, _page, _orderBy);
	}
	
	/**
	 * 保存对象
	 * 
	 * @param object
	 * @return
	 */
	public Serializable save(Object object) {
		return this.daoService.save(object);
	}

	/**
	 * 保存或更改对象
	 * 
	 * @param object
	 */
	public void saveOrUpdate(Object object) {
		this.daoService.saveOrUpdate(object);
	}

	/**
	 * 批量保存或更新
	 * 
	 * @param collectionToSaveOrUpdate
	 */
	public void saveOrUpdate(Collection collectionToSaveOrUpdate) {
		this.daoService.saveOrUpdate(collectionToSaveOrUpdate);
	}

	/**
	 * 批量保存
	 * 
	 * @param objects
	 */
	public void save(List objects) {
		this.daoService.save(objects);
	}

	/**
	 * 同步对象状态
	 * 
	 * @param object
	 */
	public int update(Object object) {
		return this.daoService.update(object);
	}

	public int update(Object[] objects) {
		return this.daoService.update(objects);
	}

	public int update(Object object, boolean isIncludeNullValue) {
		return this.daoService.update(object, isIncludeNullValue);
	}

	/**
	 * 根据过滤条件批量更新
	 * 
	 * @param modeClass
	 *            Class
	 * @param updateValue
	 *            Map 更新的属性值Map
	 * @param filternames
	 *            String[]
	 * @param filtervalues
	 *            Object[]
	 * @return int
	 */
	public int update(final Class modeClass, Map updateValue,
			String[] filternames, Object[] filtervalues) {
		return this.daoService.update(modeClass, updateValue, filternames,
				filtervalues);
	}

	/**
	 * 根据过滤条件批量更新
	 * 
	 * @param object
	 * @param updateValueMap
	 *            Map 更新的属性值Map
	 * @param filter
	 */
	public int update(Class cls, Map updateValueMap, Filter filter) {
		return this.daoService.update(cls, updateValueMap, filter);
	}

	/**
	 * 根据过滤条件批量更新 支持表达式更新数据项
	 * 
	 * @param cls
	 *            Class
	 * @param updateSet
	 *            UpdateSet
	 * @param filter
	 *            Filter
	 * @return int
	 */
	public int update(Class cls, UpdateSet updateSet, Filter filter) {
		return this.daoService.update(cls, updateSet, filter);
	}

	/**
	 * 合并对象同步对象,当连接对话中已经存在 相同对象时进行合并更新
	 * 
	 * @param object
	 */
	public void merge(Object object) {
		this.daoService.merge(object);
	}

	/**
	 * 删除单个对象
	 * 
	 * @param object
	 */
	public int delete(Object object) {
		return this.daoService.delete(object);
	}

	/**
	 * 删除一组对象
	 * 
	 * @param objects
	 *            List
	 * @return int
	 */
	public int delete(Object[] objects) {
		return this.daoService.delete(objects);
	}

	/**
	 * 根据id删除对象
	 * 
	 * @param cls
	 *            Class
	 * @param id
	 *            String
	 * @return int
	 */
	public int delete(Class cls, String id) {
		return this.daoService.delete(cls, id);
	}

	/**
	 * 根据一组id删除一组对象
	 * 
	 * @param cls
	 *            Class
	 * @param id
	 *            String[]
	 * @return int
	 */
	public int delete(Class cls, String[] id) {
		return this.daoService.delete(cls, id);
	}

	/**
	 * 根据过滤条件删除对象
	 * 
	 * @param object
	 * @param filter
	 */
	public int delete(Class cls, Filter filter) {
		return this.daoService.delete(cls, filter);
	}

	/**
	 * 对象操作刷出 每间隔一段时间，连接会执行一些必 需的SQL语句来把内存中的对象的状 态同步到JDBC连接中。这个过程被
	 * 称为刷出(flush)实际应用中不需 要显式flush()对commit()的调 用会自动触发flush()同步
	 */
	public void flush() {
		this.daoService.flush();
	}

	/**
	 * 取得对象的标识符号
	 * 
	 * @param object
	 * @return
	 */
	public Serializable getIdentifier(Object object) {
		return this.daoService.getIdentifier(object);
	}

	/**
	 * 获得实现的实体对象，当还为延迟的代理对象时自动进行对象加载，当为代理对象时自动转换成实现的实体对象
	 * 
	 * @param object
	 * @return
	 */
	public Object getImplementation(Object object) {
		return this.daoService.getImplementation(object);
	}

	/**
	 * 强制对象读取数,支持单对象,数组,叠代(Iterator),集合等实体, 离连接进行操作
	 * 
	 * @param proxy
	 */

	public void initialize(Object proxy) {
		this.daoService.initialize(proxy);
	}

	/**
	 * 强制对象读取数,支持单对象,数组,叠代(Iterator),集合等实体, 对象读取策略根据对象的关系路径
	 * path格式:根模型属性名(符合属性,支持多对一和一对多的Set类型)/子模型属性名/增子属性名/... 学生/课程(一对多Set类型)/老师
	 * 
	 * @param proxy
	 *            Object
	 * @param path
	 *            String
	 * @throws Exception
	 */
	public void initialize(Object proxyObject, String path) {
		this.daoService.initialize(proxyObject, path);
	}

	/**
	 * 根据字节数组创建Blob对象
	 * 
	 * @param bytes
	 * @return
	 */
	public Blob createBlob(byte[] bytes) {
		return this.daoService.createBlob(bytes);
	}

	/**
	 * 根据输入流创建Blob对象
	 * 
	 * @param stream
	 * @return
	 */
	public Blob createBlob(InputStream stream) {
		return this.daoService.createBlob(stream);
	}



	/**
	 * 根据字符串创建Clob对象
	 * 
	 * @param string
	 * @return
	 */
	public Clob createClob(String string) {
		return this.daoService.createClob(string);
	}

	/**
	 * 查询单对象
	 * 
	 * @param modelClass
	 * @param id
	 * @return
	 */
	public Object load(Class modelClass, Serializable id) {
		return this.daoService.load(modelClass, id);
	}

	/**
	 * @param modelClass
	 * @param names
	 * @param values
	 * @return Object
	 */
	public Object load(Class modelClass, String[] names, Object[] values) {
		return this.daoService.load(modelClass, names, values);
	}
	
	public Object load(Class modelClass, String name, Object value) {
		return daoService.load(modelClass,name,value);
	}
	/**
	 * 根据一个map条件加载对象
	 * @param modelClass
	 * @param queryArgMap
	 * @return Object
	 */
	public Object load(Class modelClass, Map queryArgMap) {
		return this.daoService.load(modelClass, queryArgMap);
	}

	/**
	 * 
	 * @param modelClass
	 * @param filter
	 * @return Object
	 */
	public Object load(Class modelClass, Filter filter) {
		return this.daoService.load(modelClass, filter);
	}


	
	
	
	/**
	 * 返回对象个数
	 * @param modelClass
	 * @return
	 */
	public int count(Class modelClass) {
		return this.daoService.count(modelClass);
	}

	/**
	 * 返回对象个数（属性数组）
	 * 
	 * @param modelClass
	 * @param filternames
	 * @param filtervalues
	 * @return
	 */
	public int count(Class modelClass, String[] filternames,
			Object[] filtervalues) {
		return this.daoService.count(modelClass, filternames, filtervalues);
	}

	/**
	 * 计算符合条件的对象的个数（Map）
	 * 
	 * @param modelClass
	 * @param filterMap
	 * @return
	 */
	public int count(Class modelClass, Map filterMap) {
		return this.daoService.count(modelClass, filterMap);
	}

	/**
	 * 计算符合条件的对象的个数（过滤器）
	 * 
	 * @param modelClass
	 * @param filter
	 * @return int
	 */
	public int count(final Class modelClass, final Filter filter) {
		return this.daoService.count(modelClass, filter);
	
	}
	
	

	
	/**
	 * 查询实体对象记录总数
	 * @param _modelClass
	 * @param _filter
	 * @return
	 */
	public int countEntity(Class _modelClass, Filter _filter) {
		return this.daoService.countEntity(_modelClass, _filter);
	}

	public void deleteEntity(String entityName, String entityId) {
		try {
			daoService.delete(Class.forName(entityName), entityId);
		} catch (ClassNotFoundException ex) {
			SysException.handleException("", ex);
		}
	}
	

	public int countByQueryName(String queryName, Map _queryArgMap) {
		// TODO 自动生成方法存根
		return daoService.countByQueryName(queryName, _queryArgMap);
	}
	
	/*
	public int countByQueryName(String _queryName, String[] _queryArgNames, Object[] _queryArgValues) {
		// TODO 自动生成方法存根
		return daoService.countByQueryName(_queryName, _queryArgNames, _queryArgValues);
	}
	
	public int countByQueryName(String _queryName, String _queryArgName, Object _queryArgValue) {
		// TODO 自动生成方法存根
		return daoService.countByQueryName(_queryName, _queryArgName, _queryArgValue);
	}

*/

	/**
	 * 执行本地SQL语句，返回map列表
	 * @param sql
	 * @return
	 */
	
	public List executeNativteQuerySQL(String sql) {
		return daoService.executeNativteSQL(sql);
	}
	/**
	 * 执行更新的sql语句
	 * @param sql
	 * @return
	 */
	public Integer executeNativteUpdateSQL(String sql){
		return daoService.queryExecuteUpdateSQL(sql);
	}
	public List queryByQueryName2Map(String _queryName, Map _queryArgMap) {
		// TODO 自动生成方法存根
		return daoService.queryByQueryName2Map(_queryName, _queryArgMap);
	}

	public List queryByQueryName2Map(String _queryName, String _queryArgName, Object _queryArgValue) {
		// TODO 自动生成方法存根
		return daoService.queryByQueryName2Map(_queryName, _queryArgName, _queryArgValue);
	}
	
	public List queryByQueryName2Map(String _queryName, String[] _queryArgNames, Object[] _queryArgValues) {
		// TODO 自动生成方法存根
		return daoService.queryByQueryName2Map(_queryName, _queryArgNames, _queryArgValues);
	}
	
	public List queryByQueryName2Map(String _queryName, Map _queryArgMap, Page _page) {
		// TODO 自动生成方法存根
		return daoService.queryByQueryName2Map(_queryName, _queryArgMap, _page);
	}
	
	public List queryByQueryName2Map(String _queryName, String _queryArgName, Object _queryArgValue, Page _page) {
		// TODO 自动生成方法存根
		return daoService.queryByQueryName2Map(_queryName, _queryArgName, _queryArgValue, _page);
	}

	public List queryByQueryName2Map(String _queryName, String[] _queryArgNames, Object[] _queryArgValues, Page _page) {
		// TODO 自动生成方法存根
		return daoService.queryByQueryName2Map(_queryName, _queryArgNames, _queryArgValues, _page);
	}
	
	
	public List queryByQueryName2Map(String _queryName, Map _queryArgMap, Page _page,String _orderClumn,String _orderSort){
		return daoService.queryByQueryName2Map(_queryName, _queryArgMap, _page, _orderClumn, _orderSort);
	}
	public Map queryByQueryName2OneMap(String _queryName, Map _queryArgMap) {
		// TODO 自动生成方法存根
		return daoService.queryByQueryName2OneMap(_queryName, _queryArgMap);
	}

	public Map queryByQueryName2OneMap(String _queryName, String _queryArgName, Object _queryArgValue) {
		// TODO 自动生成方法存根
		return daoService.queryByQueryName2OneMap(_queryName,_queryArgName, _queryArgValue);
	}

	public Map queryByQueryName2OneMap(String _queryName, String[] _queryArgNames, Object[] _queryArgValues) {
		// TODO 自动生成方法存根
		return daoService.queryByQueryName2OneMap(_queryName,_queryArgNames, _queryArgValues);
	}
	
	
	public int countByHQLQueryName(String _queryName, Map _queryArgMap) {
		// TODO 自动生成方法存根
		return daoService.countByHQLQueryName(_queryName, _queryArgMap);
	}

	public List queryByHQLQueryName2Map(String _queryName, Map _queryArgMap, Page _page) {
		// TODO 自动生成方法存根
		return daoService.queryByHQLQueryName2Map(_queryName, _queryArgMap,_page);
	}

	public List queryByHQLQueryName2Map(String _queryName, Map _queryArgMap) {
		// TODO 自动生成方法存根
		return daoService.queryByHQLQueryName2Map(_queryName, _queryArgMap);
	}

	public List queryByHQLQueryName2Map(String _queryName, Map _queryArgMap, Page _page, String _orderColumn, String _orderSort) {
		// TODO 自动生成方法存根
		return daoService.queryByHQLQueryName2Map(_queryName, _queryArgMap,_page);
	}

	public List filter(Object collection, String[] filterNames, Object[] filterValues) {
		// TODO 自动生成方法存根
		return daoService.filter(collection, filterNames, filterValues);
	}
	
	/**
	 * 通用的entityName查询,查询单个实体对象
	 * @param _modelClass
	 * @param filer：查询条件对象
	 * @param page：查询分页对象，包括开始行号，返回行数
	 * @param orderColumn：所要排序的列
	 * @param orderSort：排序方式(asc,desc)等
	 * @return
	 */
	public List query(Class _modelClass, Filter _filter, Page _page,String _orderColumn, String _orderSort) {
		return this.daoService.query(_modelClass,_filter, _page, _orderColumn,_orderSort);
	}

	/**
	 * @param entityClass
	 * @param ids 已逗号分开的id字符串
	 * @return
	 */
	protected Set parseString2Set(Class entityClass, String ids) {
		if (StringUtils.isNull(ids))
			return null;

		Set hashSet = new HashSet();

		String[] idArray = ids.split(",");
		for (int i = 0; idArray != null && i < idArray.length; i++) {
			try {
				Object obj = entityClass.newInstance();
				ClassUtils.set(obj, "id", idArray[i]);
				hashSet.add(obj);
			} catch (Exception e) {
			}
		}
		return hashSet;
	}

	
	public List query(Class _modelClass, Map queryArgMap, Page _page, String _orderColumn, String _orderSort) {
		// TODO 自动生成方法存根
		return daoService.query(_modelClass,queryArgMap,  _page,  _orderColumn,  _orderSort);
	}

	public List query(Class _modelClass, String _queryArgName, Object _queryArgValue, Page _page, String _orderColumn, String _orderSort) {
		// TODO 自动生成方法存根
		return daoService.query(_modelClass,_queryArgName,_queryArgValue,  _page,  _orderColumn,  _orderSort);
	}

	public List query(Class _modelClass, String[] _queryArgNames, Object[] _queryArgValues, Page _page, String _orderColumn, String _orderSort) {
		// TODO 自动生成方法存根
		return daoService.query(_modelClass,_queryArgNames,_queryArgValues,  _page,  _orderColumn,  _orderSort);
	}

	public List order(Object collection, OrderBy order) {
		// TODO 自动生成方法存根
		return daoService.order(collection, order);
	}

	public List query(Class modelClass, Filter filter) {
		// TODO 自动生成方法存根
		return daoService.query(modelClass, filter);
	}

	public List query(Class modelClass, Map queryArgMap) {
		// TODO 自动生成方法存根
		return daoService.query(modelClass, queryArgMap);
	}

	public List query(Class modelClass, String queryName, Object queryValue) {
		// TODO 自动生成方法存根
		return daoService.query(modelClass, queryName,queryValue);
	}

	public List query(Class modelClass, String[] queryNames, Object[] queryValues) {
		// TODO 自动生成方法存根
		return daoService.query(modelClass, queryNames,queryValues);
	}

	public List query(Class modelClass) {
		// TODO 自动生成方法存根
		return daoService.query(modelClass);
	}

	public List queryByQueryName2Entity(String _queryName, Map _queryArgMap, Page _page, String _orderClumn, String _orderSort) {
		// TODO 自动生成方法存根
		return daoService.queryByQueryName2Entity( _queryName,  _queryArgMap,  _page,  _orderClumn,  _orderSort);
	}

	public List queryByQueryName2Entity(String _queryName, Map _queryArgMap, Page _page) {
		return daoService.queryByQueryName2Entity( _queryName,  _queryArgMap,  _page);

	}

	public List queryByQueryName2Entity(String _queryName, String[] _queryArgNames, Object[] _queryArgValues, Page _page) {
		
		return daoService.queryByQueryName2Entity( _queryName,  _queryArgNames,_queryArgValues,  _page);

	}
	
	public List queryByQueryName2Entity(String _queryName, String _queryArgName, Object _queryArgValue, Page _page) {
		
		return daoService.queryByQueryName2Entity( _queryName,   _queryArgName,  _queryArgValue,  _page);
	}
	
	
	public List queryByQueryName2Entity(String _queryName, Map _queryArgMap) {
		return daoService.queryByQueryName2Entity( _queryName,_queryArgMap);

	}



	public List queryByQueryName2Entity(String _queryName, String _queryArgName, Object _queryArgValue) {
		// TODO 自动生成方法存根
		return daoService.queryByQueryName2Entity(_queryName,_queryArgName,_queryArgValue);
	}



	public List queryByQueryName2Entity(String _queryName, String[] _queryArgNames, Object[] _queryArgValues) {
		// TODO 自动生成方法存根
		return daoService.queryByQueryName2Entity(_queryName,_queryArgNames,_queryArgValues);
	}

	public List queryByHQLQueryName2Entity(String _queryName, Map _queryArgMap, Page _page, String _orderColumn, String _orderSort) {
		// TODO 自动生成方法存根
		return daoService.queryByQueryName2Entity( _queryName,  _queryArgMap,  _page,  _orderColumn,  _orderSort);
	}

	public List queryByHQLQueryName2Entity(String _queryName, Map _queryArgMap, Page _page) {
		// TODO 自动生成方法存根
		return daoService.queryByQueryName2Entity(_queryName,_queryArgMap,_page);
	}

	public List queryByHQLQueryName2Entity(String _queryName, Map _queryArgMap) {
		// TODO 自动生成方法存根
		return daoService.queryByQueryName2Entity(_queryName,_queryArgMap);
	}

	public int delete(Class _modelClass, Map _filterMap) {
		// TODO 自动生成方法存根
		return 0;
	}

	public int delete(Class _modelClass, String _filterPropName, Object _filterPropValue) {
		// TODO 自动生成方法存根
		return this.daoService.delete(_modelClass, new String[]{_filterPropName}, new Object[]{_filterPropValue});
	}

	public int delete(Class _modelClass, String[] _filterPropNames, Object[] _filterPropValues) {
		// TODO 自动生成方法存根
		return this.daoService.delete(_modelClass, _filterPropNames, _filterPropValues);
	}

	public int update(Class _modelClass, Map _updateValueMap, Map _filterMap) {
		// TODO 自动生成方法存根
		return 0;
	}

	public int update(Class _modelClass, Map _updateValueMap, String _filterPropName, Object _filterPropValue) {
		// TODO 自动生成方法存根
		return 0;
	}

}
