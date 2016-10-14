package com.dandelion.gank.view.base;

import android.app.Activity;
import android.content.Context;
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
import com.dandelion.gank.URLs;
import com.dandelion.gank.utils.SystemBarUtils;

import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;

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
        //第一：默认初始化
        // Bmob.initialize(this, "Your Application ID");

        //第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        BmobConfig config = new BmobConfig.Builder(this)
                ////设置appkey
                .setApplicationId(URLs.BMOB_ID)
                ////请求超时时间（单位为秒）：默认15s
                .setConnectTimeout(30)
                ////文件分片上传时每片的大小（单位字节），默认512*1024
                .setUploadBlockSize(1024 * 1024)
                ////文件的过期时间(单位为秒)：默认1800s
                .setFileExpiration(2500)
                .build();
        Bmob.initialize(config);
        if (isNeedLogin()) {
            finish();
//            Intent intent = new Intent(this, LoginActivity.class);
//            startActivity(intent);
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
