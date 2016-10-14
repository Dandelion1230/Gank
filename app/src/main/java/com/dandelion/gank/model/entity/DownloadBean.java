package com.dandelion.gank.model.entity;

import android.content.ClipData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dandelion on 2016/7/24.
 */
public class DownloadBean {

    private Integer totalCount;
    private Boolean isSuccess;
    private List<ClipData.Item> items = new ArrayList<>();

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }

    public List<ClipData.Item> getItems() {
        return items;
    }

    public void setItems(List<ClipData.Item> items) {
        this.items = items;
    }
}
