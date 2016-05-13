package org.maccha.dao.util;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.Logger;
import org.maccha.base.exception.SysException;
import org.maccha.base.util.ConfigUtils;
import org.maccha.base.util.StringUtils;
import org.maccha.base.util.Xml2MapUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public class SqlConfigResource {
	private static PathMatchingResourcePatternResolver _patternResolver = new PathMatchingResourcePatternResolver();
	//存储queryName和sql键值对，key为queryName，value为sql字符串
	private static Map<String,String> sqlMap=new ConcurrentHashMap<String,String>();
	//存储queryName和sql.xml键值对，key为queryName，value为sql.xml文件路径
	private static Map<String,String> fileMap=new ConcurrentHashMap<String,String>();
	//保存sql文件最后一次更新时间，key为sql.xml文件路径，value为最后一次更新时间
	private static Map<String,Long> mapLastModified = new ConcurrentHashMap<String,Long>();
	private static boolean sqlDebugable=ConfigUtils.getBooleanValue("sqlDebugable");
	private static String sqlConfigClassPath = "classpath*:sql_config/**/*.sql.xml" ;
	private static String sqlQueryPath = "/sql-querys/sql-query/" ;
	public static Logger logger = Logger.getLogger(SqlConfigResource.class.getName());
	public static String getSqlConfigClassPath() {
		return sqlConfigClassPath;
	}
	public static void setSqlConfigClassPath(String sqlConfigClassPath) {
		SqlConfigResource.sqlConfigClassPath = sqlConfigClassPath;
	}
	@SuppressWarnings("unused")
	private static void initSqlConfig(){
		try {
			logger.info("开始初始化*.sql.xml文件，class路径为:"+sqlConfigClassPath);
			Resource[] resources = _patternResolver.getResources(sqlConfigClassPath);
			for(int i=0; i <resources.length;i++){
				parseSqlConfig(resources[i]);
			}
			logger.info("完成初始化*.sql.xml文件，详细信息:"+fileMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void destroy(){
		try{
			sqlMap.clear();
			fileMap.clear();
			mapLastModified.clear();
			sqlMap = null ;
			fileMap = null ;
			mapLastModified = null ;
		}catch(Exception ex){
		}
	}
	public static void parseSqlConfig(Resource resource){
		try{
			Map<String,String> _sqlConfigMap = Xml2MapUtils.parserXML2Map(resource.getInputStream());
			Iterator<String> keyItr = _sqlConfigMap.keySet().iterator();
			String strSQLQueryName = null ;
			String strKey = null ;
			while(keyItr.hasNext()){
				strKey = keyItr.next();
				if(strKey.startsWith(sqlQueryPath)){
					strSQLQueryName=strKey.substring(sqlQueryPath.length());
				}
				if(StringUtils.isNotNull(strSQLQueryName)){
					sqlMap.put(strSQLQueryName ,_sqlConfigMap.get(strKey));
					fileMap.put(strSQLQueryName, resource.getURL().toString());
					mapLastModified.put(resource.getURL().toString(),new Long(resource.lastModified()));
				}
			}
			_sqlConfigMap = null;
		}catch(Throwable t){
			SysException.handleError("无法解析sql配置文件："+resource.getDescription(),t);
			SysException.handleMessageException("无法解析sql配置文件："+resource.getDescription(), t);
		}
	}
	public static StringBuffer getSqlByQueryName(String _queryName){
		String _queryText= null ;
		//不可调试模式
		logger.info(":::::::::sqlDebugable："+sqlDebugable);
		if(!sqlDebugable){
			_queryText = sqlMap.get(_queryName);
			if(StringUtils.isNotNull(_queryText)){
				logger.debug("运行模式："+_queryName+"="+_queryText);
				return new StringBuffer(_queryText);
			}else return null ;
		}else{
		    //调试模式
			//重新初始化配置文件
			String _sqlConfigFile=fileMap.get(_queryName);
			logger.info("调试模式：_sqlConfigFile ="+ _sqlConfigFile);
			if(_sqlConfigFile!=null){
				try{
					Resource _resource = _patternResolver.getResource(_sqlConfigFile);
					Long _saveModified = mapLastModified.get(_sqlConfigFile);
					//有更新则加载文件
					if ((_saveModified != null) && (_resource.lastModified() > _saveModified.longValue())) {
						logger.info("调试模式：发现"+ _sqlConfigFile+"文件有更新，加载最新文件。");
						mapLastModified.put(_queryName, _resource.lastModified());
						//根据配置文件名（包含全路径）重新解析出查询语句配置信息
						parseSqlConfig(_resource);
					}
				}catch(Exception ex){
				}
			}
			_queryText=sqlMap.get(_queryName);
			if(StringUtils.isNotNull(_queryText)){
				//logger.info("调试模式："+_queryName+"="+_queryText);
				return new StringBuffer(_queryText);
			}else {
				logger.info("调试模式：queryName:"+_queryName+"在现有的xml中没有找到，重新加载最近更新的sql.xml文件！");
				//更改、新增SQLQuery Name或者增加.sql.xml文件 的需要重新初始化有变化的文件才能把最新sql装载起来
				loadLastModifySqlConfig();
				_queryText=sqlMap.get(_queryName);
				if(StringUtils.isNotNull(_queryText)){
					//logger.info("初始："+_queryName+"="+_queryText);
					return new StringBuffer(_queryText);
				}
			}
		}
		if(StringUtils.isNull(_queryText)) return null;
		return new StringBuffer(_queryText);
	}
	//只加载最后更新的sql文件
	public static void loadLastModifySqlConfig(){
		try {
			Resource[] resources = _patternResolver.getResources(sqlConfigClassPath);
			for(int i=0; i <resources.length;i++){
				Long _saveModified = mapLastModified.get(resources[i].getURL().toString());
				if ((_saveModified == null) || (resources[i].lastModified() > _saveModified.longValue())) {
					logger.info("调试模式：文件更新重新加载最近更新的"+resources[i].getURL().toString()+"文件！");
					mapLastModified.put(resources[i].getURL().toString(), resources[i].lastModified());
					//根据配置文件名（包含全路径）重新解析出查询语句配置信息
					parseSqlConfig(resources[i]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
