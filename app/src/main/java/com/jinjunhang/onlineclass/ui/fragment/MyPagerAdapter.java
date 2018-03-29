package com.jinjunhang.onlineclass.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.onlineclass.R;

/**
 * Created by jinjunhang on 2018/3/21.
 */

public class MyPagerAdapter extends FragmentStatePagerAdapter
{
    public static class Fragment1 extends android.support.v4.app.Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.frag_1, container, false);

            return v;
        }

        @Override
        public void onResume() {
            LogHelper.d("Fragment1", "Fragment1 onResume called");
            super.onResume();

        }


    }

    public static class Fragment2 extends android.support.v4.app.Fragment {

        @Override
        public void onResume() {

            LogHelper.d("Fragment2", "Fragment2 onResume called");
            super.onResume();

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.frag_2, container, false);

            return v;
        }
    }

    public Fragment f1;
    public Fragment f2;

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                if (f1 == null)
                    f1 = new Fragment1();
                return f1;
            case 1:
                if (f2 == null)
                    f2 = new Fragment2();
                return f2;

            default:
                return null;
        }
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }
    @Override public int getCount() {
        return 2;
    }
}
