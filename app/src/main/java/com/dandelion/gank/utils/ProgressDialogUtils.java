package com.dandelion.gank.utils;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.dandelion.gank.R;

/**
 * Created by Administrator on 2017/6/19.
 */

public class ProgressDialogUtils {

    private static ProgressDialogUtils mProgressDialogUtils = new ProgressDialogUtils();
    private AlertDialog dialog;

    public static ProgressDialogUtils getInstance() {
        return mProgressDialogUtils;
    }

    public void showLoad(Activity activity, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_progress, null);
        TextView tvMessage = (TextView) view.findViewById(R.id.message);
        tvMessage.setText(message);
        dialog = builder.create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(view);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = SystemUtils.dip2px(activity, 180);//宽高可设置具体大小
        lp.height = SystemUtils.dip2px(activity, 180);
        dialog.getWindow().setAttributes(lp);
    }

    public void hideLoad() {
        dialog.dismiss();
    }

}
