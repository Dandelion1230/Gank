package com.dandelion.gank.view.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.dandelion.gank.R;
import com.dandelion.gank.adapter.HomeAdapter;
import com.dandelion.gank.model.entity.GankResult;
import com.dandelion.gank.net.HttpUtils;
import com.dandelion.gank.utils.SnackbarUtils;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.util.RecyclerViewStateUtils;
import com.github.jdsjlzx.view.LoadingFooter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;

public class TestActivity extends ToolBarActivity {

    @Bind(R.id.recycler)
    LRecyclerView mRecycler;

    private HomeAdapter mHomeAdapter = null;
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    private boolean isRefresh = false;

    /**服务器端一共多少条数据*/
    private static final int TOTAL_COUNTER = 34;

    /**每一页展示多少条数据*/
    private static final int REQUEST_COUNT = 10;

    /**已经获取到多少条数据了*/
    private static int mCurrentCounter = 0;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_test);
//        ButterKnife.bind(this);
//        mHomeAdapter = new HomeAdapter(this);
//        mLRecyclerViewAdapter = new LRecyclerViewAdapter(this, mHomeAdapter);
//        mRecycler.setAdapter(mLRecyclerViewAdapter);
//        initRecyclerView();
//        setListener();
//    }

    private void setListener() {
        mRecycler.setLScrollListener(new LRecyclerView.LScrollListener() {
            @Override
            public void onRefresh() {
                isRefresh = true;
                getGankHomeData();
            }

            @Override
            public void onScrollUp() {

            }

            @Override
            public void onScrollDown() {

            }

            @Override
            public void onBottom() {
                LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(mRecycler);
                if(state == LoadingFooter.State.Loading) {
//                    TLog.log("the state is Loading, just wait..");
                    return;
                }
//                RecyclerViewStateUtils.setFooterViewState(getActivity(), mRecycler, REQUEST_COUNT, LoadingFooter.State.Loading, null);
//                getGankHomeData();

                if (mCurrentCounter < TOTAL_COUNTER) {
                    // loading more
                    RecyclerViewStateUtils.setFooterViewState(TestActivity.this, mRecycler, REQUEST_COUNT, LoadingFooter.State.Loading, null);
                    getGankHomeData();
                } else {
                    //the end
                    RecyclerViewStateUtils.setFooterViewState(TestActivity.this, mRecycler, REQUEST_COUNT, LoadingFooter.State.TheEnd, null);
                }
            }

            @Override
            public void onScrolled(int distanceX, int distanceY) {

            }
        });
        mRecycler.setRefreshing(true);
        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                SnackbarUtils.show(mRecycler, "onItemClick", Snackbar.LENGTH_SHORT);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                SnackbarUtils.show(mRecycler, "onItemLongClick", Snackbar.LENGTH_SHORT);
            }
        });
    }

    private void initRecyclerView() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //防止item位置互换
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mRecycler.setLayoutManager(layoutManager);
//        mRecycler.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getGankHomeData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Subscriber<List<GankResult>> subscriber = new Subscriber<List<GankResult>>() {
                    @Override
                    public void onCompleted() {
//                mLoading.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Snackbar.make(mRecycler, e.getMessage(), Snackbar.LENGTH_INDEFINITE)
                                .setAction("重试", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        getGankHomeData();
                                    }
                                }).show();

                    }

                    @Override
                    public void onNext(List<GankResult> gankResults) {
                        proData(gankResults);
                    }
                };
                HttpUtils.getInstance().getGankHomeData(subscriber, 10, 1);
            }
        }, 1000);

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
            mRecycler.refreshComplete();
            notifyDataSetChanged();
        } else {
            RecyclerViewStateUtils.setFooterViewState(mRecycler, LoadingFooter.State.Normal);
        }
    }

    private void notifyDataSetChanged() {
        mLRecyclerViewAdapter.notifyDataSetChanged();
    }


//    @OnClick(R.id.fab)
//    public void fabOnClick(View view) {
//        mRecycler.smoothScrollToPosition(0);
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    public void onCreateAfter(Bundle savedInstanceState) {
        mHomeAdapter = new HomeAdapter(this);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(this, mHomeAdapter);
        mRecycler.setAdapter(mLRecyclerViewAdapter);
        initRecyclerView();
        setListener();

    }

    @Override
    public void initData() {

    }
}
