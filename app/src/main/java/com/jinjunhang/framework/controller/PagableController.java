package com.jinjunhang.framework.controller;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.service.PagedServerResponse;
import com.jinjunhang.framework.service.ServerResponse;
import com.jinjunhang.onlineclass.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;
import java.util.logging.LogManager;

/**
 * Created by lzn on 16/6/10.
 */
public class PagableController implements OnRefreshListener {

    private final static String TAG = LogHelper.makeLogTag(PagableController.class);

    private Activity mActivity;
    private ListView mListView;
    private PagableArrayAdapter mPagableArrayAdapter;
    private PagableRequestHandler mPagableRequestHandler;
    private SmartRefreshLayout mSwipeRefreshLayout;
    private PagableErrorResponseHandler mErrorResponseHanlder = new DefaultPagableErrorResponseHanlder();

    //用于load more
    private View mFooterView;
    private boolean mIsLoading;
    private boolean mIsRefreshing;
    private boolean mMoreDataAvailable;
    private int mPageIndex;
    private boolean mIsShowLoadCompleteTip = false;

    public PagableController(Activity activity, ListView listView) {
        mActivity = activity;
        mListView = listView;
        mIsLoading = false;
        mMoreDataAvailable = true;
        mPageIndex = 0;
    }

    public void setPagableArrayAdapter(PagableArrayAdapter pagableArrayAdapter) {
        mPagableArrayAdapter = pagableArrayAdapter;
        mListView.setAdapter(mPagableArrayAdapter);
        setFootView();
    }

    public Activity getActivity() {
        return mActivity;
    }

    public void setPagableRequestHandler(PagableRequestHandler pagableRequestHandler) {
        mPagableRequestHandler = pagableRequestHandler;
    }

    public void setErrorResponseHanlder(PagableErrorResponseHandler errorResponseHanlder) {
        mErrorResponseHanlder = errorResponseHanlder;
    }

    public void setSwipeRefreshLayout(SmartRefreshLayout swipeRefreshLayout) {
        mSwipeRefreshLayout = swipeRefreshLayout;
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    public void setOnScrollListener(AbsListView.OnScrollListener listener) {
        mListView.setOnScrollListener(listener);
    }

    public void setShowLoadCompleteTip(boolean showLoadCompleteTip) {
        mIsShowLoadCompleteTip = showLoadCompleteTip;
    }



    public boolean isShowLoadCompleteTip() {
        return mIsShowLoadCompleteTip;
    }

    public PagableArrayAdapter getPagableArrayAdapter() {
        return mPagableArrayAdapter;
    }

    public int getPageIndex() {
        return mPageIndex;
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if (!mMoreDataAvailable)
            return;

        int lastInScreen = firstVisibleItem + visibleItemCount;

        if (!mIsLoading) {
            if (lastInScreen == totalItemCount) {
                Log.d(TAG, "start loading more");
                new PagableTask(this, mPagableRequestHandler).execute();
            }
        }
    }

    public void load() {
        if (!mMoreDataAvailable)
            return;

        if (!mIsLoading) {
                Log.d(TAG, "start loading more");
                new PagableTask(this, mPagableRequestHandler).execute();
        }
    }

    public void reset() {
        mPagableArrayAdapter.clear();
        mPageIndex = 0;
        mMoreDataAvailable = true;
        mIsRefreshing = false;
        mIsLoading = false;
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        if (!mIsLoading) {
            mIsRefreshing = true;
            mMoreDataAvailable = true;
            mPageIndex = 0;
            new PagableTask(this, mPagableRequestHandler).execute();
        } else {
            mSwipeRefreshLayout.finishRefresh();
        }
    }

    private void setFootView() {
        mFooterView = LayoutInflater.from(mActivity).inflate(R.layout.loading_view, null);
        resetFootView();
        mListView.addFooterView(mFooterView, null, false);
    }

    private void resetFootView() {
        if (!mMoreDataAvailable) {
            mFooterView.findViewById(R.id.loading_progressbar).setVisibility(View.GONE);
            if (mPagableArrayAdapter.getCount() > 0) {
                //LogHelper.d(TAG, "mIsShowLoadCompleteTip = " + mIsShowLoadCompleteTip);
                if (mIsShowLoadCompleteTip) {
                    ((TextView) mFooterView.findViewById(R.id.loading_message)).setText("已加载全部数据");
                } else  {
                    ((TextView) mFooterView.findViewById(R.id.loading_message)).setText("");
                }
            }
            else {
                ((TextView) mFooterView.findViewById(R.id.loading_message)).setText("没有找到任何数据");
                ((TextView) mFooterView.findViewById(R.id.loading_message)).setTextSize(15);
            }
        }
    }

    public static abstract class PagableArrayAdapter<T> extends ArrayAdapter {
        private List<T> mDataSet;

        public PagableArrayAdapter(PagableController pagableController, List<T> dataSet) {
            super(pagableController.mActivity, 0, dataSet);
            this.mDataSet = dataSet;
        }

        public void addMoreData(List<T> moreData) {
            mDataSet.addAll(moreData);
            notifyDataSetChanged();
        }

        public void setDataSet(List<T> dataSet) {
            mDataSet = dataSet;
            notifyDataSetChanged();
        }

        public List<T> getDataSet() {
            return mDataSet;
        }

        @Override
        public int getCount() {
            return mDataSet.size();
        }

        @Override
        public T getItem(int position) {
            T result = mDataSet.get(position);
            if (result == null) {
                throw new RuntimeException("item can't be null");
            }
            LogHelper.d(TAG, "position: " + position);
            return result;
        }

    }

    public interface PagableRequestHandler {
        public PagedServerResponse handle();
    }

    public interface PagableErrorResponseHandler {
        public void handle(PagedServerResponse resp);
    }

    private class DefaultPagableErrorResponseHanlder implements PagableErrorResponseHandler {
        @Override
        public void handle(PagedServerResponse resp) {
            Log.e(TAG, "resp return error, status = " + resp.getStatus() + ", errorMessage = " + resp.getErrorMessage());
            Utils.showMessage(mActivity, resp.getErrorMessage());
            return;
        }
    }

    private class PagableTask extends AsyncTask<Void, Void, PagedServerResponse> {
        private final static String TAG = "PagableTask";
        private PagableController mPagableController;
        private PagableRequestHandler mPagableHandler;

        public PagableTask(PagableController pagableController, PagableRequestHandler pagableHandler) {
            mPagableController = pagableController;
            mPagableHandler = pagableHandler;
        }

        @Override
        protected PagedServerResponse doInBackground(Void... params) {
            //Log.d(TAG, "doInBackground");
            return mPagableHandler.handle();
            //return execute();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mPagableController.mIsLoading = true;
            if (mPagableController.mIsRefreshing) {
                mPagableController.mSwipeRefreshLayout.finishRefresh();
                Log.d(TAG, "start refresh");
            }
        }

        @Override
        protected void onPostExecute(PagedServerResponse resp) {
            super.onPostExecute(resp);

            if (mPagableController.mIsRefreshing) {
                mPagableController.mSwipeRefreshLayout.finishRefresh();
            }

            if (resp.getStatus() != ServerResponse.SUCCESS) {
                mErrorResponseHanlder.handle(resp);
                mPagableController.mIsRefreshing = false;
                mPagableController.mIsLoading = false;
                return;
            }

            if (mPagableController.mIsRefreshing) {
                Log.d(TAG, "reset dataset");
                mPagableController.mPagableArrayAdapter.setDataSet(resp.getResultSet());
            } else{
                Log.d(TAG, "add more data");
                mPagableController.mPagableArrayAdapter.addMoreData(resp.getResultSet());
            }

            if (resp.getTotalNumber() <= mPagableController.mPagableArrayAdapter.getCount()) {
                mPagableController.mMoreDataAvailable = false;
            } else {
                mPagableController.mMoreDataAvailable = true;
            }

            mPagableController.mIsRefreshing = false;

            mPagableController.mIsLoading = false;
            mPagableController.mPageIndex += 1;
            Log.d(TAG, "loading complete");

            mPagableController.resetFootView();

        }
    }


}
