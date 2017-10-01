package com.dandelion.gank.view.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dandelion.gank.model.entity.GankResult;
import com.dandelion.gank.utils.ImageLoadUtils;

import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Administrator on 2017/6/10.
 */

public class PhotoPagerAdapter extends PagerAdapter {

    private List<GankResult> mResultList;
    private Context mContext;

    public PhotoPagerAdapter(List<GankResult> resultList) {
        mResultList = resultList;
    }

    @Override
    public int getCount() {
        return mResultList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        mContext = container.getContext();
        String url = mResultList.get(position).getUrl();
        PhotoView photoView = new PhotoView(mContext);
        ImageLoadUtils.setImageView(mContext, url, photoView);
        PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(photoView);
        photoViewAttacher.setScaleType(ImageView.ScaleType.CENTER);
        photoViewAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                if (null != mOnViewTapListener) {
                    mOnViewTapListener.onViewTapListener();
                }
            }
        });
        photoViewAttacher.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (null != mOnViewTapListener) {
                    mOnViewTapListener.onLongViewTapListener();
                }
                return true;
            }
        });
        container.addView(photoView);
        return photoView;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
        container.removeView((View) object);
    }

    public interface OnViewTapListener {
        void onViewTapListener();

        void onLongViewTapListener();
    }

    public OnViewTapListener mOnViewTapListener;

    public void setOnViewTapListener(OnViewTapListener mOnViewTapListener) {
        this.mOnViewTapListener = mOnViewTapListener;
    }
}
