package com.jinjunhang.onlineclass.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.jinjunhang.framework.controller.PagableController;
import com.jinjunhang.framework.lib.LoadingAnimation;
import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.ui.cell.ListViewCell;
import com.jinjunhang.onlineclass.ui.cell.LiveCourseCell;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jinjunhang on 2018/4/9.
 */

public class CourseListFragment extends BaseFragment  {
    //public final static String EXTRA_ALBUMTYPE = "extra_albumtype";

    private final static String TAG = LogHelper.makeLogTag(CourseListFragment.class);

    private PagableController mPagableController;

    private ListView mListView;
    private LoadingAnimation mLoading;
    private ViewPager mViewPager;
    private BaseFragment[] mFragmensts;
    private ImageButton everydayBtn, vipBtn, agentBtn;
    private CoursePage[] pages;
    private ViewGroup[] mViewGroups;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_couselist, container, false);
        everydayBtn = (ImageButton) v.findViewById(R.id.everyday_btn);
        vipBtn = (ImageButton) v.findViewById(R.id.vip_btn);
        agentBtn = (ImageButton) v.findViewById(R.id.agent_btn);

        mFragmensts = DataGenerator.getFragments(getActivity(), "");

        //MyPagerAdapter pagerAdapter = new MyPagerAdapter(getActivity().getSupportFragmentManager());

        //mViewPager = (ViewPager) v.findViewById(R.id.viewpager);
        //mViewPager.setAdapter(pagerAdapter);

        pages = new CoursePage[3];
        mViewGroups = new ViewGroup[3];
        for(int i = 0; i < 3; i++) {
            pages[i] = new CoursePage(inflater, container, savedInstanceState);
        }

        mViewGroups[0] = (ViewGroup)v.findViewById(R.id.frag_1);
        mViewGroups[1] = (ViewGroup)v.findViewById(R.id.frag_2);
        mViewGroups[2] = (ViewGroup)v.findViewById(R.id.frag_3);

        mViewGroups[0].addView(pages[0].v);
        mViewGroups[1].addView(pages[1].v);
        mViewGroups[2].addView(pages[2].v);

        everydayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                everydayBtn.setImageDrawable(getActivity().getDrawable(R.drawable.everyday_s));
                vipBtn.setImageDrawable(getActivity().getDrawable(R.drawable.vip));
                agentBtn.setImageDrawable(getActivity().getDrawable(R.drawable.agent));

                showPage(0);
            }
        });

        vipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                everydayBtn.setImageDrawable(getActivity().getDrawable(R.drawable.everyday));
                vipBtn.setImageDrawable(getActivity().getDrawable(R.drawable.vip_s));
                agentBtn.setImageDrawable(getActivity().getDrawable(R.drawable.agent));

                showPage(1);
            }
        });

        agentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                everydayBtn.setImageDrawable(getActivity().getDrawable(R.drawable.everyday));
                vipBtn.setImageDrawable(getActivity().getDrawable(R.drawable.vip));
                agentBtn.setImageDrawable(getActivity().getDrawable(R.drawable.agent_s));

                showPage(2);
            }
        });


        showPage(0);
        LogHelper.d(TAG, "mfragment.length = " + mFragmensts.length);
        return v;
    }

    private void showPage(int index) {

        for(int i = 0; i < 3; i++) {
            if (i == index) {
                //mFragmensts[i].getView().setVisibility(View.VISIBLE);
                mViewGroups[i].setVisibility(View.VISIBLE);
                pages[i].v.setVisibility(View.VISIBLE);
            } else {
                //mFragmensts[i].getView().setVisibility(View.INVISIBLE);
                mViewGroups[i].setVisibility(View.INVISIBLE);
                pages[i].v.setVisibility(View.INVISIBLE);
            }
        }
        //mViewPager.setCurrentItem(index);
    }

    @Override
    public void changeActionBar() {
        AppCompatActivity activity = (AppCompatActivity)getActivity();
        if (activity != null) {
            activity.getSupportActionBar().show();
            activity.getSupportActionBar().setElevation(0);
            activity.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            final View customView = activity.getLayoutInflater().inflate(R.layout.actionbar, null);
            activity.getSupportActionBar().setCustomView(customView);
            Toolbar parent = (Toolbar) customView.getParent();

            parent.setContentInsetsAbsolute(0, 0);

            TextView text = (TextView)customView.findViewById(R.id.actionbar_text);
            text.setText("直播");

            setLightStatusBar(customView, activity);
        }
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

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

        public static BaseFragment[] getFragments(Activity activity, String from){
            BaseFragment fragments[] = new BaseFragment[3];
            try {

                fragments[0] = ChildCourseListFragment.class.newInstance();
                fragments[1] = ShopWebBrowserFragment.class.newInstance();
                fragments[2] = ChildCourseListFragment.class.newInstance();
            }catch (Exception  ex) {
                LogHelper.e("DataGenerator", ex);
            }
            return fragments;
        }
    }

    private class CoursePage  {

        public View v;
        private LoadingAnimation mLoading;
        private ListView mListView;
        private MyAdapter mMyAdapter;
        private SwipeRefreshLayout mSwipeRefreshLayout;
        private List<ListViewCell> mCells = new ArrayList<>();


        public CoursePage(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            v = inflater.inflate(R.layout.activity_fragment_pushdownrefresh, container, false);


            mListView = (ListView)v.findViewById(R.id.listView);

            //去掉列表的分割线
            mListView.setDividerHeight(0);
            mListView.setDivider(null);

            mCells = new ArrayList<>();
            mCells.add(new LiveCourseCell(getActivity()));
            mCells.add(new LiveCourseCell(getActivity()));
            mCells.add(new LiveCourseCell(getActivity()));
            mCells.add(new LiveCourseCell(getActivity()));


            mMyAdapter = new MyAdapter(getActivity(), mCells);
            mListView.setAdapter(mMyAdapter);

        }

        private class MyAdapter extends ArrayAdapter<ListViewCell> {
            private List<ListViewCell> mViewCells;

            public MyAdapter(Activity activity, List<ListViewCell> cells) {
                super(activity, 0, cells);
                mViewCells = cells;
            }

            @Override
            public int getCount() {
                return mViewCells.size();
            }

            @Override
            public ListViewCell getItem(int position) {
                return mViewCells.get(position);
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ListViewCell item = getItem(position);

                return item.getView();
            }
        }
    }
}
