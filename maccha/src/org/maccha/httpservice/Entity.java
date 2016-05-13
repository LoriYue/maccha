package org.maccha.httpservice;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import org.maccha.base.util.StringUtils;
import org.maccha.base.util.TypeConvertorUtils;
import org.maccha.httpservice.util.JsonUtils;
import org.maccha.httpservice.util.Xml2DataMessageUtilty;

public class Entity implements Serializable{
	
	public final static String NENTITY = "entity";

	public final static String NPROPERTY = "property";

	public String id = null;

	private HashMap propertyMap = null ;

	public final static String EDITSTATE = "editStatus" ;
	
	public final static String ADDEDSTATE = "insert" ;
	public final static String UPDATEDSTATE = "update" ;
	public final static String DELETEDSTATE = "delete" ;
	public final static String NONESTATE = "none" ;
	
	//对象编辑状态,状态包括：insert|update|delete|none,默认为没有任何修改
	public String editState = "none" ; 
	public Entity(){
		this.propertyMap = new HashMap() ;
	}
	public Entity(HashMap _propertyMap){
		this.propertyMap = _propertyMap;
	}
	public int size(){
		return this.propertyMap.size();
	}
	public Iterator keySet(){
		return this.propertyMap.keySet().iterator() ;
	}
	public HashMap getPropertyMap(){
		return (HashMap)this.propertyMap ;
	}
	// '根据属性名称设置实体的属性值
	public void setPropertyValue(String propertyName, Object propertyValue) {
		this.propertyMap.put(propertyName, propertyValue);
	}
	public boolean isExist(String propertyName){
		return this.propertyMap.containsKey(propertyName);
	}
	public String getEditState() {
		return editState;
	}

	public void setEditState(String _editState) {
		this.editState = _editState ;
	}

	// '根据属性名称获得实体的属性值
	public Object getPropertyValue(String propertyName) {
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
		return Float.valueOf(0.0f) ;
	}
	public Integer getInteger(String propertyName){
		Object objValue = propertyMap.get(propertyName);
		if(objValue != null) return (Integer)TypeConvertorUtils.convert(objValue, Integer.class);
		return Integer.valueOf(0) ;
	}
	public Double getDouble(String propertyName){
		Object objValue = propertyMap.get(propertyName);
		if(objValue != null) return (Double)TypeConvertorUtils.convert(objValue, Double.class);
		return Double.valueOf(0.0f) ;
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
	public String toJson() {
		return JsonUtils.parseMap2Json(propertyMap); 
	}
	//对象转换为xml文本
	public String toXml() {
		StringBuffer xmlBuffer = new StringBuffer();
		xmlBuffer.append("<" ).append( NENTITY ).append(" ");
		xmlBuffer.append(EDITSTATE).append("='").append(this.getEditState()).append("'");
		xmlBuffer.append( ">");
		Object keys[] = propertyMap.keySet().toArray();
		for (int i = 0; i < keys.length; i++) {
			String strTemp = propertyMap.get(keys[i]) + "";
			if (StringUtils.isNull(strTemp)) strTemp = "";
				xmlBuffer.append("<" ).append( NPROPERTY ).append( " name='" ).append( keys[i] ).append( "'>"
				).append(Xml2DataMessageUtilty.CDATA(strTemp)).append( "</" ).append( NPROPERTY ).append( ">");
		}
		xmlBuffer.append("</" ).append( NENTITY ).append( ">");
		return xmlBuffer.toString();
	}
}
