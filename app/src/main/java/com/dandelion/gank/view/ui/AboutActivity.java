package com.dandelion.gank.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dandelion.gank.R;
import com.dandelion.gank.utils.CacheUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class AboutActivity extends ToolBarActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.version_code)
    TextView mVersionCode;
    @BindView(R.id.cache_size)
    TextView mCacheSize;
    @BindView(R.id.new_version)
    TextView mNewVersion;
    private BottomSheetDialog dialog;

    @Override
    public int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    public void onCreateAfter(Bundle savedInstanceState) {

    }

    @Override
    public void initData() {
        setActionBarTitle("关于");
        try {
            mCacheSize.setText(CacheUtils.getTotalCacheSize(activity));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @OnClick({R.id.clear_cache, R.id.oss_license, R.id.check_update})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clear_cache:
                if (dialog == null) {
                    showDialog();
                }
                dialog.show();
                break;
            case R.id.check_update:
                break;
            case R.id.oss_license:
                startActivity(new Intent(this, OssLicenseActivity.class));
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void showDialog() {
        dialog = new BottomSheetDialog(activity);
        View itemView = LayoutInflater.from(activity).inflate(R.layout.bottom_sheet_layout, null);
        dialog.setContentView(itemView);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        ListView listView = (ListView) itemView.findViewById(R.id.list_view);
        TextView cancel = (TextView) itemView.findViewById(R.id.cancel);
        listView.setAdapter(new ArrayAdapter<>(context, R.layout.item_text, new String[]{"确定"}));
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
        CacheUtils.clearAllCache(context);
    }
}
