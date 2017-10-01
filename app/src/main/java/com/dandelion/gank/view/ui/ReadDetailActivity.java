package com.dandelion.gank.view.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dandelion.gank.R;
import com.dandelion.gank.model.entity.GankResult;
import com.dandelion.gank.utils.SharesUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ReadDetailActivity extends ToolBarActivity {

    @BindView(R.id.progress_bar)
    ContentLoadingProgressBar mProgressBar;
    @BindView(R.id.webview)
    WebView mWebView;

    private static final String EXTRA_DESC = "desc";
    private static final String EXTRA_URL = "url";
    private String desc;
    private String url;

    public static Intent newIntent(Context context, String desc, String url) {
        Intent intent = new Intent(context, ReadDetailActivity.class);
        intent.putExtra(EXTRA_DESC, desc);
        intent.putExtra(EXTRA_URL, url);
        return intent;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_read_detail;
    }

    @Override
    public void onCreateAfter(Bundle savedInstanceState) {
        url = getIntent().getStringExtra(EXTRA_URL);
        desc = getIntent().getStringExtra(EXTRA_DESC);

        setActionBarTitle(desc);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        if (null != savedInstanceState) {
            mWebView.restoreState(savedInstanceState);
        }
        mWebView.loadUrl(url);
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                mProgressBar.setProgress(newProgress);
            }
        });
        mWebView.addJavascriptInterface(this, "imagelistner");
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                view.getSettings().setJavaScriptEnabled(true);
                super.onPageFinished(view, url);
                addImageClickListner();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                view.getSettings().setJavaScriptEnabled(true);
                super.onPageStarted(view, url, favicon);
            }
        });

    }

    // js通信接口
    @JavascriptInterface
    public void openImage(String img) {
        Log.e("TAG", img);
        System.out.println(img);
        GankResult result = new GankResult();
        result.setUrl(img);
        List<GankResult> resultList = new ArrayList<>();
        resultList.add(result);
        startActivity(PhotoActivity.newIntent(context, resultList, 0));
    }

    // 注入js函数监听
    private void addImageClickListner() {
        try {
            // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
            mWebView.loadUrl("javascript:(function(){"
                    + "var objs = document.getElementsByTagName(\"img\"); "
                    + "for(var i=0;i<objs.length;i++)  " + "{"
                    + "    objs[i].onclick=function()  " + "    {  "
                    + "        window.imagelistner.openImage(this.src);  "
                    + "    }  " + "}" + "})()");
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.read_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                SharesUtils.shareText(activity, url);
                break;
            case R.id.refresh:
                mWebView.loadUrl(url);
                break;
            case R.id.copy_link:
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", url);
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                showSnackbar(mWebView, "复制成功");
                break;
            case R.id.open_link:
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(url);
                intent.setData(content_url);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
