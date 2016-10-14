package com.dandelion.gank.view.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dandelion.gank.R;
import com.dandelion.gank.adapter.HomeAdapter;
import com.dandelion.gank.view.adapter.AllAdapter;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.util.RecyclerViewStateUtils;
import com.github.jdsjlzx.view.LoadingFooter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/8/26.
 */
public abstract class BaseFragment extends Fragment implements BaseViewInterface, OnItemClickListener {

    private View view;

    @Bind(R.id.recycler)
    public LRecyclerView mRecyclerView;

    public HomeAdapter mHomeAdapter = null;
    public AllAdapter mAllAdapter = null;
    public LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    public boolean isRefresh = false;

    /**服务器端一共多少条数据*/
    public static int TOTAL_COUNTER = 10000;

    /**每一页展示多少条数据*/
    public static int REQUEST_COUNT = 10;

    /**已经获取到多少条数据了*/
    public static int mCurrentCounter = 0;
    protected String type;
    protected int pageSize = 10;
    protected int pageNo = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getLayoutId() == 0) {
            new IllegalThreadStateException("没有给fragment设置布局");
        } else {
            view = inflater.inflate(getLayoutId(), null);
            ButterKnife.bind(this, view);
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        onCreateAfter(savedInstanceState);
        if (adapterType() == 1) {
            mHomeAdapter = new HomeAdapter(getActivity());
            mLRecyclerViewAdapter = new LRecyclerViewAdapter(getActivity(), mHomeAdapter);
            mRecyclerView.setAdapter(mLRecyclerViewAdapter);
        } else if(adapterType() == 2){
            mAllAdapter = new AllAdapter();
            mLRecyclerViewAdapter = new LRecyclerViewAdapter(getActivity(), mAllAdapter);
            mRecyclerView.setAdapter(mLRecyclerViewAdapter);
        }
        initRecyclerView();
        setListener();
    }

    private void setListener() {
        mLRecyclerViewAdapter.setOnItemClickListener(this);
        mRecyclerView.setLScrollListener(new LRecyclerView.LScrollListener() {
            @Override
            public void onRefresh() {
                isRefresh = true;
                if (isVisibleGone() || isGone) {
                    pageNo = 1;
                    getGankData();
                }
            }

            @Override
            public void onScrollUp() {

            }

            @Override
            public void onScrollDown() {

            }

            @Override
            public void onBottom() {
                LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(mRecyclerView);
                if(state == LoadingFooter.State.Loading) {
//                    TLog.log("the state is Loading, just wait..");
                    return;
                }

                if (mCurrentCounter < TOTAL_COUNTER) {
                    // loading more
                    RecyclerViewStateUtils.setFooterViewState(getActivity(), mRecyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
                    pageNo += 1;
                    getGankData();
                } else {
                    //the end
                    RecyclerViewStateUtils.setFooterViewState(getActivity(), mRecyclerView, REQUEST_COUNT, LoadingFooter.State.TheEnd, null);
                }
            }

            @Override
            public void onScrolled(int distanceX, int distanceY) {

            }
        });
        mRecyclerView.setRefreshing(true);
        isGone = true; // 解决onRefresh和setUserVisibleHint的冲突
    }

    public abstract int adapterType();
    protected boolean isGone = false;
    public boolean isVisibleGone() {
        return false;
    }

    protected abstract void getGankData();

    protected abstract void initRecyclerView();

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            Bundle arguments = getArguments();
            if (arguments != null) {
                type = arguments.getString("data");
                Log.e("TAG", type);
            }
            getGankData();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(view);
    }




}
