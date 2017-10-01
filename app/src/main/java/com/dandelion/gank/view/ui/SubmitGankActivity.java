package com.dandelion.gank.view.ui;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.dandelion.gank.R;
import com.dandelion.gank.utils.Check;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class SubmitGankActivity extends ToolBarActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.gank_url)
    EditText mGankUrl;
    @BindView(R.id.gank_who)
    EditText mGankWho;
    @BindView(R.id.gank_type)
    TextView mGankType;
    @BindView(R.id.gank_desc)
    EditText mGankDesc;
    private String[] mTabTexts = {"安卓", "IOS", "APP", "前端", "休息视频", "瞎推荐", "拓展资源"};
    private BottomSheetDialog dialog;
    private String sGankUrl;
    private String sGankWho;
    private String sGankType;
    private String sGankDesc;


    @Override
    public int getLayoutId() {
        return R.layout.activity_submit_gank;
    }

    @Override
    public void onCreateAfter(Bundle savedInstanceState) {
        setActionBarTitle("提交干货");
    }

    @Override
    public void initData() {

    }

    @Override
    public void setActionBarTitle(String title) {
        super.setActionBarTitle(title);
    }

    @OnClick({R.id.select_type, R.id.submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.select_type:
                if (dialog == null) {
                    showDialog();
                }
                dialog.show();
                break;
            case R.id.submit:
                sGankUrl = mGankUrl.getText().toString().trim();
                sGankWho = mGankWho.getText().toString().trim();
                sGankType = mGankType.getText().toString().trim();
                sGankDesc = mGankDesc.getText().toString().trim();
                if (Check.isEmpty(sGankUrl)) {
                    showSnackbar(view, "请输入干货地址");
                    return;
                }
                if (Check.isEmpty(sGankWho)) {
                    showSnackbar(view, "请输入干货提交者ID");
                    return;
                }
                if (Check.isEmpty(sGankType)) {
                    showSnackbar(view, "请选择干货类型");
                    return;
                }
                if (Check.isEmpty(sGankDesc)) {
                    showSnackbar(view, "请输入干货描述");
                    return;
                }else {
                    submitGank();
                }
                break;
        }
    }

    private void submitGank() {
        Map<String, Object> params = new HashMap<>();
        params.put("url", sGankUrl);
        params.put("who", sGankWho);
        params.put("type", sGankType);
        params.put("desc", sGankDesc);
        params.put("debug", true);

//        Subscriber<String>
    }

    public void showDialog() {
        dialog = new BottomSheetDialog(activity);
        View itemView = LayoutInflater.from(activity).inflate(R.layout.bottom_sheet_layout, null);
        dialog.setContentView(itemView);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        ListView listView = (ListView) itemView.findViewById(R.id.list_view);
        TextView cancel = (TextView) itemView.findViewById(R.id.cancel);
        listView.setAdapter(new ArrayAdapter<>(context, R.layout.item_text, mTabTexts));
        listView.setOnItemClickListener(this);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mGankType.setText(mTabTexts[position]);
        dialog.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
