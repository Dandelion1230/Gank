package com.dandelion.gank.view.ui;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.Menu;
import android.view.View;
import com.dandelion.gank.R;
import com.dandelion.gank.model.entity.ErrorResult;
import com.dandelion.gank.model.entity.LoginResult;
import com.dandelion.gank.model.entity.RegisteredResult;
import com.dandelion.gank.net.RxUtils;
import com.dandelion.gank.utils.Check;
import com.dandelion.gank.utils.ProgressDialogUtils;
import com.dandelion.gank.utils.SPUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

public class LoginActivity extends ToolBarActivity {

    @BindView(R.id.user_name)
    TextInputEditText mUserName;
    @BindView(R.id.user_password)
    TextInputEditText mUserPass;
    private Map<String, Object> params;
    private String sUserName;
    private String sUserPass;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void init() {
        super.init();
        setActionBarTitle("登录");
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onCreateAfter(Bundle savedInstanceState) {

    }

    @Override
    public void initData() {

    }

    @OnClick(value = {R.id.btn_login/*, R.id.tv_register, R.id.forget_password*/})
    public void onClick(final View view) {
        if (view.getId() == R.id.btn_login) {
            sUserName = mUserName.getText().toString().trim();
            sUserPass = mUserPass.getText().toString().trim();
            if (Check.isEmpty(sUserName)) {
                showSnackbar(view, "用户名不能为空");
                return;
            }
            if (Check.isEmpty(sUserPass)) {
                showSnackbar(view, "密码不能为空");
                return;
            } else {
                params = new HashMap<>();
                params.put("username", sUserName);
                params.put("password", sUserPass);
                loginUser();
            }

        }
//        else if (view.getId() == R.id.tv_register) {
//            startActivity(new Intent(context, RegisteredActivity.class));
//        }else {
//            startActivity(new Intent(context, RegisteredActivity.class));
//        }
    }

    private void loginUser() {
        ProgressDialogUtils.getInstance().showLoad(activity, "正在登录，请稍等...");
        Subscriber<LoginResult> subscriber = new Subscriber<LoginResult>() {
            @Override
            public void onCompleted() {
                ProgressDialogUtils.getInstance().hideLoad();
            }

            @Override
            public void onError(Throwable throwable) {
                if(throwable instanceof HttpException){
                    HttpException httpException= (HttpException) throwable;
                    try {
                        String errorBody = httpException.response().errorBody().string();
                        Gson gson = new Gson();
                        ErrorResult errorResult = gson.fromJson(errorBody, ErrorResult.class);
                        if (errorResult.getCode() == 101) {
                            registeredUser();
                        } else {
                            ProgressDialogUtils.getInstance().hideLoad();
                            showSnackbar(mUserName, errorResult.getError());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    ProgressDialogUtils.getInstance().hideLoad();
                    showSnackbar(mUserName, "登录失败:" + throwable.getMessage());
                }
            }

            @Override
            public void onNext(LoginResult loginResult) {
                SPUtils.setSP(context, "userId", loginResult.getObjectId());
                SPUtils.setSP(context, "isLogin", true);
                SPUtils.setSP(context, "loginName", sUserName);
//                showSnackbar(mUserName, "登录成功");
                EventBus.getDefault().post(sUserName);
                finish();

            }
        };
        RxUtils.getInstance().getLoginData(subscriber, sUserName, sUserPass);
    }

    private void registeredUser() {
        Subscriber<RegisteredResult> subscriber = new Subscriber<RegisteredResult>() {
            @Override
            public void onCompleted() {
                ProgressDialogUtils.getInstance().hideLoad();
            }

            @Override
            public void onError(Throwable throwable) {
                ProgressDialogUtils.getInstance().hideLoad();
                if(throwable instanceof HttpException){
                    HttpException httpException= (HttpException) throwable;
                    try {
                        String errorBody = httpException.response().errorBody().string();
                        Gson gson = new Gson();
                        ErrorResult errorResult = gson.fromJson(errorBody, ErrorResult.class);
                        if (errorResult.getCode() == 202) {
                            showSnackbar(mUserName, "该用户名已被注册，请换个用户名");
                        } else {
                            showSnackbar(mUserName, errorResult.getError());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    showSnackbar(mUserName, "注册失败:" + throwable.getMessage());
                }
            }

            @Override
            public void onNext(RegisteredResult registeredResult) {
                SPUtils.setSP(context, "userId", registeredResult.getObjectId());
                SPUtils.setSP(context, "loginName", sUserName);
                SPUtils.setSP(context, "isLogin", true);
//                showSnackbar(mUserName, "登录成功");
                EventBus.getDefault().post(sUserName);
                finish();
            }
        };
        RxUtils.getInstance().getRegisteredData(subscriber, params);
    }
}
