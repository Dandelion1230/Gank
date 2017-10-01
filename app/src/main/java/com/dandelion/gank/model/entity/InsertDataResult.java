package com.dandelion.gank.model.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/18.
 */

public class InsertDataResult implements Serializable {

    private String createdAt;
    private String objectId;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
}
