package org.maccha.httpservice;

import com.google.gson.Gson;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.maccha.base.util.ClassUtils;
import org.maccha.base.util.ObjectUtils;
import org.maccha.httpservice.util.JsonUtils;

public class DataSet implements Serializable{

	public final static String  NDATASET = "dataSet";
	public final static String  NMETADATA = "metadata";
	public final static String  NPROPERTY = "property";
	public String dataSetName = null;
	private HashMap metaDataMap = new HashMap();
	private ArrayList entityList = new ArrayList();
	private ArrayList excludePoPropertyArray = new ArrayList();
	private ArrayList includePoPropertyArray = new ArrayList();
	private HashMap hashProp = new HashMap();
	private Gson _gson = new Gson();
	public void setProperty(String key,Object val){
		hashProp.put(key,val);
	}
	public Object getProperty(String key){
		return hashProp.get(key);
	}	
	public DataSet exclude(Object[] fields) {
		for (int i = 0; i < fields.length; i++)
			excludePoPropertyArray.add(fields[i]);
		return this;
	}

	public DataSet include(Object[] fields) {
		for (int i = 0; i < fields.length; i++)
			includePoPropertyArray.add(fields[i]);
		return this;
	}
	/**
	 * 将Collection对象设置到dataSet中
	 * 
	 * @param conPo
	 */
	public void addCollectionEntity(java.util.Collection conPo) {
		Iterator itr = conPo.iterator();
		while (itr.hasNext()) {
			Map mapEntity = (Map)itr.next();
			try{
				Entity entity = this.parserMap2Entity(mapEntity);
				this.addEntity(entity);
			}catch(Exception ex){
			}
		}
	}
	/**
	 * 将Map对象数据解析到Entity对象
	 * 
	 * @param objPoEntity
	 * @return Entity
	 * @throws Exception
	 */
	private Entity parserMap2Entity(Map objMapEntity) throws Exception {
		Entity entity = new Entity();
		Iterator itr = objMapEntity.keySet().iterator();
		while(itr.hasNext()){
			String strPropName = (String)itr.next();
			//将多层对象路径转换为.
			//strPropName = strPropName.replaceAll("_", ".");
			if (excludePoPropertyArray.contains(strPropName))continue;
			if (!includePoPropertyArray.isEmpty() && !includePoPropertyArray.contains(strPropName))continue;
			Object objPropValue = objMapEntity.get(strPropName);
			//System.out.println("into parserMap2Entity strPropName = "+strPropName+",objPropValue ="+objPropValue.getClass().getName());
			entity.setPropertyValue(strPropName, objPropValue);
		}
		return entity;
	}
	/**
	 * 将PO对象设置到dataSet中
	 * 
	 * @param conPo
	 */
	public void addPoEntity(java.util.Collection conPo) {
		Iterator itr = conPo.iterator();
		while (itr.hasNext())this.addPoEntity(itr.next());
	}

	/**
	 * 将PO对象设置到dataSet中
	 * 
	 * @param conPo
	 */
	public void addPoEntity(Object objPoEntity) {
		try {
			Entity entity = parserPo2Entity(objPoEntity);
			this.addEntity(entity);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	/**
	 * 将PO对象数据解析到Entity对象
	 * 
	 * @param objPoEntity
	 * @return Entity
	 * @throws Exception
	 */
	private Entity parserPo2Entity(Object objPoEntity) throws Exception {
		return parserPo2Entity(objPoEntity,null);
	}
	/**
	 * 将PO对象数据解析到Entity对象
	 * 
	 * @param objPoEntity
	 * @return Entity
	 * @throws Exception
	 */
	private Entity parserPo2Entity(Object objPoEntity,List listFields) throws Exception {
		Entity entity = new Entity();
		if(listFields == null || listFields.isEmpty()){
			listFields = ObjectUtils.getFields(objPoEntity);
		}
		int intMetadataCount = 0;
		for (int i = 0; i < listFields.size(); i++) {
			String strPropName = (String)listFields.get(i);
			if (!"class".equals(strPropName)) {
				if (excludePoPropertyArray.contains(strPropName))continue;
				if (!includePoPropertyArray.isEmpty() && !includePoPropertyArray.contains(strPropName))continue;
				Object objPropValue = ClassUtils.get(objPoEntity,strPropName);
				entity.setPropertyValue(strPropName, objPropValue);
			}
		}
		return entity;
	}

	// '增加数据实体信息
	public void addEntity(Entity entity) {
		entityList.add(entity);
	}

	// '增加元数据信息
	public void addMetadata(String metadataName, String metadataType) {
		HashMap metaDataMap_ = new HashMap();
		metaDataMap_.put("name", metadataName);
		metaDataMap_.put("type", metadataType);
		metaDataMap.put(metadataName, metaDataMap_);
	}

	public void addMetadata(String metadataName, Map metadataProperty) {
		metaDataMap.put(metadataName, metadataProperty);
	}
	public HashMap getMetadata(int index) {
		Object objMetaData = metaDataMap.get(index+"");
		HashMap hashMetaData = new HashMap(); 
		if(objMetaData != null)hashMetaData = (HashMap)objMetaData ;	
		return hashMetaData ;
	}
	public int getMetadataCount(){
		return metaDataMap.size() ;
	}
	public HashMap[] getMetadata() {
		Object[] objMetaData = metaDataMap.entrySet().toArray() ;
		return (HashMap[])objMetaData ;
	}
	public Entity getEntity(int index) {
		return (Entity) entityList.get(index);
	}

	public int getEntityCount() {
		return entityList.size();
	}
	
	public String toJson(){
		StringBuffer buff = new StringBuffer("{");
		buff.append("\"name\":").append(_gson.toJson(this.dataSetName)).append(",\n");
		//解析Metadata信息
		buff.append("\""+NMETADATA+"\"").append(":[") ;
		Iterator itrMetaDataMap = metaDataMap.keySet().iterator();
		String str = null ;
		int intIndex = 0 ;
		while(itrMetaDataMap.hasNext()){
			if( intIndex > 0 )buff.append( ",\n" );
			Object objKey = itrMetaDataMap.next();
			HashMap metadataProperty = (HashMap) metaDataMap.get(objKey);
			str = JsonUtils.parseMap2Json(metadataProperty);
			if(str != null)buff.append(str) ;
			str = null ;
			intIndex++;
		}
		buff.append("],\n");
		//解析Entity信息
		buff.append("\"entityList\"").append(":[") ;
		for (int i = 0; i < entityList.size(); i++) {			
			str = ((Entity)entityList.get(i)).toJson();			
			if(str != null){
				if( i > 0 )buff.append( ",\n" );
				buff.append(str) ;
			}
			str = null ;
		}
		buff.append(']');
		buff.append("}");
		return buff.toString();
	}
	// '对象转换为xml文本
	public String toXml() {
		StringBuffer xmlBuffer = new StringBuffer();
		xmlBuffer.append("<").append( NDATASET).append(" name='" ).append(dataSetName).append("' ");
		//添加属性
		Iterator itrPropKey = hashProp.keySet().iterator();
		while(itrPropKey.hasNext()){
			String strKey = (String)itrPropKey.next();
			xmlBuffer.append(strKey).append("='").append((String)hashProp.get(strKey)).append("' ");
		}
		xmlBuffer.append( ">");
		xmlBuffer.append("<" ).append(NMETADATA ).append(">");
		Object[] keys = metaDataMap.keySet().toArray();

		for (int i = 0; i < metaDataMap.size(); i++) {

			HashMap metadataProperty = (HashMap) metaDataMap.get(keys[i]);

			Object[] keys_ = metadataProperty.keySet().toArray();

			String tmpStr = "";
			for (int k = 0; k < metadataProperty.size(); k++) {
				tmpStr = tmpStr + " " + keys_[k] + "='"
						+ metadataProperty.get(keys_[k]) + "'";
			}
			xmlBuffer.append("<").append( NPROPERTY ).append( " " ).append( tmpStr ).append( "/>");
			tmpStr = "";
		}
		xmlBuffer.append("</" ).append( NMETADATA ).append( ">");
		for (int i = 0; i < entityList.size(); i++) {
			xmlBuffer.append(((Entity) entityList.get(i)).toXml());
		}
		xmlBuffer.append("</" ).append( NDATASET ).append( ">");
		return xmlBuffer.toString();
	}

}
