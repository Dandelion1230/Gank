package com.dandelion.gank.net;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.dandelion.gank.GankApi;
import com.dandelion.gank.URLs;
import com.dandelion.gank.model.entity.GankResult;
import com.dandelion.gank.model.entity.GankRoot;
import com.dandelion.gank.model.entity.InsertDataResult;
import com.dandelion.gank.model.entity.LoginResult;
import com.dandelion.gank.model.entity.RegisteredResult;
import com.dandelion.gank.model.entity.xiandu.XianduResult;
import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/8/24.
 */
public class RxUtils {
    private static RxUtils mHttpUtils;
    private OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(50, TimeUnit.SECONDS)
            .readTimeout(50, TimeUnit.SECONDS)
            .build();
    private Retrofit retrofit;
    private GankApi gankApi;
    private String mBaseApi = "";
    private Gson gson = new Gson();

//    private RxUtils() {
//        retrofit = new Retrofit.Builder()
//                .baseUrl(URLs.APIURL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .client(client)
//                .build();
//        gankApi = retrofit.create(GankApi.class);
//    }


    public static RxUtils getInstance() {
        if (mHttpUtils == null) {
            synchronized (RxUtils.class) {
                if (mHttpUtils == null) {
                    mHttpUtils = new RxUtils();
                }
            }
        }
        return mHttpUtils;
    }
    public void getGankHomeData(Subscriber<List<GankResult>> subscriber, int pageSize, int page) {
        getGankApi(URLs.API_BASE);
        gankApi.getHomeData(pageSize, page)
                .map(new HttpResultFunc<List<GankResult>>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);


    }
    public void getGankAllData(Subscriber<List<GankResult>> subscriber, int pageSize, int page) {
        getGankApi(URLs.API_BASE);
        gankApi.getAllData(pageSize, page)
                .map(new HttpResultFunc<List<GankResult>>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
    public void getGankOtherData(Subscriber<List<GankResult>> subscriber, String type, int pageSize, int page) {
        getGankApi(URLs.API_BASE);
        gankApi.getOtherData(type, pageSize, page)
                .map(new HttpResultFunc<List<GankResult>>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 注册
     * @param subscriber
     * @param object
     */
    public void getRegisteredData(Subscriber<RegisteredResult> subscriber, Object object) {
        getGankApi(URLs.BMOB_BASE);
        gankApi.getRegisteredData(getBody(object))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 登录
     * @param subscriber
     * @param username
     * @param password
     */
    public void getLoginData(Subscriber<LoginResult> subscriber, String username, String password) {
        getGankApi(URLs.BMOB_BASE);
        gankApi.getLoginData(username, password)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 添加收藏
     * @param subscriber
     * @param object
     */
    public void getAddCollectData(Subscriber<InsertDataResult> subscriber, Object object) {
        getGankApi(URLs.BMOB_BASE);
        gankApi.getAddCollectionData(getBody(object))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 查询收藏数据
     * @param subscriber
     * @param where
     */
    public void getCollectionData(Subscriber<List<GankResult>> subscriber, String where) {
        getGankApi(URLs.BMOB_BASE);
        gankApi.getCollectionData(where)
                .map(new HttpResultFunc<List<GankResult>>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
    /**
     * 删除收藏数据
     * @param subscriber
     * @param objectId
     */
    public void getDelCollectionData(Subscriber<GankRoot> subscriber, String objectId) {
        getGankApi(URLs.BMOB_BASE);
        gankApi.getDelCollectionData(objectId)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 反馈
     * @param subscriber
     * @param object
     */
    public void AddFeedbackData(Subscriber<InsertDataResult> subscriber, Object object) {
        getGankApi(URLs.BMOB_BASE);
        gankApi.AddFeedbackData(getBody(object))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取查询结果
     * @param subscriber
     * @param keyword
     * @param pageSize
     * @param page
     */
    public void getGankSearchData(Subscriber<List<GankResult>> subscriber, String keyword, int pageSize, int page) {
        getGankApi(URLs.API_BASE);
        gankApi.getGankSearchData(keyword, pageSize, page)
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
                    bitmap = Glide.with(context)
                            .load(url)
                            .asBitmap() //必须
                            .centerCrop()
                            .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .get();
                } catch (Exception e) {
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
                long currentTimeMillis = System.currentTimeMillis();
                String imageName;
                if (url.toLowerCase().contains("gif")) {
                    imageName = currentTimeMillis + ".gif";
                }else if (url.toLowerCase().contains("png")) {
                    imageName = currentTimeMillis + ".png";
                }else {
                    imageName = currentTimeMillis + ".jpg";
                }
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

    /**
     * 解析Html
     * @param path
     * @return
     */
    public Observable<List<XianduResult>> getHtmlAnalyticalResult(final String path) {
        return Observable.create(new Observable.OnSubscribe<List<XianduResult>>() {
            @Override
            public void call(Subscriber<? super List<XianduResult>> subscriber) {
                Document document = null;
                try {
                    document = Jsoup.connect(path).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (document == null) {
                    subscriber.onError(new Exception("网页解析失败"));
                }
                List<XianduResult> results = document2List(document);
                subscriber.onNext(results);
            }
        }).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io());
    }

    public List<XianduResult> document2List(Document document) {
        List<XianduResult> results = new ArrayList<>();
        Elements contentEles = document.getElementsByClass("xiandu_item");
        for (Element element : contentEles) {
            Element xianduLeft = element.getElementsByClass("xiandu_left").first();
            Element titleEle = xianduLeft.getElementsByClass("site-title").first();
            String title = titleEle.text();
            String url = titleEle.attr("abs:href");
            Element small = element.getElementsByTag("small").first();
            String time = small.text();
            Element xianduRight = element.getElementsByClass("xiandu_right").first();
            Element siteImg = xianduRight.getElementsByClass("site-img").first();
            String img = siteImg.attr("src");
            XianduResult result = new XianduResult(title, url, time, img);
            results.add(result);
        }

        return results;
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

    private RequestBody getBody(Object object) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                gson.toJson(object));
        return requestBody;
    }

    public void getGankApi(String path) {
        if (mBaseApi.equals(path)) {
            return;
        }else {
            mBaseApi = path;
            retrofit = new Retrofit.Builder()
                    .baseUrl(path)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(client)
                    .build();
            gankApi = retrofit.create(GankApi.class);
        }
    }




}
