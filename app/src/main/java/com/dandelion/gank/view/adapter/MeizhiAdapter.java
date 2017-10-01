package com.dandelion.gank.view.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dandelion.gank.R;
import com.dandelion.gank.utils.SPUtils;
import com.dandelion.gank.view.base.BaseAdapter;
import com.dandelion.gank.model.entity.GankResult;
import com.dandelion.gank.utils.NetworkUtils;
import com.dandelion.gank.utils.ImageLoadUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/8/25.
 */
public class MeizhiAdapter extends BaseAdapter<GankResult> {

    private Context mContext;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new ViewHold(LayoutInflater.from(mContext).inflate(R.layout.home_item,parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        GankResult mGankResult = mDataList.get(position);
        ViewHold viewHold = (ViewHold) holder;
        viewHold.mHomeIamge.setScaleType(ImageView.ScaleType.FIT_XY);
        boolean wifi = NetworkUtils.isWifi(mContext);
        Boolean isWifiLoadImage = (Boolean) SPUtils.getSp(mContext, "isWifiLoadImage", false);
        if (!wifi && isWifiLoadImage) {
            ImageLoadUtils.setImageView(mContext, mGankResult.getUrl(), ((ViewHold) holder).mHomeIamge);
        } else {
            ImageLoadUtils.setImageView(mContext, mGankResult.getUrl(), ((ViewHold) holder).mHomeIamge);
        }
        //修改高度，模拟交错效果
//        if (position > 2) {
//            viewHold.mCardView.getLayoutParams().height = position % 2 != 0 ? largeCardHeight : smallCardHeight;
//        }
//        Random mRandom = new Random();
//        int height = mRandom.nextInt(250)%(250-220) + 220;
//        int px = SystemUtils.dip2px(context, height);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                px);
//        viewHold.mCardView.setLayoutParams(params);

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class ViewHold extends RecyclerView.ViewHolder {
        @BindView(R.id.card_view)
        CardView mCardView;
        @BindView(R.id.home_image)
        ImageView mHomeIamge;
        public ViewHold(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
