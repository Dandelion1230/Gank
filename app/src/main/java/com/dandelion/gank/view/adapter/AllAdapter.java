package com.dandelion.gank.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dandelion.gank.R;
import com.dandelion.gank.adapter.BaseAdapter;
import com.dandelion.gank.model.entity.GankResult;
import com.dandelion.gank.utils.PicassoUtils;
import com.dandelion.gank.utils.SpannableUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/8/26.
 */
public class AllAdapter extends BaseAdapter<GankResult> {

    private Context context;

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHold(LayoutInflater.from(context).inflate(R.layout.all_item,parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        GankResult mGankResult = mDataList.get(position);
        ViewHold viewHold = (ViewHold) holder;
//        viewHold.mHomeIamge.setScaleType(ImageView.ScaleType.FIT_XY);
//        PicassoUtils.setImageView(context, mGankResult.getUrl(), ((ViewHold) holder).mHomeIamge);
        //修改高度，模拟交错效果
//        if (position > 2) {
//            viewHold.mCardView.getLayoutParams().height = position % 2 != 0 ? largeCardHeight : smallCardHeight;
//        }
        if (mGankResult.getType().equals("福利")) {
            viewHold.mLTitle.setVisibility(View.GONE);
            viewHold.mMeizi.setVisibility(View.VISIBLE);
            PicassoUtils.setImageView(context, mGankResult.getUrl(), viewHold.mMeizi);
        } else {
            if (mGankResult.getType().equals("iOS"))
                viewHold.mLogo.setImageResource(R.mipmap.apple);
            else if (mGankResult.getType().equals("Android"))
                viewHold.mLogo.setImageResource(R.mipmap.android);
            else if (mGankResult.getType().equals("前端"))
                viewHold.mLogo.setImageResource(R.mipmap.js);
            else if (mGankResult.getType().equals("休息视频"))
                viewHold.mLogo.setImageResource(R.mipmap.video);
            else if (mGankResult.getType().equals("瞎推荐"))
                viewHold.mLogo.setImageResource(R.mipmap.recommend);
            else if (mGankResult.getType().equals("拓展资源"))
                viewHold.mLogo.setImageResource(R.mipmap.resource);
            else if (mGankResult.getType().equals("App"))
                viewHold.mLogo.setImageResource(R.mipmap.app);
            viewHold.mLTitle.setVisibility(View.VISIBLE);
            viewHold.mMeizi.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(mGankResult.getWho())) {
                viewHold.mTitle.setText(mGankResult.getDesc()+"  ["+mGankResult.getWho()+"]");
                SpannableUtils.setSpannableColor(viewHold.mTitle, Color.parseColor("#666666"), viewHold.mTitle.getText().length()
                        -mGankResult.getWho().length()-2, viewHold.mTitle.getText().length());
            } else {
                viewHold.mTitle.setText(mGankResult.getDesc());
//                showItemAnim(viewHold.itemView, position);
//                SpannableUtils.setSpannableColor(viewHold.mTitle, Color.parseColor("#666666"), viewHold.mTitle.getText().length()
//                        -mGankResult.getWho().length()-2, viewHold.mTitle.getText().length());
            }
        }

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class ViewHold extends RecyclerView.ViewHolder {
        @Bind(R.id.ll_title)
        LinearLayout mLTitle;
        @Bind(R.id.tv_title)
        TextView mTitle;
        @Bind(R.id.iv_logo)
        ImageView mLogo;
        @Bind(R.id.iv_meizi)
        ImageView mMeizi;
        public ViewHold(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
