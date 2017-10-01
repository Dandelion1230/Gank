package com.dandelion.gank.model.entity.xiandu;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/22.
 */

public class XianduResult implements Serializable {

    private String title;
    private String url;
    private String time;
    private String img;

    public XianduResult(String title, String url, String time, String img) {
        this.title = title;
        this.url = url;
        this.time = time;
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
