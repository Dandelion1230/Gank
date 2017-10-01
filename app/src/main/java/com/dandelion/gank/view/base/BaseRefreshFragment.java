package com.dandelion.gank.view.base;

import android.os.Bundle;
import android.util.Log;

import com.dandelion.gank.R;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

import butterknife.BindView;


/**
 * Created by Administrator on 2017/6/8.
 */

public abstract class BaseRefreshFragment extends BaseFragment {

    @BindView(R.id.recycler)
    public LRecyclerView mRecyclerView;

    public LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    protected String[] sBottomSheetText = {"收藏", "取消"};


    /**服务器端一共多少条数据*/
    public static int TOTAL_COUNTER = 10000;

    /**已经获取到多少条数据了*/
    public static int mCurrentCounter = 0;
    protected String type;
    protected int pageSize = 10;
    protected int pageNo = 1;

    protected boolean isLoad = false; // 是否加载过数据，第二次就不加载数据了
    private boolean isVisible;

    protected abstract void getGankData();

    protected abstract BaseAdapter getAdapter();

    protected abstract void initRecyclerView();

    @Override
    public void onCreateAfter(Bundle savedInstanceState) {
        initRecyclerView();
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(getAdapter());
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);
        mRecyclerView.setHasFixedSize(true);
        setListener();
        onVisible();

    }

    private void setListener() {

        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAdapter().clear();
                mLRecyclerViewAdapter.notifyDataSetChanged();
                mCurrentCounter = 0;
                pageNo = 1;
                getGankData();
            }
        });
        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (mCurrentCounter < TOTAL_COUNTER) {
                    pageNo++;
                    getGankData();
                }else {
                    mRecyclerView.setNoMore(true);
                }
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            Bundle arguments = getArguments();
            if (arguments != null) {
                type = arguments.getString("data");
                Log.e("TAG", type);
            }
            isVisible = true;
            onVisible();
        }
    }

    public void onVisible() {
        if (isVisible && view != null && !isLoad) {
            isLoad = true;
            mRecyclerView.refresh();
        }
    }

}
