package org.maccha.httpservice;

import com.google.gson.Gson;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.maccha.base.util.ClassUtils;
import org.maccha.base.util.ObjectUtils;
import org.maccha.httpservice.util.JsonUtils;

public class DataSet
  implements Serializable
{
  public static final String NDATASET = "dataSet";
  public static final String NMETADATA = "metadata";
  public static final String NPROPERTY = "property";
  public String dataSetName = null;

  private HashMap metaDataMap = new HashMap();

  private ArrayList entityList = new ArrayList();

  private ArrayList excludePoPropertyArray = new ArrayList();

  private ArrayList includePoPropertyArray = new ArrayList();

  private HashMap hashProp = new HashMap();

  private Gson _gson = new Gson();

  public void setProperty(String key, Object val) { this.hashProp.put(key, val); }

  public Object getProperty(String key) {
    return this.hashProp.get(key);
  }
  public DataSet exclude(Object[] fields) {
    for (int i = 0; i < fields.length; i++)
      this.excludePoPropertyArray.add(fields[i]);
    return this;
  }

  public DataSet include(Object[] fields) {
    for (int i = 0; i < fields.length; i++)
      this.includePoPropertyArray.add(fields[i]);
    return this;
  }

  public void addCollectionEntity(Collection conPo)
  {
    Iterator itr = conPo.iterator();
    while (itr.hasNext()) {
      Map mapEntity = (Map)itr.next();
      try {
        Entity entity = parserMap2Entity(mapEntity);
        addEntity(entity);
      }
      catch (Exception ex)
      {
      }
    }
  }

  private Entity parserMap2Entity(Map objMapEntity)
    throws Exception
  {
    Entity entity = new Entity();
    Iterator itr = objMapEntity.keySet().iterator();
    while (itr.hasNext()) {
      String strPropName = (String)itr.next();

      if ((this.excludePoPropertyArray.contains(strPropName)) || (
        (!this.includePoPropertyArray.isEmpty()) && (!this.includePoPropertyArray.contains(strPropName)))) continue;
      Object objPropValue = objMapEntity.get(strPropName);

      entity.setPropertyValue(strPropName, objPropValue);
    }
    return entity;
  }

  public void addPoEntity(Collection conPo)
  {
    Iterator itr = conPo.iterator();
    while (itr.hasNext()) addPoEntity(itr.next());
  }

  public void addPoEntity(Object objPoEntity)
  {
    try
    {
      Entity entity = parserPo2Entity(objPoEntity);
      addEntity(entity);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private Entity parserPo2Entity(Object objPoEntity)
    throws Exception
  {
    return parserPo2Entity(objPoEntity, null);
  }

  private Entity parserPo2Entity(Object objPoEntity, List listFields)
    throws Exception
  {
    Entity entity = new Entity();
    if ((listFields == null) || (listFields.isEmpty())) {
      listFields = ObjectUtils.getFields(objPoEntity);
    }
    int intMetadataCount = 0;
    for (int i = 0; i < listFields.size(); i++) {
      String strPropName = (String)listFields.get(i);
      if (("class".equals(strPropName)) || 
        (this.excludePoPropertyArray.contains(strPropName)) || (
        (!this.includePoPropertyArray.isEmpty()) && (!this.includePoPropertyArray.contains(strPropName)))) continue;
      Object objPropValue = ClassUtils.get(objPoEntity, strPropName);
      entity.setPropertyValue(strPropName, objPropValue);
    }

    return entity;
  }

  public void addEntity(Entity entity)
  {
    this.entityList.add(entity);
  }

  public void addMetadata(String metadataName, String metadataType)
  {
    HashMap metaDataMap_ = new HashMap();
    metaDataMap_.put("name", metadataName);
    metaDataMap_.put("type", metadataType);
    this.metaDataMap.put(metadataName, metaDataMap_);
  }

  public void addMetadata(String metadataName, Map metadataProperty) {
    this.metaDataMap.put(metadataName, metadataProperty);
  }
  public HashMap getMetadata(int index) {
    Object objMetaData = this.metaDataMap.get(index + "");
    HashMap hashMetaData = new HashMap();
    if (objMetaData != null) hashMetaData = (HashMap)objMetaData;
    return hashMetaData;
  }
  public int getMetadataCount() {
    return this.metaDataMap.size();
  }
  public HashMap[] getMetadata() {
    Object[] objMetaData = this.metaDataMap.entrySet().toArray();
    return (HashMap[])(HashMap[])objMetaData;
  }
  public Entity getEntity(int index) {
    return (Entity)this.entityList.get(index);
  }

  public int getEntityCount() {
    return this.entityList.size();
  }

  public String toJson() {
    StringBuffer buff = new StringBuffer("{");
    buff.append("\"name\":").append(this._gson.toJson(this.dataSetName)).append(",\n");

    buff.append("\"metadata\"").append(":[");
    Iterator itrMetaDataMap = this.metaDataMap.keySet().iterator();
    String str = null;
    int intIndex = 0;
    while (itrMetaDataMap.hasNext()) {
      if (intIndex > 0) buff.append(",\n");
      Object objKey = itrMetaDataMap.next();
      HashMap metadataProperty = (HashMap)this.metaDataMap.get(objKey);
      str = JsonUtils.parseMap2Json(metadataProperty);
      if (str != null) buff.append(str);
      str = null;
      intIndex++;
    }
    buff.append("],\n");

    buff.append("\"entityList\"").append(":[");
    for (int i = 0; i < this.entityList.size(); i++) {
      str = ((Entity)this.entityList.get(i)).toJson();
      if (str != null) {
        if (i > 0) buff.append(",\n");
        buff.append(str);
      }
      str = null;
    }
    buff.append(']');
    buff.append("}");
    return buff.toString();
  }

  public String toXml()
  {
    StringBuffer xmlBuffer = new StringBuffer();
    xmlBuffer.append("<").append("dataSet").append(" name='").append(this.dataSetName).append("' ");

    Iterator itrPropKey = this.hashProp.keySet().iterator();
    while (itrPropKey.hasNext()) {
      String strKey = (String)itrPropKey.next();
      xmlBuffer.append(strKey).append("='").append((String)this.hashProp.get(strKey)).append("' ");
    }
    xmlBuffer.append(">");
    xmlBuffer.append("<").append("metadata").append(">");
    Object[] keys = this.metaDataMap.keySet().toArray();

    for (int i = 0; i < this.metaDataMap.size(); i++)
    {
      HashMap metadataProperty = (HashMap)this.metaDataMap.get(keys[i]);

      Object[] keys_ = metadataProperty.keySet().toArray();

      String tmpStr = "";
      for (int k = 0; k < metadataProperty.size(); k++) {
        tmpStr = tmpStr + " " + keys_[k] + "='" + metadataProperty.get(keys_[k]) + "'";
      }

      xmlBuffer.append("<").append("property").append(" ").append(tmpStr).append("/>");
      tmpStr = "";
    }
    xmlBuffer.append("</").append("metadata").append(">");
    for (int i = 0; i < this.entityList.size(); i++) {
      xmlBuffer.append(((Entity)this.entityList.get(i)).toXml());
    }

    xmlBuffer.append("</").append("dataSet").append(">");
    return xmlBuffer.toString();
  }
}
