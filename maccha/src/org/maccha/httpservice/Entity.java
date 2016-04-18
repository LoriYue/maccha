package org.maccha.httpservice;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import org.maccha.base.util.StringUtils;
import org.maccha.base.util.TypeConvertorUtils;
import org.maccha.httpservice.util.JsonUtils;
import org.maccha.httpservice.util.Xml2DataMessageUtilty;

public class Entity
  implements Serializable
{
  public static final String NENTITY = "entity";
  public static final String NPROPERTY = "property";
  public String id = null;

  private HashMap propertyMap = null;
  public static final String EDITSTATE = "editStatus";
  public static final String ADDEDSTATE = "insert";
  public static final String UPDATEDSTATE = "update";
  public static final String DELETEDSTATE = "delete";
  public static final String NONESTATE = "none";
  public String editState = "none";

  public Entity() { this.propertyMap = new HashMap(); }

  public Entity(HashMap _propertyMap) {
    this.propertyMap = _propertyMap;
  }
  public int size() {
    return this.propertyMap.size();
  }
  public Iterator keySet() {
    return this.propertyMap.keySet().iterator();
  }
  public HashMap getPropertyMap() {
    return this.propertyMap;
  }

  public void setPropertyValue(String propertyName, Object propertyValue) {
    this.propertyMap.put(propertyName, propertyValue);
  }
  public boolean isExist(String propertyName) {
    return this.propertyMap.containsKey(propertyName);
  }
  public String getEditState() {
    return this.editState;
  }

  public void setEditState(String _editState) {
    this.editState = _editState;
  }

  public Object getPropertyValue(String propertyName)
  {
    return this.propertyMap.get(propertyName);
  }
  public String getString(String propertyName) {
    Object objValue = this.propertyMap.get(propertyName);
    if (objValue != null) return (String)objValue;
    return null;
  }
  public Float getFloat(String propertyName) {
    Object objValue = this.propertyMap.get(propertyName);
    if (objValue != null) return (Float)TypeConvertorUtils.convert(objValue, Float.class);
    return Float.valueOf(0.0F);
  }
  public Integer getInteger(String propertyName) {
    Object objValue = this.propertyMap.get(propertyName);
    if (objValue != null) return (Integer)TypeConvertorUtils.convert(objValue, Integer.class);
    return Integer.valueOf(0);
  }
  public Double getDouble(String propertyName) {
    Object objValue = this.propertyMap.get(propertyName);
    if (objValue != null) return (Double)TypeConvertorUtils.convert(objValue, Double.class);
    return Double.valueOf(0.0D);
  }
  public Date getDate(String propertyName) {
    Object objValue = this.propertyMap.get(propertyName);
    if (objValue != null) return (Date)TypeConvertorUtils.convert(objValue, Date.class);
    return null;
  }
  public Boolean getBoolean(String propertyName) {
    Object objValue = this.propertyMap.get(propertyName);
    if (objValue != null) return (Boolean)TypeConvertorUtils.convert(objValue, Boolean.class);
    return null;
  }
  public String toJson() {
    return JsonUtils.parseMap2Json(this.propertyMap);
  }

  public String toXml() {
    StringBuffer xmlBuffer = new StringBuffer();
    xmlBuffer.append("<").append("entity").append(" ");
    xmlBuffer.append("editStatus").append("='").append(getEditState()).append("'");
    xmlBuffer.append(">");
    Object[] keys = this.propertyMap.keySet().toArray();
    for (int i = 0; i < keys.length; i++) {
      String strTemp = this.propertyMap.get(keys[i]) + "";
      if (StringUtils.isNull(strTemp)) strTemp = "";
      xmlBuffer.append("<").append("property").append(" name='").append(keys[i]).append("'>").append(Xml2DataMessageUtilty.CDATA(strTemp)).append("</").append("property").append(">");
    }

    xmlBuffer.append("</").append("entity").append(">");
    return xmlBuffer.toString();
  }
}
