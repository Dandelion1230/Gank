package com.dandelion.gank.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dandelion.gank.R;
import com.dandelion.gank.view.adapter.TabAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/7/20.
 */
public class TabFragment extends Fragment {
    @Bind(R.id.tabs)
    TabLayout mTabs;
    @Bind(R.id.view_pager)
    ViewPager mViewPager;

    private String[] mTabTexts = {"全部","安卓", "IOS", "APP", "前端", "休息视频",  "瞎推荐", "拓展资源"};
    private TabAdapter mAdapter;
    private List<Fragment> TabContentFragment;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        setTabProperty();
    }

    private void setTabProperty() {
        mTabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        mViewPager.setOffscreenPageLimit(mTabTexts.length - 1);
//        mAdapter = new TabAdapter(getActivity().getSupportFragmentManager(), mTabTexts.length);
        mAdapter = new TabAdapter(getFragmentManager(), mTabTexts.length);
        mViewPager.setAdapter(mAdapter);
        mTabs.setupWithViewPager(mViewPager);

        for (int i = 0; i < mTabTexts.length; i++) {
            mTabs.getTabAt(i).setText(mTabTexts[i]);
//            mTabs.addTab(mTabs.newTab().setText(mTabTexts[i]));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
