package com.dandelion.gank.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.dandelion.gank.R;
import com.dandelion.gank.model.entity.MyUser;
import com.dandelion.gank.utils.Check;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends ToolBarActivity {

    @Bind(R.id.user_name)
    TextInputEditText mUserName;
    @Bind(R.id.user_password)
    TextInputEditText mUserPass;

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

    @OnClick(value = {R.id.btn_login, R.id.btn_register})
    public void onClick(final View view) {
        if (view.getId() == R.id.btn_login) {
            String sUserName = mUserName.getText().toString().trim();
            String sUserPass = mUserPass.getText().toString().trim();
            if (Check.isEmpty(sUserName)) {
                showSnackbar(view, "用户名不能为空");
                return;
            }
            if (Check.isEmpty(sUserPass)) {
                showSnackbar(view, "密码不能为空");
                return;
            } else {
                MyUser myUser = new MyUser();
                myUser.setUsername(sUserName);
                myUser.setPassword(sUserPass);
                myUser.login(new SaveListener<MyUser>() {
                    @Override
                    public void done(MyUser myUser, BmobException e) {
                        if (e == null) {
                            MyUser user = BmobUser.getCurrentUser(MyUser.class);
                            Log.e("TAG", user.toString());
                            Boolean verified = user.getEmailVerified();
                            if (verified) {
                                showSnackbar(view, "登录成功");
                            } else {
                                showSnackbar(view, "请去邮箱激活");
                            }
                        } else {
                            showSnackbar(view, "登录失败" + e.getMessage());
                        }
                    }
                });
            }

        } else if (view.getId() == R.id.btn_register)
            startActivity(new Intent(context, RegisteredActivity.class));
    }
}
