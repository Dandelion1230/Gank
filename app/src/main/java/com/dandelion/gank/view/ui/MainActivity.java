package com.dandelion.gank.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.dandelion.gank.R;
import com.dandelion.gank.utils.AppUpdateHelp;
import com.dandelion.gank.utils.PreferencesLoader;
import com.dandelion.gank.utils.SharesUtils;
import com.dandelion.gank.view.adapter.MainAdapter;
import com.dandelion.gank.view.fragment.CollectFragment;
import com.dandelion.gank.view.fragment.HomeFragment;
import com.dandelion.gank.view.fragment.NotesFragment;
import com.dandelion.gank.view.fragment.TabFragment;
import com.mikepenz.itemanimators.AlphaCrossFadeAnimator;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SwitchDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class MainActivity extends ToolBarActivity{


    @Bind(R.id.drawer)
    DrawerLayout drawer;

    Toolbar toolBar;

    private List<String> recyclerData = new ArrayList<>();
    private MainAdapter mAdapter;

    private AccountHeader header = null;
    private Drawer result = null;

    private HomeFragment homeFragment;
    private TabFragment tabFragment;
    private CollectFragment collectFragment;
    private NotesFragment notesFragment;

    private FragmentManager fm;
    private FragmentTransaction transaction;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onCreateAfter(Bundle savedInstanceState) {
        AppUpdateHelp.startUpdate(activity, false);
        fm = getSupportFragmentManager();
        toolBar = getToolbar();
        setMaterialDrawer();
        setFragmentStatus(1);
    }

    @Override
    public void initData() {

    }

    private void setFragmentStatus(int index) {

        transaction = fm.beginTransaction();

        /** 先隐藏所有的Fragment */
        hideFragments(transaction);
        switch (index) {
            case 1:
                setActionBarTitle("首页");
                if (tabFragment == null) {
                    tabFragment = new TabFragment();
                    transaction.add(R.id.content, tabFragment);
                } else {
                    transaction.show(tabFragment);
                }
                break;
            case 2:
                setActionBarTitle("闲读");
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.content, homeFragment);
                } else {
                    transaction.show(homeFragment);
                }
                break;
            case 3:
                setActionBarTitle("我的收藏");
                if (collectFragment == null) {
                    collectFragment = new CollectFragment();
                    transaction.add(R.id.content, collectFragment);
                } else {
                    transaction.show(collectFragment);
                }
                break;
            case 4:
                setActionBarTitle("我的笔记");
                if (notesFragment == null) {
                    notesFragment = new NotesFragment();
                    transaction.add(R.id.content, notesFragment);
                } else {
                    transaction.show(notesFragment);
                }
                break;
        }
        transaction.commit();


    }

    private void hideFragments(FragmentTransaction transaction) {
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (tabFragment != null) {
            transaction.hide(tabFragment);
        }
        if (collectFragment != null) {
            transaction.hide(collectFragment);
        }
        if (notesFragment != null) {
            transaction.hide(notesFragment);
        }
    }

    private void setMaterialDrawer() {
        IProfile profile = new ProfileDrawerItem().withEmail("未登录，点击登录").withIcon(R.mipmap.profile3).withIdentifier(100);

        header = new AccountHeaderBuilder()
                .withActivity(this)
//                .withTranslucentStatusBar(false)
                .withHeaderBackground(R.mipmap.header2)
                .withProfileImagesVisible(true)
                .withPaddingBelowHeader(true)
                .addProfiles(
                        profile
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        if (profile instanceof IDrawerItem && profile.getIdentifier() == 100) {
                            startActivity(new Intent(context, LoginActivity.class));

                        }

                        return false;
                    }
                })
                .build();
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolBar)
                .withHasStableIds(true)
                .withItemAnimator(new AlphaCrossFadeAnimator())
                .withAccountHeader(header)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.action_home).withIcon(R.mipmap.account_circle).withIdentifier(1).withSelectable(true),
                        new PrimaryDrawerItem().withName(R.string.action_read).withIcon(R.mipmap.account_circle).withIdentifier(2).withSelectable(true),
                        new PrimaryDrawerItem().withName(R.string.action_collect).withIcon(R.mipmap.account_circle).withIdentifier(3).withSelectable(true),
                        new PrimaryDrawerItem().withName(R.string.action_notes).withIcon(R.mipmap.account_circle).withIdentifier(4).withSelectable(true),
                        new DividerDrawerItem(),
                        new SwitchDrawerItem().withName(R.string.action_wifi).withTag("wifi").withIcon(R.mipmap.account_circle).withChecked(false).withOnCheckedChangeListener(onCheckedChangeListener).withSelectable(false),
                        new SwitchDrawerItem().withName(R.string.action_theme).withTag("theme").withIcon(R.mipmap.account_circle).withChecked(false).withOnCheckedChangeListener(onCheckedChangeListener).withSelectable(false),
//                        new PrimaryDrawerItem().withName(R.string.action_version).withIcon(R.mipmap.account_circle).withIdentifier(5).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.action_grammar).withIcon(R.mipmap.account_circle).withIdentifier(5).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.action_share).withIcon(R.mipmap.account_circle).withIdentifier(6).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.action_feedback).withIcon(R.mipmap.account_circle).withIdentifier(7).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.action_about).withIcon(R.mipmap.account_circle).withIdentifier(8).withSelectable(false)
//                        new PrimaryDrawerItem().withName(R.string.action_quit).withIcon(R.mipmap.account_circle).withIdentifier(9).withSelectable(false)

                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem != null) {
                            if (position <= 4) {
                                setFragmentStatus((int)drawerItem.getIdentifier());
                            } else if(position == 9) {
                                SharesUtils.shareApp(activity);
                            }
                        }
                        return false;
                    }
                })
                .build();

//        result.removeItem(9);

    }

    private OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(IDrawerItem drawerItem, CompoundButton buttonView, boolean isChecked) {
            if (drawerItem instanceof Nameable) {
                String tag = (String) drawerItem.getTag();
                String text = ((Nameable) drawerItem).getName().getText();
                if(tag.equals("wifi")) {
                    PreferencesLoader loader = new PreferencesLoader(context);
                    if(isChecked) {
                        loader.saveBoolean("isWifiLoadImage", true);
                    } else {
                        loader.saveBoolean("isWifiLoadImage", false);
                    }
                }
            } else {
                Log.i("material-drawer", "toggleChecked: " + isChecked);
            }
        }
    };

    private boolean isExit = false;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!isExit) {
                isExit = true;
                Toast.makeText(context, "再按一次退出应用", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isExit = false;
                    }
                }, 2000);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        return super.onOptionsItemSelected(item);
//    }
}
