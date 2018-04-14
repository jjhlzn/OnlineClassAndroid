package com.jinjunhang.onlineclass.ui.fragment.search;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jinjunhang.framework.lib.LoadingAnimation;
import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.Album;
import com.jinjunhang.onlineclass.model.AlbumType;
import com.jinjunhang.onlineclass.service.GetAlbumSongsRequest;
import com.jinjunhang.onlineclass.service.GetHotSearchWordsRequest;
import com.jinjunhang.onlineclass.service.GetHotSearchWordsResponse;
import com.jinjunhang.onlineclass.service.SearchRequest;
import com.jinjunhang.onlineclass.service.SearchResponse;
import com.jinjunhang.onlineclass.ui.activity.WebBrowserActivity;
import com.jinjunhang.onlineclass.ui.cell.CourseCell;
import com.jinjunhang.onlineclass.ui.cell.ListViewCell;
import com.jinjunhang.onlineclass.ui.cell.SearchResultItemCell;
import com.jinjunhang.onlineclass.ui.cell.SectionSeparatorCell;
import com.jinjunhang.onlineclass.ui.cell.mainpage.HeaderAdvCell;
import com.jinjunhang.onlineclass.ui.fragment.album.AlbumListFragment;
import com.jinjunhang.onlineclass.ui.fragment.mainpage.Page;
import com.jinjunhang.onlineclass.ui.lib.BaseListViewOnItemClickListener;
import com.jinjunhang.onlineclass.ui.lib.ExtendFunctionManager;
import com.jinjunhang.onlineclass.ui.lib.ExtendFunctoinVariableInfoManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jinjunhang on 2018/4/4.
 */

public class SearchResultPage {

    private String TAG = LogHelper.makeLogTag(SearchResultPage.class);

    public View view;

    private Activity mActivity;
    private ListView mListView;
    private SearchResultAdapter mSearchResultAdapter;
    private List<ListViewCell> mCells = new ArrayList<>();
    private List<SearchResponse.SearchResult> mSearchResults = new ArrayList<>();
    private TextView mCountTextView;


    public SearchResultPage(FragmentActivity activity, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mActivity = activity;
        view = inflater.inflate(R.layout.activity_fragment_search_frag2, container, false);

        mListView = (ListView) view.findViewById(R.id.listView);
        mCountTextView = (TextView)view.findViewById(R.id.totalCountText);

        //mLoading = new LoadingAnimation(activity);

        //去掉列表的分割线
        mListView.setDividerHeight(0);
        mListView.setDivider(null);

        mListView.setOnItemClickListener(new BaseListViewOnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                SearchResponse.SearchResult result = (SearchResponse.SearchResult) ((SearchResultItemCell) (mSearchResultAdapter.getItem(position))).getSearchResult();

                Intent i = new Intent(mActivity, WebBrowserActivity.class)
                        .putExtra(WebBrowserActivity.EXTRA_TITLE, result.getTitle())
                        .putExtra(WebBrowserActivity.EXTRA_URL, result.getClickUrl());
                mActivity.startActivity(i);
            }
        });



        mSearchResultAdapter = new SearchResultAdapter(mActivity, mCells);
        mListView.setAdapter(mSearchResultAdapter);
    }

    public void executeSearch() {
        new SearchTask().execute();
    }

    public void showResult() {
        mCells.clear();

        for (SearchResponse.SearchResult result : mSearchResults) {
            mCells.add(new SearchResultItemCell(mActivity, result));
        }

        mCountTextView.setText("共找到"+mSearchResults.size()+"条记录");

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

    private class SearchTask extends AsyncTask<Void, Void, SearchResponse> {

        @Override
        protected SearchResponse doInBackground(Void... voids) {
            SearchRequest request = new SearchRequest();
            return new BasicService().sendRequest(request);
        }


        @Override
        protected void onPostExecute(SearchResponse response) {
            super.onPostExecute(response);

            if (!response.isSuccess()) {
                LogHelper.e(TAG, response.getErrorMessage());
                return;
            }

            mSearchResults = response.getSearchResults();
            showResult();
        }
    }

}
