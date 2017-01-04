package com.dandelion.gank;

import com.dandelion.gank.model.entity.GankResult;
import com.dandelion.gank.model.entity.GankRoot;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import rx.Observable;

/**
 * Created by Dandelion on 2016/7/24.
 */
public interface GankApi {
//    http://180.153.105.145/imtt.dd.qq.com/16891/EF12067216FBAF87928628A120425014.apk?mkey=5794e6df5be2cf81&f=1c58&c=0&fsname=com.tencent.mobileqq_6.5.0_390.apk&csr=4d5s&p=.apk
    @Streaming
    @GET("16891/EF12067216FBAF87928628A120425014.apk")
    Call<ResponseBody> downloadUpdate();

    @GET("data/福利/{pageSize}/{page}")
    Observable<GankRoot<List<GankResult>>> getHomeData(@Path("pageSize") int pageSzie, @Path("page") int page);

    @GET("data/all/{pageSize}/{page}")
    Observable<GankRoot<List<GankResult>>> getAllData(@Path("pageSize") int pageSzie, @Path("page") int page);

    @GET("data/{type}/{pageSize}/{page}")
    Observable<GankRoot<List<GankResult>>> getOtherData(@Path("type") String type, @Path("pageSize") int pageSzie, @Path("page") int page);
//    @POST("add2gank")
//    Observable submitData(@PartMap("url") String url, )

}
