package com.dandelion.gank.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.dandelion.gank.update.DownLoadService;

/**
 * Created by Administrator on 2016/7/21.
 */
public class AppUpdateHelp  {

    private static Context mContext;
    /**
     *
     * @param context 上下文
     * @param update 是否强制更新
     */
    public static void startUpdate(Context context, boolean update) {
        mContext = context;
        // 获取版本号
        int appVersionCode = SystemUtils.getAppVersionCode(context);
        if (appVersionCode < 10) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            if (update) {
                builder.setTitle("更新")
                        .setMessage("更新到最新版")
                        .setCancelable(false)
                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                updateApp();
                            }
                        })
                        .show();
            } else {
                builder.setTitle("更新")
                        .setCancelable(false)
                        .setMessage("是否更新到最新版本？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                updateApp();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }

        }
    }

    private static void updateApp() {
//        HttpMethod.downloadUpdate();
        mContext.startService(new Intent(mContext, DownLoadService.class));
    }

}
