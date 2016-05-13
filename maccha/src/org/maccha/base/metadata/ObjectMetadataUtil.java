package org.maccha.base.metadata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.Formula;
import org.hibernate.mapping.MetaAttribute;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.maccha.base.util.StringUtils;
import org.maccha.spring.SpringManager;
import org.maccha.base.metadata.ObjectMetadata;
import org.maccha.base.metadata.ObjectMetadataUtil;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;

public class ObjectMetadataUtil
{
  public static final String CLASS_IMPLEMENTS = "class-implements";
  public static final String CLASS_DESCRIPTION = "class-description";
  public static final String CLASS_GB2312_NAME = "class-gb2312-name";
  public static final String CLASS_GLOBALDAP = "class-globaldap";
  public static final String CLASS_GLOBALDAPTYPE = "class-globaldap-type";
  public static final String PROPERTY_GLOBALDAP = "property-globaldap";
  public static final String PROPERTY_DESCRIPTION = "property-description";
  public static final String PROPERTY_GB2312_NAME = "property-gb2312-name";
  public static final String PROPERTY_RBAC_VISIBLE = "property-rbac-visible";
  public static final String PROPERTY_DISCOVER_VISIBLE = "property-discover-visible";
  public static final String PROPERTY_DISCOVER_INDEX = "property-discover-index";
  public static final String PROPERTY_DISCOVER_WIDTH = "property-discover-width";
  public static final String PROPERTY_DISCOVER_HIDDEN = "property-discover-hidden";
  public static final String PROPERTY_DISCOVER_EXPRESSION = "property-discover-expression";
  public static final String PROPERTY_UI_POMANAGER_VISIBLE = "property-ui-pomanager-visible";
  public static final String PROPERTY_UI_POMANAGER_EDITABLE = "property-ui-pomanager-editable";
  public static final String PROPERTY_UI_POMANAGER_WIDTH = "property-ui-pomanager-width";
  public static final String PROPERTY_UI_POMANAGER_INDEX = "property-ui-pomanager-index";
  public static final String PROPERTY_UI_POMANAGER_PARAM = "property-ui-pomanager-param";
  public static final String PROPERTY_UI_POMANAGER_SEARCHABLE = "property-ui-pomanager-searchable";
  public static final String PROPERTY_UI_POMANAGER_EXPORTABLE = "property-ui-pomanager-exportable";
  public static final String PROPERTY_UI_POMANAGER_TITLENAME = "property-ui-pomanager-titlename";
  public static final String PROPERTY_UI_POMANAGER_SHOWTITLENAME = "property-ui-pomanager-showtitlename";
  public static final String PROPERTY_UI_POMANAGER_EDITATTRIBUTE = "property-ui-pomanager-editattribute";
  public static final String PROPERTY_UI_POMANAGER_EDITTYPE = "property-ui-pomanager-edittype";
  public static final String PROPERTY_IS_SECURITYABLE = "property-is-securityable";
  public static final String PROPERTY_GLOBALDAPTYPE = "property-globaldap-type";
  
  	/**
  	 * 获取Hibernate Configuration
  	 * @return
  	 */
	public static Configuration getConfiguration() {
		LocalSessionFactoryBean sessionFactoryBean = (LocalSessionFactoryBean) SpringManager
				.getComponent("&sessionFactory");
		Configuration conf = sessionFactoryBean.getConfiguration();
		return conf;
	}
	/**
	 * 
	 * @return
	 */
	public static List getEntityMetadata() {
		java.util.Iterator itr = getConfiguration().getClassMappings();
		List listEntityMetadata = new ArrayList();
		while (itr.hasNext()) {
			PersistentClass persistentClass = (PersistentClass) itr.next();
			listEntityMetadata.add(getEntityMetadata(persistentClass));
		}
		return listEntityMetadata;
	}
	/**
	 * 获得实体属性Metadata数据
	 * 
	 * @param entityName
	 *            实体对象class名
	 * @return ObjectMetadata
	 */
	public static ObjectMetadata getEntityMetadata(String entityName) {
		PersistentClass persistentClass = getConfiguration().getClassMapping(entityName);
		return getEntityMetadata(persistentClass);
	}
	/**
	 * 获得实体属性Metadata数据
	 * 
	 * @param entityName
	 *            实体对象class名
	 * @return ObjectMetadata
	 */
	private static ObjectMetadata getEntityMetadata(PersistentClass persistentClass) {
		ObjectMetadata propMetadata = new ObjectMetadata();
		String strSubClassHead = "" ;
		//如果对象为子类则取需要通过类的前缀作为Metadata信息
		if(persistentClass instanceof org.hibernate.mapping.SingleTableSubclass){
	//		System.out.println("EntityName:::::::::::::::"+persistentClass.getClassName());
			strSubClassHead = StringUtils.unqualify(persistentClass.getClassName(),'.')+"-";
		}
	//	MetaAttribute metaGBAttr = persistentClass.getMetaAttribute((strSubClassHead+ObjectMetadataUtil.CLASS_GB2312_NAME).trim());
		MetaAttribute metaGBAttr = persistentClass.getMetaAttribute((ObjectMetadataUtil.CLASS_GB2312_NAME).trim());
		MetaAttribute metaDescAttr = persistentClass.getMetaAttribute((ObjectMetadataUtil.CLASS_DESCRIPTION).trim());
		MetaAttribute metaImplementsAttr = persistentClass.getMetaAttribute(ObjectMetadataUtil.CLASS_IMPLEMENTS);
		MetaAttribute metaGlobalDapAttr = persistentClass.getMetaAttribute(ObjectMetadataUtil.CLASS_GLOBALDAP);
		propMetadata.setEntityName(persistentClass.getClassName());
		propMetadata.setTableName(persistentClass.getTable().getName());
	    propMetadata.setGbName(getMetadataString(metaGBAttr));
		propMetadata.setDescription(getMetadataString(metaDescAttr));
	    propMetadata.setEntityImplements(getMetadataString(metaImplementsAttr));
		propMetadata.setIsGlobalPO(getMetadataString(metaGlobalDapAttr));
	//	MetaAttribute metaGlobalDapTypeAttr = persistentClass.getMetaAttribute(ObjectMetadataUtil.CLASS_GLOBALDAPTYPE);
	//	if (metaGlobalDapTypeAttr != null) {
	//		propMetadata.setGlobalPOType(metaGlobalDapTypeAttr.getValue());
	//	}
		return propMetadata;
	}
	private static String getMetadataString(MetaAttribute metaAttr){
		String str=null;
		if(metaAttr!=null){
			List list=metaAttr.getValues();
			if(list.size()>0){
				str=(String)list.get(list.size()-1);
			}
		}
		return str;
	}
	/**
	 * 获得实体属性Metadata数据
	 * 
	 * @param entityName 实体对象class名
	 * @return Map key:property name ,value :{@link com.icitic.util.PropertyMetadata}
	 */
	public static Map getEntityPropertyMetadata(String entityName) {
		PersistentClass persistentClass = getConfiguration().getClassMapping(entityName);
		if (persistentClass == null)return null;
		Map hashPropMetadata = new HashMap();
		// 处理主键
		Property propKey = persistentClass.getIdentifierProperty();
		String propKeyName = propKey.getName();
		PropertyMetadata propKeyMetadata = createPropertyMetadataFromHbmProperty(propKey);
		hashPropMetadata.put(propKeyName, propKeyMetadata);
		// 处理其他键值
		getEntityPropertyMetadata(persistentClass,hashPropMetadata);
		return hashPropMetadata;
	}
	private static void getEntityPropertyMetadata(PersistentClass persistentClass, Map mapPropMetadata) {		
		Iterator itr = persistentClass.getPropertyIterator();
		while (itr.hasNext()) {
			Property prop = (Property) itr.next();
			String propName = prop.getName();
			PropertyMetadata propMetadata = createPropertyMetadataFromHbmProperty(prop);
			mapPropMetadata.put(propName, propMetadata);
		}
		PersistentClass superPersistentClass = persistentClass.getSuperclass();
		if (superPersistentClass != null)
			getEntityPropertyMetadata(superPersistentClass,mapPropMetadata);		
	}
	private static PropertyMetadata createPropertyMetadataFromHbmProperty(Property prop) {
		int intDiscoverWidth = 0;
		int intDiscoverIndex = 0;
		PropertyMetadata propMetadata = new PropertyMetadata();
		String propName = prop.getName();
		String colName = null;
		
		String propertyType = prop.getType().getName();
		Iterator itrColumn = prop.getColumnIterator();
		if (itrColumn.hasNext()) {
			Object obj = itrColumn.next();
			if (obj instanceof Column) {
				Column column = (Column) obj;
				colName = column.getName();
			} else if (obj instanceof Formula) {
				Formula column = (Formula) obj;
				colName = propName;
			}
		}
		propMetadata.setPropertyRelation("none");
		org.hibernate.mapping.Value propValue = prop.getValue();
		//org.hibernate.type.Type type = prop.getType();
		
		if (propValue instanceof org.hibernate.mapping.OneToOne){
			propMetadata.setPropertyType(((org.hibernate.mapping.OneToOne)propValue).getReferencedEntityName());
			propMetadata.setPropertyRelation(propMetadata.RELATION_MANY_TO_ONE);
			propMetadata.setEntityType(true);
		}else if (propValue instanceof org.hibernate.mapping.ManyToOne){
			propMetadata.setPropertyType(((org.hibernate.mapping.ManyToOne)propValue).getReferencedEntityName());
			propMetadata.setPropertyRelation(propMetadata.RELATION_MANY_TO_ONE);
			propMetadata.setEntityType(true);
		}else if (propValue instanceof org.hibernate.mapping.Set){
			org.hibernate.mapping.Set setObj = (org.hibernate.mapping.Set)propValue ;
			Object objElement = ((org.hibernate.mapping.Set)propValue).getElement();
			if(objElement instanceof org.hibernate.mapping.OneToMany){
				org.hibernate.mapping.OneToMany valueMany = (org.hibernate.mapping.OneToMany)objElement;
				propMetadata.setPropertyType(valueMany.getReferencedEntityName());
			}else if(objElement instanceof org.hibernate.mapping.ManyToOne){
				propMetadata.isCollectionType=true;
				org.hibernate.mapping.ManyToOne valueMany = (org.hibernate.mapping.ManyToOne)objElement;
				propMetadata.setPropertyType(valueMany.getReferencedEntityName());
				propMetadata.collectionKeyColumnName=setObj.getCollectionTable().getColumn(0).getName();
				propMetadata.collectionForeignColumnName=setObj.getCollectionTable().getColumn(2).getName();
	//			System.out.println(setObj.getCollectionTable().getColumn(0).getName());
	//			System.out.println(setObj.getCollectionTable().getColumn(2).getName());
				propMetadata.collectionTableName=valueMany.getTable().getName();
				
			}
			propMetadata.setPropertyRelation(propMetadata.RELATION_ONE_TO_MANY);			
		}else{
			propMetadata.setPropertyType(propertyType);
		}
	
		org.hibernate.type.Type type = prop.getType();
		if (type instanceof org.hibernate.type.AnyType) {
			propMetadata.setEntityType(true);
		}
		propMetadata.setColumnName(colName);
		propMetadata.setPropertyName(propName);
		propMetadata.setGbName(getMeteAttribute(prop,ObjectMetadataUtil.PROPERTY_GB2312_NAME));
		propMetadata.setDescription(getMeteAttribute(prop,ObjectMetadataUtil.PROPERTY_DESCRIPTION));
		propMetadata.setRbacVisible(getMeteAttribute(prop,ObjectMetadataUtil.PROPERTY_RBAC_VISIBLE));
		propMetadata.setUiPomanagerEditable(getMeteAttribute(prop,ObjectMetadataUtil.PROPERTY_UI_POMANAGER_EDITABLE));
		propMetadata.setUiPomanagerIndex(getMeteAttribute(prop,ObjectMetadataUtil.PROPERTY_UI_POMANAGER_INDEX));		
		propMetadata.setUiPomanagerTitlename(getMeteAttribute(prop,ObjectMetadataUtil.PROPERTY_UI_POMANAGER_TITLENAME));
		propMetadata.setUiPomanagerShowTitlename(getMeteAttribute(prop, ObjectMetadataUtil.PROPERTY_UI_POMANAGER_SHOWTITLENAME));
		propMetadata.setUiPomanagerEdittype(getMeteAttribute(prop,ObjectMetadataUtil.PROPERTY_UI_POMANAGER_EDITTYPE));
		propMetadata.setUiPomanagerEditattribute(getMeteAttribute(prop,ObjectMetadataUtil.PROPERTY_UI_POMANAGER_EDITATTRIBUTE));
		propMetadata.setUiPomanagerVisible(getMeteAttribute(prop,ObjectMetadataUtil.PROPERTY_UI_POMANAGER_VISIBLE));
		propMetadata.setUiPomanagerWidth(getMeteAttribute(prop,ObjectMetadataUtil.PROPERTY_UI_POMANAGER_WIDTH));
		propMetadata.setUiPomanagerParam(getMeteAttribute(prop,ObjectMetadataUtil.PROPERTY_UI_POMANAGER_PARAM));
		propMetadata.setUiPomanagerSearchable(getMeteAttribute(prop,ObjectMetadataUtil.PROPERTY_UI_POMANAGER_SEARCHABLE));
		propMetadata.setUiPomanagerExportable(getMeteAttribute(prop,ObjectMetadataUtil.PROPERTY_UI_POMANAGER_EXPORTABLE));
		propMetadata.setDiscoverExpression(getMeteAttribute(prop,ObjectMetadataUtil.PROPERTY_DISCOVER_EXPRESSION));
		propMetadata.setDiscoverVisible(getMeteAttribute(prop,ObjectMetadataUtil.PROPERTY_DISCOVER_VISIBLE));
		propMetadata.setDiscoverIndex(getMeteAttribute(prop,ObjectMetadataUtil.PROPERTY_DISCOVER_INDEX));
		propMetadata.setDiscoverWidth(getMeteAttribute(prop,ObjectMetadataUtil.PROPERTY_DISCOVER_WIDTH));
		propMetadata.setDiscoverHidden(getMeteAttribute(prop,ObjectMetadataUtil.PROPERTY_DISCOVER_HIDDEN));
		propMetadata.setIsGlobalProperty(getMeteAttribute(prop,ObjectMetadataUtil.PROPERTY_GLOBALDAP));
		propMetadata.setGlobalPOType(getMeteAttribute(prop,ObjectMetadataUtil.PROPERTY_GLOBALDAPTYPE));
		propMetadata.setIsSecurityable(getMeteAttribute(prop,ObjectMetadataUtil.PROPERTY_IS_SECURITYABLE));
		return propMetadata;
	}
	
	/**
	 * 获得实体属性Metadata数据
	 * 
	 * @param entityName 实体对象class名
	 * @return Map key:property name ,value :{@link com.icitic.util.PropertyMetadata}
	 */
	public static List getEntityPropertyMetadataList(String entityName) {
		PersistentClass persistentClass = getConfiguration().getClassMapping(entityName);		
		List listPropMetadata = new ArrayList();
		// 处理主键
		Property propKey = persistentClass.getIdentifierProperty();
		String propKeyName = propKey.getName();
		PropertyMetadata propKeyMetadata = createPropertyMetadataFromHbmProperty(propKey);
		listPropMetadata.add(propKeyMetadata);
		//处理其他键值
		getEntityPropertyMetadataInfo(persistentClass, listPropMetadata);
		return listPropMetadata;
	}
	
	private static void getEntityPropertyMetadataInfo(PersistentClass persistentClass, List listPropMetadata) {		
		Iterator itr = persistentClass.getPropertyIterator();
		while (itr.hasNext()) {
			Property prop = (Property) itr.next();
			PropertyMetadata propMetadata = createPropertyMetadataFromHbmProperty(prop);
			listPropMetadata.add(propMetadata);
		}
		PersistentClass superPersistentClass = persistentClass.getSuperclass();
		if (superPersistentClass != null)
			getEntityPropertyMetadataInfo(superPersistentClass,listPropMetadata);
	}
	
	private static String getMeteAttribute(Property prop, String metaName) {
		String strRetrun = null;
		// LogUtils.info(":::::::::::::::::metaName = " + metaName);
		//System.out.println(":::::::::::::::::metaName = " + metaName);
		MetaAttribute metaAttr = prop.getMetaAttribute(metaName);
		if (metaAttr == null)return strRetrun;
		strRetrun = metaAttr.getValue();
		return strRetrun;
	}
}
