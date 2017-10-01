package com.dandelion.gank.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dandelion.gank.R;
import com.dandelion.gank.model.entity.xiandu.XianduResult;
import com.dandelion.gank.utils.ImageLoadUtils;
import com.dandelion.gank.view.base.BaseAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/8/26.
 */
public class ReadAdapter extends BaseAdapter<XianduResult> {

    private Context context;

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHold(LayoutInflater.from(context).inflate(R.layout.item_read,parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        XianduResult mResult = mDataList.get(position);
        ViewHold viewHold = (ViewHold) holder;
        viewHold.mTitle.setText(mResult.getTitle());
        viewHold.mTime.setText(mResult.getTime());
        ImageLoadUtils.setCircleImageView(context, mResult.getImg(), viewHold.mLogo);


    }


    public class ViewHold extends RecyclerView.ViewHolder {
        @BindView(R.id.title)
        TextView mTitle;
        @BindView(R.id.time)
        TextView mTime;
        @BindView(R.id.logo)
        ImageView mLogo;
        public ViewHold(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
