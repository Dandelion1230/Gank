package com.dandelion.gank.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dandelion.gank.R;
import com.dandelion.gank.view.adapter.MainAdapter;
import com.dandelion.gank.view.adapter.OnItemClickListener;
import com.dandelion.gank.view.ui.TabActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/7/21.
 */
public class MeizhiFragment extends Fragment {
    @Bind(R.id.recycler)
    RecyclerView mRecycler;

    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;

    private List<String> recyclerData = new ArrayList<>();
    private MainAdapter mAdapter;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        mSwipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        mSwipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefresh.setRefreshing(true);
            }
        });
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setRecyclerData();
            }
        });
    }

    private void setRecyclerData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <= 100; i++) {
                    String data = "妹子数据" + i;
                    recyclerData.add(data);
                }
                mSwipeRefresh.setRefreshing(false);
                mRecycler.setLayoutManager(new LinearLayoutManager(context));
                mAdapter = new MainAdapter(recyclerData);
                mRecycler.setAdapter(mAdapter);
                setListener();
            }
        }, 2000);



    }

    private void setListener() {
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(new Intent(context, TabActivity.class));
            }

            @Override
            public void onLongItemClick(View view, int position) {
//                Snackbar.make(view, "onLongItemClick", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            setRecyclerData();
        }

    }

//    @OnClick(R.id.fab)
//    public void fabOnClick(View view) {
//        mRecycler.smoothScrollToPosition(0);
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
