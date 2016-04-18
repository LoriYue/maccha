package org.maccha.base.util;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.log4j.Logger;
import org.maccha.base.util.Xml2MapUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class ConfigUtils {
	/**
	 * 属性文件名
	 */
	private static String configFileClassPath = "config.xml";
	private String catalogName;
	private static Map<String,String> configMap =new ConcurrentHashMap<String,String>();
	public static Logger logger = Logger.getLogger(ConfigUtils.class.getName());
	public ConfigUtils(String _catalogName) {
		this.catalogName=_catalogName;
	}
	public ConfigUtils(){}
	public static void initConfig(){
		Resource _configFileResource = new ClassPathResource(configFileClassPath);
		try {
			logger.info("....初始化config.xml开始....");
			InputStream _in = _configFileResource.getInputStream() ;
			Xml2MapUtils.parserXML2Map(_in, configMap);
			logger.info("....初始化config.xml完成....");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

  public void destroy() {
    try {
      configMap.clear();
      configMap = null; 
    } catch (Exception ex) {
    	
    }
  }
  /**
   * 获取config.xml的资源位置
   * @return
   */
  public static String getConfigFileClassPath() {
    return configFileClassPath;
  }
  /**
   * 设置config.xml的资源位置（Spring容器调用才有效？）
   * @param configFileClassPath
   */
  public static void setConfigFileClassPath(String configFileClassPath) {
    configFileClassPath = configFileClassPath;
  }
  /**
   * 根据name获取config节点下的属性值
   * @param _name
   * @return
   */
  public static final String getValue(String _name) {
    return getValue("", _name);
  }
  /**
   * 根据name获取config节点下的属性值
   * @param _name
   * @return
   */
  public static final boolean getBooleanValue(String _name) {
    String _value = getValue("", _name);
    if (StringUtils.isNull(_value))
      return false;
    try {
      return Boolean.parseBoolean(_value); } catch (Exception e) {
    }
    return false;
  }
  /**
   * 根据name获取config节点下的属性值
   * @param _name
   * @return
   */
  public static final int getIntValue(String _name) {
    String _value = getValue("", _name);
    if (StringUtils.isNull(_value))
      return 0;
    try {
      return Integer.parseInt(_value); } catch (Exception e) {
    }
    return 0;
  }
  /**
   * 根据name获取config节点下的属性值
   * @param _name
   * @return
   */
  public static final float getFloatValue(String _name) {
    String _value = getValue("", _name);
    if (StringUtils.isNull(_value))
      return 0.0F;
    try {
      return Float.parseFloat(_value); } catch (Exception e) {
    }
    return 0.0F;
  }

  /**  获取config的子节点中的属性值
	 * @param _path 子节点路径，多个节点之间用/分隔
	 * @param _name
	 * @return
	 */
  public final static String getValue(String _path,String _name){
	if(configMap.isEmpty())throw new NestableRuntimeException("config.xml 没有被初始化，请在application.xml中初始化ConfigUtils对象或者调用ConfigUtils.init方法初始化。");
	try{
		_path=parsePathByPropName(_path,_name);
		logger.debug(_path);
		return configMap.get(_path);
	}catch(Exception e){
		e.printStackTrace();
		return null;
	}
  }

  private static final String parsePathByPropName(String _path, String _name) {
	if(StringUtils.isNull(_path))_path="/config";
	if(_path.indexOf("config") == -1){
		if(_path.startsWith("/")){
			_path="/config"+_path;
		}else{
			_path="/config/"+_path;
		}
	}else{
		if(!_path.startsWith("/")){
			_path="/"+_path ;
		}
	}
	if(_path.endsWith("/")){
		_path=_path+"property[@name="+_name+"]";
	}else{
		_path=_path+"/property[@name="+_name+"]";
	}
	return _path;
  }
  private static final String parsePath(String _path, String _name) {
    if (StringUtils.isNull(_path)) _path = "/config";
    if (_path.indexOf("config") < 0) {
      if (_path.startsWith("/"))
        _path = "/config" + _path;
      else {
        _path = "/config/" + _path;
      }
    }
    else if (!_path.startsWith("/")) {
      _path = "/" + _path;
    }

    if (_path.endsWith("/"))
      _path = _path + "property/" + _name;
    else {
      _path = _path + "/property/" + _name;
    }
    return _path;
  }
  /**
	 * 获得config节点下的属性文本
	 * @param _name
	 */
  public static final String getText(String _name) {
    return getText("", _name);
  }
  /**  获取config的子节点中的属性文本
	 * @param _path 子节点路径，多个节点之间用/分隔
	 * @param _name
	 * @return
	 */
  public static final String getText(String _path, String _name) {
	  if(configMap.isEmpty())throw new NestableRuntimeException("config.xml 没有被初始化，请在application.xml中初始化ConfigUtils对象或者调用ConfigUtils.init方法初始化。");
		try{
			_path=parsePath(_path,_name);
			logger.debug(_path);
			return configMap.get(_path);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
  }
  /**
   * 获取config子节点的configUtils实例
   */
  public static final ConfigUtils getConfigUtils(String _catalogName) {
    return new ConfigUtils(_catalogName);
  }
  /**
   * 获取子节点中的属性值
   * @param _path 子节点路径，多个节点之间用/分隔
   * @param _name
   * @return
   */
  public String getSubValue(String _path, String _name) {
	  if(configMap.isEmpty())throw new NestableRuntimeException("config.xml 没有被初始化，请在application.xml中初始化ConfigUtils对象或者调用ConfigUtils.init方法初始化。");
		try{
			if(StringUtils.isNull(_path)){
				_path=parsePathByPropName(catalogName,_name);
			}else{
				_path=parsePathByPropName(catalogName+"/"+_path,_name);
			}
			logger.debug(_path);
			return configMap.get(_path);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
  }
  /**
   * 获取子节点中的属性值
   * @param _name
   * @return
   */
  public String getSubValue(String _name) {
    return getSubValue(null, _name);
  }
  /**
   * 获取子节点中的属性文本
   * @param _path 子节点路径，多个节点之间用/分隔
   * @param _name
   * @return
   */
  public String getSubText(String _path, String _name) {
	if(configMap.isEmpty())throw new NestableRuntimeException("config.xml 没有被初始化，请在application.xml中初始化ConfigUtils对象或者调用ConfigUtils.init方法初始化。");
	try{
		if(StringUtils.isNull(_path)){
			_path=parsePath(catalogName,_name);
		}else{
			_path=parsePath(catalogName+"/"+_path,_name);
		}
		logger.debug(_path);
		return configMap.get(_path);
	}catch(Exception e){
		e.printStackTrace();
		return null;
	}
  }
  /**
   * 获取子节点中的属性文本
   * @param _name
   * @return
   */
  public String getSubText(String _name) {
    return getSubText("", _name);
  }
}
