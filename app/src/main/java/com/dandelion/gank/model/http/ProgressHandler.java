package com.dandelion.gank.model.http;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.dandelion.gank.model.entity.DownloadBean;

/**
 * Created by Dandelion on 2016/7/24.
 */
public abstract class ProgressHandler {
    protected abstract void senMessage(DownloadBean bean);
    protected abstract void handleMessage(Message message);
    protected abstract void onProgress(Long progress, Long total, Boolean done);

    protected abstract class ResponseHandle extends Handler {
        protected ProgressHandler mProgressHandler;
        public ResponseHandle (ProgressHandler mProgressHandler, Looper looper) {
            super(looper);
            this.mProgressHandler = mProgressHandler;

        }

        @Override
        public void handleMessage(Message msg) {
            mProgressHandler.handleMessage(msg);
        }
    }
}
