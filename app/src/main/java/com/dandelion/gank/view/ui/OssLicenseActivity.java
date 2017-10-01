package com.dandelion.gank.view.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;

import com.dandelion.gank.R;
import com.dandelion.gank.view.adapter.LicensesAdapter;

import java.util.Arrays;

import butterknife.BindView;

public class OssLicenseActivity extends ToolBarActivity {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private String[] mOpenProject = {"butterknife-JakeWharton-Apache Software License 2.0-https://github.com/JakeWharton/butterknife",
            "MaterialDrawer-mikepenz-Apache Software License 2.0-https://github.com/mikepenz/MaterialDrawer",
            "RxJava-ReactiveX-Apache Software License 2.0-https://github.com/ReactiveX/RxJava",
            "RxAndroid-ReactiveX-Apache Software License 2.0-https://github.com/ReactiveX/RxAndroid",
            "retrofit-Square-Apache Software License 2.0-https://github.com/square/retrofit",
            "AVLoadingIndicatorView-81813780-Apache Software License 2.0-https://github.com/81813780/AVLoadingIndicatorView",
            "PhotoView-chrisbanes-Apache Software License 2.0-https://github.com/chrisbanes/PhotoView",
            "glide-bumptech-Apache Software License 2.0-https://github.com/bumptech/glide",
            "LRecyclerView-jdsjlzx-Apache Software License 2.0-https://github.com/jdsjlzx/LRecyclerView"};
    private LicensesAdapter mLicensesAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_oss_license;
    }

    @Override
    public void onCreateAfter(Bundle savedInstanceState) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mLicensesAdapter = new LicensesAdapter();
        mRecyclerView.setAdapter(mLicensesAdapter);
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void initData() {
        mLicensesAdapter.addAll(Arrays.asList(mOpenProject));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
