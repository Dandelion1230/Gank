package com.dandelion.gank.model.http;

import android.os.Environment;
import android.util.Log;

import com.dandelion.gank.GankApi;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Dandelion on 2016/7/24.
 */
public class HttpMethod {

    private static Retrofit.Builder retrofit;
    private static OkHttpClient client;

    // 下载更新
    public static void downloadUpdate() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://180.153.105.145/imtt.dd.qq.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
        GankApi gankApi = retrofit.build().create(GankApi.class);

        Call<ResponseBody> call = gankApi.downloadUpdate();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    InputStream is = response.body().byteStream();
                    File file = new File(Environment.getExternalStorageDirectory(), "QQ.apk");
                    FileOutputStream fos = new FileOutputStream(file);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = bis.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                        fos.flush();
                    }
                    fos.close();
                    bis.close();
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.e("TAG", "success");

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e("TAG", "onFailure");
            }
        });
    }


}
