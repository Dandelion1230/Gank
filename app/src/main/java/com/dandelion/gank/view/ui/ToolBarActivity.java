package com.dandelion.gank.view.ui;

import android.app.SearchManager;
import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.DecelerateInterpolator;

import com.dandelion.gank.R;
import com.dandelion.gank.view.base.BaseActivity;

import butterknife.Bind;

public abstract class ToolBarActivity extends BaseActivity {

//    @Bind(R.id.app_tool)
    Toolbar appTool;

//    @Bind(R.id.app_bar)
    AppBarLayout appBar;

    private boolean mIsHidden = false;


    @Override
    protected void init() {
        super.init();
        appTool = (Toolbar) findViewById(R.id.app_tool);
        appBar = (AppBarLayout) findViewById(R.id.app_bar);
        if (appTool == null) {
            new IllegalThreadStateException("找不到ToolBar");
        } else {
            initActionBar();
        }
    }

    private void initActionBar() {
        setSupportActionBar(appTool);
        if (hasBackButton()) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void setActionBarTitle(String title) {
        appTool.setTitle(title);
    }

    // 返回true,有返回键
    protected boolean hasBackButton() {
        return false;
    }


    public Toolbar getToolbar() {
        return appTool;
    }

    protected void hideOrShowToolbar() {
        appBar.animate()
                .translationY(mIsHidden ? 0 : -appBar.getHeight())
                .setInterpolator(new DecelerateInterpolator(2))
                .start();
        mIsHidden = !mIsHidden;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
