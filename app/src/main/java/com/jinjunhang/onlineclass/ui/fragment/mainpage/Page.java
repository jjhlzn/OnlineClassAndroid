package com.jinjunhang.onlineclass.ui.fragment.mainpage;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jinjunhang.framework.lib.LoadingAnimation;
import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.ui.cell.ListViewCell;
import com.jinjunhang.onlineclass.ui.cell.MainPageWhiteSeparatorCell;
import com.jinjunhang.onlineclass.ui.cell.mainpage.CourseNotifyCell;
import com.jinjunhang.onlineclass.ui.cell.mainpage.FooterCell;
import com.jinjunhang.onlineclass.ui.cell.mainpage.HeaderAdvCell;
import com.jinjunhang.onlineclass.ui.lib.ExtendFunctionManager;
import com.jinjunhang.onlineclass.ui.lib.ExtendFunctoinVariableInfoManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jinjunhang on 2018/3/31.
 */

public class Page implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = LogHelper.makeLogTag(Page.class);

    public View v;
    private LoadingAnimation mLoading;
    private ListView mListView;
    private ExtendFunctionManager mFunctionManager;
    private MainPageAdapter mMainPageAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<ListViewCell> mCells = new ArrayList<>();

    public Page(FragmentActivity activity, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_fragment_pushdownrefresh, container, false);

        mListView = (ListView) v.findViewById(R.id.listView);

        mLoading = new LoadingAnimation(activity);

        //去掉列表的分割线
        mListView.setDividerHeight(0);
        mListView.setDivider(null);

        int maxShowRows = 10;

        mFunctionManager = new ExtendFunctionManager(ExtendFunctoinVariableInfoManager.getInstance(), maxShowRows,
                activity, true);

        if (mCells.size() == 0) {
            mCells.add(new HeaderAdvCell(activity, mLoading));

            mCells.add(new CourseNotifyCell(activity));

            int functionRowCount = mFunctionManager.getRowCount();
            for (int i = 0; i < functionRowCount; i++) {
                mCells.add(mFunctionManager.getCell(i));
            }
            mCells.add(new MainPageWhiteSeparatorCell(activity));
            mCells.add(new FooterCell(activity));
        }

        mMainPageAdapter = new MainPageAdapter(activity, mCells);
        mListView.setAdapter(mMainPageAdapter);

        //可以下拉刷新
        mSwipeRefreshLayout =  (SwipeRefreshLayout)v.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogHelper.d(TAG, "mList onItemClick: ", position);
            }
        });
    }

    @Override
    public void onRefresh() {

    }

    private class MainPageAdapter extends ArrayAdapter<ListViewCell> {
        private List<ListViewCell> mViewCells;

        public MainPageAdapter(Activity activity, List<ListViewCell> cells) {
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