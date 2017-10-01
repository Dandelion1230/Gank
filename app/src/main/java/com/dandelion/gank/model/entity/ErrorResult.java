package com.dandelion.gank.model.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/17.
 */

public class ErrorResult implements Serializable {

    private int code;
    private String error;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
