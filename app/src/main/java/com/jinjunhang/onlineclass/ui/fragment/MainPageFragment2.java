package com.jinjunhang.onlineclass.ui.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jinjunhang.framework.lib.LoadingAnimation;
import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.framework.service.ServerResponse;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.db.HeaderAdvManager;
import com.jinjunhang.onlineclass.service.GetFunctionMessageRequest;
import com.jinjunhang.onlineclass.service.GetFunctionMessageResponse;
import com.jinjunhang.onlineclass.service.GetHeaderAdvRequest;
import com.jinjunhang.onlineclass.service.GetHeaderAdvResponse;
import com.jinjunhang.onlineclass.ui.cell.ListViewCell;
import com.jinjunhang.onlineclass.ui.cell.mainpage.FooterCell;
import com.jinjunhang.onlineclass.ui.cell.mainpage.HeaderAdvCell;
import com.jinjunhang.onlineclass.ui.lib.ExtendFunctionManager;
import com.jinjunhang.onlineclass.ui.lib.ExtendFunctoinMessageManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by lzn on 16/6/10.
 */
public class MainPageFragment2 extends android.support.v4.app.Fragment {

    private static final String TAG = LogHelper.makeLogTag(MainPageFragment2.class);

    private List<ListViewCell> mCells = new ArrayList<>();
    private ExtendFunctionManager mFunctionManager;

    private ListView mListView;
    private MainPageAdapter mMainPageAdapter;
    private ExtendFunctoinMessageManager mFunctoinMessageManager = ExtendFunctoinMessageManager.getInstance();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_fragment_pushdownrefresh, container, false);
        mListView = (ListView) v.findViewById(R.id.listView);

        mLoading = new LoadingAnimation(getActivity());

        //去掉列表的分割线
        mListView.setDividerHeight(0);
        mListView.setDivider(null);

        int maxShowRows = 10;

        mFunctionManager = new ExtendFunctionManager(ExtendFunctoinMessageManager.getInstance(), maxShowRows, getActivity(), true);

        if (mCells.size() == 0) {
            mCells.add(new HeaderAdvCell(getActivity(), mLoading));

            int functionRowCount = mFunctionManager.getRowCount();
            for (int i = 0; i < functionRowCount; i++) {
                mCells.add(mFunctionManager.getCell(i));
            }

            mCells.add(new FooterCell(getActivity()));
        }

        mMainPageAdapter = new MainPageAdapter(mCells);
        mListView.setAdapter(mMainPageAdapter);

        //不能下拉刷新
        v.findViewById(R.id.swipe_refresh_layout).setEnabled(false);


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogHelper.d(TAG, "mList onItemClick: ", position);
            }
        });
        new GetFunctionMessageRequestTask().execute();
        new GetHeaderAdvTask().execute();

        //开始定时刷新主图
        scheduleUpdateHeaderAdv();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMainPageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
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

    private class GetFunctionMessageRequestTask extends AsyncTask<Void, Void, GetFunctionMessageResponse> {

        @Override
        protected GetFunctionMessageResponse doInBackground(Void... voids) {
            GetFunctionMessageRequest request = new GetFunctionMessageRequest();
            return new BasicService().sendRequest(request);
        }

        @Override
        protected void onPostExecute(GetFunctionMessageResponse response) {
            super.onPostExecute(response);

            if (!response.isSuccess()){
                LogHelper.e(TAG, response.getStatus(), response.getErrorMessage());
                return;
            }

            Map<String, Integer> map = response.getMap();
            Iterator<Map.Entry<String, Integer>> it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Integer> entry = it.next();
                String key = entry.getKey();
                Integer value = entry.getValue();
                mFunctoinMessageManager.update(key, value);
            }
            mFunctoinMessageManager.reload();

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



}
