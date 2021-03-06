package com.dandelion.gank.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.dandelion.gank.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Administrator on 2016/8/25.
 */
public class BaseAdapter<GankResult> extends RecyclerView.Adapter {
    protected Context mContext;
    private int lastPosition = -1;

    public ArrayList<GankResult> mDataList = new ArrayList<>();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public List<GankResult> getDataList() {
        return mDataList;
    }

    public void setDataList(Collection<GankResult> list) {
        this.mDataList.clear();
        this.mDataList.addAll(list);
        notifyDataSetChanged();
    }

    public void addAll(Collection<GankResult> list) {
        int lastIndex = this.mDataList.size();
        if (this.mDataList.addAll(list)) {
            notifyItemRangeInserted(lastIndex, list.size());
        }
    }

    public void delete(int position) {
        mDataList.remove(position);
        notifyItemRemoved(position);
    }

    public void clear() {
        mDataList.clear();
        notifyDataSetChanged();
    }

    public void showItemAnim(final View view, int position) {
        final Context context = view.getContext();
        view.post(new Runnable() {
            @Override
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.recycler_anim);
                view.startAnimation(animation);
            }
        });
        lastPosition = position;
    }


}
