package com.dandelion.gank.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dandelion.gank.R;
import com.dandelion.gank.model.entity.GankResult;
import com.dandelion.gank.utils.NetworkUtils;
import com.dandelion.gank.utils.PicassoUtils;
import com.dandelion.gank.utils.PreferencesLoader;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/8/25.
 */
public class HomeAdapter extends BaseAdapter<GankResult> {

    private LayoutInflater mLayoutInflater;
//    private int largeCardHeight, smallCardHeight;
    private Context context;
    public HomeAdapter(Context context) {
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
//        largeCardHeight = (int)context.getResources().getDisplayMetrics().density * 300;
//        smallCardHeight = (int)context.getResources().getDisplayMetrics().density * 280;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHold(mLayoutInflater.inflate(R.layout.home_item,parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        GankResult mGankResult = mDataList.get(position);
        ViewHold viewHold = (ViewHold) holder;
        viewHold.mHomeIamge.setScaleType(ImageView.ScaleType.FIT_XY);
        boolean wifi = NetworkUtils.isWifi(context);
        PreferencesLoader loader = new PreferencesLoader(context);
        Boolean isWifiLoadImage = loader.getBoolean("isWifiLoadImage", false);
        if (!wifi && isWifiLoadImage) {
            PicassoUtils.setImageView(context, mGankResult.getUrl(), ((ViewHold) holder).mHomeIamge);
        } else {
            PicassoUtils.setImageView(context, mGankResult.getUrl(), ((ViewHold) holder).mHomeIamge);
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
        @Bind(R.id.card_view)
        CardView mCardView;
        @Bind(R.id.home_image)
        ImageView mHomeIamge;
        public ViewHold(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
