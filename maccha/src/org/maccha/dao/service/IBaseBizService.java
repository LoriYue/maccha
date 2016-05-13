package org.maccha.dao.service;

import java.io.InputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.Clob;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.maccha.dao.Filter;
import org.maccha.dao.OrderBy;
import org.maccha.dao.Page;
import org.maccha.dao.Select;

public interface IBaseBizService {
	public static String BASEBIZSERVICE = IBaseBizService.class.getName();
	/**
	 * 保存对象
	 * @param object
	 * @return
	 */
	public Serializable save(Object _object);
	/**
	 * 保存或更改对象
	 * @param object
	 */
	public void saveOrUpdate(Object _object) ;
	/**
	 * 批量保存或更新
	 * @param _coll
	 */
	public void saveOrUpdate(Collection _coll);
	/**
	 * 批量保存
	 * @param objects
	 */
	public void save(List objects);
	/**
	 * 执行queryName对应更新语句
	 * @param _queryName
	 * @param _argsMap
	 * @return
	 */
	public int updateByQueryName(final String queryName,final Map paramMap) ;
	/**
	 * 执行queryName对应插入语句
	 * @param _queryName
	 * @param _argsMap
	 * @return
	 */
	public int insertByQueryName(final String _queryName,final Map _argsMap) ;
	/**
	 * 更新数据
	 * @param object
	 */
	public int update(Object _object);
	/**
	 *批量更新数据，包含null值
	 * @param objects
	 * @return
	 */
	public int update(Object[] _objects);
    /**
     * 更新数据，可指定是否包含null值
     * @param _object
     * @param _isIncludeNullValue
     * @return
     */
	public int update(Object _object,boolean _isIncludeNullValue);
	/**
	 * 根据过滤条件批量更新
	 * @param _modelClass
	 * @param _updateValueMap 更新的属性值
	 * @param _filterPropName
	 * @param _filterPropValue
	 * @return
	 */
	public int update(final Class _modelClass, Map _updateValueMap,String _filterPropName, Object _filterPropValue);
	/**
	 * 根据过滤条件批量更新
	 * @param _modelClass
	 * @param _updateValueMap
	 * @param _filterPropNames
	 * @param _filterPropValues
	 * @return
	 */
	public int update(final Class _modelClass, Map _updateValueMap,String[] _filterPropNames, Object[] _filterPropValues);
	/**
	 * 根据过滤条件批量更新
	 * @param _modelClass
	 * @param _updateValueMap
	 * @param _filterMap
	 * @return
	 */
	public int update(final Class _modelClass, Map _updateValueMap,Map _filterMap);
	/**
	 * 根据过滤条件批量更新
	 * @param _modelClass
	 * @param _updateValueMap
	 * @param _filter
	 * @return
	 */
	public int update(Class _modelClass, Map _updateValueMap, Filter _filter);
	/**
	 * 删除单个对象
	 * @param object
	 */
	public int delete(Object _object);
	/**
	 * 删除一组对象
	 * 
	 * @param objects
	 *            List
	 * @return int
	 */
	public int delete(Object[] _objects);
	/**
	 * 根据id删除对象
	 * @param _modelClass
	 * @param _id
	 * @return
	 */
	public int delete(Class _modelClass, String _id);
	/**
	 * 根据一组id数组批量删除
	 * @param _modelClass
	 * @param _ids
	 * @return
	 */
	public int delete(Class _modelClass, String[] _ids);
	/**
	 * 根据条件删除数据
	 * @param _modelClass
	 * @param _filterPropName
	 * @param _filterPropValue
	 * @return
	 */
	public int delete(Class _modelClass, String _filterPropName,Object _filterPropValue);
	/**
	 * 根据条件删除数据
	 * @param _modelClass
	 * @param _filterPropNames
	 * @param _filterPropValues
	 * @return
	 */
	public int delete(Class _modelClass, String[] _filterPropNames,Object[] _filterPropValues);
	/**
	 * 根据条件删除数据
	 * @param _modelClass
	 * @param _filterMap
	 * @return
	 */
	public int delete(Class _modelClass, Map _filterMap);
	/**
	 * 根据条件删除数据
	 * @param _modelClass
	 * @param _filter
	 */
	public int delete(Class _modelClass, Filter _filterMap);
	
	/**
	 * 返回对象个数
	 * @param _modelClass
	 * @return
	 */
	public int count(Class _modelClass);
	/**
	 * 返回对象个数（属性数组）
	 * @param modelClass
	 * @param _filterPropNames
	 * @param _filterPropValues
	 * @return
	 */
	public int count(Class _modelClass, String[] _filterPropNames,Object[] _filterPropValues);
	/**
	 * 计算符合条件的对象的个数（Map）
	 * @param _modelClass
	 * @param _filterMap
	 * @return
	 */
	public int count(Class _modelClass, Map _filterMap);
	/**
	 * 计算符合条件的对象的个数（过滤器）
	 * @param modelClass
	 * @param _filter 过滤器
	 * @return
	 */
	public int count(final Class _modelClass, final Filter _filter);
	/**
	 * 通过QueryName查询数据条目总数
	 * @param queryName   查询名称
	 * @param queryArgMap 查询参数
	 * @return
	*/
	public int countByQueryName(String _queryName, Map _queryArgMap);
	/**
	 * 通过HQLQueryName查询记录总数
	 * @param _queryName
	 * @param _queryArgMap
	 * @return
	 */
	public int countByHQLQueryName(String _queryName, Map _queryArgMap);
	/**
	 * 通过modelClass和id加载一个实体对象
	 * @param modelClass
	 * @param id
	 * @return
	 */
	public Object load(Class _modelClass, Serializable _id);
	/**
	 * 通过modelClass 和过滤条件加载一个实体对象
	 * @param _modelClass
	 * @param _filterMap
	 * @return
	 */
	public Object load(Class _modelClass, Map _filterMap);
	/**
	 * 通过modelClass 和过滤条件加载一个实体对象
	 * @param _modelClass Class
	 * @param _filterPropNames
	 * @param _filterPropValues
	 * @return
	 */
	public Object load(Class _modelClass, String[] _filterPropNames, Object[] _filterPropValues);
	/**
	 * 通过modelClass 和过滤条件加载一个实体对象
	 * @param _modelClass
	 * @param _filterPropName
	 * @param _filterPropValue
	 * @return
	 */
	public Object load(Class _modelClass, String _filterPropName, Object _filterPropValue);
	/**
	 * 通过modelClass 和过滤条件加载一个实体对象
	 * @param _modelClass
	 * @param _filter
	 * @return
	 */
	public Object load(Class _modelClass, Filter _filter);
	/**
	 * 通过实体类查询对象列表
	 * @param modelClass
	 * @return
	 */
	public List query(Class modelClass);
	/**
	 * 通过实体类查询对象列表
	 * @param modelClass
	 * @param filter
	 * @return
	 */
	public List query(Class modelClass, Filter filter);
	/**
	 * 通过实体类查询对象列表
	 * @param modelClass
	 * @param queryArgMap
	 * @return
	 */
	public List query(Class modelClass, Map queryArgMap);
	/**
	 * 通过实体类查询对象列表
	 * @param modelClass
	 * @param queryNames
	 * @param queryValues
	 * @return
	 */
	public List query(Class modelClass, String[] queryNames,Object[] queryValues);
	/**
	 * 通过实体类查询对象列表
	 * @param modelClass
	 * @param queryName
	 * @param queryValue
	 * @return
	 */
	public List query(Class modelClass, String queryName,Object queryValue);
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
	 * 通过entityName,fetch查询单个实体对象
	 * @param _entityName
	 * @param _select
	 * @param _filter
	 * @param _page
	 * @param _orderBy
	 * @return
	 */
	public List query(Class _modelClass, Select _select, Filter _filter,Page _page, OrderBy _orderBy) ;
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
	 * 通过实体类名查询单个实体对象
	 * @param modelClass
	 * @param _filter：查询条件对象
	 * @param _page：查询分页对象，包括开始行号，返回行数
	 * @param _orderColumn：所要排序的列
	 * @param _orderSort：排序方式(asc,desc)等
	 * @return
	 */
	public List query(Class _modelClass, Select _select, Filter _filter,Page _page, String _orderColumn,String _orderSort);
	/**
	 * 通过实体类名和fetch查询单个实体对象
	 * @param modelClass
	 * @param _filter：查询条件对象
	 * @param _page：查询分页对象，包括开始行号，返回行数
	 * @param _orderColumn：所要排序的列
	 * @param _orderSort：排序方式(asc,desc)等
	 * @param _fetch
	 * @return
	 */
	public List query(Class _modelClass, Select _select, Filter _filter,Page _page, String _orderColumn,String _orderSort, List _fetch);
	/**
	 * 合并对象同步对象,当连接对话中已经存在 相同对象时进行合并更新
	 * @param object
	 */
	public void merge(Object object);
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
	 * 通过QueryName查询返回单条记录(以结果Map格式组织,以别名为Map的key值)
	 * @param _queryName
	 * @param _queryArgMap
	 * @return
	 */
	public Map queryByQueryName2OneMap(String _queryName, Map _queryArgMap);
	/**
	 * 通过QueryName查询返回单条记录(以结果Map格式组织,以别名为Map的key值)
	 * @param _queryName
	 * @param _queryArgName
	 * @param _queryArgValue
	 * @return
	 */
	public Map queryByQueryName2OneMap(String _queryName, String _queryArgName, Object _queryArgValue);
	/**
	 * 通过QueryName查询返回单条记录(以结果Map格式组织,以别名为Map的key值)
	 * @param _queryName
	 * @param _queryArgNames
	 * @param _queryArgValues
	 * @return
	 */
	public Map queryByQueryName2OneMap(String _queryName, String[] _queryArgNames, Object[] _queryArgValues);
	/**
	 * 通过QueryName查询(以结果Map格式组织,以别名为Map的key值)
	 * @param _queryName
	 * @param _queryArgMap
	 * @return
	 */
	public List queryByQueryName2Map(String _queryName, Map _queryArgMap);
	/**
	 * 通过QueryName查询(以结果Map格式组织,以别名为Map的key值)
	 * @param _queryName
	 * @param _queryArgNames
	 * @param _queryArgValues
	 * @return
	 */
	public List queryByQueryName2Map(String _queryName, String[] _queryArgNames, Object[] _queryArgValues);
	/**
	 * 通过QueryName查询(以结果Map格式组织,以别名为Map的key值)
	 * @param _queryName
	 * @param _queryArgName
	 * @param _queryArgValue
	 * @return
	 */
	public List queryByQueryName2Map(String _queryName, String _queryArgName, Object _queryArgValue);
	/**
	 * 通过queryName查询(支持分页 以结果Map格式组织,以别名为Map的key值)
	 * @param _queryName
	 * @param _queryArgMap
	 * @param _page
	 * @return
	 */
	public List queryByQueryName2Map(String _queryName, Map _queryArgMap, Page _page);
	/**
	 * 通过queryName查询(支持分页 以结果Map格式组织,以别名为Map的key值)
	 * @param _queryName
	 * @param _queryArgNames
	 * @param _queryArgValues
	 * @param _page
	 * @return
	 */
	public List queryByQueryName2Map(String _queryName, String[] _queryArgNames, Object[] _queryArgValues, Page _page);
	/**
	 * 通过queryName查询(支持分页 以结果Map格式组织,以别名为Map的key值)
	 * @param queryName
	 * @param queryArgName
	 * @param queryArgValue
	 * @param page：查询分页对象，包括开始行号，返回行数	 
	 * @return
	 */
	public List queryByQueryName2Map(String _queryName, String _queryArgName, Object _queryArgValue, Page _page);
	/**
	 * 通过queryName查询(支持分页和排序 以结果Map格式组织,以别名为Map的key值)
	 * @param _queryName
	 * @param _queryArgMap
	 * @param _page
	 * @param _orderClumn
	 * @param _orderSort（des acs）
	 * @return
	 */
	public List queryByQueryName2Map(String _queryName, Map _queryArgMap, Page _page,String _orderClumn,String _orderSort);
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
	 * 执行本地SQL语句，返回map列表
	 * @param sql
	 * @return
	 */
	public List executeNativteQuerySQL(String sql) ;
	/**
	 * 执行更新的sql语句
	 * @param sql
	 * @return
	 */
	public Integer executeNativteUpdateSQL(String sql);
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
	/**
	 * 根据查询名称发回查询类型（类型包括：sql语句查询 hql语句查询 实体对象查询）
	 * @param _queryName
	 * @return String 范围 |hqlQuery|entityQuery
	 */
	public  String getQueryNameType(String _queryName);	
}
