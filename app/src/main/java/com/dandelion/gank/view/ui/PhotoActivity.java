package com.dandelion.gank.view.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;

import com.dandelion.gank.R;
import com.dandelion.gank.model.entity.GankResult;
import com.dandelion.gank.net.RxUtils;
import com.dandelion.gank.utils.SharesUtils;
import com.dandelion.gank.view.adapter.PhotoPagerAdapter;
import com.dandelion.gank.view.widget.PhotoViewPager;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class PhotoActivity extends ToolBarActivity {

//    @Bind(R.id.photo_image)
//    PhotoView mPhotoImage;
    @BindView(R.id.view_pager)
    PhotoViewPager mViewPager;

    public static final String EXTRA_URL = "url";
    public static final String EXTRA_POSITION = "position";

//    private Toolbar toolbar;
//    private PhotoViewAttacher mPhotoViewAttacher;
    private List<GankResult> mResults;
    private String url;
    private int position;
    private PhotoPagerAdapter mPhotoPagerAdapter;
    //    private String imageName;

    public static Intent newIntent(Context context, List<GankResult> results, int position) {
        Intent intent = new Intent(context, PhotoActivity.class);
        intent.putExtra(EXTRA_URL, (Serializable) results);
        intent.putExtra(EXTRA_POSITION, position);
        return intent;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_photo;
    }

    @Override
    public void onCreateAfter(Bundle savedInstanceState) {
        setActionBarTitle("大图");
        mResults = (List<GankResult>) getIntent().getSerializableExtra(EXTRA_URL);
        position = getIntent().getIntExtra(EXTRA_POSITION, 0);
        mPhotoPagerAdapter = new PhotoPagerAdapter(mResults);
        mViewPager.setAdapter(mPhotoPagerAdapter);
        mViewPager.setCurrentItem(position);
        setListener();
    }

    private void setListener() {
        mPhotoPagerAdapter.setOnViewTapListener(new PhotoPagerAdapter.OnViewTapListener() {
            @Override
            public void onViewTapListener() {
                hideOrShowToolbar();
            }

            @Override
            public void onLongViewTapListener() {
                new AlertDialog.Builder(activity)
                        .setMessage("保存到手机？")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                saveImageToGallery();
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.photo_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int currentItem = mViewPager.getCurrentItem();
        url = mResults.get(currentItem).getUrl();
        switch (item.getItemId()) {
            case R.id.save:
                saveImageToGallery();
                break;
            case R.id.share:
                RxUtils.getInstance().getBitmapUri(context, url)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Uri>() {
                            @Override
                            public void call(Uri uri) {
                                Intent scannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
                                context.sendBroadcast(scannerIntent);
                                SharesUtils.shareImage(PhotoActivity.this, uri);
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable error) {
                                showSnackbar(mViewPager, error.getMessage());
                            }
                        });
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private synchronized void  saveImageToGallery() {
        int currentItem = mViewPager.getCurrentItem();
        url = mResults.get(currentItem).getUrl();
        RxUtils.getInstance().getBitmapUri(context, url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Uri>() {
                    @Override
                    public void call(Uri uri) {
                        showSnackbar(mViewPager,  "图片保存到："+uri.getPath());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable error) {
                        showSnackbar(mViewPager, error.getMessage());
                    }
                });

    }
}
