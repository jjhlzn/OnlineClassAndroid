package com.jinjunhang.onlineclass.ui.fragment.search;

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
import com.jinjunhang.onlineclass.ui.cell.CourseCell;
import com.jinjunhang.onlineclass.ui.cell.ListViewCell;
import com.jinjunhang.onlineclass.ui.cell.SearchResultItemCell;
import com.jinjunhang.onlineclass.ui.cell.SectionSeparatorCell;
import com.jinjunhang.onlineclass.ui.cell.mainpage.HeaderAdvCell;
import com.jinjunhang.onlineclass.ui.fragment.mainpage.Page;
import com.jinjunhang.onlineclass.ui.lib.ExtendFunctionManager;
import com.jinjunhang.onlineclass.ui.lib.ExtendFunctoinVariableInfoManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jinjunhang on 2018/4/4.
 */

public class SearchResultPage {

    public View view;

    private Activity mActivity;
    private ListView mListView;
    private SearchResultAdapter mSearchResultAdapter;
    private List<ListViewCell> mCells = new ArrayList<>();


    public SearchResultPage(FragmentActivity activity, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mActivity = activity;
        view = inflater.inflate(R.layout.activity_fragment_search_frag2, container, false);

        mListView = (ListView) view.findViewById(R.id.listView);

        //mLoading = new LoadingAnimation(activity);

        //去掉列表的分割线
        mListView.setDividerHeight(0);
        mListView.setDivider(null);

    }

    public void showResult() {
        mCells.clear();
        mSearchResultAdapter = new SearchResultAdapter(mActivity, mCells);
        mListView.setAdapter(mSearchResultAdapter);

        mCells.add(new SearchResultItemCell(mActivity));

        mSearchResultAdapter.notifyDataSetChanged();

    }


    private class SearchResultAdapter extends ArrayAdapter<ListViewCell> {

        private List<ListViewCell> mViewCells;

        public SearchResultAdapter(Activity activity, List<ListViewCell> cells) {
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
