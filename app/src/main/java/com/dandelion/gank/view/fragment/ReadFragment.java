package com.dandelion.gank.view.fragment;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.dandelion.gank.R;
import com.dandelion.gank.model.entity.xiandu.XianduResult;
import com.dandelion.gank.net.RxUtils;
import com.dandelion.gank.view.adapter.ReadAdapter;
import com.dandelion.gank.view.base.BaseAdapter;
import com.dandelion.gank.view.base.BaseRefreshFragment;
import com.dandelion.gank.view.ui.ReadDetailActivity;
import com.github.jdsjlzx.interfaces.OnItemClickListener;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Administrator on 2016/7/20.
 */
public class ReadFragment extends BaseRefreshFragment {

    private ReadAdapter mReadAdapter;
    private BottomSheetDialog dialog;
    private int positon; // 点击的item的position

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
                startActivity(ReadDetailActivity.newIntent(getActivity(), mReadAdapter.getDataList().get(i).getTitle(),
                        mReadAdapter.getDataList().get(i).getUrl()));
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
        String path = "http://gank.io/xiandu/" + type + "/page/" + pageNo;

        RxUtils.getInstance().getHtmlAnalyticalResult(path)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<XianduResult>>() {
                    @Override
                    public void call(List<XianduResult> results) {
                        mRecyclerView.refreshComplete(pageSize);
                        mCurrentCounter += results.size();
                        mReadAdapter.addAll(results);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mRecyclerView.refreshComplete(pageSize);
                        showShortSnackbar(throwable.getMessage());
                    }
                });

    }

    @Override
    protected BaseAdapter getAdapter() {
        if (mReadAdapter == null) {
            mReadAdapter = new ReadAdapter();
        }
        return mReadAdapter;
    }

}
