package com.dandelion.gank.model.http;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * 日期：16/3/17 20:53
 * <p>
 * 描述：
 * 修复：
 */
public class ProgressInterceptor implements Interceptor
{
    private ProgressListener progressListener;


    public ProgressInterceptor(ProgressListener progressListener)
    {
        this.progressListener = progressListener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException
    {
        Response originalResponse = chain.proceed(chain.request());
        return originalResponse.newBuilder().body(new ProgressResponseBody(originalResponse.body(), progressListener)).build();
    }
}
