package org.maccha.base.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class PropertyFileUtils {
	/**
	 * 通过文件路径loadpropertis文件
	 * @param propFileName
	 * @return
	 */
	public static PropertiesConfiguration loadPropertyFile(String propFilePathName)throws ConfigurationException{
		PropertiesConfiguration conf = null ;
		try{
			File filename = new File(propFilePathName);
			conf = new PropertiesConfiguration(filename);
		}catch(ConfigurationException ex){
			throw ex ;
		}catch(Exception ex){
			throw new ConfigurationException(ex.getCause());
		}
		return conf ;
	}
	/**
	 * 通过文件名（在classpath 下的*.properties） 读取配置信息
	 * @param propertiesFilename
	 * @return
	 * @throws IOException
	 */
	public static Properties  loadProp(String propertiesFilename) throws IOException {
		URL u = PropertyFileUtils.class.getClassLoader().getResource(
				propertiesFilename);
		
		Properties props=new Properties();
		File file = null;
		try {
			file = new File(u.toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		InputStream in = new FileInputStream(file);
		props.load(in);
		return props;

	}
}
