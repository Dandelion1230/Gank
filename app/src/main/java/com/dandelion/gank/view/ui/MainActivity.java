package com.dandelion.gank.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
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
import com.dandelion.gank.utils.SPUtils;
import com.dandelion.gank.utils.SharesUtils;
import com.dandelion.gank.view.fragment.CollectFragment;
import com.dandelion.gank.view.fragment.MeizhiFragment;
import com.dandelion.gank.view.fragment.NotesFragment;
import com.dandelion.gank.view.fragment.TabFragment;
import com.dandelion.gank.view.fragment.TabReadFragment;
import com.mikepenz.itemanimators.AlphaCrossFadeAnimator;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends ToolBarActivity{


    @BindView(R.id.drawer)
    DrawerLayout drawer;
    @BindView(R.id.fab)
    FloatingActionButton mFab;

    Toolbar toolBar;


    private AccountHeader header = null;
    private Drawer result = null;

    private MeizhiFragment mMZFragment;
    private TabFragment tabFragment;
    private CollectFragment collectFragment;
    private NotesFragment notesFragment;
    private TabReadFragment mTabReadFragment;

    private FragmentManager fm;
    private FragmentTransaction transaction;
//    private IProfile profile;
    private boolean isLogin;
    private String withEmail;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onCreateAfter(Bundle savedInstanceState) {
//        AppUpdateHelp.startUpdate(activity, false);
        EventBus.getDefault().register(this);
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
                setActionBarTitle("妹纸");
                if (mMZFragment == null) {
                    mMZFragment = new MeizhiFragment();
                    transaction.add(R.id.content, mMZFragment);
                } else {
                    transaction.show(mMZFragment);
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
            case 0:
                setActionBarTitle("闲读");
                if (mTabReadFragment == null) {
                    mTabReadFragment = new TabReadFragment();
                    transaction.add(R.id.content, mTabReadFragment);
                } else {
                    transaction.show(mTabReadFragment);
                }
                break;
        }
        transaction.commit();


    }

    private void hideFragments(FragmentTransaction transaction) {
        if (mMZFragment != null) {
            transaction.hide(mMZFragment);
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
        if (mTabReadFragment != null) {
            transaction.hide(mTabReadFragment);
        }
    }

    private void setMaterialDrawer() {
        isLogin = (boolean) SPUtils.getSp(context, "isLogin", false);
        if (isLogin) {
            withEmail = (String) SPUtils.getSp(context, "loginName", "");
        }else {
            withEmail = "未登录，点击登录";
        }
        IProfile profile = new ProfileDrawerItem().withEmail(withEmail).withIcon(R.mipmap.profile3).withIdentifier(100);

        header = new AccountHeaderBuilder()
                .withActivity(this)
//                .withTranslucentStatusBar(false)
                .withHeaderBackground(R.mipmap.header2)
                .withProfileImagesVisible(true)
                .withPaddingBelowHeader(true)
                .addProfiles(profile)
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        if (profile instanceof IDrawerItem && profile.getIdentifier() == 100) {
                            if (!isLogin)
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
                        new PrimaryDrawerItem().withName(R.string.action_home).withIcon(R.drawable.ic_home_red_24dp).withIdentifier(1).withSelectable(true),
                        new PrimaryDrawerItem().withName(R.string.action_girl).withIcon(R.drawable.ic_redeem_red_24dp).withIdentifier(2).withSelectable(true),
                        new PrimaryDrawerItem().withName(R.string.action_read).withIcon(R.drawable.ic_redeem_red_24dp).withIdentifier(0).withSelectable(true),
                        new PrimaryDrawerItem().withName(R.string.action_collect).withIcon(R.drawable.ic_collections_bookmark_red_24dp).withIdentifier(3).withSelectable(true),
                        new PrimaryDrawerItem().withName(R.string.action_notes).withIcon(R.drawable.ic_event_note_red_36dp).withIdentifier(4).withSelectable(true),
                        new DividerDrawerItem(),
//                        new SwitchDrawerItem().withName(R.string.action_wifi).withTag("wifi").withIcon(R.mipmap.account_circle).withChecked(false).withOnCheckedChangeListener(onCheckedChangeListener).withSelectable(false),
//                        new SwitchDrawerItem().withName(R.string.action_theme).withTag("theme").withIcon(R.drawable.ic_brightness_red_36dp).withChecked(false).withOnCheckedChangeListener(onCheckedChangeListener).withSelectable(false),
//                        new PrimaryDrawerItem().withName(R.string.action_version).withIcon(R.mipmap.account_circle).withIdentifier(5).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.action_grammar).withIcon(R.drawable.ic_help_red_36dp).withIdentifier(5).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.action_share).withIcon(R.drawable.ic_share_red_36dp).withIdentifier(6).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.action_feedback).withIcon(R.drawable.ic_feedback_red_36dp).withIdentifier(7).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.action_about).withIcon(R.drawable.ic_info_red_36dp).withIdentifier(8).withSelectable(false)
//                        new PrimaryDrawerItem().withName(R.string.action_quit).withIcon(R.mipmap.account_circle).withIdentifier(9).withSelectable(false)

                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem != null) {
                            if (drawerItem.getIdentifier() <= 4) {
                                setFragmentStatus((int)drawerItem.getIdentifier());
                            } else if(drawerItem.getIdentifier() == 5) {
                                startActivity(new Intent(activity, HelpGrammarActivity.class));
                            }else if(drawerItem.getIdentifier() == 6) {
                                SharesUtils.shareApp(activity);
                            } else if(drawerItem.getIdentifier() == 7) {
                                startActivity(new Intent(activity, FeedbackActivity.class));
                            } else if(drawerItem.getIdentifier() == 8) {
                                startActivity(new Intent(activity, AboutActivity.class));
                            } else if(drawerItem.getIdentifier() == 9) {
                                SPUtils.setSP(context, "isLogin", false);
                                isLogin = false;
                                header.updateProfile(new ProfileDrawerItem().withEmail("未登录，点击登录").withIcon(R.mipmap.profile3).withIdentifier(100));
                                result.removeItem(9);
                                showSnackbar(view, "退出登录");
                            }
                        }
                        return false;
                    }
                })
                .build();
        if (isLogin) {
            result.addItem(new PrimaryDrawerItem().withName(R.string.action_quit).withIcon(R.drawable.ic_exit_login_red_36dp).withIdentifier(9).withSelectable(false));
        }


    }

    private OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(IDrawerItem drawerItem, CompoundButton buttonView, boolean isChecked) {
            if (drawerItem instanceof Nameable) {
                String tag = (String) drawerItem.getTag();
                String text = ((Nameable) drawerItem).getName().getText();
                if(tag.equals("wifi")) {
                    if(isChecked) {
                        SPUtils.setSP(context, "isWifiLoadImage", true);
                    } else {
                        SPUtils.setSP(context, "isWifiLoadImage", false);
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
    protected boolean hasBackButton() {
        return false;
    }

    @OnClick(R.id.fab)
    public void onClick(View view) {
        startActivity(new Intent(this, SubmitGankActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        return super.onOptionsItemSelected(item);
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(String userName) {
        header.updateProfile(new ProfileDrawerItem().withEmail(userName).withIcon(R.mipmap.profile3).withIdentifier(100));
        result.addItem(new PrimaryDrawerItem().withName(R.string.action_quit).withIcon(R.drawable.ic_exit_login_red_36dp).withIdentifier(9).withSelectable(false));
        isLogin = true;
        showSnackbar(drawer, "登录成功");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
