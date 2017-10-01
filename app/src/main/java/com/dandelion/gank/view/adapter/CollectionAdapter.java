package com.dandelion.gank.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dandelion.gank.R;
import com.dandelion.gank.model.entity.GankResult;
import com.dandelion.gank.utils.ImageLoadUtils;
import com.dandelion.gank.view.base.BaseAdapter;
import com.dandelion.gank.view.widget.SwipeMenuView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/6/18.
 */

public class CollectionAdapter extends BaseAdapter<GankResult> {

    private Context context;

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new CollectionAdapter.ViewHold(LayoutInflater.from(context).inflate(R.layout.collection_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        GankResult mGankResult = mDataList.get(position);
        final ViewHold viewHold = (ViewHold) holder;
        if (mGankResult.getType().equals("iOS"))
            viewHold.mImageType.setImageResource(R.mipmap.apple);
        else if (mGankResult.getType().equals("Android"))
            viewHold.mImageType.setImageResource(R.mipmap.android);
        else if (mGankResult.getType().equals("前端"))
            viewHold.mImageType.setImageResource(R.mipmap.js);
        else if (mGankResult.getType().equals("休息视频"))
            viewHold.mImageType.setImageResource(R.mipmap.video);
        else if (mGankResult.getType().equals("瞎推荐"))
            viewHold.mImageType.setImageResource(R.mipmap.recommend);
        else if (mGankResult.getType().equals("拓展资源"))
            viewHold.mImageType.setImageResource(R.mipmap.resource);
        else if (mGankResult.getType().equals("App"))
            viewHold.mImageType.setImageResource(R.mipmap.app);
        viewHold.mType.setText(mGankResult.getType());
        viewHold.mPublishTime.setText(mGankResult.getPublishedAt().substring(0, 10));
        viewHold.mTitle.setText(mGankResult.getDesc());
        String url = mGankResult.getUrl();
        String[] split = url.split("/");
        viewHold.mSource.setText("——" + split[2] + "\t[" + mGankResult.getWho() + "]");
        if (mGankResult.getImages() == null) {
            viewHold.mImageView.setVisibility(View.GONE);
        } else {
            viewHold.mImageView.setVisibility(View.VISIBLE);
            ImageLoadUtils.setImageView(context, mGankResult.getImages().get(0), viewHold.mImageView);
        }
        viewHold.mBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnItemClickListener) {
                    mOnItemClickListener.onDelClick(position);
                }
            }
        });
        viewHold.mItemContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnItemClickListener) {
                    mOnItemClickListener.onItemClick(position);
                }
            }
        });




    }

    public interface OnItemClickListener{
        void onDelClick(int positon);
        void onItemClick(int positon);
    }
    public OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnDelClickListener) {
        this.mOnItemClickListener = mOnDelClickListener;
    }

    public class ViewHold extends RecyclerView.ViewHolder {
        @BindView(R.id.swipe_view)
        SwipeMenuView mSwipeView;
        @BindView(R.id.item_content)
        LinearLayout mItemContent;
        @BindView(R.id.btn_delete)
        Button mBtnDelete;
        @BindView(R.id.tv_title)
        TextView mTitle;
        @BindView(R.id.tv_source)
        TextView mSource;
        @BindView(R.id.iv_image)
        ImageView mImageView;
        @BindView(R.id.tv_type)
        TextView mType;
        @BindView(R.id.tv_publish)
        TextView mPublishTime;
        @BindView(R.id.iv_type)
        ImageView mImageType;

        public ViewHold(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
