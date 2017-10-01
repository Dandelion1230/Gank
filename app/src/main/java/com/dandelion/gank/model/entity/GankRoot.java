package com.dandelion.gank.model.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/24.
 */
public class GankRoot<T> implements Serializable {
    public boolean error;
    private String msg;
    public T results;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public T getResults() {
        return results;
    }

    public void setResults(T results) {
        this.results = results;
    }
}
