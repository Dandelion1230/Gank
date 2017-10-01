package com.dandelion.gank.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dandelion.gank.R;
import com.dandelion.gank.view.base.BaseAdapter;
import com.dandelion.gank.view.ui.ReadDetailActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/6/19.
 */

public class LicensesAdapter extends BaseAdapter<String> {

    private Context mContext;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_license, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        String data = mDataList.get(position);
        final String[] datas = data.split("-");
        viewHolder.mProjectName.setText(datas[0]+"-"+datas[1]);
//        viewHolder.mProjectAuthor.setText(datas[1]);
        viewHolder.mProjectLicense.setText(datas[2]);
        viewHolder.mProjectUrl.setText(datas[3]);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(ReadDetailActivity.newIntent(mContext, datas[0]+"-"+datas[1], datas[3]));

            }
        });

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.project_name)
        TextView mProjectName;
//        @BindView(R.id.project_author)
//        TextView mProjectAuthor;
        @BindView(R.id.project_url)
        TextView mProjectUrl;
        @BindView(R.id.project_license)
        TextView mProjectLicense;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
