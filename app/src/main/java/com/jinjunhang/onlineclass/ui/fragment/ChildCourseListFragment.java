package com.jinjunhang.onlineclass.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jinjunhang.framework.lib.LoadingAnimation;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.ui.cell.ListViewCell;
import com.jinjunhang.onlineclass.ui.cell.LiveCourseCell;
import com.jinjunhang.onlineclass.ui.fragment.mainpage.Page;
import com.jinjunhang.onlineclass.ui.lib.ExtendFunctionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jinjunhang on 2018/4/10.
 */

public class ChildCourseListFragment extends BaseFragment {

    public View v;
    private LoadingAnimation mLoading;
    private ListView mListView;
    private MyAdapter mMyAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<ListViewCell> mCells = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_fragment_login, container, false);

        /*
        mListView = (ListView)v.findViewById(R.id.listView);

        mCells = new ArrayList<>();
        mCells.add(new LiveCourseCell(getActivity()));
        mCells.add(new LiveCourseCell(getActivity()));
        mCells.add(new LiveCourseCell(getActivity()));
        mCells.add(new LiveCourseCell(getActivity()));


        mMyAdapter = new MyAdapter(getActivity(), mCells);
        mListView.setAdapter(mMyAdapter);
        */
        return v;

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
