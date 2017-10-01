package com.dandelion.gank.view.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import com.dandelion.gank.R;
import com.dandelion.gank.model.entity.InsertDataResult;
import com.dandelion.gank.net.RxUtils;
import com.dandelion.gank.utils.Check;
import com.dandelion.gank.utils.ProgressDialogUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;

public class FeedbackActivity extends ToolBarActivity {


    @BindView(R.id.contact_way)
    EditText mContactWay;
    @BindView(R.id.feedback_content)
    EditText mFeedback;

    @Override
    public int getLayoutId() {
        return R.layout.activity_feedback;
    }

    @Override
    public void onCreateAfter(Bundle savedInstanceState) {
    }

    @Override
    public void initData() {
        setActionBarTitle("反馈");
    }

    @OnClick(R.id.submit)
    public void onClick(final View view) {
        String sContactWay = mContactWay.getText().toString().trim();
        String sFeedback = mFeedback.getText().toString().trim();
        if (Check.isEmpty(sContactWay)) {
            showSnackbar(view, "请输入联系方式");
            return;
        }
        if (Check.isEmpty(sFeedback)) {
            showSnackbar(view, "请输入反馈内容");
            return;
        }else {
            ProgressDialogUtils.getInstance().showLoad(activity, "正在上传，请稍等...");
            Map<String, Object> params = new HashMap<>();
            params.put("contactWay", sContactWay);
            params.put("feedback", sFeedback);
            Subscriber<InsertDataResult> subscriber = new Subscriber<InsertDataResult>() {
                @Override
                public void onCompleted() {
                    ProgressDialogUtils.getInstance().hideLoad();
                }

                @Override
                public void onError(Throwable e) {
                    ProgressDialogUtils.getInstance().hideLoad();
                    showSnackbar(view, e.getMessage());
                }

                @Override
                public void onNext(InsertDataResult insertDataResult) {
                    showSnackbar(view, "反馈成功");
                }
            };
            RxUtils.getInstance().AddFeedbackData(subscriber, params);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
