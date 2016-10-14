package com.dandelion.gank.view.base;

import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 2016/7/21.
 */
public class BaseApplication extends Application {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static synchronized Context context(){
        return context;
    }
}
