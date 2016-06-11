package com.jinjunhang.framework.controller;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.service.PagedServerResponse;
import com.jinjunhang.framework.service.ServerResponse;
import com.jinjunhang.onlineclass.R;

import java.util.List;

/**
 * Created by lzn on 16/6/10.
 */
public class PagableController {

    private final static String TAG = "PagableController";

    private Activity mActivity;
    private ListView mListView;
    private PagableArrayAdapter mPagableArrayAdapter;
    private PagableTask mPagableTask;
    private PagableRequestHandler mPagableRequestHandler;

    //用于load more
    private View mFooterView;
    private boolean mIsLoading;
    private boolean mMoreDataAvailable;

    public PagableController(Activity activity, ListView listView) {
        mActivity = activity;
        mListView = listView;
        mIsLoading = false;
        mMoreDataAvailable = true;
    }

    public void setPagableArrayAdapter(PagableArrayAdapter pagableArrayAdapter) {
        mPagableArrayAdapter = pagableArrayAdapter;
        mListView.setAdapter(mPagableArrayAdapter);
        setFootView();
    }

    public void setPagableRequestHandler(PagableRequestHandler pagableRequestHandler) {
        mPagableRequestHandler = pagableRequestHandler;
    }

    public void setOnScrollListener(AbsListView.OnScrollListener listener) {
        mListView.setOnScrollListener(listener);
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


    private void setFootView() {
        mFooterView = LayoutInflater.from(mActivity).inflate(R.layout.loading_view, null);

        resetFootView();
        mListView.addFooterView(mFooterView, null, false);

    }

    private void resetFootView() {
        if (!mMoreDataAvailable) {
            mFooterView.findViewById(R.id.loading_progressbar).setVisibility(View.GONE);
            if (mPagableArrayAdapter.getCount() > 0)
                ((TextView)mFooterView.findViewById(R.id.loading_message)).setText("已加载全部数据");
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

        @Override
        public int getCount() {
            return mDataSet.size();
        }

        @Override
        public T getItem(int position) {
            return mDataSet.get(position);
        }

    }

    public interface PagableRequestHandler {
        public PagedServerResponse handle();
    }

    public static class PagableTask extends AsyncTask<Void, Void, PagedServerResponse> {
        private final static String TAG = "PagableTask";
        private PagableController mPagableController;
        private PagableRequestHandler mPagableHandler;

        public PagableTask(PagableController pagableController, PagableRequestHandler pagableHandler) {
            mPagableController = pagableController;
            mPagableHandler = pagableHandler;
        }

        @Override
        protected PagedServerResponse doInBackground(Void... params) {
            Log.d(TAG, "doInBackground");
            return mPagableHandler.handle();
            //return execute();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mPagableController.mIsLoading = true;
        }

        @Override
        protected void onPostExecute(PagedServerResponse resp) {
            super.onPostExecute(resp);

            if (resp.getStatus() != ServerResponse.SUCCESS) {
                Log.e(TAG, "resp return error, status = " + resp.getStatus() + ", errorMessage = " + resp.getErrorMessage());
                Utils.showMessage(mPagableController.mActivity, resp.getErrorMessage());
                return;
            }

            mPagableController.mPagableArrayAdapter.addMoreData(resp.getResultSet());

            if (resp.getTotalNumber() <= mPagableController.mPagableArrayAdapter.getCount()) {
                mPagableController.mMoreDataAvailable = false;
            } else {
                mPagableController.mMoreDataAvailable = true;
            }

            mPagableController.mIsLoading = false;
            Log.d(TAG, "loading complete");

            mPagableController.resetFootView();

        }
    }


}
