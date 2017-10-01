package com.dandelion.gank.view.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.dandelion.gank.view.fragment.ReadFragment;

/**
 * Created by Administrator on 2016/7/20.
 */
public class TabReadAdapter extends FragmentPagerAdapter {
    private String[] types;

    public TabReadAdapter(FragmentManager fm, String[] types) {
        super(fm);
        this.types = types;
    }

    @Override
    public Fragment getItem(int position) {
        ReadFragment readFragment = new ReadFragment();
        Bundle bundle = new Bundle();
        bundle.putString("data", types[position]);
        readFragment.setArguments(bundle);
        return readFragment;
    }

    @Override
    public int getCount() {
        return types.length;
    }
}
