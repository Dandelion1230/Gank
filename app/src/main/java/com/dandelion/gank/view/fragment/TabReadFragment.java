package com.dandelion.gank.view.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.dandelion.gank.R;
import com.dandelion.gank.view.adapter.TabReadAdapter;
import com.dandelion.gank.view.base.BaseFragment;

import butterknife.BindView;

/**
 * Created by Administrator on 2016/7/20.
 */
public class TabReadFragment extends BaseFragment {
    @BindView(R.id.tabs)
    TabLayout mTabs;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    private String[] mTabTexts = {"科技资讯", "趣味软件", "装备党", "草根新闻", "Android",  "创业新闻", "独立思想", "iOS", "团队博客"};
    private String[] types = {"wow", "apps", "imrich", "funny", "android",  "diediedie", "thinking", "iOS", "teamblog"};
    private TabReadAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_tab;
    }

    @Override
    public void onCreateAfter(Bundle savedInstanceState) {
        setTabProperty();
    }

    @Override
    public void initData() {

    }

    private void setTabProperty() {
        mTabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        mViewPager.setOffscreenPageLimit(mTabTexts.length - 1);
        mAdapter = new TabReadAdapter(getChildFragmentManager(), types);
        mViewPager.setAdapter(mAdapter);
        mTabs.setupWithViewPager(mViewPager);

        for (int i = 0; i < mTabTexts.length; i++) {
            mTabs.getTabAt(i).setText(mTabTexts[i]);
        }
    }



}
