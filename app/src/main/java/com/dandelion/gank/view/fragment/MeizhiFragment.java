package com.dandelion.gank.view.fragment;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.dandelion.gank.R;
import com.dandelion.gank.net.RxUtils;
import com.dandelion.gank.view.base.BaseAdapter;
import com.dandelion.gank.view.adapter.MeizhiAdapter;
import com.dandelion.gank.model.entity.GankResult;
import com.dandelion.gank.view.base.BaseRefreshFragment;
import com.dandelion.gank.view.ui.PhotoActivity;

import java.util.List;

import rx.Subscriber;

/**
 * Created by Administrator on 2016/7/21.
 */
public class MeizhiFragment extends BaseRefreshFragment {

    private MeizhiAdapter mMeizhiAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_first;
    }

    @Override
    public void onCreateAfter(Bundle savedInstanceState) {
        super.onCreateAfter(savedInstanceState);
        mLRecyclerViewAdapter.setOnItemClickListener(new com.github.jdsjlzx.interfaces.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                startActivity(PhotoActivity.newIntent(getActivity(), mMeizhiAdapter.getDataList(), i));
            }
        });
    }

    @Override
    public void initData() {
        mRecyclerView.refresh();
    }



    @Override
    protected void initRecyclerView() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //防止item位置互换
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mRecyclerView.setLayoutManager(layoutManager);
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
                mRecyclerView.refreshComplete(pageSize);
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
                mRecyclerView.refreshComplete(pageSize);
                proData(gankResults);
            }
        };
        RxUtils.getInstance().getGankHomeData(subscriber, pageSize, pageNo);

    }

    @Override
    protected BaseAdapter getAdapter() {
        if (mMeizhiAdapter == null) {
            mMeizhiAdapter = new MeizhiAdapter();
        }
        return mMeizhiAdapter;
    }


    private void proData(List<GankResult> gankResults) {
        mCurrentCounter += gankResults.size();
        getAdapter().addAll(gankResults);
        mLRecyclerViewAdapter.notifyDataSetChanged();


    }
}
