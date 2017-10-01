package com.dandelion.gank.view.ui;

import android.os.Bundle;
import android.view.Menu;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dandelion.gank.R;
import com.dandelion.gank.utils.SystemUtils;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;

public class HelpGrammarActivity extends ToolBarActivity {

    @BindView(R.id.web_view)
    WebView mWebView;
    private String str;

    @Override
    public int getLayoutId() {
        return R.layout.activity_help_grammar;
    }

    @Override
    public void onCreateAfter(Bundle savedInstanceState) {
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new MdWebViewClient());
        mWebView.loadUrl("file:///android_asset/markdown.html");
        try {
            InputStream inputStream = context.getAssets().open("syntax_hepler.md");
            str = new String(SystemUtils.readInputStream(inputStream));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void initData() {
        setActionBarTitle("参考语法");
    }

    private class MdWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            boolean z = true;
            mWebView.loadUrl("javascript:parseMarkdown(\"" + str.replace("\n", "\\n").replace("\"", "\\\"").replace("'", "\\'") + "\", " + z + ")");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    protected void onDestroy() {
        mWebView.stopLoading();
        mWebView.removeAllViews();
        mWebView.destroy();
        mWebView = null;
        super.onDestroy();
    }
}
