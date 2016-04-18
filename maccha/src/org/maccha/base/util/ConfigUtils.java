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

  public static String getConfigFileClassPath() {
    return configFileClassPath;
  }
  public static void setConfigFileClassPath(String configFileClassPath) {
    configFileClassPath = configFileClassPath;
  }

  public static final String getValue(String _name)
  {
    return getValue("", _name);
  }

  public static final boolean getBooleanValue(String _name) {
    String _value = getValue("", _name);
    if (StringUtils.isNull(_value))
      return false;
    try {
      return Boolean.parseBoolean(_value); } catch (Exception e) {
    }
    return false;
  }

  public static final int getIntValue(String _name)
  {
    String _value = getValue("", _name);
    if (StringUtils.isNull(_value))
      return 0;
    try {
      return Integer.parseInt(_value); } catch (Exception e) {
    }
    return 0;
  }

  public static final float getFloatValue(String _name)
  {
    String _value = getValue("", _name);
    if (StringUtils.isNull(_value))
      return 0.0F;
    try {
      return Float.parseFloat(_value); } catch (Exception e) {
    }
    return 0.0F;
  }

  public static final String getValue(String _path, String _name)
  {
    if (configMap.isEmpty()) throw new NestableRuntimeException("config.xml 没有被初始化，请在application.xml中初始化ConfigUtils对象或者调用ConfigUtils.init方法初始化。"); try
    {
      _path = parsePathByPropName(_path, _name);
      logger.debug(_path);
      return (String)configMap.get(_path);
    } catch (Exception e) {
      e.printStackTrace();
    }return null;
  }

  private static final String parsePathByPropName(String _path, String _name) {
    if (StringUtils.isNull(_path)) _path = "/config";
    if (_path.indexOf("config") == -1) {
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
      _path = _path + "property[@name=" + _name + "]";
    else {
      _path = _path + "/property[@name=" + _name + "]";
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
  public static final String getText(String _name) {
    return getText("", _name);
  }

  public static final String getText(String _path, String _name)
  {
    if (configMap.isEmpty()) throw new NestableRuntimeException("config.xml 没有被初始化，请在application.xml中初始化ConfigUtils对象或者调用ConfigUtils.init方法初始化。"); try
    {
      _path = parsePath(_path, _name);
      logger.debug(_path);
      return (String)configMap.get(_path);
    } catch (Exception e) {
      e.printStackTrace();
    }return null;
  }

  public static final ConfigUtils getConfigUtils(String _catalogName) {
    return new ConfigUtils(_catalogName);
  }

  public String getSubValue(String _path, String _name) {
    if (configMap.isEmpty()) throw new NestableRuntimeException("config.xml 没有被初始化，请在application.xml中初始化ConfigUtils对象或者调用ConfigUtils.init方法初始化。"); try
    {
      if (StringUtils.isNull(_path))
        _path = parsePathByPropName(this.catalogName, _name);
      else {
        _path = parsePathByPropName(this.catalogName + "/" + _path, _name);
      }
      logger.debug(_path);
      return (String)configMap.get(_path);
    } catch (Exception e) {
      e.printStackTrace();
    }return null;
  }

  public String getSubValue(String _name)
  {
    return getSubValue(null, _name);
  }

  public String getSubText(String _path, String _name) {
    if (configMap.isEmpty()) throw new NestableRuntimeException("config.xml 没有被初始化，请在application.xml中初始化ConfigUtils对象或者调用ConfigUtils.init方法初始化。"); try
    {
      if (StringUtils.isNull(_path))
        _path = parsePath(this.catalogName, _name);
      else {
        _path = parsePath(this.catalogName + "/" + _path, _name);
      }
      logger.debug(_path);
      return (String)configMap.get(_path);
    } catch (Exception e) {
      e.printStackTrace();
    }return null;
  }

  public String getSubText(String _name) {
    return getSubText("", _name);
  }
}
