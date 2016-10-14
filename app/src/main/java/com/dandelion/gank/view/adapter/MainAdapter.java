package com.dandelion.gank.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2016/7/18.
 */
public class MainAdapter extends AnimRecyclerAdapter<MainAdapter.MainViewHolder> {

    private OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    private List<String> recyclerData;

    public MainAdapter(List<String> recyclerData) {
        this.recyclerData = recyclerData;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MainViewHolder holder = new MainViewHolder(LayoutInflater.from(
                parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MainViewHolder holder, final int position) {
        holder.tv.setText(recyclerData.get(position));
        showItemAnim(holder.tv, position);

        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(holder.itemView, holder.getLayoutPosition());
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemClickListener.onLongItemClick(holder.itemView, holder.getLayoutPosition());
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return recyclerData.size();
    }

    class MainViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        public MainViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(android.R.id.text1);
        }
    }

}
