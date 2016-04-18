package org.maccha.dao.impl;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.type.IntegerType;
import org.maccha.dao.Filter;
import org.maccha.dao.IQueryInterceptor;
import org.maccha.dao.IQueryService;
import org.maccha.dao.OrderBy;
import org.maccha.dao.Page;
import org.maccha.dao.Select;
import org.maccha.dao.SqlExpr;
import org.maccha.dao.SqlFunc;
import org.maccha.dao.type.ObjectType;
import org.maccha.dao.util.DaoUtils;
import org.maccha.base.exception.SysException;
import org.maccha.base.util.ArrayUtils;
import org.maccha.base.util.ClassUtils;
import org.maccha.base.util.ObjectUtils;
import org.maccha.base.util.StringUtils;

public class QueryServiceImpl
  implements IQueryService, IQueryInterceptor
{
  private static Logger log = Logger.getLogger(QueryServiceImpl.class);

  private String queryType = null;

  private Session session = null;

  private Class modelClass = null;

  private Filter filter = null;

  private OrderBy orderBy = null;

  private boolean isReturnCount = true;

  private boolean isResultToMapModel = true;

  private int returnType = 1;

  private List<String> resultPrevExprList = new ArrayList();

  private Select complexQuerySelect = null;

  private String _nameQuery_QueryName = null;

  private Map _nameQuery_Parameter = null;

  private List fetchGroup = new ArrayList();

  private Query result_totalQuery = null;

  private Query result_dataQuery = null;

  private Map resultPrevExprJavaFuncMap = new HashMap();

  public Object getEntity()
  {
    List list = list();
    if ((list == null) || (list.size() <= 0)) return null;
    return list().get(0);
  }

  public QueryServiceImpl()
  {
  }

  public QueryServiceImpl(Session _session, Class _modelClass, Filter _filter, boolean _isReturnCount)
  {
    this.queryType = "QUERY_TYPE_SIMPLE";
    this.session = _session;
    this.modelClass = _modelClass;
    this.filter = _filter;
    this.isReturnCount = _isReturnCount;
  }

  public QueryServiceImpl(Session _session, Class modelClass, Select _complexQuerySelect, Filter _filter, boolean _isReturnCount)
  {
    this.queryType = "QUERY_TYPE_COMPLEX";
    this.session = _session;
    this.modelClass = modelClass;
    this.filter = _filter;
    this.complexQuerySelect = _complexQuerySelect;
    this.isReturnCount = _isReturnCount;
  }

  public QueryServiceImpl(Session session, String queryName, Map parameter, boolean isReturnCount)
  {
    this(session, queryName, parameter, isReturnCount, false, false);
  }

  public QueryServiceImpl(Session session, String queryName, Map parameter, boolean isReturnCount, boolean isResultToEntityMapModel)
  {
    this(session, queryName, parameter, isReturnCount, isResultToEntityMapModel, false);
  }

  public QueryServiceImpl(Session _session, String _queryName, Map _parameter, boolean _isReturnCount, boolean _isResultToMapModel, boolean _isHQL)
  {
    if (_isHQL) this.queryType = "QUERY_TYPE_HQL_NAMEQUERY"; else
      this.queryType = "QUERY_TYPE_SQL_NAMEQUERY";
    this.session = _session;
    this._nameQuery_QueryName = _queryName;
    this._nameQuery_Parameter = _parameter;
    this.isReturnCount = _isReturnCount;
    this.isResultToMapModel = _isResultToMapModel;
  }

  public void setFetchMode(String fetch, int fetchMode)
  {
    this.fetchGroup.add(fetch);
  }

  public List beginList(int count)
  {
    return list(0, count);
  }

  public Iterator iterator() {
    excuteQuery();
    return this.result_dataQuery.iterate();
  }
  public Iterator iterator(int start, int end) {
    excuteQuery();
    this.result_dataQuery.setFirstResult(start);
    this.result_dataQuery.setMaxResults(end);
    return this.result_dataQuery.iterate();
  }

  public List lastList(int count)
  {
    int start = size() - count;
    if (start < 0) start = 0;
    this.result_dataQuery.setFirstResult(start);
    this.result_dataQuery.setMaxResults(count);
    List _resultList = this.result_dataQuery.list();
    parseResultPrevExpr2Value(_resultList);
    return _resultList;
  }

  public List list() {
    excuteQuery();
    List _resultList = this.result_dataQuery.list();
    parseResultPrevExpr2Value(_resultList);
    return _resultList;
  }

  public List list(int start, int end) {
    excuteQuery();
    this.result_dataQuery.setFirstResult(start);
    this.result_dataQuery.setMaxResults(end);
    List _resultList = this.result_dataQuery.list();
    parseResultPrevExpr2Value(_resultList);
    return _resultList;
  }

  public void parseResultPrevExpr2Value(List _list)
  {
    if (this.resultPrevExprList.size() < 0) return;

    if (isResultToMapModel())
    {
      for (int i = 0; i < _list.size(); i++) {
        Map _rowMap = (Map)_list.get(i);
        Object[] _keyArray = _rowMap.keySet().toArray();

        for (int j = 0; (_keyArray != null) && (j < _keyArray.length); j++) {
          Object _key = _keyArray[j];
          Object _valueObj = _rowMap.get(_key);
          if ((_valueObj == null) || 
            (!(_valueObj instanceof String)))
            continue;
          for (int k = 0; k < this.resultPrevExprList.size(); k++) {
            String _prevExpr = (String)this.resultPrevExprList.get(k);
            if (_valueObj.toString().indexOf(_prevExpr) < 0)
              continue;
            String _regexStr = _prevExpr + "\\[([.=\\w\\u4e00-\\u9fa5]*)\\]";
            Pattern _patt = Pattern.compile(_regexStr);
            Matcher _matc = null;
            try
            {
              _matc = _patt.matcher(_valueObj.toString());
              if (_matc.find()) {
                String _paraValuesStr = _matc.group(1);
                String _className = (String)this.resultPrevExprJavaFuncMap.get(_prevExpr + "_className");
                String _methodName = (String)this.resultPrevExprJavaFuncMap.get(_prevExpr + "_methodName");
                String _paraNames = (String)this.resultPrevExprJavaFuncMap.get(_prevExpr + "_paraNames");

                Map _paraMap = (Map)this.resultPrevExprJavaFuncMap.get(_prevExpr + "_paraMap");

                String[] _paraValues = _paraValuesStr.split("=");

                Map _tempParaMap = StringUtils.splitToMap(_paraValuesStr, ",");
                if (_tempParaMap != null) _paraMap.putAll(_tempParaMap);

                _valueObj = parseResultExpr(_className, _methodName, _paraNames, _paraMap, _matc);
                _matc.reset();
                _rowMap.put(_key, _valueObj);
              }
            }
            catch (Throwable t)
            {
            }
          }
        }
      }
    }

    if ((this.returnType == 0) && (_list.size() > 0))
    {
      List _fieldList = ObjectUtils.getFields(_list.get(0));

      for (int i = 0; (_fieldList != null) && (_fieldList.size() > 0) && (i < _list.size()); i++)
      {
        Object _rowObj = _list.get(i);

        for (int j = 0; (_fieldList != null) && (j < _fieldList.size()); j++)
        {
          String _fieldName = (String)_fieldList.get(j);
          Object _valueObj = null;
          try {
            _valueObj = ClassUtils.get(_rowObj, (String)_fieldList.get(j));
          } catch (Throwable t) {
            _valueObj = null;
            SysException.handleWarn(t);
          }

          if (_valueObj == null)
            continue;
          if (!(_valueObj instanceof String)) {
            continue;
          }
          for (int k = 0; k < this.resultPrevExprList.size(); k++)
          {
            String _prevExpr = (String)this.resultPrevExprList.get(k);

            if (_valueObj.toString().indexOf(_prevExpr) < 0)
              continue;
            String _regexStr = _prevExpr + "\\[([.=\\w\\u4e00-\\u9fa5]*)\\]";
            Pattern _patt = Pattern.compile(_regexStr);

            Matcher _matc = null;
            try
            {
              _matc = _patt.matcher(_valueObj.toString());
              if (!_matc.find())
                continue;
              String _paraValuesStr = _matc.group(1);

              String _className = (String)this.resultPrevExprJavaFuncMap.get(_prevExpr + "_className");
              String _methodName = (String)this.resultPrevExprJavaFuncMap.get(_prevExpr + "_methodName");
              String _paraNames = (String)this.resultPrevExprJavaFuncMap.get(_prevExpr + "_paraNames");

              Map _paraMap = (Map)this.resultPrevExprJavaFuncMap.get(_prevExpr + "_paraMap");

              String[] _paraValues = _paraValuesStr.split("=");

              Map _tempParaMap = StringUtils.splitToMap(_paraValuesStr, ",");
              if (_tempParaMap != null) _paraMap.putAll(_tempParaMap);

              _valueObj = parseResultExpr(_className, _methodName, _paraNames, _paraMap, _matc);
              _matc.reset();
              ClassUtils.set(_rowObj, _fieldName, _valueObj);
            }
            catch (Throwable t) {
              SysException.handleWarn(t);
            }
          }
        }
      }
    }
  }

  private String parseResultExpr(String _className, String _methodName, String _paraNames, Map _paraMap, Matcher _matc)
  {
    Object _objValue = null;
    try
    {
      Object _inst = Class.forName(_className).newInstance();

      List _paraValueList = new ArrayList();

      if ((_inst != null) && (StringUtils.isNotNull(_paraNames)))
      {
        String[] _paraNameArray = _paraNames.split(",");

        for (int i = 0; (_paraNameArray != null) && (i < _paraNameArray.length); i++)
        {
          if (StringUtils.isNull(_paraNameArray[i]))
            continue;
          _paraNameArray[i] = _paraNameArray[i].replaceAll(":", "");

          Object _paraValue = _paraMap.get(_paraNameArray[i]);

          if (_paraValue == null) continue; _paraValueList.add(_paraValue);
        }
      }
      log.info(_className + "." + _methodName + ">" + _paraNames + ":" + _paraValueList);
      if (_inst != null) _objValue = ClassUtils.invoke(_inst, _methodName, _paraValueList.toArray());
      log.info(_objValue);
    } catch (Throwable t) {
      _objValue = null;
    }

    String _valueStr = "";

    if (_objValue != null) _valueStr = _objValue.toString();

    return _matc.replaceFirst(_valueStr);
  }

  public List list(Page page) {
    if (page == null) return list();
    if (this.isReturnCount) page.setTotalRowCount(size()); else {
      excuteQuery();
    }
    this.result_dataQuery.setFirstResult(page.getStartRow());
    this.result_dataQuery.setMaxResults(page.getRowCount());

    List _resultList = this.result_dataQuery.list();
    parseResultPrevExpr2Value(_resultList);

    if ((this.complexQuerySelect == null) || (this.complexQuerySelect.getMode() != 2)) return _resultList;

    String[] propertyNames = this.complexQuerySelect.getPropertyNames();

    Class[] propertyTypes = this.complexQuerySelect.getPropertyTypes();

    List _tempList = new ArrayList();
    for (int i = 0; (_resultList != null) && (i < _resultList.size()); i++) {
      try {
        Object modelObj = null;
        modelObj = this.modelClass.newInstance();
        Object[] rowObjs = (Object[])(Object[])_resultList.get(i);
        for (int j = 0; j < propertyNames.length; j++)
          ClassUtils.set(modelObj, propertyNames[j], propertyTypes[j], rowObjs[j]);
        _tempList.add(modelObj);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return _tempList;
  }

  public void setOrderBy(OrderBy _orderBy) {
    this.orderBy = _orderBy;
  }

  public OrderBy getOrderBy() {
    return this.orderBy;
  }
  public int size() {
    excuteQuery();
    List list = this.result_totalQuery.list();
    int count = 0;
    if ((list != null) && (list.size() >= 1)) {
      Object objCount = list.get(0);
      if ((objCount instanceof Integer))
        count = ((Integer)list.get(0)).intValue();
      else count = ((Long)list.get(0)).intValue();
    }
    return count;
  }

  private void excuteQuery()
  {
    Query[] queryArray = null;
    if ("QUERY_TYPE_SIMPLE".equals(this.queryType)) {
      queryArray = excuteSimpleQuery(this.session, this.modelClass, this.filter, this.orderBy);
    }
    if ("QUERY_TYPE_SQL_NAMEQUERY".equals(this.queryType)) {
      queryArray = excuteSQLNameQuery(this.session, this._nameQuery_QueryName, this._nameQuery_Parameter, this.orderBy);
    }
    if ("QUERY_TYPE_COMPLEX".equals(this.queryType)) {
      queryArray = excuteComplexQuery(this.session, this.modelClass, this.complexQuerySelect, this.filter, this.orderBy);
    }
    if ("QUERY_TYPE_HQL_NAMEQUERY".equals(this.queryType)) {
      queryArray = excuteHQLNameQuery(this.session, this._nameQuery_QueryName, this._nameQuery_Parameter, this.orderBy);
    }
    if (queryArray != null) {
      this.result_totalQuery = queryArray[0];
      this.result_dataQuery = queryArray[1];
    }
  }

  private Query[] excuteSimpleQuery(Session _session, Class modelClass, Filter filter, OrderBy order) {
    _session.createCriteria(modelClass).setFetchMode("", FetchMode.EAGER);
    Query[] _queryArray = new Query[2];
    if (this.isReturnCount) _queryArray[0] = getTotalRowsQuery(modelClass, filter, _session); else
      _queryArray[0] = null;
    _queryArray[1] = getSimpleSelectQuery(modelClass, filter, order, _session);
    return _queryArray;
  }
  private Query[] excuteComplexQuery(Session _session, Class startModelClass, Select select, Filter filter, OrderBy order) {
    Query[] _queryArray = new Query[2];
    if (this.isReturnCount) _queryArray[0] = getTotalRowsQuery(startModelClass, select, filter, _session); else
      _queryArray[0] = null;
    _queryArray[1] = getComplexSelectQuery(startModelClass, select, filter, order, _session);
    return _queryArray;
  }

  private static String[] parseRowCountSql(StringBuffer _strSQLQuery, Map _paraMap)
  {
    String _regexStr = "(##returnRowCountSql([\\S\\w\\s]*)##)";

    Pattern _patt = Pattern.compile(_regexStr);
    Matcher _matc = null;
    String _sqlExpr = null;
    String _rowCountSql = null;
    String[] _resultSqlArray = { null, _strSQLQuery.toString() };
    try
    {
      _matc = _patt.matcher(_strSQLQuery);
      if ((_matc != null) && (_matc.find())) {
        _sqlExpr = _matc.group(1);
        _resultSqlArray[0] = _matc.group(2);

        _resultSqlArray[1] = _matc.replaceAll("");

        return _resultSqlArray;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return _resultSqlArray;
  }

  private List<String> parseResultPrevExprList(StringBuffer _strSQLQuery, Map _paraMap)
  {
    List _resultPrevExprList = new ArrayList();

    String _regexStr = "(##\\!((([a-zA-Z_][a-zA-Z0-9_]*).([a-zA-Z][A-Za-z0-9_]*))*)\\((([.:\\w]*\\,*)*)\\)##)";

    Pattern _patt = Pattern.compile(_regexStr);

    Matcher _matc = null;
    String _funcExpr = null;
    String _className = null;
    String _methodName = null;
    String _paraNames = null;
    try
    {
      _matc = _patt.matcher(_strSQLQuery);

      while ((_matc != null) && (_matc.find()))
      {
        _funcExpr = _matc.group(1);
        _className = _matc.group(2);
        _methodName = _matc.group(5);
        _paraNames = _matc.group(6);

        _className = _className.replaceAll("." + _methodName, "");

        String _resultPrevExpr = parseResultPrevExpr(_funcExpr, _paraNames, _paraMap, _matc, _strSQLQuery);

        this.resultPrevExprJavaFuncMap.put(_resultPrevExpr + "_className", _className);
        this.resultPrevExprJavaFuncMap.put(_resultPrevExpr + "_methodName", _methodName);
        this.resultPrevExprJavaFuncMap.put(_resultPrevExpr + "_paraNames", _paraNames);
        this.resultPrevExprJavaFuncMap.put(_resultPrevExpr + "_paraMap", _paraMap);

        if (StringUtils.isNotNull(_resultPrevExpr)) _resultPrevExprList.add(_resultPrevExpr);
        _matc.reset();
      }
    }
    catch (Exception e) {
    }
    return _resultPrevExprList;
  }

  private String _getAddOptExpr()
  {
    if ("sqlserver".equals(DaoServiceImpl.getDbName()))
      return "+";
    if ("oracle".equals(DaoServiceImpl.getDbName()))
      return "||";
    return "+";
  }

  private String parseResultPrevExpr(String _funcExpr, String _paraNames, Map _paraMap, Matcher _matc, StringBuffer _strSQLQuery) {
    String _valuesStr = "";
    if (StringUtils.isNotNull(_paraNames))
    {
      String[] _paraNameArray = _paraNames.split(",");

      for (int i = 0; (_paraNameArray != null) && (i < _paraNameArray.length); i++)
      {
        if (StringUtils.isNull(_paraNameArray[i]))
          continue;
        if (_paraNameArray[i].indexOf(":") < 0)
        {
          if (i == 0)
            _valuesStr = _valuesStr + "'" + _paraNameArray[i] + "='" + _getAddOptExpr() + _paraNameArray[i];
          else {
            _valuesStr = _valuesStr + "'," + _paraNameArray[i] + "='" + _getAddOptExpr() + _paraNameArray[i];
          }
          log.info("=========================== " + _valuesStr);
        } else {
          _paraNameArray[i] = _paraNameArray[i].replaceAll(":", "");
          Object _paraValue = _paraMap.get(_paraNameArray[i]);
          if (!StringUtils.isNull(_paraValue)) {
            if (i == 0)
              _valuesStr = _valuesStr + "'" + _paraNameArray[i] + "=" + _paraValue + "'";
            else {
              _valuesStr = _valuesStr + "'," + _paraNameArray[i] + "=" + _paraValue + "'";
            }
            log.info("=========================== " + _valuesStr);
          }
        }
      }
    }
    String _resultPrevExpr = _funcExpr.replaceAll("#", "zzzzzz");
    _resultPrevExpr = _resultPrevExpr.replaceAll("!", "z");
    _resultPrevExpr = _resultPrevExpr.replaceAll("\\(", "AA");
    _resultPrevExpr = _resultPrevExpr.replaceAll("\\)", "AA");
    _funcExpr = _resultPrevExpr;
    log.info("_resultPrevExpr1 =========================== " + _resultPrevExpr);
    if (StringUtils.isNotNull(_valuesStr))
      _resultPrevExpr = "'" + _resultPrevExpr + "['" + _getAddOptExpr() + _valuesStr + _getAddOptExpr() + "']'";
    else
      _resultPrevExpr = "'" + _resultPrevExpr + "[]'";
    log.info("_resultPrevExpr2 =========================== " + _resultPrevExpr);
    String newStrSQLQuery = _matc.replaceFirst(_resultPrevExpr);
    log.info("newStrSQLQuery =========================== " + newStrSQLQuery);
    _strSQLQuery.replace(0, _strSQLQuery.length(), newStrSQLQuery);

    return _funcExpr;
  }

  private Query[] excuteSQLNameQuery(Session _session, String _queryName, Map _parameter, OrderBy _order)
  {
    Query[] _queryArray = new Query[2];

    StringBuffer _strSQLQuery = DaoUtils.getNamedSQLQuery(_queryName);

    SqlExpr.parseExpr(_strSQLQuery, _parameter);

    SqlExpr.parseJavaFuncExpr(_strSQLQuery, _parameter);

    SqlFunc.parseSqlFunc(_strSQLQuery, _parameter);

    this.resultPrevExprList = parseResultPrevExprList(_strSQLQuery, _parameter);

    String[] _resultSqlArray = parseRowCountSql(_strSQLQuery, _parameter);

    if (this.isReturnCount) {
      StringBuffer _buffCountSql = new StringBuffer();

      if ((_resultSqlArray[0] != null) && (_resultSqlArray[0].length() > 0))
        _buffCountSql.append(_resultSqlArray[0]);
      else {
        _buffCountSql.append("select count(1) countnum from (").append(_resultSqlArray[1]).append(") t");
      }

      _queryArray[0] = excuteSQLQuery(_session, _buffCountSql.toString(), 2, null);
      DaoUtils.setQueryParameters(_queryArray[0], _parameter);
    } else {
      _queryArray[0] = null;
    }
    if (this.isResultToMapModel) {
      _queryArray[1] = excuteSQLQuery(_session, _resultSqlArray[1], 1, null);
    } else {
      Class _clsReturn = DaoUtils.getNamedSQLQueryReturnClass(_queryName);
      _queryArray[1] = excuteSQLQuery(_session, _resultSqlArray[1], 0, _clsReturn);
    }
    DaoUtils.setQueryParameters(_queryArray[1], _parameter);

    return _queryArray;
  }

  private Query[] excuteHQLNameQuery(Session _session, String queryName, Map parameter, OrderBy order)
  {
    Query[] _queryArray = new Query[2];
    String strHql = DaoUtils.getNamedHQLQuery(queryName).toString();

    if (this.isReturnCount)
    {
      String countHQL = " select count (*) " + removeSelect(removeOrders(strHql));

      _queryArray[0] = excuteHQLQuery(_session, countHQL, 2);

      DaoUtils.setQueryParameters(_queryArray[0], parameter);
    } else {
      _queryArray[0] = null;
    }
    if (this.isResultToMapModel)
      _queryArray[1] = excuteHQLQuery(_session, strHql, 1);
    else {
      _queryArray[1] = excuteHQLQuery(_session, strHql, 0);
    }
    DaoUtils.setQueryParameters(_queryArray[1], parameter);

    return _queryArray;
  }

  private static String removeSelect(String sql) {
    int beginPos = sql.toLowerCase().indexOf("from");
    return sql.substring(beginPos);
  }

  private static String removeOrders(String sql) {
    Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", 2);
    Matcher m = p.matcher(sql);
    StringBuffer sb = new StringBuffer();
    while (m.find()) {
      m.appendReplacement(sb, "");
    }
    m.appendTail(sb);
    return sb.toString();
  }

  private Query getSimpleSelectQuery(Class cls, Filter filter, OrderBy order, Session _session)
  {
    String className = StringUtils.unqualify(cls.getName());
    String alias = className + "Obj";
    alias = StringUtils.uncapitalizeFirst(alias);

    StringBuffer selectBuffer = new StringBuffer();
    selectBuffer.append("from ").append(className).append(" ").append(alias).append(getFetchGroup(alias));

    String whereStr = "";
    String orderStr = "";
    if (filter != null) {
      filter.setAlias(alias);
      whereStr = filter.getFilterExpr();
      if (StringUtils.hasText(whereStr)) whereStr = " where " + whereStr;
    }
    if (order != null) {
      orderStr = order.getOrderByExpr(alias);
    }

    selectBuffer.append(whereStr).append(orderStr);

    Query query = _session.createQuery(selectBuffer.toString());
    if (filter != null) {
      query = setQueryParameter(query, filter);
    }
    return query;
  }

  private Query getComplexSelectQuery(Class cls, Select select, Filter filter, OrderBy order, Session _session)
  {
    filter = addFilter(this.session, cls, filter);

    String className = StringUtils.unqualify(cls.getName());
    String alias = DaoUtils.getClassAlias(cls.getName());

    StringBuffer selectBuffer = new StringBuffer();
    if (select != null) {
      select.setClassModel(cls);
      selectBuffer.append(select.getSelectExpr(alias));
    }
    selectBuffer.append(" from ").append(className).append(" ").append(alias).append(" ").append(getFetchGroup(alias));
    String whereStr = "";
    if (filter != null) {
      filter.setAlias(alias);
      whereStr = filter.getFilterExpr();
      if (StringUtils.hasText(whereStr)) whereStr = " where " + whereStr;
    }

    String orderStr = "";
    if (order != null) {
      orderStr = order.getOrderByExpr(alias);
    }

    selectBuffer.append(whereStr).append(orderStr);
    Query query = _session.createQuery(selectBuffer.toString());

    if (filter != null) {
      query = setQueryParameter(query, filter);
    }

    return query;
  }

  private Query getTotalRowsQuery(Class cls, Filter filter, Session _session) {
    filter = addFilter(this.session, cls, filter);
    String className = StringUtils.unqualify(cls.getName());

    String alias = className + "Obj";
    alias = StringUtils.uncapitalizeFirst(alias);

    StringBuffer selectBuffer = new StringBuffer();

    selectBuffer.append("select count(*) from ").append(className).append(" ").append(alias).append(getFetchGroup(alias));

    String whereStr = "";
    if (filter != null) {
      filter.setAlias(alias);
      whereStr = filter.getFilterExpr();
      if (StringUtils.hasText(whereStr)) whereStr = " where " + whereStr;
    }
    selectBuffer.append(whereStr);
    Query query = _session.createQuery(selectBuffer.toString());
    log.info("DaoServiceImpl.getTotalRowsQuery is =" + selectBuffer.toString());

    if (filter == null) {
      return query;
    }
    query = setQueryParameter(query, filter);
    return query;
  }

  private Query getTotalRowsQuery(Class cls, Select select, Filter filter, Session _session)
  {
    filter = addFilter(this.session, cls, filter);
    String className = StringUtils.unqualify(cls.getName());

    String alias = className + "Obj";
    alias = StringUtils.uncapitalizeFirst(alias);

    StringBuffer selectBuffer = new StringBuffer();

    selectBuffer.append("select count(*) from ").append(className).append(" ").append(alias).append(getFetchGroup(alias));

    if (select != null) {
      select.setClassModel(cls);
      String _selectStr = select.getTotalSelectExpr(alias);

      if ((_selectStr != null) && (_selectStr.trim().length() > 0)) {
        selectBuffer.append(_selectStr).append(" from ").append(className).append(" ").append(alias).append(getFetchGroup(alias));
      }
    }

    String whereStr = "";
    if (filter != null) {
      filter.setAlias(alias);
      whereStr = filter.getFilterExpr();
      if (StringUtils.hasText(whereStr)) whereStr = " where " + whereStr;
    }
    selectBuffer.append(whereStr);
    Query query = _session.createQuery(selectBuffer.toString());
    log.info("DaoServiceImpl.getTotalRowsQuery is =" + selectBuffer.toString());

    if (filter == null) {
      return query;
    }
    query = setQueryParameter(query, filter);
    return query;
  }
  private Query setQueryParameter(Query query, Filter filter) {
    String[] nameArray = ArrayUtils.toStringArray(filter.getParameterNames().toArray());
    Object[] valueArray = filter.getParameterValues().toArray();
    ISQLExprProcess iSQLExprProcess = filter.getSqlExprProcess();
    for (int i = 0; (nameArray != null) && (i < nameArray.length); i++) {
      if (nameArray[i].indexOf(".") != -1) {
        nameArray[i] = nameArray[i].replaceAll("\\.", "_");
      }
      if ((valueArray[i] instanceof Object[])) {
        log.info("^^^^setParameterList  " + nameArray[i] + "=" + Arrays.toString((Object[])(Object[])valueArray[i]));
        Object[] objectArray = (Object[])(Object[])valueArray[i];
        if ((objectArray != null) && (objectArray.length > 0))
          if ((iSQLExprProcess instanceof IsBetweenExprProcess)) {
            query.setParameter(nameArray[i] + "_0", valueArray[0]);
            query.setParameter(nameArray[i] + "_1", valueArray[1]);
          } else {
            query.setParameterList(nameArray[i], objectArray, new ObjectType());
          }
      }
      else {
        log.info("^^^^setParameter  " + nameArray[i] + "=" + valueArray[i]);
        query.setParameter(nameArray[i], valueArray[i], new ObjectType());
      }

    }

    return query;
  }

  private String getFetchGroup(String alias) {
    StringBuffer buff = new StringBuffer();
    for (int i = 0; i < this.fetchGroup.size(); i++) {
      buff.append(" left join ").append(alias).append(".").append(this.fetchGroup.get(i));
    }
    return buff.toString();
  }

  public Select getComplexQuerySelect() {
    return this.complexQuerySelect;
  }

  public void setComplexQuerySelect(Select _complexQuerySelect) {
    this.complexQuerySelect = _complexQuerySelect;
  }

  public Filter get_filter() {
    return this.filter;
  }

  public void set_filter(Filter _filter) {
    this.filter = _filter;
  }

  public boolean isResultToMapModel() {
    return this.isResultToMapModel;
  }

  public void setResultToMapModel(boolean _isResultToMapModel) {
    this.isResultToMapModel = _isResultToMapModel;
  }

  public boolean isReturnCount() {
    return this.isReturnCount;
  }

  public void setReturnCount(boolean _isReturnCount) {
    this.isReturnCount = _isReturnCount;
  }

  public Class get_modelClass() {
    return this.modelClass;
  }

  public void set_modelClass(Class _modelClass) {
    this.modelClass = _modelClass;
  }

  public Map get_nameQuery_Parameter() {
    return this._nameQuery_Parameter;
  }

  public void set_nameQuery_Parameter(Map query_Parameter) {
    this._nameQuery_Parameter = query_Parameter;
  }

  public String get_nameQuery_QueryName() {
    return this._nameQuery_QueryName;
  }

  public void set_nameQuery_QueryName(String query_QueryName) {
    this._nameQuery_QueryName = query_QueryName;
  }

  public String get_queryType()
  {
    return this.queryType;
  }

  public void set_queryType(String _queryType) {
    this.queryType = _queryType;
  }

  public Session get_session() {
    return this.session;
  }

  public void set_session(Session _session) {
    this.session = _session;
  }

  public List getFetchGroup() {
    return this.fetchGroup;
  }

  public void setFetchGroup(List fetchGroup) {
    this.fetchGroup = fetchGroup;
  }

  public Query excuteSQLQuery(Session session, String sql, int _returnType, Class cls)
  {
    Query query = null;
    this.returnType = _returnType;
    switch (_returnType) {
    case 1:
      query = session.createSQLQuery(sql).setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
      break;
    case 2:
      query = session.createSQLQuery(sql).addScalar("countnum", new IntegerType());
      break;
    default:
      query = session.createSQLQuery(sql).addEntity(cls);
    }

    return query;
  }

  public Query excuteHQLQuery(Session session, String hql, int _returnType) {
    Query query = null;
    this.returnType = _returnType;
    switch (_returnType) {
    case 1:
      query = session.createQuery(hql).setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
      break;
    case 2:
      query = session.createQuery(hql);
      break;
    default:
      query = session.createQuery(hql);
    }

    return query;
  }

  public Filter addFilter(Session session, Class cls, Filter filter) {
    return filter;
  }
}
