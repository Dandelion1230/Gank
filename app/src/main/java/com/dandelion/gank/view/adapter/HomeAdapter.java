package com.dandelion.gank.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dandelion.gank.R;
import com.dandelion.gank.model.entity.GankResult;
import com.dandelion.gank.utils.ImageLoadUtils;
import com.dandelion.gank.view.base.BaseAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/8/26.
 */
public class HomeAdapter extends BaseAdapter<GankResult> {

    private Context context;

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHold(LayoutInflater.from(context).inflate(R.layout.all_item,parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        GankResult mGankResult = mDataList.get(position);
        ViewHold viewHold = (ViewHold) holder;
//        if (mGankResult.getType().equals("福利")) {
//            viewHold.mLTitle.setVisibility(View.GONE);
//            viewHold.mMeizi.setVisibility(View.VISIBLE);
//            ImageLoadUtils.setImageView(context, mGankResult.getUrl(), viewHold.mMeizi);
//        } else {
//            if (mGankResult.getType().equals("iOS"))
//                viewHold.mLogo.setImageResource(R.mipmap.apple);
//            else if (mGankResult.getType().equals("Android"))
//                viewHold.mLogo.setImageResource(R.mipmap.android);
//            else if (mGankResult.getType().equals("前端"))
//                viewHold.mLogo.setImageResource(R.mipmap.js);
//            else if (mGankResult.getType().equals("休息视频"))
//                viewHold.mLogo.setImageResource(R.mipmap.video);
//            else if (mGankResult.getType().equals("瞎推荐"))
//                viewHold.mLogo.setImageResource(R.mipmap.recommend);
//            else if (mGankResult.getType().equals("拓展资源"))
//                viewHold.mLogo.setImageResource(R.mipmap.resource);
//            else if (mGankResult.getType().equals("App"))
//                viewHold.mLogo.setImageResource(R.mipmap.app);
//            viewHold.mLTitle.setVisibility(View.VISIBLE);
//            viewHold.mMeizi.setVisibility(View.GONE);
//            if (!TextUtils.isEmpty(mGankResult.getWho())) {
//                viewHold.mTitle.setText(mGankResult.getDesc()+"  ["+mGankResult.getWho()+"]");
//                SpannableUtils.setSpannableColor(viewHold.mTitle, Color.parseColor("#666666"), viewHold.mTitle.getText().length()
//                        -mGankResult.getWho().length()-2, viewHold.mTitle.getText().length());
//            } else {
//                viewHold.mTitle.setText(mGankResult.getDesc());
//            }
//        }

//        if (!TextUtils.isEmpty(mGankResult.getWho())) {
//            viewHold.mTitle.setText(mGankResult.getDesc()+"  ["+mGankResult.getWho()+"]");
//            SpannableUtils.setSpannableColor(viewHold.mTitle, Color.parseColor("#666666"), viewHold.mTitle.getText().length()
//                    -mGankResult.getWho().length()-2, viewHold.mTitle.getText().length());
//        }else {
//        }
        viewHold.mTitle.setText(mGankResult.getDesc());
        String url = mGankResult.getUrl();
        String[] split = url.split("/");
        viewHold.mSource.setText("——" + split[2] + "\t[" + mGankResult.getWho() + "]");
        if (mGankResult.getImages() == null) {
            viewHold.mImageView.setVisibility(View.GONE);
        }else {
            viewHold.mImageView.setVisibility(View.VISIBLE);
            ImageLoadUtils.setImageView(context, mGankResult.getImages().get(0), viewHold.mImageView);
            if (mGankResult.getImages().size() > 1) {
                for (String image : mGankResult.getImages()) {
                    Log.e("TAG", image);
                }
            }
        }

    }


    public class ViewHold extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title)
        TextView mTitle;
        @BindView(R.id.tv_source)
        TextView mSource;
//        @Bind(R.id.iv_logo)
//        ImageView mLogo;
        @BindView(R.id.iv_image)
        ImageView mImageView;
//        @Bind(R.id.iv_meizi)
//        ImageView mMeizi;
        public ViewHold(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
