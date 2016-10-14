package com.dandelion.gank.utils;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by Administrator on 2016/8/25.
 */
public class SnackbarUtils {
    public static void show(View view, String text, int time) {
        Snackbar.make(view, text, time).show();
    }
}
