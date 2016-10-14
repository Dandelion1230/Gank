package com.dandelion.gank.model.http;

/**
 * Created by Dandelion on 2016/7/24.
 */
public interface ProgressListener {
    void onProgress (Long progress, long total, boolean done);
}
