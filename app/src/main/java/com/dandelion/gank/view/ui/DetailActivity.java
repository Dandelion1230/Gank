package com.dandelion.gank.view.ui;

import android.content.Context;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.dandelion.gank.R;
import com.dandelion.gank.view.base.BaseActivity;

import butterknife.Bind;

public class DetailActivity extends ToolBarActivity {

    public static final String EXTRA_URL = "url";
    public static final String EXTRA_DESC = "desc";

    @Bind(R.id.progress_bar)
    ContentLoadingProgressBar mProgressBar;
    @Bind(R.id.webview)
    WebView mWebView;
    private String url;
    private String desc;


    public static Intent newIntent(Context context, String desc, String url) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(EXTRA_DESC, desc);
        intent.putExtra(EXTRA_URL, url);
        return intent;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_detail;
    }

    @Override
    public void onCreateAfter(Bundle savedInstanceState) {
        url = getIntent().getStringExtra(EXTRA_URL);
        desc = getIntent().getStringExtra(EXTRA_DESC);
        setActionBarTitle(desc);
//        TextView mTextView = new TextView(context);
//        mTextView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
//        mTextView.setSingleLine(true);
//        mTextView.setText(desc);
//        appTool.addView(mTextView);
//        setTitle(desc);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        if (null != savedInstanceState) {
            mWebView.restoreState(savedInstanceState);
        }
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                mProgressBar.setProgress(newProgress);
            }
        });
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        mWebView.loadUrl(url);

    }

    @Override
    public void initData() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mWebView.saveState(outState);
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() { // 推出当前Activity销毁mWebView
        mWebView.stopLoading();
        mWebView.removeAllViews();
        mWebView.destroy();
        mWebView = null;
        super.onDestroy();
    }
}
