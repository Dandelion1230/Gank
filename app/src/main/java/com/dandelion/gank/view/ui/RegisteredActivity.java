package com.dandelion.gank.view.ui;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.Menu;
import android.view.View;

import com.dandelion.gank.R;
import com.dandelion.gank.model.entity.RegisteredResult;
import com.dandelion.gank.net.RxUtils;
import com.dandelion.gank.utils.Check;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;

public class RegisteredActivity extends ToolBarActivity {

    @BindView(R.id.user_name)
    TextInputEditText mUserName;
    @BindView(R.id.user_password)
    TextInputEditText mUserPass;
    @BindView(R.id.user_email)
    TextInputEditText mUserEmail;
    private Map<String, Object> params;

    @Override
    public int getLayoutId() {
        return R.layout.activity_registered;
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    public void onCreateAfter(Bundle savedInstanceState) {

    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.btn_register})
    public void registerClick(final View view) {
        if (view.getId() == R.id.btn_register) {
            String sUserName = mUserName.getText().toString().trim();
            String sUserPass = mUserPass.getText().toString().trim();
            String sUserEmail = mUserEmail.getText().toString().trim();
            if (Check.isEmpty(sUserName)) {
                showSnackbar(view, "用户名不能为空");
                return;
            }
            if (Check.isEmpty(sUserPass)) {
                showSnackbar(view, "密码不能为空");
                return;
            }
            if (Check.isEmpty(sUserEmail)) {
                showSnackbar(view, "验证码不能为空");
                return;
            } else {
                params = new HashMap<>();
                params.put("username", sUserName);
                params.put("password", sUserPass);
                registeredUser();
            }
        }

    }

    private void registeredUser() {
        Subscriber<RegisteredResult> subscriber = new Subscriber<RegisteredResult>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                showSnackbar(mUserName, "注册失败:" + e.getMessage());
            }

            @Override
            public void onNext(RegisteredResult registeredResult) {
                showSnackbar(mUserName, "注册成功:" + registeredResult.getSessionToken());
            }
        };
        RxUtils.getInstance().getRegisteredData(subscriber, params);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

}
