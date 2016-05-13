package org.maccha.base.metadata;

import java.io.Serializable;

/**
 * Created by Lori Yue on 16-3-4.
 */
public class ObjectMetadata implements Serializable {
    public String entityName;
    public String tableName;
    public String description;
    public String gbName;
    public String entityImplements;
    public String isGlobalPO;
    public String globalPOType;

    public String getEntityName() {
        return entityName;
    }

    public String getTableName() {
        return tableName;
    }

    public String getDescription() {
        return description;
    }

    public String getGbName() {
        return gbName;
    }

    public String getEntityImplements() {
        return entityImplements;
    }

    public String getIsGlobalPO() {
        return isGlobalPO;
    }

    public String getGlobalPOType() {
        return globalPOType;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGbName(String gbName) {
        this.gbName = gbName;
    }

    public void setEntityImplements(String entityImplements) {
        this.entityImplements = entityImplements;
    }

    public void setIsGlobalPO(String isGlobalPO) {
        this.isGlobalPO = isGlobalPO;
    }

    public void setGlobalPOType(String globalPOType) {
        this.globalPOType = globalPOType;
    }
}
