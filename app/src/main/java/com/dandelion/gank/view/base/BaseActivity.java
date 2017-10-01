package com.dandelion.gank.view.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.dandelion.gank.AppManager;
import com.dandelion.gank.R;
import com.dandelion.gank.utils.SPUtils;
import com.dandelion.gank.utils.SystemBarUtils;
import com.dandelion.gank.view.ui.LoginActivity;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity implements BaseViewInterface {

    protected BaseApplication application;
    protected LayoutInflater inflater;
    protected Context context;
    protected Activity activity;

    private InputMethodManager inputManager;

//    @Bind(R.id.drawer)
//    DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isNeedLogin()) {
            launchLogin();
        }
        if (getLayoutId() != 0) { // 没有设置布局
            setContentView(getLayoutId());
            ButterKnife.bind(this);
        } else {
            new IllegalThreadStateException("没有设置布局LayoutId");
        }
        initStatusBar();
        init();
        onCreateAfter(savedInstanceState);
    }

    private boolean isFirstFocus = true;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (isFirstFocus && hasFocus) {
            initData(); // 此时去初始化数据
        }
    }

    protected void init() {
        AppManager.getAppManager().addActivity(this);
        activity = this;
        context = getApplicationContext();
        application = (BaseApplication) getApplication();
        inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

    }

    protected void initStatusBar() {
//        int color = getResources().getColor(R.color.md_pink_200);
//        SystemBarUtils.tintStatusBarForDrawer(this, mDrawerLayout, color);
        SystemBarUtils.tintStatusBar(this, getResources().getColor(R.color.md_pink_200));
    }


    public boolean isNeedLogin() {
        return false;
    }

    public boolean launchLogin () {
        boolean isLogin = (boolean) SPUtils.getSp(this, "isLogin", false);
        if (!isLogin) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        return isLogin;
    }

    /**
     * 显示输入法
     *
     * @param editTextTemp edittext
     */
    public void showInput(final EditText editTextTemp) {
        if (editTextTemp != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    editTextTemp.setFocusable(true);
                    editTextTemp.requestFocus();
                    inputManager.showSoftInput(editTextTemp, 2);
                }
            }, 200);
        }
    }

    /**
     * 隐藏输入法
     *
     * @param editTextTemp editText
     */
    public void dismissInput(EditText editTextTemp) {
        if (editTextTemp != null && editTextTemp.isFocused()) {
            inputManager.hideSoftInputFromWindow(editTextTemp.getWindowToken(), 0);
        }
    }

    public void showSnackbar(View view, String text) {
        Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show();
    }
}
