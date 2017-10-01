package com.dandelion.gank.view.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dandelion.gank.utils.SPUtils;
import com.dandelion.gank.view.ui.LoginActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2016/8/26.
 */
public abstract class BaseFragment extends Fragment implements BaseViewInterface {

    public View view;
    private Unbinder bind;
    protected Context mContext;
    protected Activity mActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getLayoutId() == 0) {
            new IllegalThreadStateException("没有给fragment设置布局");
        } else {
            view = inflater.inflate(getLayoutId(), null);
            bind = ButterKnife.bind(this, view);
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getContext();
        mActivity = getActivity();
        onCreateAfter(savedInstanceState);
        initData();
    }


    public boolean launchLogin() {
        boolean isLogin = (boolean) SPUtils.getSp(mContext, "isLogin", false);
        if (!isLogin) {
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
        }
        return isLogin;
    }

    public void showActionSnackbar(String msg, String textAction, View.OnClickListener onClickListener) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).setAction(textAction, onClickListener).show();
    }
    public void showShortSnackbar(String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show();
    }
    public void showLongSnackbar(String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
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
        bind.unbind();
    }

}
