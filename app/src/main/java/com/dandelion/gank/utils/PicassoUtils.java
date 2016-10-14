package com.dandelion.gank.utils;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Administrator on 2016/8/25.
 */
public class PicassoUtils {
    public static void setImageView(Context context, String path, ImageView imageView) {
        Picasso.with(context).load(path).into(imageView);
    }
}
