package org.maccha.dao;

import java.io.InputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.Clob;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface IDaoService {
	public final static String DAOSERVICE = IDaoService.class.getName();
	/**
	 * 保存对象
	 * @param object
	 * @return
	 */
	public Serializable save(Object object);
	/**
	 * 保存或更改对象
	 * @param object
	 */
	public void saveOrUpdate(Object object);
	/**
	 * 批量保存或更新
	 * @param collectionToSaveOrUpdate
	 */
	public void saveOrUpdate(Collection collectionToSaveOrUpdate);
	/**
	 * 批量保存
	 * @param objects
	 */
	public void save(List objects);
	/**
	 * 同步对象状态
	 * @param object
	 */
	public int update(Object object);
	/**
	 * 更新数据，包含null值
	 * @param objects
	 * @return
	 */
	public int update(Object[] objects);
    /**
     * 更新数据，可指定是否包含null值
     * @param object
     * @param isIncludeNullValue
     * @return
     */
	public int update(Object object,boolean isIncludeNullValue);
	public String queryNativteSQL(final String sql);
	public List executeNativteSQL(final String sql) ;
	public Integer queryExecuteUpdateSQL(final String sql);
	/**
	 * 根据过滤条件批量更新
	 * @param modeClass
	 * @param updateValue Map 更新的属性值Map
	 * @param filterNames  String[]
	 * @param filterValues Object[]
	 * @return int
	 */
	public int update(final Class modeClass, Map updateValue,String[] filterNames, Object[] filterValues);
	/**
	 * 根据过滤条件批量更新
	 * 
	 * @param object
	 * @param updateValueMap Map 更新的属性值Map
	 * @param filter
	 */
	public int update(Class cls, Map updateValueMap, Filter filter);

	/**
	 * 根据过滤条件批量更新 支持表达式更新数据项
	 * @param cls Class
	 * @param updateSet UpdateSet
	 * @param filter Filter
	 * @return int
	 */
	public int update(Class cls, UpdateSet updateSet, Filter filter);

	/**
	 * 执行queryname对应更新语句
	 * @param queryName queryname
	 * @param paramMap 参数
	 * @return
	 */
	public int updateByQueryName(final String queryName,final Map paramMap) ;

	/**
	 * 执行queryname对应插入语句
	 * @param queryName queryname
	 * @param paramMap 参数
	 * @param whereMap 条件参数
	 * @return
	 */
	public int insertByQueryName(final String queryName,final Map paramMap) ;	
	/**
	 * 合并对象同步对象,当连接对话中已经存在 相同对象时进行合并更新
	 * 
	 * @param object
	 */
	public void merge(Object object);

	/**
	 * 删除单个对象
	 * 
	 * @param object
	 */
	public int delete(Object object);

	/**
	 * 删除一组对象
	 * 
	 * @param objects  List
	 * @return int 返回删除条数
	 */
	public int delete(Object[] objects);

	/**
	 * 根据条件删除对象
	 * 
	 * @param cls
	 *            Class
	 * @param filternames
	 *            String[] 属性名数组
	 * @param filtervalues
	 *            Object[] 属性值数组 如删除 new String[]{}, new Object[]{(Object)new
	 *            Integer(30),(Object)"北京"} = age>30 and region='北京'
	 * @return int
	 */
	public int delete(Class cls, String[] filternames, Object[] filtervalues);

	/**
	 * 根据id删除对象
	 * 
	 * @param cls
	 *            Class
	 * @param id
	 *            String
	 * @return int
	 */
	public int delete(Class cls, String id);

	/**
	 * 根据一组id删除一组对象
	 * 
	 * @param cls
	 *            Class
	 * @param id
	 *            String[]
	 * @return int
	 */
	public int delete(Class cls, String[] id);

	/**
	 * 根据过滤条件删除对象
	 * 
	 * @param object
	 * @param filter
	 */
	public int delete(Class cls, Filter filter);

	/**
	 * 对象操作刷出 每间隔一段时间，连接会执行一些必 需的SQL语句来把内存中的对象的状 态同步到JDBC连接中。这个过程被
	 * 称为刷出(flush)实际应用中不需 要显式flush()对commit()的调 用会自动触发flush()同步
	 */
	public void flush();

	/**
	 * 取得对象的标识符号
	 * 
	 * @param object
	 * @return
	 */
	public Serializable getIdentifier(Object object);

	/**
	 * 获得实现的实体对象，当还为延迟的代理对象时自动进行对象加载，当为代理对象时自动转换成实现的实体对象
	 * 
	 * @param object
	 * @return
	 */
	public Object getImplementation(Object object);

	/**
	 * 强制对象读取数,支持单对象,数组,叠代(Iterator),集合等实体, 离连接进行操作
	 * 
	 * @param proxy
	 */

	public void initialize(Object proxy);

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
	public void initialize(Object proxyObject, String path);

	/**
	 * 根据字节数组创建Blob对象
	 * 
	 * @param bytes
	 * @return
	 */
	public Blob createBlob(byte[] bytes);

	/**
	 * 根据输入流创建Blob对象
	 * 
	 * @param stream
	 * @return
	 */
	public Blob createBlob(InputStream stream);

	/**
	 * 根据字符串创建Clob对象
	 * 
	 * @param string
	 * @return
	 */
	public Clob createClob(String string);
	

	/**
	 * 通过modeClass和id加载一个实体对象
	 * @param modelClass
	 * @param id
	 * @return
	 */
	public Object load(Class modelClass, Serializable id);

	/**
	 * 通过modeClass加载一个实体对象
	 * @param modelClass
	 * @param queryArgMap
	 * @return Object
	 */
	public Object load(Class modelClass, Map queryArgMap);
	
	/**
	 * 通过modeClass加载一个实体对象
	 * @param modelClass Class
	 * @param names String[]
	 * @param values Object[]
	 * @return Object
	 */
	public Object load(Class modelClass, String[] names, Object[] values);
	/**
	 * 通过modeClass加载一个实体对象
	 * @param modelClass
	 * @param name
	 * @param value
	 * @return
	 */
	public Object load(Class modelClass, String name, Object value);
	
	/**
	 * 通过modeClass加载一个实体对象
	 * @param modelClass
	 * @param filter
	 * @return Object
	 */
	public Object load(Class _modelClass, Filter _filter);
	
	
	/**
	 * 通过实体类查询对象列表
	 * @param modeClass
	 * @return
	 */
	public List query(Class _modelClass);
	
	/**
	 * 通过实体类查询对象列表
	 * @param modeClass
	 * @param filter
	 * @return
	 */
	public List query(Class _modelClass, Filter _filter);
	/**
	 * 通过实体类查询对象列表
	 * @param modeClass
	 * @param queryArgMap
	 * @return
	 */
	public List query(Class _modelClass, Map _queryArgMap);
	/**
	 * 通过实体类查询对象列表
	 * @param modeClass
	 * @param queryNames
	 * @param queryValues
	 * @return
	 */
	public List query(Class _modelClass, String[] _queryArgNames,Object[] _queryArgValues);
	/**
	 * 通过实体类查询对象列表
	 * @param modeClass
	 * @param queryName
	 * @param queryValue
	 * @return
	 */
	public List query(Class _modelClass, String _queryArgName,Object _queryArgValue);

	/**
	 * @param modelClass
	 * @param queryName
	 * @param queryValue
	 * @param _page
	 * @param _orderColumn
	 * @param _orderSort
	 * @return
	 */
	public List query(Class _modelClass,  String _queryArgName,Object _queryArgValue,Page _page, String _orderColumn,String _orderSort);
	
	/**
	 * 
	 * @param modelClass
	 * @param queryNames
	 * @param queryValues
	 * @param _page
	 * @param _orderColumn
	 * @param _orderSort
	 * @return
	 */
	public List query(Class _modelClass,  String[] _queryArgNames,Object[] _queryArgValues,Page _page, String _orderColumn,String _orderSort);
	
	/**
	 * 
	 * @param modelClass
	 * @param queryArgMap
	 * @param _page
	 * @param _orderColumn
	 * @param _orderSort
	 * @return
	 */
	public List query(Class _modelClass, Map queryArgMap,Page _page, String _orderColumn,String _orderSort);
	
	
	/**
	 * 通过entityName查询单个实体对象
	 * @param _entityName
	 * @param _filter
	 * @param _page
	 * @param _orderColumn
	 * @param _orderSort
	 * @return
	 */
	public List query(Class _modelClass, Filter _filter,Page _page, String _orderColumn,String _orderSort);
	
	
	
	/**
	 * 通过entityName查询单个实体对象
	 * @param _entityName
	 * @param _select
	 * @param _filter
	 * @param _page
	 * @param _orderColumn
	 * @param _orderSort
	 * @return
	 */
	public List query(Class modelClass, Select _select, Filter _filter,Page _page, String _orderColumn,String _orderSort);
	/**
	 * 通过entityName,fetch查询单个实体对象
	 * @param _entityName
	 * @param _select
	 * @param _filter
	 * @param _page
	 * @param _orderColumn
	 * @param _orderSort
	 * @param fetch
	 * @return
	 */
	public List query(Class modelClass, Select _select, Filter _filter,Page _page, String _orderColumn,String _orderSort, List _fetch);
	
	/**
	 * 通过entityName,fetch查询单个实体对象
	 * @param _entityName
	 * @param _select
	 * @param _filter
	 * @param _page
	 * @param _orderBy
	 * @return
	 */
	public List query(Class _modelClass, Select _select, Filter _filter,Page _page, OrderBy _orderBy);	
	/**
	 * 返回对象个数
	 * 
	 * @param modeClass
	 * @return
	 */
	public int count(Class modeClass);

	/**
	 * 返回对象个数（属性数组）
	 * 
	 * @param modeClass
	 * @param filternames
	 * @param filtervalues
	 * @return
	 */
	public int count(Class modeClass, String[] filterNames,Object[] filterValues);
	/**
	 * 计算符合条件的对象的个数
	 * @param modeClass
	 * @param filterMap
	 * @return
	 */
	public int count(Class modeClass, Map filterMap);

	/**
	 * 计算符合条件的对象的个数
	 * @param modelClass
	 * @param filter
	 * @return int
	 */
	public int count(final Class modelClass, final Filter filter);

	/**
	 * 获取查询服务接口(单模型类)
	 * @param modeClass 模型类
	 * @return 查询服务接口
	 */
	public IQueryService getQueryService(final Class modeClass);

	/**
	 * 获取查询服务接口(单模型类,数组类型简单过滤条件)
	 * 
	 * @param modelClass
	 * @param filterNames
	 * @param filterValues
	 * @return 查询服务接口
	 */
	public IQueryService getQueryService(Class modelClass,String[] filterNames, Object[] filterValues);

	/**
	 * 获取查询服务接口(单模型类,单条件简单过滤条件)
	 * 
	 * @param modelClass
	 * @param filterName
	 * @param filterValue
	 * @return 查询服务接口
	 */
	public IQueryService getQueryService(Class modelClass, String filterName,
			Object filterValue);

	/**
	 * 获取查询服务接口(单模型类,单条件简单过滤条件)
	 * 
	 * @param modelClass
	 * @param filterName
	 * @param filterValues
	 *            可以为一个数组对象
	 * @return 查询服务接口
	 */
	public IQueryService getQueryService(Class modelClass, String filterName,Object[] filterValue);

	/**
	 * 获取查询服务接口(单模型类,Map类型简单过滤条件)
	 * @param modelClass
	 * @param filterMap
	 * @return 查询服务接口
	 */
	public IQueryService getQueryService(Class modelClass, Map filterMap);
	/**
	 * 获取查询服务接口(单模型类,过滤器)
	 * 
	 * @param modelClass
	 * @param filter
	 * @return
	 */
	public IQueryService getQueryService(Class modelClass, Filter filter);

	/**
	 * 获取查询服务接口(起始模型类,数组类型简单过滤条件)
	 * 
	 * @param startModelClass
	 * @param select  查询属性
	 * @param filterNames
	 * @param filterValues
	 * @return 查询服务接口 Student
	 * name,age,school.name,teacher.name,caucse.techcher caucse.type
	 */
	public IQueryService getQueryService(Class startModelClass, Select select,String[] filterNames, Object[] filterValues);

	/**
	 * 获取查询服务接口(起始模型类,Map类型简单过滤条件)
	 * @param startModelClass
	 * @param select 查询属性
	 * @param filterMap
	 * @param filterValues
	 * @return 查询服务接口
	 */
	public IQueryService getQueryService(Class startModelClass, Select select, Map filterMap);

	/**
	 * 获取查询服务接口(起始模型类,过滤器)
	 * 
	 * @param startModelClass
	 * @param select 查询属性
	 * @param filter
	 * @return
	 */
	public IQueryService getQueryService(Class startModelClass, Select select,
			Filter filter);
	
	/**
	 * 通过查询名获取查询服务接口
	 * @param queryName
	 * @param queryArgMap
	 * @return QueryService
	 */
	public IQueryService getQueryServiceByQueryName2Entity(String queryName,Map queryArgMap);
	
	/**
	 * 通过查询名获取查询服务接口
	 * @param queryName
	 * @param queryArgMap
	 * @param isReturnCount 是否查询记录总条数
	 * @return
	 */
	public IQueryService getQueryServiceByQueryName2Entity(String queryName,Map queryArgMap, final boolean isReturnCount);
	/**
	 * 通过查询名获取查询服务接口
	 * @param queryName
	 * @param queryArgMap
	 * @return QueryService
	 */
	public IQueryService getQueryServiceByQueryName2Map(String queryName,Map queryArgMap);
	/**
	 * 通过查询名获取查询服务接口
	 * @param queryName
	 * @param queryArgMap
	 * @param isReturnCount 是否查询记录总条数
	 * @return
	 */
	public IQueryService getQueryServiceByQueryName2Map(String queryName,Map queryArgMap, final boolean isReturnCount);
	
	
	/**
	 * 通过QueryName查询查询记录总数
	 * @param queryName： 查询名称
	 * @param queryArgMap：查询参数
	 * @return
	*/
	public int countByQueryName(String queryName, Map _queryArgMap);
	/**
	 * 通过QueryName查询查询记录总数
	 * @param _queryName
	 * @param _queryArgName
	 * @param _queryArgValue
	 * @return
	 */
	//public int countByQueryName(String _queryName, String _queryArgName, Object _queryArgValue);
	/**
	 * 通过QueryName查询查询记录总数
	 * @param _queryName
	 * @param _queryArgNames
	 * @param _queryArgValues
	 * @return
	 */
	//public int countByQueryName(String _queryName, String[] _queryArgNames, Object[] _queryArgValues);
	
	
	/**
	 * 通过QueryName单条记录查询(以别名为key值组织成List<Map>数据格式返回)
	 * @param _queryName
	 * @param _queryArgMap
	 * @return
	 */
	public Map queryByQueryName2OneMap(String _queryName, Map _queryArgMap);
	/**
	 * 通过QueryName单条记录查询(以别名为key值组织成List<Map>数据格式返回)
	 * @param _queryName
	 * @param _queryArgName
	 * @param _queryArgValue
	 * @return
	 */
	public Map queryByQueryName2OneMap(String _queryName, String _queryArgName, Object _queryArgValue);

	/**
	 * 通过QueryName单条记录查询(以别名为key值组织成List<Map>数据格式返回)
	 * @param _queryName
	 * @param _queryArgNames
	 * @param _queryArgValues
	 * @return
	 */
	public Map queryByQueryName2OneMap(String _queryName, String[] _queryArgNames, Object[] _queryArgValues);
	
	
	/**
	 * 通过QueryName查询(以别名为key值组织成List<Map>数据格式返回)
	 * @param _queryName
	 * @param _queryArgMap
	 * @return
	 */
	public List queryByQueryName2Map(String _queryName, Map _queryArgMap);
	/**
	 * 通过QueryName查询(以别名为key值组织成List<Map>数据格式返回)
	 * @param _queryName
	 * @param _queryArgNames
	 * @param _queryArgValues
	 * @return
	 */
	public List queryByQueryName2Map(String _queryName, String[] _queryArgNames, Object[] _queryArgValues);
	/**
	 * 通过QueryName查询(以别名为key值组织成List<Map>数据格式返回)
	 * @param _queryName
	 * @param _queryArgName
	 * @param _queryArgValue
	 * @return
	 */
	public List queryByQueryName2Map(String _queryName, String _queryArgName, Object _queryArgValue);
	

	
	/**
	 * 通过QueryName分页查询(以别名为key值组织成List<Map>数据格式返回)
	 * @param _queryName
	 * @param _queryArgMap
	 * @param _page
	 * @return
	 */
	public List queryByQueryName2Map(String _queryName, Map _queryArgMap, Page _page);
	/**
	 * 通过QueryName分页查询(以别名为key值组织成List<Map>数据格式返回)
	 * @param _queryName
	 * @param _queryArgMap
	 * @param _page
	 * @param _orderClumn
	 * @param _orderSort（des acs）
	 * @return
	 */
	public List queryByQueryName2Map(String _queryName, Map _queryArgMap, Page _page,String _orderClumn,String _orderSort);
	/**
	 * 通过QueryName分页查询(以别名为key值组织成List<Map>数据格式返回)
	 * @param _queryName
	 * @param _queryArgNames
	 * @param _queryArgValues
	 * @param _page
	 * @return
	 */
	public List queryByQueryName2Map(String _queryName, String[] _queryArgNames, Object[] _queryArgValues, Page _page);
	/**
	 * 通过QueryName分页查询(以别名为key值组织成List<Map>数据格式返回)
	 * @param queryName
	 * @param queryArgName
	 * @param queryArgValue
	 * @param page：查询分页对象，包括开始行号，返回行数	 
	 * @return
	 */
	public List queryByQueryName2Map(String _queryName, String _queryArgName, Object _queryArgValue, Page _page);
	
	
	/**
	 * 通过QueryName查询(按sqlQuery中PO实体类组织成List<Entity>数据格式返回)
	 * @param _queryName
	 * @param _queryArgMap
	 * @return
	 */
	public List queryByQueryName2Entity(String _queryName, Map _queryArgMap);
	/**
	 * 通过QueryName查询(按sqlQuery中PO实体类组织成List<Entity>数据格式返回)
	 * @param _queryName
	 * @param _queryArgNames
	 * @param _queryArgValues
	 * @return
	 */
	public List queryByQueryName2Entity(String _queryName, String[] _queryArgNames, Object[] _queryArgValues);
	/**
	 * 通过QueryName查询(按sqlQuery中PO实体类组织成List<Entity>数据格式返回)
	 * @param _queryName
	 * @param _queryArgName
	 * @param _queryArgValue
	 * @return
	 */
	public List queryByQueryName2Entity(String _queryName, String _queryArgName, Object _queryArgValue);
	
	/**
	 * 通过QueryName查询(按sqlQuery中PO实体类组织成List<Entity>数据格式返回)
	 * @param _queryName
	 * @param _queryArgMap
	 * @param _page
	 * @return
	 */
	public List queryByQueryName2Entity(String _queryName, Map _queryArgMap, Page _page);
	/**
	 * 通过QueryName查询(按sqlQuery中PO实体类组织成List<Entity>数据格式返回)
	 * @param _queryName
	 * @param _queryArgMap
	 * @param _page
	 * @param _orderClumn
	 * @param _orderSort（des acs）
	 * @return
	 */
	public List queryByQueryName2Entity(String _queryName, Map _queryArgMap, Page _page,String _orderClumn,String _orderSort);
	/**
	 * 通过QueryName查询(按sqlQuery中PO实体类组织成List<Entity>数据格式返回)
	 * @param _queryName
	 * @param _queryArgNames
	 * @param _queryArgValues
	 * @param _page
	 * @return
	 */
	public List queryByQueryName2Entity(String _queryName, String[] _queryArgNames, Object[] _queryArgValues, Page _page);
	/**
	 * 通过QueryName查询(按sqlQuery中PO实体类组织成List<Entity>数据格式返回)
	 * @param queryName
	 * @param queryArgName
	 * @param queryArgValue
	 * @param page：查询分页对象，包括 开始行号，返回行数等信息 
	 * @return
	 */
	public List queryByQueryName2Entity(String _queryName, String _queryArgName, Object _queryArgValue, Page _page);
	
	/**
	 * 通过HQLQueryName查询记录总数
	 * @param _queryName
	 * @param _queryArgMap
	 * @return
	 */
	public int countByHQLQueryName(String _queryName, Map _queryArgMap);
	
	
	/**
	 * 通过HQLQueryName查询
	 * @param _queryName
	 * @param _queryArgMap
	 * @return
	 */
	public List queryByHQLQueryName2Entity(String _queryName, Map _queryArgMap);
	/**
	 * 通过HQLQueryName查询(支持分页)
	 * @param _queryName
	 * @param _queryArgMap
	 * @param _page
	 * @return
	 */
	public List queryByHQLQueryName2Entity(String _queryName, Map _queryArgMap,Page _page);
	
	/**
	 * 通过HQLQueryName查询(支持分页和排序)
	 * @param _queryName
	 * @param _queryArgMap
	 * @param _page
	 * @param _orderColumn
	 * @param _orderSort
	 * @return
	 */
	public List queryByHQLQueryName2Entity(String _queryName, Map _queryArgMap,Page _page,String _orderColumn,String _orderSort);
	
	
	
	/**
	 * 通过HQLQueryName查询
	 * @param _queryName
	 * @param _queryArgMap
	 * @return
	 */
	public List queryByHQLQueryName2Map(String _queryName, Map _queryArgMap);
	/**
	 * 通过HQLQueryName查询(支持分页)
	 * @param _queryName
	 * @param _queryArgMap
	 * @param _page
	 * @return
	 */
	public List queryByHQLQueryName2Map(String _queryName, Map _queryArgMap,Page _page);
	
	/**
	 * 通过HQLQueryName查询(支持分页和排序)
	 * @param _queryName
	 * @param _queryArgMap
	 * @param _page
	 * @param _orderColumn
	 * @param _orderSort
	 * @return
	 */
	public List queryByHQLQueryName2Map(String _queryName, Map _queryArgMap,Page _page,String _orderColumn,String _orderSort);	
	
	
	
	
	
	
	/**
	 * 通过HQL查询名获取查询服务接口
	 * @param queryName
	 * @param queryArgMap
	 * @return
	 */
	public IQueryService getQueryServiceByHQLQueryName2Entity(String queryName,Map queryArgMap);
	/**
	 * 通过HQL查询名获取查询服务接口
	 * @param queryName
	 * @param queryArgMap
	 * @param isReturnCount 是否查询记录总条数
	 * @return
	 */
	public IQueryService getQueryServiceByHQLQueryName2Entity(String queryName,Map queryArgMap,final boolean isReturnCount);
	
	/**
	 * 通过HQL查询名获取查询服务接口
	 * @param queryName
	 * @param queryArgMap
	 * @return
	 */
	public IQueryService getQueryServiceByHQLQueryName2Map(String queryName,Map queryArgMap);
	
	/**
	 * 通过HQL查询名获取查询服务接口
	 * @param queryName
	 * @param queryArgMap
	 * @param isReturnCount 是否查询记录总条数
	 * @return
	 */
	
	public IQueryService getQueryServiceByHQLQueryName2Map(String queryName,Map queryArgMap,final boolean isReturnCount); 
	
	/**
	 * 通过entity Class获取查询服务接口
	 * @param startModelClass
	 * @param select
	 * @param filter
	 * @param isReturnCount 是否查询记录总条数
	 * @return
	 */
	public IQueryService getQueryService(final Class startModelClass,final Select select, final Filter filter,final boolean isReturnCount);
	
	/**
	 * 通过entity Class获取查询服务接口
	 * @param modelClass
	 * @param filter
	 * @param isReturnCount
	 * @return
	 */
	public IQueryService getQueryService(final Class modelClass,final Filter filter, boolean isReturnCount);
	
	

	
	/**
	 * 查询实体对象记录总数
	 * @param _modelClass
	 * @param _filer
	 * @return
	 */
	public int countEntity(Class _modelClass, Filter _filer);
	
	/**
	 * 对集合或者数组进行二次过滤
	 * @param collection 集合或数组
	 * @param filterNames 过滤属性名数组
	 * @param filterValues 过滤属性值数组
	 * @return
	 */
	public  List filter(final Object collection, String[] filterNames,Object[] filterValues);
	
	/**
	 * 对集合或者数组进行二次排序
	 * @param collection
	 * @param order 排序对象,用法见类说明
	 * @return
	 */
	public List order(final Object collection, OrderBy order);
	
	public List find(final String hql);
	
	/**
	 * 调用存储过程
	 * @param _procName
	 * @param agrsMap
	 * @return
	 */
	public List call(final String _queryName,Map _argsMap);
	/**
	 * 调用存储过程
	 * @param _queryName
	 * @param _agrsMap
	 * @param _isAutoCommit
	 * @return
	 */
	public List call(final String _queryName,Map _argsMap,boolean _isAutoCommit);

	//sql语句查询
	public static final String QUERY_NAME_TYPE_SQL_QUERY = "sqlQuery";
	//hql语句查询
	public static final String QUERY_NAME_TYPE_HQL_QUERY = "hqlQuery";
	//实体对象查询
	public static final String QUERY_NAME_TYPE_ENTITY_QUERY = "entityQuery";
	/**
	 * 根据查询名称发回查询类型（类型包括：sql语句查询 hql语句查询 实体对象查询）
	 * @param _queryName
	 * @return String 范围 |hqlQuery|entityQuery
	 */
	public  String getQueryNameType(String _queryName);
}
