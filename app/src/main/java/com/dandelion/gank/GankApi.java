package com.dandelion.gank;

import com.dandelion.gank.model.entity.GankResult;
import com.dandelion.gank.model.entity.GankRoot;
import com.dandelion.gank.model.entity.InsertDataResult;
import com.dandelion.gank.model.entity.LoginResult;
import com.dandelion.gank.model.entity.RegisteredResult;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import rx.Observable;

/**
 * Created by Dandelion on 2016/7/24.
 */
public interface GankApi {
    @Streaming
    @GET("16891/EF12067216FBAF87928628A120425014.apk")
    Call<ResponseBody> downloadUpdate();

    @GET("data/福利/{pageSize}/{page}")
    Observable<GankRoot<List<GankResult>>> getHomeData(@Path("pageSize") int pageSzie, @Path("page") int page);

    @GET("data/all/{pageSize}/{page}")
    Observable<GankRoot<List<GankResult>>> getAllData(@Path("pageSize") int pageSzie, @Path("page") int page);

    @GET("data/{type}/{pageSize}/{page}")
    Observable<GankRoot<List<GankResult>>> getOtherData(@Path("type") String type, @Path("pageSize") int pageSzie, @Path("page") int page);

    @GET("search/query/{keyword}/category/all/count/{pageSize}/page/{page}") //search/query/listview/category/Android/count/10/page/1
    Observable<GankRoot<List<GankResult>>> getGankSearchData(@Path("keyword") String keyword, @Path("pageSize") int pageSzie, @Path("page") int page);

//    @POST("add2gank")
//    Observable submitData(@PartMap("url") String url, )

    @Headers({
            "X-Bmob-Application-Id: c8bf3da674279ad6abea64b35b8320e2",
            "X-Bmob-REST-API-Key: 9f95115dbc86dd89ab1228ae18392d17",
            "Content-Type: application/json"
    })
    @POST("users")
    Observable<RegisteredResult> getRegisteredData(@Body RequestBody requestBody);

    @Headers({
            "X-Bmob-Application-Id: c8bf3da674279ad6abea64b35b8320e2",
            "X-Bmob-REST-API-Key: 9f95115dbc86dd89ab1228ae18392d17",
            "Content-Type: application/json"
    })
    @GET("login")
    Observable<LoginResult> getLoginData(@Query("username") String username, @Query("password") String password);

    @Headers({
            "X-Bmob-Application-Id: c8bf3da674279ad6abea64b35b8320e2",
            "X-Bmob-REST-API-Key: 9f95115dbc86dd89ab1228ae18392d17",
            "Content-Type: application/json"
    })
    @POST("classes/Collection")
    Observable<InsertDataResult> getAddCollectionData(@Body RequestBody requestBody);

    @Headers({
            "X-Bmob-Application-Id: c8bf3da674279ad6abea64b35b8320e2",
            "X-Bmob-REST-API-Key: 9f95115dbc86dd89ab1228ae18392d17",
            "Content-Type: application/json"
    })
    @GET("classes/Collection")
    Observable<GankRoot<List<GankResult>>> getCollectionData(@Query("where") String where);

    @Headers({
            "X-Bmob-Application-Id: c8bf3da674279ad6abea64b35b8320e2",
            "X-Bmob-REST-API-Key: 9f95115dbc86dd89ab1228ae18392d17",
            "Content-Type: application/json"
    })
    @DELETE("classes/Collection/{objectId}")
    Observable<GankRoot> getDelCollectionData(@Path("objectId") String objectId);

    @Headers({
            "X-Bmob-Application-Id: c8bf3da674279ad6abea64b35b8320e2",
            "X-Bmob-REST-API-Key: 9f95115dbc86dd89ab1228ae18392d17",
            "Content-Type: application/json"
    })
    @POST("classes/Feedback")
    Observable<InsertDataResult> AddFeedbackData(@Body RequestBody requestBody);

}
