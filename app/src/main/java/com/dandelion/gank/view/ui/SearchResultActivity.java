package com.dandelion.gank.view.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.dandelion.gank.R;
import com.dandelion.gank.model.entity.GankResult;
import com.dandelion.gank.net.RxUtils;
import com.dandelion.gank.utils.ProgressDialogUtils;
import com.dandelion.gank.view.adapter.SearchAdapter;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

import java.util.List;

import butterknife.BindView;
import rx.Subscriber;

public class SearchResultActivity extends ToolBarActivity {

    @BindView(R.id.recycler)
    public LRecyclerView mRecyclerView;

    public LRecyclerViewAdapter mLRecyclerViewAdapter = null;

    /**服务器端一共多少条数据*/
    public static int TOTAL_COUNTER = 10000;

    /**已经获取到多少条数据了*/
    public static int mCurrentCounter = 0;
    protected String type;
    protected int pageSize = 10;
    protected int pageNo = 1;
    private SearchAdapter mSearchAdapter;
    private String keyword = "";


    @Override
    public int getLayoutId() {
        return R.layout.activity_search_result;
    }

    @Override
    public void onCreateAfter(Bundle savedInstanceState) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        mSearchAdapter = new SearchAdapter();
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mSearchAdapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);
        mRecyclerView.setHasFixedSize(true);
        setListener();

    }

    @Override
    public void initData() {
        setActionBarTitle("搜索");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                keyword = query;
                mSearchAdapter.clear();
                mLRecyclerViewAdapter.notifyDataSetChanged();
                mCurrentCounter = 0;
                pageNo = 1;
                getGankData(keyword);
                if (searchView != null) {
                    // 得到输入管理对象
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        // 这将让键盘在所有的情况下都被隐藏，但是一般我们在点击搜索按钮后，输入法都会乖乖的自动隐藏的。
                        imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0); // 输入法如果是显示状态，那么就隐藏输入法
                    }
                    searchView.clearFocus(); // 不获取焦点
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
    }

    private void setListener() {

        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSearchAdapter.clear();
                mLRecyclerViewAdapter.notifyDataSetChanged();
                mCurrentCounter = 0;
                pageNo = 1;
                getGankData(keyword);
            }
        });
        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (mCurrentCounter < TOTAL_COUNTER) {
                    pageNo++;
                    getGankData(keyword);
                }else {
                    mRecyclerView.setNoMore(true);
                }
            }
        });
        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                startActivity(HomeDetailActivity.newIntent(activity, mSearchAdapter.getDataList().get(i)));
            }
        });
    }

    protected void getGankData(String keyword) {
        ProgressDialogUtils.getInstance().showLoad(activity, "正在请求，请稍等...");
        Subscriber<List<GankResult>> subscriber = new Subscriber<List<GankResult>>() {
            @Override
            public void onCompleted() {
                ProgressDialogUtils.getInstance().hideLoad();
                mRecyclerView.refreshComplete(pageSize);
            }

            @Override
            public void onError(Throwable e) {
                ProgressDialogUtils.getInstance().hideLoad();
                showSnackbar(mRecyclerView, e.getMessage());

            }

            @Override
            public void onNext(List<GankResult> gankResults) {
                mRecyclerView.refreshComplete(pageSize);
                proData(gankResults);
            }
        };
        RxUtils.getInstance().getGankSearchData(subscriber, keyword, pageSize, pageNo);

    }

    private void proData(List<GankResult> collectionList) {
        mCurrentCounter += collectionList.size();
        mSearchAdapter.addAll(collectionList);
        mLRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean isSearch() {
        return true;
    }
}
