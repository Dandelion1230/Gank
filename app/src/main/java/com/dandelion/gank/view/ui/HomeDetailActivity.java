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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.dandelion.gank.R;
import com.dandelion.gank.model.entity.CollectionResult;
import com.dandelion.gank.model.entity.GankResult;
import com.dandelion.gank.model.entity.GankRoot;
import com.dandelion.gank.model.entity.InsertDataResult;
import com.dandelion.gank.net.RxUtils;
import com.dandelion.gank.utils.SPUtils;
import com.dandelion.gank.view.widget.ExpandableLinearLayout;
import com.dandelion.gank.view.widget.MyBottomSheetDialog;
import com.dandelion.gank.view.widget.TabIconView;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Subscriber;


public class HomeDetailActivity extends ToolBarActivity implements View.OnClickListener {

    public static final String EXTRA_OBJ = "object";

    @BindView(R.id.progress_bar)
    ContentLoadingProgressBar mProgressBar;
    @BindView(R.id.webview)
    WebView mWebView;
//    private String url;
//    private String desc;
    private MyBottomSheetDialog dialog;
    private ImageView closeDialog;
    private ImageView actionUndo;
    private ImageView actionRedo;
    private ImageView actionSave;
    private ImageView actionMoreSwitch;
    private ImageView actionMore;
    private ExpandableLinearLayout actionOther;
    private TabIconView tabIconView;
    private TextView title;
    private TextView content;
    private MenuItem collectMenu;
    private GankResult mGankResult;
    private String objectId;


    public static Intent newIntent(Context context, Object object) {
        Intent intent = new Intent(context, HomeDetailActivity.class);
        intent.putExtra(EXTRA_OBJ, (Serializable) object);
//        intent.putExtra(EXTRA_URL, url);
        return intent;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_detail;
    }

    @Override
    public void onCreateAfter(Bundle savedInstanceState) {
//        url = getIntent().getStringExtra(EXTRA_URL);
//        desc = getIntent().getStringExtra(EXTRA_DESC);
        mGankResult = (GankResult) getIntent().getSerializableExtra(EXTRA_OBJ);
        setActionBarTitle(mGankResult.getDesc());
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        if (null != savedInstanceState) {
            mWebView.restoreState(savedInstanceState);
        }
        mWebView.loadUrl(mGankResult.getUrl());
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
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_menu, menu);
        collectMenu = menu.findItem(R.id.collect);
        getCollectData();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.note:
                if (dialog == null) {
                    dialog = new MyBottomSheetDialog(activity);
                    View itemView = LayoutInflater.from(activity).inflate(R.layout.bottom_sheet_note, null);
                    initDialogView(itemView);
                    dialog.setContentView(itemView);
                }
                dialog.show();
                break;
            case R.id.collect:
                if (collectMenu.isChecked()) {
                    delCollectData();
                } else {
                    addCollectData();
                }
                break;
            case R.id.refresh:
                mWebView.loadUrl(mGankResult.getUrl());
                break;
            case R.id.copy_link:
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", mGankResult.getUrl());
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                showSnackbar(mWebView, "复制成功");
                break;
            case R.id.open_link:
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(mGankResult.getUrl());
                intent.setData(content_url);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addCollectData() {
        Gson gson = new Gson();
        CollectionResult result = gson.fromJson(gson.toJson(mGankResult), CollectionResult.class);
        String userId = (String) SPUtils.getSp(context, "userId", "");
        result.setUserId(userId);
        result.setId(mGankResult.get_id());
        Subscriber<InsertDataResult> subscriber = new Subscriber<InsertDataResult>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                showSnackbar(mWebView, e.getMessage());
            }

            @Override
            public void onNext(InsertDataResult addCollectionResult) {
                showSnackbar(mWebView, "收藏成功");
                objectId = addCollectionResult.getObjectId();
                collectMenu.setChecked(true);
            }
        };
        RxUtils.getInstance().getAddCollectData(subscriber, result);
    }

    private void delCollectData() {
        Subscriber<GankRoot> subscriber = new Subscriber<GankRoot>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                showSnackbar(mWebView, e.getMessage());
            }

            @Override
            public void onNext(GankRoot root) {
                if (root.getMsg().equals("ok")) {
                    showSnackbar(mWebView, "取消收藏");
                    collectMenu.setChecked(false);
                }
            }
        };
        RxUtils.getInstance().getDelCollectionData(subscriber, objectId);
    }

    private void initDialogView(View itemView) {
        closeDialog = (ImageView) itemView.findViewById(R.id.action_close);
        closeDialog.setOnClickListener(this);
        actionUndo = (ImageView) itemView.findViewById(R.id.action_undo);
        actionUndo.setOnClickListener(this);
        actionRedo = (ImageView) itemView.findViewById(R.id.action_redo);
        actionRedo.setOnClickListener(this);
        actionSave = (ImageView) itemView.findViewById(R.id.action_save);
        actionSave.setOnClickListener(this);
        actionMoreSwitch = (ImageView) itemView.findViewById(R.id.action_more_switch);
        actionMoreSwitch.setOnClickListener(this);
        actionMore = (ImageView) itemView.findViewById(R.id.action_more);
        actionMore.setOnClickListener(this);
        actionOther = (ExpandableLinearLayout) itemView.findViewById(R.id.action_other_operate);
        tabIconView = (TabIconView) itemView.findViewById(R.id.tab_icon_view);
        tabIconView.addTab(R.drawable.ic_format_list_bulleted_24dp, R.id.id_shortcut_list_bulleted, this);
        tabIconView.addTab(R.drawable.ic_format_list_numbered_24dp, R.id.id_shortcut_format_numbers, this);
        tabIconView.addTab(R.drawable.ic_insert_link_24dp, R.id.id_shortcut_insert_link, this);
        tabIconView.addTab(R.drawable.ic_insert_photo_24dp, R.id.id_shortcut_insert_photo, this);
//        tabIconView.addTab(R.drawable.ic_shortcut_console, R.id.id_shortcut_console, this);
        tabIconView.addTab(R.drawable.ic_format_bold_24dp, R.id.id_shortcut_format_bold, this);
        tabIconView.addTab(R.drawable.ic_format_italic_24dp, R.id.id_shortcut_format_italic, this);
//        tabIconView.addTab(R.drawable.ic_shortcut_format_header_1, R.id.id_shortcut_format_header_1, this);
//        tabIconView.addTab(R.drawable.ic_shortcut_format_header_2, R.id.id_shortcut_format_header_2, this);
//        tabIconView.addTab(R.drawable.ic_shortcut_format_header_3, R.id.id_shortcut_format_header_3, this);
        tabIconView.addTab(R.drawable.ic_format_quote_24dp, R.id.id_shortcut_format_quote, this);
//        tabIconView.addTab(R.drawable.ic_shortcut_xml, R.id.id_shortcut_xml, this);
        tabIconView.addTab(R.drawable.ic_divider_24dp, R.id.id_shortcut_minus, this);
        tabIconView.addTab(R.drawable.ic_format_strikethrough_24dp, R.id.id_shortcut_format_strikethrough, this);
        tabIconView.addTab(R.drawable.ic_grid_on_24dp, R.id.id_shortcut_grid, this);
//        tabIconView.addTab(R.drawable.ic_shortcut_format_header_4, R.id.id_shortcut_format_header_4, this);
//        tabIconView.addTab(R.drawable.ic_shortcut_format_header_5, R.id.id_shortcut_format_header_5, this);
//        tabIconView.addTab(R.drawable.ic_shortcut_format_header_6, R.id.id_shortcut_format_header_6, this);


        title = (TextView) itemView.findViewById(R.id.title);
        content = (TextView) itemView.findViewById(R.id.content);
    }

    @Override
    protected void onDestroy() { // 推出当前Activity销毁mWebView
        mWebView.stopLoading();
        mWebView.removeAllViews();
        mWebView.destroy();
        mWebView = null;
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_close:
                dialog.dismiss();
                break;
            case R.id.action_undo:
                break;
            case R.id.action_redo:
                break;
            case R.id.action_save:
                break;
            case R.id.action_more_switch:
                if (!actionOther.isExpanded())
                    //没有展开，但是接下来就是展开，设置向上箭头
                    actionMoreSwitch.setImageResource(R.drawable.ic_expand_less_white_24dp);
                else
                    actionMoreSwitch.setImageResource(R.drawable.ic_expand_more_white_24dp);
                actionOther.toggle();
                break;
            case R.id.action_more:
                break;
        }
    }

    /**
     * 查询此文章是否被收藏
     */
    public void getCollectData() {
        String userId = (String) SPUtils.getSp(context, "userId", "");
        String id;
        if (mGankResult.get_id() == null) {
            id = mGankResult.getId();
        }else {
            id = mGankResult.get_id();
        }
        String where = "{\"$and\":[{\"userId\":"+"\""+userId+"\""+"},{\"id\":"+"\""+id+"\""+"}]}";
        Subscriber<List<GankResult>> subscriber = new Subscriber<List<GankResult>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(List<GankResult> results) {
                if (results.size() > 0) {
                    collectMenu.setChecked(true);
                    objectId = results.get(0).getObjectId();
                }
            }
        };
        RxUtils.getInstance().getCollectionData(subscriber, where);
    }
}
