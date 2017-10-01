package com.dandelion.gank.view.fragment;

import android.os.Bundle;

import com.dandelion.gank.R;
import com.dandelion.gank.view.base.BaseFragment;

/**
 * Created by Administrator on 2016/7/21.
 */
public abstract class AllFragment extends BaseFragment {


    @Override
    public int getLayoutId() {
        return R.layout.fragment_first;
    }

    @Override
    public void onCreateAfter(Bundle savedInstanceState) {
    }

    @Override
    public void initData() {

    }
//
//    @Override
//    public void onItemClick(View view, int position) {
//        if (mAllAdapter.mDataList.get(position).getType().equals("福利")) {
//            Intent intent = PhotoActivity.newIntent(getActivity(), mAllAdapter.getDataList().get(position).getUrl());
//            startActivity(intent);
//        } else {
//            Intent intent = HomeDetailActivity.newIntent(getActivity(), mAllAdapter.getDataList().get(position).getDesc(), mAllAdapter.getDataList().get(position).getUrl());
//            startActivity(intent);
//        }
//    }
//
//
//    @Override
//    protected void initRecyclerView() {
////        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
////        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
////        //防止item位置互换
////        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
////        mRecyclerView.setLayoutManager(layoutManager);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//    }
//
//    @Override
//    public int adapterType() {
//        return 2;
//    }
//
//    @Override
//    protected void getGankData() {
//        Subscriber<List<GankResult>> subscriber = new Subscriber<List<GankResult>>() {
//            @Override
//            public void onCompleted() {
////                mLoading.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Snackbar.make(mRecyclerView, e.getMessage(), Snackbar.LENGTH_INDEFINITE)
//                        .setAction("重试", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                getGankData();
//                            }
//                        }).show();
//
//            }
//
//            @Override
//            public void onNext(List<GankResult> gankResults) {
//                proData(gankResults);
//            }
//        };
//        RxUtils.getInstance().getGankAllData(subscriber, pageSize, pageNo);
//        Log.e("TAG", "pageSize:" + pageSize + "  pageNo:" + pageNo);
//
//    }
//
//
//    private void proData(List<GankResult> gankResults) {
//        if (isRefresh) {  // 下拉刷新
//            mAllAdapter.clear();
//            mCurrentCounter = 0;
////            mHomeAdapter.addAll(gankResults);
//        }
//        mAllAdapter.addAll(gankResults);
//        mCurrentCounter += gankResults.size();
//        if (isRefresh) {
//            isRefresh = false;
//            mRecyclerView.refreshComplete();
//            notifyDataSetChanged();
//        } else {
//            RecyclerViewStateUtils.setFooterViewState(mRecyclerView, LoadingFooter.State.Normal);
//        }
//    }
//
//    private void notifyDataSetChanged() {
//        mLRecyclerViewAdapter.notifyDataSetChanged();
//    }

}
