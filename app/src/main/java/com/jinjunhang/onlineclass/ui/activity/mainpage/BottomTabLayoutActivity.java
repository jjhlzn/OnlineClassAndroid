package com.jinjunhang.onlineclass.ui.activity.mainpage;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.ui.fragment.BaseFragment;
import com.jinjunhang.onlineclass.ui.fragment.mainpage.MainPageFragment;
import com.jinjunhang.onlineclass.ui.fragment.SettingsFragment;
import com.jinjunhang.onlineclass.ui.fragment.ShopWebBrowserFragment;
import com.jinjunhang.onlineclass.ui.fragment.user.MeFragment;

/**
 * Created by jinjunhang on 2018/3/29.
 */

public class BottomTabLayoutActivity extends AppCompatActivity {
    private static String TAG = LogHelper.makeLogTag(BottomTabLayoutActivity.class);

    private TabLayout mTabLayout;
    private BaseFragment[] mFragmensts;

    private boolean isInited = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tablayout);
        mFragmensts = DataGenerator.getFragments("TabLayout Tab");
        setActionBar();
        initView();
        isInited = true;
    }


    public void setActionBar() {
        AppCompatActivity activity = this;
        LogHelper.d(TAG, "activity = " + activity);
        activity.getSupportActionBar().show();
        activity.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        final View customView = activity.getLayoutInflater().inflate(R.layout.actionbar_main, null);
        activity.getSupportActionBar().setCustomView(customView);
        Toolbar parent =(Toolbar) customView.getParent();

        parent.setContentInsetsAbsolute(0, 0);

        setLightStatusBar(customView, activity);
    }

    protected void setLightStatusBar(View view, Activity activity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            activity.getWindow().setStatusBarColor(Color.WHITE);
        }
    }


    private void initView() {
        mTabLayout = (TabLayout) findViewById(R.id.bottom_tab_layout);


        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                onTabItemSelected(tab.getPosition());
                // Tab 选中之后，改变各个Tab的状态
                for (int i=0;i<mTabLayout.getTabCount();i++){
                    View view = mTabLayout.getTabAt(i).getCustomView();
                    ImageView icon = (ImageView) view.findViewById(R.id.tab_content_image);
                    TextView text = (TextView) view.findViewById(R.id.tab_content_text);
                    if(i == tab.getPosition()){ // 选中状态
                        icon.setImageResource(DataGenerator.mTabResPressed[i]);
                        text.setTextColor(getResources().getColor(R.color.tab_selected_color));
                    }else{// 未选中状态
                        icon.setImageResource(DataGenerator.mTabRes[i]);
                        text.setTextColor(getResources().getColor(android.R.color.darker_gray));
                    }
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // 提供自定义的布局添加Tab
        for(int i=0;i<5;i++){
            mTabLayout.addTab(mTabLayout.newTab().setCustomView(DataGenerator.getTabView(this,i)));
        }


    }

    private BaseFragment currentFragment;
    private void onTabItemSelected(int position){


        BaseFragment fragment = null;
        switch (position){
            case 0:
                fragment = mFragmensts[0];
                break;
            case 1:
                fragment = mFragmensts[1];
                //((ShopWebBrowserFragment)fragment).setTabChanged();
                break;
            case 2:
                fragment = mFragmensts[2];
                break;
            case 3:
                fragment = mFragmensts[3];
                break;
            case 4:
                fragment = mFragmensts[4];
                break;
        }

        if(fragment!=null) {

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }
            //如果之前没有添加过
            if(!fragment.isAdded()){
                transaction.add(R.id.home_container,fragment);
            }else{
                transaction.show(fragment);
            }

            //全局变量，记录当前显示的fragment
            currentFragment = fragment;

            transaction.commit();

            if (isInited)
                currentFragment.changeActionBar();
        }
    }


    public static class DataGenerator {

        public static final int []mTabRes = new int[]{R.drawable.icon_home,R.drawable.icon_study, R.drawable.icon_live, R.drawable.icon_shop,R.drawable.icon_my,};
        public static final int []mTabResPressed = new int[]{R.drawable.icon_home_s, R.drawable.icon_study_s, R.drawable.icon_live_s, R.drawable.icon_shop_s,R.drawable.icon_my_s,};
        public static final String []mTabTitle = new String[]{"首页", "学习", "直播", "商城","我的"};

        public static BaseFragment[] getFragments(String from){
            BaseFragment fragments[] = new BaseFragment[5];
            try {
                fragments[0] = MainPageFragment.class.newInstance();
                fragments[1] = ShopWebBrowserFragment.class.newInstance();
                fragments[2] = MeFragment.class.newInstance();
                fragments[3] = ShopWebBrowserFragment.class.newInstance();
                fragments[4] = MeFragment.class.newInstance();
            }catch (Exception  ex) {
                LogHelper.e("DataGenerator", ex);
            }
            return fragments;
        }

        /**
         * 获取Tab 显示的内容
         * @param context
         * @param position
         * @return
         */

        public static View getTabView(Context context, int position){
            View view = LayoutInflater.from(context).inflate(R.layout.tabitem,null);
            ImageView tabIcon = (ImageView) view.findViewById(R.id.tab_content_image);
            tabIcon.setImageResource(DataGenerator.mTabRes[position]);
            TextView tabText = (TextView) view.findViewById(R.id.tab_content_text);
            tabText.setText(mTabTitle[position]);
            return view;
        }
    }

}
