package com.dandelion.gank.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.dandelion.gank.R;
import com.dandelion.gank.model.entity.GankResult;
import com.dandelion.gank.net.HttpUtils;
import com.dandelion.gank.view.base.BaseFragment;
import com.dandelion.gank.view.ui.PhotoActivity;
import com.github.jdsjlzx.util.RecyclerViewStateUtils;
import com.github.jdsjlzx.view.LoadingFooter;

import java.util.List;

import rx.Subscriber;

/**
 * Created by Administrator on 2016/7/20.
 */
public class HomeFragment extends BaseFragment {

//    public HomeAdapter mHomeAdapter = null;
//    public LRecyclerViewAdapter mLRecyclerViewAdapter = null;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_first;
    }

    @Override
    public void onCreateAfter(Bundle savedInstanceState) {
//        mHomeAdapter = new HomeAdapter(getActivity());
//        mLRecyclerViewAdapter = new LRecyclerViewAdapter(getActivity(), mHomeAdapter);
//        mRecyclerView.setAdapter(mLRecyclerViewAdapter);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = PhotoActivity.newIntent(getActivity(), mHomeAdapter.getDataList().get(position).getUrl());
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

    @Override
    protected void initRecyclerView() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //防止item位置互换
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mRecyclerView.setLayoutManager(layoutManager);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public int adapterType() {
        return 1;
    }

    @Override
    public boolean isVisibleGone() {
        return true;
    }

    @Override
    protected void getGankData() {
        Subscriber<List<GankResult>> subscriber = new Subscriber<List<GankResult>>() {
            @Override
            public void onCompleted() {
//                mLoading.setVisibility(View.GONE);
            }

            @Override
            public void onError(Throwable e) {
                Snackbar.make(mRecyclerView, e.getMessage(), Snackbar.LENGTH_INDEFINITE)
                        .setAction("重试", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getGankData();
                            }
                        }).show();

            }

            @Override
            public void onNext(List<GankResult> gankResults) {
                proData(gankResults);
            }
        };
        HttpUtils.getInstance().getGankHomeData(subscriber, pageSize, pageNo);

    }


    private void proData(List<GankResult> gankResults) {
        if (isRefresh) {  // 下拉刷新
            mHomeAdapter.clear();
            mCurrentCounter = 0;
//            mHomeAdapter.addAll(gankResults);
        }
        mHomeAdapter.addAll(gankResults);
        mCurrentCounter += gankResults.size();
        if (isRefresh) {
            isRefresh = false;
            mRecyclerView.refreshComplete();
            notifyDataSetChanged();
        } else {
            RecyclerViewStateUtils.setFooterViewState(mRecyclerView, LoadingFooter.State.Normal);
        }
    }

    private void notifyDataSetChanged() {
        mLRecyclerViewAdapter.notifyDataSetChanged();
    }


//    @OnClick(R.id.fab)
//    public void fabOnClick(View view) {
//        mRecycler.smoothScrollToPosition(0);
//    }


}
