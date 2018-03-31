package com.jinjunhang.onlineclass.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
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
 * Created by jinjunhang on 2018/3/21.
 */

public class MainPageFragment2 extends android.support.v4.app.Fragment {

    private  static final String TAG = LogHelper.makeLogTag(MainPageFragment2.class);

    private Page rongziPage;
    private Page touziPage;

    private ImageButton touziBtn;
    private ImageButton rongziBtn;
    private ViewGroup rongziView;
    private ViewGroup touziView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_main, container, false);

        touziBtn = (ImageButton)((AppCompatActivity)getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.touzi_btn);
        rongziBtn = (ImageButton)((AppCompatActivity)getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.rongzi_btn);

        rongziView = (ViewGroup)v.findViewById(R.id.frag_1);
        touziView = (ViewGroup)v.findViewById(R.id.frag_2);

        touziBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                touziBtn.setImageDrawable(getContext().getDrawable(R.drawable.touzi_btn_s));
                rongziBtn.setImageDrawable(getContext().getDrawable(R.drawable.rongzi_btn));
                rongziView.setVisibility(View.VISIBLE);
                touziView.setVisibility(View.INVISIBLE);

            }
        });

        rongziBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                touziBtn.setImageDrawable(getContext().getDrawable(R.drawable.touzi_btn));
                rongziBtn.setImageDrawable(getContext().getDrawable(R.drawable.rongzi_btn_s));
                rongziView.setVisibility(View.INVISIBLE);
                touziView.setVisibility(View.VISIBLE);
            }
        });

        rongziPage = new Page(getActivity(), inflater, container, savedInstanceState);
        rongziView.addView(rongziPage.v);
        touziPage = new Page(getActivity(), inflater, container, savedInstanceState);
        touziView.addView(touziPage.v);

        return v;
    }


    public class Page implements SwipeRefreshLayout.OnRefreshListener {

        public View v;
        private LoadingAnimation mLoading;
        private ListView mListView;
        private ExtendFunctionManager mFunctionManager;
        private MainPageFragment2.MainPageAdapter mMainPageAdapter;
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

            mMainPageAdapter = new MainPageFragment2.MainPageAdapter(mCells);
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
    }

    private class MainPageAdapter extends ArrayAdapter<ListViewCell> {
        private List<ListViewCell> mViewCells;

        public MainPageAdapter(List<ListViewCell> albumTypes) {
            super(getActivity(), 0, albumTypes);
            mViewCells = albumTypes;
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




