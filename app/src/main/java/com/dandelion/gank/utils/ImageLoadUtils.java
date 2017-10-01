package com.dandelion.gank.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dandelion.gank.view.widget.GlideCircleTransform;

/**
 * Created by Administrator on 2016/8/25.
 */
public class ImageLoadUtils {

//    private static ProgressDialog dialog;

    public static void setImageView(final Context context, String path, ImageView imageView) {
//        dialog = new ProgressDialog(context);
//        dialog.show();
        Glide.with(context).load(path).diskCacheStrategy(DiskCacheStrategy.SOURCE)/*.listener(new RequestListener<String, GlideDrawable>() {

            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                Log.e("TAG", "图片加载完成");
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                dialog.dismiss();
                Toast.makeText(context, "图片加载完成", Toast.LENGTH_SHORT).show();
                Log.e("TAG", "图片加载完成");
                return false;
            }
        })*/.crossFade().into(imageView);
    }

    public static void setCircleImageView(final Context context, String path, ImageView imageView) {
        Glide.with(context)
                .load(path)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .transform(new GlideCircleTransform(context))
                .crossFade()
                .into(imageView);
    }
}