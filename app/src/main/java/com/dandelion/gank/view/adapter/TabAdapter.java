package com.dandelion.gank.view.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.dandelion.gank.view.fragment.AllFragment;
import com.dandelion.gank.view.fragment.OtherFragment;

/**
 * Created by Administrator on 2016/7/20.
 */
public class TabAdapter extends FragmentPagerAdapter {
    private FragmentManager fm;
    private int count;
    private String[] types = {"all", "Android" , "iOS" , "App" , "前端" , "休息视频", "瞎推荐", "拓展资源"};

    public TabAdapter(FragmentManager fm, int count) {
        super(fm);
        this.fm = fm;
        this.count = count;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0) {
            Fragment allFragment = new AllFragment();
            return allFragment;
        } else {
            Fragment otherFragment = new OtherFragment();
            Bundle bundle = new Bundle();
            bundle.putString("data", types[position]);
            otherFragment.setArguments(bundle);
            return otherFragment;
        }
    }

    @Override
    public int getCount() {
        return count;
    }
}
