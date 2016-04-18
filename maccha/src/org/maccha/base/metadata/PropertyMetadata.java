package org.maccha.base.metadata;

/**
 * Created by Lori Yue on 16-3-4.
 */
public class PropertyMetadata {
    public String propertyName;
    public String columnName;
    public String description;
    public String gbName;
    public String rbacVisible;
    public boolean isEntityType;
    public String propertyType;
    public String propertyRelation;
    public boolean isCollectionType;
    public String collectionEntityName;
    public String collectionTableName;
    public String collectionForeignColumnName;
    public String collectionKeyColumnName;
    public String discoverVisible;
    public String discoverIndex;
    public String discoverWidth;
    public String discoverHidden;
    public String discoverExpression;
    public String uiPomanagerVisible;
    public String uiPomanagerEditable;
    public String uiPomanagerWidth;
    public String uiPomanagerIndex;
    public String uiPomanagerSearchable;
    public String uiPomanagerExportable;
    public String uiPomanagerTitlename;
    public String uiPomanagerShowTitlename;
    public String uiPomanagerParam;
    public String uiPomanagerEditattribute;
    public String uiPomanagerEdittype;
    public String isSecurityable;
    public String isGlobalProperty;
    public String globalPOType;
    public static final String RELATION_ONE_TO_ONE = "one-to-one";
    public static final String RELATION_ONE_TO_MANY = "one-to-many";
    public static final String RELATION_MANY_TO_MANY = "many-to-many";
    public static final String RELATION_MANY_TO_ONE = "many-to-one";

    public String getPropertyName() {
        return propertyName;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getDescription() {
        return description;
    }

    public String getGbName() {
        return gbName;
    }

    public String getRbacVisible() {
        return rbacVisible;
    }

    public boolean isEntityType() {
        return isEntityType;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public String getPropertyRelation() {
        return propertyRelation;
    }

    public boolean isCollectionType() {
        return isCollectionType;
    }

    public String getCollectionEntityName() {
        return collectionEntityName;
    }

    public String getCollectionTableName() {
        return collectionTableName;
    }

    public String getCollectionForeignColumnName() {
        return collectionForeignColumnName;
    }

    public String getCollectionKeyColumnName() {
        return collectionKeyColumnName;
    }

    public String getDiscoverVisible() {
        return discoverVisible;
    }

    public String getDiscoverIndex() {
        return discoverIndex;
    }

    public String getDiscoverWidth() {
        return discoverWidth;
    }

    public String getDiscoverHidden() {
        return discoverHidden;
    }

    public String getDiscoverExpression() {
        return discoverExpression;
    }

    public String getUiPomanagerVisible() {
        return uiPomanagerVisible;
    }

    public String getUiPomanagerEditable() {
        return uiPomanagerEditable;
    }

    public String getUiPomanagerWidth() {
        return uiPomanagerWidth;
    }

    public String getUiPomanagerIndex() {
        return uiPomanagerIndex;
    }

    public String getUiPomanagerSearchable() {
        return uiPomanagerSearchable;
    }

    public String getUiPomanagerExportable() {
        return uiPomanagerExportable;
    }

    public String getUiPomanagerTitlename() {
        return uiPomanagerTitlename;
    }

    public String getUiPomanagerShowTitlename() {
        return uiPomanagerShowTitlename;
    }

    public String getUiPomanagerParam() {
        return uiPomanagerParam;
    }

    public String getUiPomanagerEditattribute() {
        return uiPomanagerEditattribute;
    }

    public String getUiPomanagerEdittype() {
        return uiPomanagerEdittype;
    }

    public String getIsSecurityable() {
        return isSecurityable;
    }

    public String getIsGlobalProperty() {
        return isGlobalProperty;
    }

    public String getGlobalPOType() {
        return globalPOType;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGbName(String gbName) {
        this.gbName = gbName;
    }

    public void setRbacVisible(String rbacVisible) {
        this.rbacVisible = rbacVisible;
    }

    public void setEntityType(boolean isEntityType) {
        this.isEntityType = isEntityType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public void setPropertyRelation(String propertyRelation) {
        this.propertyRelation = propertyRelation;
    }

    public void setCollectionType(boolean isCollectionType) {
        this.isCollectionType = isCollectionType;
    }

    public void setCollectionEntityName(String collectionEntityName) {
        this.collectionEntityName = collectionEntityName;
    }

    public void setCollectionTableName(String collectionTableName) {
        this.collectionTableName = collectionTableName;
    }

    public void setCollectionForeignColumnName(String collectionForeignColumnName) {
        this.collectionForeignColumnName = collectionForeignColumnName;
    }

    public void setCollectionKeyColumnName(String collectionKeyColumnName) {
        this.collectionKeyColumnName = collectionKeyColumnName;
    }

    public void setDiscoverVisible(String discoverVisible) {
        this.discoverVisible = discoverVisible;
    }

    public void setDiscoverIndex(String discoverIndex) {
        this.discoverIndex = discoverIndex;
    }

    public void setDiscoverWidth(String discoverWidth) {
        this.discoverWidth = discoverWidth;
    }

    public void setDiscoverHidden(String discoverHidden) {
        this.discoverHidden = discoverHidden;
    }

    public void setDiscoverExpression(String discoverExpression) {
        this.discoverExpression = discoverExpression;
    }

    public void setUiPomanagerVisible(String uiPomanagerVisible) {
        this.uiPomanagerVisible = uiPomanagerVisible;
    }

    public void setUiPomanagerEditable(String uiPomanagerEditable) {
        this.uiPomanagerEditable = uiPomanagerEditable;
    }

    public void setUiPomanagerWidth(String uiPomanagerWidth) {
        this.uiPomanagerWidth = uiPomanagerWidth;
    }

    public void setUiPomanagerIndex(String uiPomanagerIndex) {
        this.uiPomanagerIndex = uiPomanagerIndex;
    }

    public void setUiPomanagerSearchable(String uiPomanagerSearchable) {
        this.uiPomanagerSearchable = uiPomanagerSearchable;
    }

    public void setUiPomanagerExportable(String uiPomanagerExportable) {
        this.uiPomanagerExportable = uiPomanagerExportable;
    }

    public void setUiPomanagerTitlename(String uiPomanagerTitlename) {
        this.uiPomanagerTitlename = uiPomanagerTitlename;
    }

    public void setUiPomanagerShowTitlename(String uiPomanagerShowTitlename) {
        this.uiPomanagerShowTitlename = uiPomanagerShowTitlename;
    }

    public void setUiPomanagerParam(String uiPomanagerParam) {
        this.uiPomanagerParam = uiPomanagerParam;
    }

    public void setUiPomanagerEditattribute(String uiPomanagerEditattribute) {
        this.uiPomanagerEditattribute = uiPomanagerEditattribute;
    }

    public void setUiPomanagerEdittype(String uiPomanagerEdittype) {
        this.uiPomanagerEdittype = uiPomanagerEdittype;
    }

    public void setIsSecurityable(String isSecurityable) {
        this.isSecurityable = isSecurityable;
    }

    public void setIsGlobalProperty(String isGlobalProperty) {
        this.isGlobalProperty = isGlobalProperty;
    }

    public void setGlobalPOType(String globalPOType) {
        this.globalPOType = globalPOType;
    }
}
