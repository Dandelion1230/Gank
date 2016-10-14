package com.dandelion.gank.view.ui;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.View;

import com.dandelion.gank.R;
import com.dandelion.gank.model.entity.MyUser;
import com.dandelion.gank.utils.Check;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisteredActivity extends ToolBarActivity {

    @Bind(R.id.user_name)
    TextInputEditText mUserName;
    @Bind(R.id.user_password)
    TextInputEditText mUserPass;
    @Bind(R.id.user_email)
    TextInputEditText mUserEmail;



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

    @OnClick(R.id.btn_register)
    public void registerClick(final View view) {
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
            showSnackbar(view, "邮箱不能为空");
            return;
        } else if (!Check.isEmail(sUserEmail)) {
            showSnackbar(view, "邮箱格式不对");
            return;
        } else {
            MyUser myUser = new MyUser();
            myUser.setUsername(sUserName);
            myUser.setPassword(sUserPass);
            myUser.setEmail(sUserEmail);
            myUser.signUp(new SaveListener<MyUser>() {
                @Override
                public void done(MyUser myUser, BmobException e) {
                    if(e==null){
                        showSnackbar(view, "注册成功:" + myUser.toString());
                    }else{
//                        loge();
                        showSnackbar(view, e.getMessage());

                    }
                }
            });
        }
    }

}
