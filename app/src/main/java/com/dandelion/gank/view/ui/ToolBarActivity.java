package com.dandelion.gank.view.ui;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.dandelion.gank.R;
import com.dandelion.gank.view.base.BaseActivity;

import butterknife.BindView;

public abstract class ToolBarActivity extends BaseActivity {

    @BindView(R.id.app_tool)
    Toolbar appTool;

    @BindView(R.id.app_bar)
    AppBarLayout appBar;

//    @Bind(R.id.tool_bar_title)
    TextView mToolTitle;

    private boolean mIsHidden = false;
    public SearchView searchView;


    @Override
    protected void init() {
        super.init();
//        appTool = (Toolbar) findViewById(R.id.app_tool);
//        appBar = (AppBarLayout) findViewById(R.id.app_bar);
        mToolTitle = (TextView) findViewById(R.id.tool_bar_title);
        if (appTool == null) {
            new IllegalThreadStateException("找不到ToolBar");
        } else {
            initActionBar();
        }
    }

    private void initActionBar() {
        setSupportActionBar(appTool);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        if (hasBackButton()) {
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);

            }
        }
    }

    public void setActionBarTitle(String title) {
        mToolTitle.setText(title);
    }

    // 返回true,有返回键
    protected boolean hasBackButton() {
        return true;
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
        if (isSearch()) {
            inflater.inflate(R.menu.search_menu, menu);
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            searchView = (SearchView) menu.findItem(R.id.search).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(true);
            searchView.setFocusable(true);
            searchView.setIconified(false);
            searchView.requestFocusFromTouch();
        }else {
            inflater.inflate(R.menu.main_menu, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    public boolean isSearch() {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if(item.getItemId() == R.id.search) {
            startActivity(new Intent(activity, SearchResultActivity.class));
        } else if(item.getItemId() == R.id.about) {
            startActivity(new Intent(activity, AboutActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }



}
