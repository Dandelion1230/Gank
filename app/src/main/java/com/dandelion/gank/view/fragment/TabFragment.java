package com.dandelion.gank.view.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.dandelion.gank.R;
import com.dandelion.gank.view.adapter.TabAdapter;
import com.dandelion.gank.view.base.BaseFragment;

import butterknife.BindView;

/**
 * Created by Administrator on 2016/7/20.
 */
public class TabFragment extends BaseFragment {

    @BindView(R.id.tabs)
    TabLayout mTabs;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

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

    private String[] mTabTexts = {"Android", "iOS", "App", "前端", "休息视频",  "瞎推荐", "拓展资源"};
    private TabAdapter mAdapter;

    private void setTabProperty() {
        mTabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        mViewPager.setOffscreenPageLimit(mTabTexts.length - 1);
        mAdapter = new TabAdapter(getChildFragmentManager(), mTabTexts);
        mViewPager.setAdapter(mAdapter);
        mTabs.setupWithViewPager(mViewPager);

        for (int i = 0; i < mTabTexts.length; i++) {
            mTabs.getTabAt(i).setText(mTabTexts[i]);
//            mTabs.addTab(mTabs.newTab().setText(mTabTexts[i]));
        }
    }



}
