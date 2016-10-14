package com.dandelion.gank.model.http;

import com.dandelion.gank.model.entity.DownloadBean;

import okhttp3.OkHttpClient;

/**
 * Created by Dandelion on 2016/7/24.
 */
public class ProgressHelper {

    private static DownloadBean bean = new DownloadBean();
    private static ProgressHandler mProgressHandler;

    public static OkHttpClient.Builder addProgress(OkHttpClient.Builder builder) {
        if (builder == null) {
            builder = new OkHttpClient.Builder();
        }
        return builder;
    }

    ProgressListener listener = new ProgressListener() {
        @Override
        public void onProgress(Long progress, long total, boolean done) {

        }
    };
}
