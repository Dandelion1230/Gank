package com.dandelion.gank.view.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.dandelion.gank.view.fragment.HomeFragment;

/**
 * Created by Administrator on 2016/7/20.
 */
public class TabAdapter extends FragmentPagerAdapter {
    private FragmentManager fm;
    private String[] types;

    public TabAdapter(FragmentManager fm, String[] types) {
        super(fm);
        this.fm = fm;
        this.types = types;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment homeFragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("data", types[position]);
        homeFragment.setArguments(bundle);
        return homeFragment;
    }

    @Override
    public int getCount() {
        return types.length;
    }
}
