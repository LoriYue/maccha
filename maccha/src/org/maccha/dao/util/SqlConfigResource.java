package org.maccha.dao.util;

import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.Logger;
import org.maccha.base.exception.SysException;
import org.maccha.base.util.ConfigUtils;
import org.maccha.base.util.StringUtils;
import org.maccha.base.util.Xml2MapUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public class SqlConfigResource
{
  private static PathMatchingResourcePatternResolver _patternResolver = new PathMatchingResourcePatternResolver();

  private static Map<String, String> sqlMap = new ConcurrentHashMap();

  private static Map<String, String> fileMap = new ConcurrentHashMap();

  private static Map<String, Long> mapLastModified = new ConcurrentHashMap();

  private static boolean sqlDebugable = ConfigUtils.getBooleanValue("sqlDebugable");
  private static String sqlConfigClassPath = "classpath*:sql_config/**/*.sql.xml";
  private static String sqlQueryPath = "/sql-querys/sql-query/";

  public static Logger logger = Logger.getLogger(SqlConfigResource.class.getName());

  public static String getSqlConfigClassPath()
  {
    return sqlConfigClassPath;
  }
  public static void setSqlConfigClassPath(String sqlConfigClassPath) {
    sqlConfigClassPath = sqlConfigClassPath;
  }

  private static void initSqlConfig() {
    try {
      logger.info("开始初始化*.sql.xml文件，class路径为:" + sqlConfigClassPath);
      Resource[] resources = _patternResolver.getResources(sqlConfigClassPath);
      for (int i = 0; i < resources.length; i++) {
        parseSqlConfig(resources[i]);
      }
      logger.info("完成初始化*.sql.xml文件，详细信息:" + fileMap);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void destroy() {
    try {
      sqlMap.clear();
      fileMap.clear();
      mapLastModified.clear();
      sqlMap = null;
      fileMap = null;
      mapLastModified = null; } catch (Exception ex) {
    }
  }

  public static void parseSqlConfig(Resource resource) {
    try {
      Map _sqlConfigMap = Xml2MapUtils.parserXML2Map(resource.getInputStream());
      Iterator keyItr = _sqlConfigMap.keySet().iterator();
      String strSQLQueryName = null;
      String strKey = null;
      while (keyItr.hasNext()) {
        strKey = (String)keyItr.next();
        if (strKey.startsWith(sqlQueryPath)) {
          strSQLQueryName = strKey.substring(sqlQueryPath.length());
        }
        if (StringUtils.isNotNull(strSQLQueryName)) {
          sqlMap.put(strSQLQueryName, (String)_sqlConfigMap.get(strKey));
          fileMap.put(strSQLQueryName, resource.getURL().toString());
          mapLastModified.put(resource.getURL().toString(), new Long(resource.lastModified()));
        }
      }
      _sqlConfigMap = null;
    } catch (Throwable t) {
      SysException.handleError("无法解析sql配置文件：" + resource.getDescription(), t);
      SysException.handleMessageException("无法解析sql配置文件：" + resource.getDescription(), t);
    }
  }

  public static StringBuffer getSqlByQueryName(String _queryName) {
    String _queryText = null;

    logger.info(":::::::::sqlDebugable：" + sqlDebugable);
    if (!sqlDebugable) {
      _queryText = (String)sqlMap.get(_queryName);
      if (StringUtils.isNotNull(_queryText)) {
        logger.debug("运行模式：" + _queryName + "=" + _queryText);
        return new StringBuffer(_queryText);
      }return null;
    }

    String _sqlConfigFile = (String)fileMap.get(_queryName);
    logger.info("调试模式：_sqlConfigFile =" + _sqlConfigFile);
    if (_sqlConfigFile != null)
      try {
        Resource _resource = _patternResolver.getResource(_sqlConfigFile);
        Long _saveModified = (Long)mapLastModified.get(_sqlConfigFile);

        if ((_saveModified != null) && (_resource.lastModified() > _saveModified.longValue())) {
          logger.info("调试模式：发现" + _sqlConfigFile + "文件有更新，加载最新文件。");
          mapLastModified.put(_queryName, Long.valueOf(_resource.lastModified()));

          parseSqlConfig(_resource);
        }
      }
      catch (Exception ex) {
      }
    _queryText = (String)sqlMap.get(_queryName);
    if (StringUtils.isNotNull(_queryText))
    {
      return new StringBuffer(_queryText);
    }
    logger.info("调试模式：queryName:" + _queryName + "在现有的xml中没有找到，重新加载最近更新的sql.xml文件！");

    loadLastModifySqlConfig();
    _queryText = (String)sqlMap.get(_queryName);
    if (StringUtils.isNotNull(_queryText))
    {
      return new StringBuffer(_queryText);
    }

    if (StringUtils.isNull(_queryText)) return null;
    return new StringBuffer(_queryText);
  }

  public static void loadLastModifySqlConfig() {
    try {
      Resource[] resources = _patternResolver.getResources(sqlConfigClassPath);
      for (int i = 0; i < resources.length; i++) {
        Long _saveModified = (Long)mapLastModified.get(resources[i].getURL().toString());
        if ((_saveModified == null) || (resources[i].lastModified() > _saveModified.longValue())) {
          logger.info("调试模式：文件更新重新加载最近更新的" + resources[i].getURL().toString() + "文件！");
          mapLastModified.put(resources[i].getURL().toString(), Long.valueOf(resources[i].lastModified()));

          parseSqlConfig(resources[i]);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
