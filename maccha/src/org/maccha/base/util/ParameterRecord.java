package org.maccha.base.util;

import java.util.Date;
import java.util.HashMap;

public class ParameterRecord {
	private HashMap propertyMap = null ; 
	public ParameterRecord(){
		this.propertyMap = new HashMap();
	}
	public ParameterRecord(HashMap propertyMap){
		this.propertyMap = propertyMap ;
	}
	// '根据属性名称获得实体的属性值
	public Object getValue(String propertyName) {
		return propertyMap.get(propertyName);
	}
	public String getString(String propertyName){
		Object objValue = propertyMap.get(propertyName);
		if(objValue != null) return (String)objValue ;
		return null ;
	}
	public Float getFloat(String propertyName){
		Object objValue = propertyMap.get(propertyName);
		if(objValue != null) return (Float)TypeConvertorUtils.convert(objValue, Float.class);
		return null ;
	}
	public Integer getInteger(String propertyName){
		Object objValue = propertyMap.get(propertyName);
		if(objValue != null) return (Integer)TypeConvertorUtils.convert(objValue, Integer.class);
		return null ;
	}
	public Double getDouble(String propertyName){
		Object objValue = propertyMap.get(propertyName);
		if(objValue != null) return (Double)TypeConvertorUtils.convert(objValue, Double.class);
		return null ;
	}
	public Date getDate(String propertyName){
		Object objValue = propertyMap.get(propertyName);
		if(objValue != null) return (Date)TypeConvertorUtils.convert(objValue, Date.class);
		return null ;
	}
	public Boolean getBoolean(String propertyName){
		Object objValue = propertyMap.get(propertyName);
		if(objValue != null) return (Boolean)TypeConvertorUtils.convert(objValue, Boolean.class);
		return null ;
	}
}
