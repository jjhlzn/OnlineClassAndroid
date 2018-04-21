package com.jinjunhang.onlineclass.ui.fragment.mainpage;

import android.app.Activity;
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

import com.jinjunhang.framework.lib.LoadingAnimation;
import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.Album;
import com.jinjunhang.onlineclass.service.GetTuijianCoursesRequest;
import com.jinjunhang.onlineclass.service.GetTuijianCoursesResponse;
import com.jinjunhang.onlineclass.ui.cell.MainPageCourseCell;
import com.jinjunhang.onlineclass.ui.cell.ListViewCell;
import com.jinjunhang.onlineclass.ui.cell.SectionSeparatorCell;
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
    private String mType;
    private HeaderAdvCell mHeaderAdvCell;
    private List<Album> mCourses;
    private FragmentActivity mActivity;
    private boolean mIsLoading;

    public Page(FragmentActivity activity, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, String type) {
        mActivity = activity;
        mCourses = new ArrayList<>();
        this.mType = type;

        v = inflater.inflate(R.layout.activity_fragment_pushdownrefresh, container, false);

        mListView = (ListView) v.findViewById(R.id.listView);

        mLoading = new LoadingAnimation(activity);

        //去掉列表的分割线
        mListView.setDividerHeight(0);
        mListView.setDivider(null);

        int maxShowRows = 10;

        mFunctionManager = new ExtendFunctionManager(ExtendFunctoinVariableInfoManager.getInstance(), maxShowRows,
                activity, false, type);

        if (mCells.size() == 0) {
            mHeaderAdvCell = new HeaderAdvCell(activity, mLoading);
            mCells.add(mHeaderAdvCell);

            //mCells.add(new CourseNotifyCell(activity));
            mCells.add(new SectionSeparatorCell(activity));

            int functionRowCount = mFunctionManager.getRowCount();
            for (int i = 0; i < functionRowCount; i++) {
                mCells.add(mFunctionManager.getCell(i));
            }
            mCells.add(new SectionSeparatorCell(activity));

            //mCells.add(new MainPageWhiteSeparatorCell(activity));
            //mCells.add(new FooterCell(activity));
        }

        mHeaderAdvCell.updateAds();
        mHeaderAdvCell.updateTouTiao();

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

        new GetTuijianCoursesTask().execute();
    }

    private void setCoursesView() {
        if (mCells.size() > 4) {
            int size = mCells.size();
            for(int i = 0; i < size - 4; i++) {
                mCells.remove(4);
            }
        }

        for(Album course : mCourses) {
            mCells.add(new MainPageCourseCell(mActivity, course));
        }

        mMainPageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        new GetTuijianCoursesTask().execute();
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

    private class GetTuijianCoursesTask extends AsyncTask<Void, Void, GetTuijianCoursesResponse> {

        @Override
        protected GetTuijianCoursesResponse doInBackground(Void... voids) {
            GetTuijianCoursesRequest request = new GetTuijianCoursesRequest();
            return new BasicService().sendRequest(request);
        }

        @Override
        protected void onPostExecute(final GetTuijianCoursesResponse response) {
            super.onPostExecute(response);

            if (!response.isSuccess()) {
                LogHelper.e(TAG, response.getErrorMessage());
                return;
            }

            mCourses = response.getCourses();
            LogHelper.d("mCourse.size() = " + mCourses.size());
            setCoursesView();
        }
    }


}