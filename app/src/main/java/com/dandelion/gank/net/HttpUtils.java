package com.dandelion.gank.net;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import com.dandelion.gank.GankApi;
import com.dandelion.gank.URLs;
import com.dandelion.gank.model.entity.GankResult;
import com.dandelion.gank.model.entity.GankRoot;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/8/24.
 */
public class HttpUtils {
    private static HttpUtils mHttpUtils;
    private OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .build();
    private Retrofit retrofit;
    private final GankApi gankApi;

    private HttpUtils() {
        retrofit = new Retrofit.Builder()
                .baseUrl(URLs.APIURL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();
        gankApi = retrofit.create(GankApi.class);
    }


    public static HttpUtils getInstance() {
        if (mHttpUtils == null) {
            mHttpUtils = new HttpUtils();
        }
        return mHttpUtils;
    }
    public void getGankHomeData(Subscriber<List<GankResult>> subscriber, int pageSize, int page) {
        gankApi.getHomeData(pageSize, page)
                .map(new HttpResultFunc<List<GankResult>>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);


    }
    public void getGankAllData(Subscriber<List<GankResult>> subscriber, int pageSize, int page) {
        gankApi.getAllData(pageSize, page)
                .map(new HttpResultFunc<List<GankResult>>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
    public void getGankOtherData(Subscriber<List<GankResult>> subscriber, String type, int pageSize, int page) {
        gankApi.getOtherData(type, pageSize, page)
                .map(new HttpResultFunc<List<GankResult>>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 保存图片并返回uri
     * @param context
     * @param url
     * @return
     */
    public rx.Observable<Uri> getBitmapUri(final Context context, final String url) {
        return rx.Observable.create(new rx.Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                Bitmap bitmap = null;
                try {
                    bitmap = Picasso.with(context).load(url).get();
                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
                if (bitmap == null) {
                    subscriber.onError(new Exception("无法下载到图片"));
                }
                subscriber.onNext(bitmap);
                subscriber.onCompleted();

            }
        }).flatMap(new Func1<Bitmap, rx.Observable<Uri>>() {
            @Override
            public rx.Observable<Uri> call(Bitmap bitmap) {
                int index = url.lastIndexOf("/");
                String imageName = url.substring(index+1, url.length());
                File dir = new File(Environment.getExternalStorageDirectory(), "Gank");
                if (!dir.exists()) {
                    dir.mkdir();
                }
                File file = new File(dir, imageName);
                FileOutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    outputStream.flush();
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Uri uri = Uri.fromFile(file);
                // 通知图库更新
                Intent scannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
                context.sendBroadcast(scannerIntent);
                return rx.Observable.just(uri);
            }
        }).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io());
    }

    public class HttpResultFunc<T> implements Func1<GankRoot<T>, T> {

        @Override
        public T call(GankRoot<T> root) {
            if (root.error) {
                throw new RuntimeException("获取数据失败");
            } else {
                return root.getResults();
            }
        }
    }






}
