package com.dandelion.gank.view.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.dandelion.gank.R;
import com.dandelion.gank.net.HttpUtils;
import com.dandelion.gank.utils.PicassoUtils;
import com.dandelion.gank.utils.SharesUtils;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class PhotoActivity extends ToolBarActivity {

    @Bind(R.id.photo_image)
    PhotoView mPhotoImage;

    public static final String EXTRA_URL = "url";

    private Toolbar toolbar;
    private PhotoViewAttacher mPhotoViewAttacher;
    private String url;
    private String imageName;

    public static Intent newIntent(Context context, String url) {
        Intent intent = new Intent(context, PhotoActivity.class);
        intent.putExtra(EXTRA_URL, url);
        return intent;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_photo;
    }

    @Override
    public void onCreateAfter(Bundle savedInstanceState) {
        toolbar = getToolbar();
        setActionBarTitle("大图");
        url = getIntent().getStringExtra(EXTRA_URL);
        PicassoUtils.setImageView(context, url, mPhotoImage);
        int index = url.lastIndexOf("/");
        imageName = url.substring(index+1, url.length());
        mPhotoViewAttacher = new PhotoViewAttacher(mPhotoImage);
//        mPhotoViewAttacher.setScaleType(ImageView.ScaleType.CENTER);
        setListener();
    }

    private void setListener() {
        mPhotoViewAttacher.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                hideOrShowToolbar();
            }
        });
        mPhotoViewAttacher.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
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
                return true;
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.photo_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                saveImageToGallery();
                break;
            case R.id.share:
                HttpUtils.getInstance().getBitmapUri(context, url)
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
                                showSnackbar(mPhotoImage, error.getMessage());
                            }
                        });
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private synchronized void  saveImageToGallery() {
        HttpUtils.getInstance().getBitmapUri(context, url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Uri>() {
                    @Override
                    public void call(Uri uri) {
                        showSnackbar(mPhotoImage,  "图片下载到："+uri.getPath());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable error) {
                        showSnackbar(mPhotoImage, error.getMessage());
                    }
                });
//        CompositeSubscription mCompositeSubscription = new CompositeSubscription();
//        mCompositeSubscription.add(subscription);

    }
}
