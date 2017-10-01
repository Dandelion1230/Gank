package com.dandelion.gank.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.dandelion.gank.R;
import com.dandelion.gank.model.entity.GankResult;
import com.dandelion.gank.net.RxUtils;
import com.dandelion.gank.view.adapter.HomeAdapter;
import com.dandelion.gank.view.base.BaseAdapter;
import com.dandelion.gank.view.base.BaseRefreshFragment;
import com.dandelion.gank.view.ui.HomeDetailActivity;
import com.github.jdsjlzx.interfaces.OnItemClickListener;

import java.util.List;

import rx.Subscriber;

/**
 * Created by Administrator on 2016/7/20.
 */
public class HomeFragment extends BaseRefreshFragment {

    private HomeAdapter mHomeAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_first;
    }

    @Override
    public void onCreateAfter(Bundle savedInstanceState) {
        super.onCreateAfter(savedInstanceState);
        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                startActivity(HomeDetailActivity.newIntent(getActivity(), mHomeAdapter.getDataList().get(i)));
            }
        });
    }

    @Override
    public void initData() {
//        mRecyclerView.refresh();
    }


    @Override
    protected void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    @Override
    protected void getGankData() {
        Subscriber<List<GankResult>> subscriber = new Subscriber<List<GankResult>>() {
            @Override
            public void onCompleted() {
                mRecyclerView.refreshComplete(pageSize);
            }

            @Override
            public void onError(Throwable e) {
                View.OnClickListener onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getGankData();
                    }
                };
                showActionSnackbar(e.getMessage(), "重试", onClickListener);

            }

            @Override
            public void onNext(List<GankResult> gankResults) {
                mRecyclerView.refreshComplete(pageSize);
                proData(gankResults);
            }
        };
        RxUtils.getInstance().getGankOtherData(subscriber, type, pageSize, pageNo);

    }

    @Override
    protected BaseAdapter getAdapter() {
        if (mHomeAdapter == null) {
            mHomeAdapter = new HomeAdapter();
        }
        return mHomeAdapter;
    }


    private void proData(List<GankResult> gankResults) {
        mCurrentCounter += gankResults.size();
        getAdapter().addAll(gankResults);
        mLRecyclerViewAdapter.notifyDataSetChanged();
    }
}
