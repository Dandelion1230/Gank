package com.dandelion.gank.model.entity;

import java.io.Serializable;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Administrator on 2016/7/27.
 */
public class MyUser extends BmobUser implements Serializable {
    private BmobFile notes; // 用户的笔记
    private String collect; // 用户的收藏
    private BmobFile icon;

    public BmobFile getIcon() {
        return icon;
    }

    public void setIcon(BmobFile icon) {
        this.icon = icon;
    }

    public String getCollect() {
        return collect;
    }

    public void setCollect(String collect) {
        this.collect = collect;
    }

    public BmobFile getNotes() {
        return notes;
    }

    public void setNotes(BmobFile notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "MyUser{" +
                "collect='" + collect + '\'' +
                ", notes=" + notes +
                ", icon=" + icon +
                '}';
    }
}
