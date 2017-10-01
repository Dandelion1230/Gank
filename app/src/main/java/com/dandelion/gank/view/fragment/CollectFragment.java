package com.dandelion.gank.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.dandelion.gank.R;
import com.dandelion.gank.model.entity.GankResult;
import com.dandelion.gank.model.entity.GankRoot;
import com.dandelion.gank.net.RxUtils;
import com.dandelion.gank.utils.SPUtils;
import com.dandelion.gank.view.adapter.CollectionAdapter;
import com.dandelion.gank.view.base.BaseAdapter;
import com.dandelion.gank.view.base.BaseRefreshFragment;
import com.dandelion.gank.view.ui.HomeDetailActivity;

import java.util.List;

import rx.Subscriber;

/**
 * Created by Administrator on 2016/7/20.
 */
public class CollectFragment extends BaseRefreshFragment implements CollectionAdapter.OnItemClickListener {
    private CollectionAdapter mCollectAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_collect;
    }

    @Override
    public void onCreateAfter(Bundle savedInstanceState) {
        super.onCreateAfter(savedInstanceState);

    }

    @Override
    public void initData() {
        mRecyclerView.refresh();
    }


    @Override
    protected void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            mRecyclerView.refresh();
        }
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
            public void onNext(List<GankResult> collectionList) {
                mRecyclerView.refreshComplete(pageSize);
                proData(collectionList);
            }
        };
        String userId = (String) SPUtils.getSp(mContext, "userId", "");
        String where = "{\"userId\":\""+userId+"\"}";
        RxUtils.getInstance().getCollectionData(subscriber, where);

    }

    @Override
    protected BaseAdapter getAdapter() {
        if (mCollectAdapter == null) {
            mCollectAdapter = new CollectionAdapter();
            mCollectAdapter.setOnItemClickListener(this);
        }
        return mCollectAdapter;
    }


    private void proData(List<GankResult> collectionList) {
        mCurrentCounter += collectionList.size();
        getAdapter().addAll(collectionList);
    }


    @Override
    public void onDelClick(int positon) {
        delCollectionData(positon);
    }

    @Override
    public void onItemClick(int positon) {
        startActivity(HomeDetailActivity.newIntent(getActivity(), mCollectAdapter.getDataList().get(positon)));
    }

    private void delCollectionData(final int positon) {
        GankResult result = (GankResult) getAdapter().getDataList().get(positon);
        String objectId = result.getObjectId();
        Subscriber<GankRoot> subscriber = new Subscriber<GankRoot>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                showShortSnackbar(e.getMessage());
            }

            @Override
            public void onNext(GankRoot root) {
                if (root.getMsg().equals("ok")) {
                    showShortSnackbar("删除成功");
                    getAdapter().delete(positon);
                }
            }
        };
        RxUtils.getInstance().getDelCollectionData(subscriber, objectId);

    }
}
