package com.jinjunhang.onlineclass.ui.activity.mainpage;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.ui.fragment.BaseFragment;
import com.jinjunhang.onlineclass.ui.fragment.CourseListFragment;
import com.jinjunhang.onlineclass.ui.fragment.mainpage.MainPageFragment;
import com.jinjunhang.onlineclass.ui.fragment.SettingsFragment;
import com.jinjunhang.onlineclass.ui.fragment.ShopWebBrowserFragment;
import com.jinjunhang.onlineclass.ui.fragment.user.MeFragment;

/**
 * Created by jinjunhang on 2018/3/29.
 */

public class BottomTabLayoutActivity extends AppCompatActivity {
    private static String TAG = LogHelper.makeLogTag(BottomTabLayoutActivity.class);

    private ViewPager mViewPager;

    private TabLayout mTabLayout;
    private BaseFragment[] mFragmensts;

    private MyPagerAdapter mMyPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tablayout);
        mFragmensts = DataGenerator.getFragments(this, "TabLayout Tab");
        setActionBar();
        initView();
        mMyPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager)findViewById(R.id.viewpager);
        mViewPager.setAdapter(mMyPagerAdapter);
        mViewPager.setCurrentItem(0);


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabLayout.getTabAt(position).select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mViewPager.setOffscreenPageLimit(4);
        setActionBar();
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

        LogHelper.d(TAG, "tab selected position = " + position);
        if (mViewPager != null) {
            mViewPager.setCurrentItem(position);
        }

        mFragmensts[position].changeActionBar();
    }

    public class MyPagerAdapter extends FragmentPagerAdapter{

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmensts[position];
        }

        @Override
        public int getCount() {
            return mFragmensts.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "";
        }
    }


    public static class DataGenerator {

        public static final int []mTabRes = new int[]{R.drawable.icon_home,R.drawable.icon_study, R.drawable.icon_live, R.drawable.icon_shop,R.drawable.icon_my,};
        public static final int []mTabResPressed = new int[]{R.drawable.icon_home_s, R.drawable.icon_study_s, R.drawable.icon_live_s, R.drawable.icon_shop_s,R.drawable.icon_my_s,};
        public static final String []mTabTitle = new String[]{"首页", "学习", "直播", "商城","我的"};

        public static BaseFragment[] getFragments(Activity activity, String from){
            BaseFragment fragments[] = new BaseFragment[5];
            try {

                fragments[0] = MainPageFragment.class.newInstance();
                ShopWebBrowserFragment frag1 = new ShopWebBrowserFragment();
                Bundle args = new Bundle();
                args.putString("title", mTabTitle[1]);
                frag1.setArguments(args);
                fragments[1] = frag1;
                fragments[2] = CourseListFragment.class.newInstance();
                ShopWebBrowserFragment frag3 = new ShopWebBrowserFragment();
                Bundle args3 = new Bundle();
                args3.putString("title", mTabTitle[3]);
                frag3.setArguments(args3);
                fragments[3] = frag3;
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
