package com.jinjunhang.onlineclass.ui.activity;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.wx.Util;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.db.LoginUserDao;
import com.jinjunhang.onlineclass.model.LoginUser;
import com.jinjunhang.onlineclass.ui.fragment.MainPageFragment;
import com.jinjunhang.onlineclass.ui.fragment.user.MeFragment;
import com.jinjunhang.onlineclass.ui.fragment.search.SearchFragment;
import com.jinjunhang.onlineclass.ui.fragment.SettingsFragment;
import com.jinjunhang.player.utils.LogHelper;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends BaseMusicActivity  {

    public static final int REQUEST_ME_UPDATE_USER_IAMGE = 1;
    public static final int REQUEST_ME_UPDATE_PERSONAL_INFO = 2;

    private static final String TAG = LogHelper.makeLogTag(MainActivity.class);

    private BottomBar mBottomBar;
    private Map<Class, Fragment> fragmentMap;

    public final static String EXTRA_TAB = "selecttab";

    private LoginUserDao mLoginUserDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoginUserDao = LoginUserDao.getInstance(this);

        LoginUser loginUser = mLoginUserDao.get();
        LogHelper.d(TAG, "loginuser = " + loginUser);

        setContentView(R.layout.activity_main);

        setCommonActionBar();

        fragmentMap = new HashMap();
        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.setMaxFixedTabs(5);
        mBottomBar.setItemsFromMenu(R.menu.bottombar, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                String title = "";
                FragmentManager fm = getSupportFragmentManager();
                Fragment fragment = null;
                switch (menuItemId) {
                    case R.id.bottomBarHome:
                        title = "巨方助手";
                        fragment = getFragment(MainPageFragment.class);
                        setCommonActionBar();
                        break;
                    case R.id.bottomBarSearch:
                        title = "搜索";
                        fragment = getFragment(SearchFragment.class);
                        setSearchActionBar();
                        break;
                    case R.id.bottomBarMe:
                        title = "";
                        fragment = getFragment(MeFragment.class);
                        setCommonActionBar();
                        break;
                    case R.id.bottomBarSetting:
                        title = "设置";
                        setCommonActionBar();
                        fragment = getFragment(SettingsFragment.class);
                        break;
                }

                if (fragment != null) {
                    android.support.v4.app.FragmentTransaction transaction = fm.beginTransaction();
                    transaction.replace(R.id.fragmentContainer, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }

                if (menuItemId != R.id.bottomBarSearch)
                    ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.actionbar_text)).setText(title);
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {

            }
        });

        mBottomBar.noTopOffset();
        mBottomBar.hideShadow();

        mBottomBar.post(new Runnable() {
            @Override
            public void run() {
                int height = mBottomBar.getBar().getHeight();
                Utils.BOTTOM_BAR_HEIGHT = height;
                LogHelper.d(TAG, "bottom bar height = " + Utils.BOTTOM_BAR_HEIGHT+", " + height);
            }
        });

        int selectTab = getIntent().getIntExtra(EXTRA_TAB, 0);
        LogHelper.d(TAG, "selectTab = " + selectTab);
        mBottomBar.selectTabAtPosition(selectTab, true);

        mPlayerController.attachToView(mBottomBar);
    }

    private void setCommonActionBar() {
        getSupportActionBar().show();
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        final View customView = getLayoutInflater().inflate(R.layout.actionbar, null);
        getSupportActionBar().setCustomView(customView);
        Toolbar parent =(Toolbar) customView.getParent();

        customView.post(new Runnable(){
            public void run(){
                int height = customView.findViewById(R.id.action_bar).getHeight();
                int heightInPixel = Utils.dip2px(MainActivity.this, height);
                LogHelper.d(TAG, "action bar = " + height);
            }
        });
        parent.setContentInsetsAbsolute(0, 0);

    }

    private void setSearchActionBar() {
        getSupportActionBar().show();
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View customView = getLayoutInflater().inflate(R.layout.actionbar_search, null);
        getSupportActionBar().setCustomView(customView);
        Toolbar parent =(Toolbar) customView.getParent();
        parent.setContentInsetsAbsolute(0, 0);
    }

    private void setMeActionBar() {
        getSupportActionBar().hide();
        /*
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View customView = getLayoutInflater().inflate(R.layout.actionbar_search, null);
        getSupportActionBar().setCustomView(customView);
        Toolbar parent =(Toolbar) customView.getParent();
        parent.setContentInsetsAbsolute(0, 0); */
    }

    private <T extends Fragment> Fragment getFragment(Class<T> fragmentClass) {
        Fragment fragment = fragmentMap.get(fragmentClass);
        if (fragment == null) {
            try {
                fragment = fragmentClass.newInstance();
            } catch (Exception ex) {

            }
            fragmentMap.put(fragmentClass, fragment);
        }
        return fragment;
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPlayerController.attachToView(mBottomBar);
        mPlayerController.updateView();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPlayerController.updateView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ME_UPDATE_USER_IAMGE) {
            getFragment(MeFragment.class).onActivityResult(requestCode, resultCode, data);
        } else if (requestCode == REQUEST_ME_UPDATE_PERSONAL_INFO) {
            getFragment(MeFragment.class).onActivityResult(requestCode, resultCode, data);
        }
    }
}
