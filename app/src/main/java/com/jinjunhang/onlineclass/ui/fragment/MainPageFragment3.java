package com.jinjunhang.onlineclass.ui.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import com.jinjunhang.onlineclass.db.HeaderAdvManager;
import com.jinjunhang.onlineclass.service.GetCourseNotifyRequest;
import com.jinjunhang.onlineclass.service.GetCourseNotifyResponse;
import com.jinjunhang.onlineclass.service.GetExtendFunctionInfoRequest;
import com.jinjunhang.onlineclass.service.GetExtendFunctionInfoResponse;
import com.jinjunhang.onlineclass.service.GetFooterAdvsRequest;
import com.jinjunhang.onlineclass.service.GetFooterAdvsResponse;
import com.jinjunhang.onlineclass.service.GetHeaderAdvRequest;
import com.jinjunhang.onlineclass.service.GetHeaderAdvResponse;
import com.jinjunhang.onlineclass.ui.cell.ListViewCell;
import com.jinjunhang.onlineclass.ui.cell.MainPageWhiteSeparatorCell;
import com.jinjunhang.onlineclass.ui.cell.mainpage.CourseNotifyCell;
import com.jinjunhang.onlineclass.ui.cell.mainpage.FooterCell;
import com.jinjunhang.onlineclass.ui.cell.mainpage.HeaderAdvCell;
import com.jinjunhang.onlineclass.ui.lib.ExtendFunctionManager;
import com.jinjunhang.onlineclass.ui.lib.ExtendFunctoinVariableInfoManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by lzn on 16/6/10.
 */
public class MainPageFragment3 extends android.support.v4.app.Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = LogHelper.makeLogTag(MainPageFragment3.class);

    private List<ListViewCell> mCells = new ArrayList<>();
    private ExtendFunctionManager mFunctionManager;

    private ListView mListView;
    private MainPageAdapter mMainPageAdapter;
    private ExtendFunctoinVariableInfoManager mFunctoinMessageManager = ExtendFunctoinVariableInfoManager.getInstance();
    private HeaderAdvManager mHeaderAdvManager = HeaderAdvManager.getInstance();
    private LoadingAnimation mLoading;

    //定时刷新主图
    private final Handler mHandler = new Handler();
    private ScheduledFuture<?> mScheduleFuture;
    private final ScheduledExecutorService mExecutorService = Executors.newSingleThreadScheduledExecutor();
    private final Runnable mUpdateHeaderAdv = new Runnable() {
        @Override
        public void run() {
            new GetHeaderAdvTask().execute();
        }
    };
    private static final long UPDATE_HEADER_ADV_INTERVAL = 60000;
    private static final long UPDATE_HEADER_ADV_INITIAL_INTERVAL = 30000;

    private boolean mIsLoading = false;
    private SwipeRefreshLayout mSwipeRefreshLayout;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_fragment_pushdownrefresh, container, false);
        mListView = (ListView) v.findViewById(R.id.listView);

        mLoading = new LoadingAnimation(getActivity());

        //去掉列表的分割线
        mListView.setDividerHeight(0);
        mListView.setDivider(null);

        int maxShowRows = 10;

        mFunctionManager = new ExtendFunctionManager(ExtendFunctoinVariableInfoManager.getInstance(), maxShowRows, getActivity(), false, ExtendFunctionManager.RONGZI_TYPE);

        if (mCells.size() == 0) {
            mCells.add(new HeaderAdvCell(getActivity(), mLoading));

            mCells.add(new CourseNotifyCell(getActivity()));

            int functionRowCount = mFunctionManager.getRowCount();
            for (int i = 0; i < functionRowCount; i++) {
                mCells.add(mFunctionManager.getCell(i));
            }
            mCells.add(new MainPageWhiteSeparatorCell(getActivity()));
            mCells.add(new FooterCell(getActivity()));
        }

        mMainPageAdapter = new MainPageAdapter(mCells);
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
        new GetFunctionInfoRequestTask().execute();
        new GetHeaderAdvTask().execute();
        new GetFooterAdvTask().execute();
        new GetCourseNotifyTask().execute();

        //开始定时刷新主图
        scheduleUpdateHeaderAdv();



        return v;
    }

    @Override
    public void onResume() {
        LogHelper.d(TAG, "MainPageFragment onResume called");
        super.onResume();
        new GetCourseNotifyTask().execute();
        mMainPageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onRefresh() {
        if (mIsLoading) {
            return;
        }
        mIsLoading = true;

        new GetHeaderAdvTask().execute();
        new GetCourseNotifyTask().execute();
    }

    private void stopUpdateHeaderAdv() {
        if (mScheduleFuture != null) {
            mScheduleFuture.cancel(false);
        }
    }

    protected void scheduleUpdateHeaderAdv() {
        stopUpdateHeaderAdv();
        if (!mExecutorService.isShutdown()) {
            mScheduleFuture = mExecutorService.scheduleAtFixedRate(
                    new Runnable() {
                        @Override
                        public void run() {
                            mHandler.post(mUpdateHeaderAdv);
                        }
                    }, UPDATE_HEADER_ADV_INTERVAL,
                    UPDATE_HEADER_ADV_INITIAL_INTERVAL, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopUpdateHeaderAdv();
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

    private class GetFunctionInfoRequestTask extends AsyncTask<Void, Void, GetExtendFunctionInfoResponse> {

        @Override
        protected GetExtendFunctionInfoResponse doInBackground(Void... voids) {
            GetExtendFunctionInfoRequest request = new GetExtendFunctionInfoRequest();
            return new BasicService().sendRequest(request);
        }

        @Override
        protected void onPostExecute(GetExtendFunctionInfoResponse response) {
            super.onPostExecute(response);

            if (!response.isSuccess()){
                LogHelper.e(TAG, response.getStatus(), response.getErrorMessage());
                return;
            }

            List<GetExtendFunctionInfoResponse.ExtendFunctionInfo> functions = response.getFunctions();
            if (functions.size() == 0) {
                return;
            }

            for (GetExtendFunctionInfoResponse.ExtendFunctionInfo function : functions) {
                mFunctoinMessageManager.update(function.getCode(), function.hasMessage() ? 1 : 0);
                mFunctoinMessageManager.updateName(function.getCode(), function.getName());
                LogHelper.d(TAG, "imageUrl: " + function.getImageUrl());
                mFunctoinMessageManager.updateImageUrl(function.getCode(), function.getImageUrl());
            }

            mFunctoinMessageManager.reload();
            mFunctionManager.reload();

            LogHelper.d(TAG, "notify adpter data changed");
            mMainPageAdapter.notifyDataSetChanged();
        }
    }

    private class GetHeaderAdvTask extends AsyncTask<Void, Void, GetHeaderAdvResponse> {

        @Override
        protected GetHeaderAdvResponse doInBackground(Void... voids) {
            GetHeaderAdvRequest request = new GetHeaderAdvRequest();
            return new BasicService().sendRequest(request);
        }


        @Override
        protected void onPostExecute(GetHeaderAdvResponse response) {
            super.onPostExecute(response);
            mIsLoading = false;
            mSwipeRefreshLayout.setRefreshing(false);

            if (!response.isSuccess()) {
                LogHelper.e(TAG, response.getErrorMessage());
                return;
            }
            List<GetHeaderAdvResponse.HeaderAdvImage> advImages = response.getImageAdvs();
            if (advImages.size() == 0) {
                return;
            }
            GetHeaderAdvResponse.HeaderAdvImage advImage = advImages.get(0);
            mHeaderAdvManager.update(advImage);
            mMainPageAdapter.notifyDataSetChanged();
        }
    }

    private class GetFooterAdvTask extends AsyncTask<Void, Void, GetFooterAdvsResponse> {

        @Override
        protected GetFooterAdvsResponse doInBackground(Void... voids) {
            return new BasicService().sendRequest(new GetFooterAdvsRequest());
        }

        @Override
        protected void onPostExecute(GetFooterAdvsResponse resp) {
            super.onPostExecute(resp);
            if (!resp.isSuccess()) {
                LogHelper.e(TAG, resp.getErrorMessage());
                return;
            }

            List<GetFooterAdvsResponse.FooterAdv> advs = resp.getImageAdvs();
            if (advs.size() != 4) {
                LogHelper.e(TAG, "count of footer advs: ", resp.getImageAdvs().size());
                return;
            }

            FooterCell footerCell = (FooterCell) mMainPageAdapter.getItem(6);
            footerCell.setAdvs(advs);
            mMainPageAdapter.notifyDataSetChanged();
        }
    }

    private class GetCourseNotifyTask extends  AsyncTask<Void, Void, GetCourseNotifyResponse> {

        @Override
        protected GetCourseNotifyResponse doInBackground(Void... voids) {
            return new BasicService().sendRequest(new GetCourseNotifyRequest());
        }

        @Override
        protected void onPostExecute(GetCourseNotifyResponse resp) {
            super.onPostExecute(resp);
            if (!resp.isSuccess()) {
                LogHelper.e(TAG, resp.getErrorMessage());
                return;
            }

            CourseNotifyCell courseNotifyCell = (CourseNotifyCell) mMainPageAdapter.getItem(1);
            courseNotifyCell.setCourseNotify(resp.getCourseNotifies());
            mMainPageAdapter.notifyDataSetChanged();
        }
    }



}
