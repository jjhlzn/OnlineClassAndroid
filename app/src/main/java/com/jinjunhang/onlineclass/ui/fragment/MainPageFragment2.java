package com.jinjunhang.onlineclass.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.AlbumType;
import com.jinjunhang.onlineclass.service.GetFunctionMessageRequest;
import com.jinjunhang.onlineclass.service.GetFunctionMessageResponse;
import com.jinjunhang.onlineclass.service.GetParameterInfoResponse;
import com.jinjunhang.onlineclass.ui.activity.album.AlbumListActivity;
import com.jinjunhang.onlineclass.ui.cell.AlbumTypeCell;
import com.jinjunhang.onlineclass.ui.cell.ListViewCell;
import com.jinjunhang.onlineclass.ui.cell.mainpage.FooterCell;
import com.jinjunhang.onlineclass.ui.cell.mainpage.HeaderCell;
import com.jinjunhang.onlineclass.ui.fragment.album.AlbumListFragment;
import com.jinjunhang.onlineclass.ui.lib.BaseListViewOnItemClickListener;
import com.jinjunhang.onlineclass.ui.lib.ExtendFunctionManager;
import com.jinjunhang.onlineclass.ui.lib.ExtendFunctoinMessageManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
    private boolean hasExecCreateViewCompleted = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_fragment_pushdownrefresh, container, false);
        mListView = (ListView) v.findViewById(R.id.listView);

        //去掉列表的分割线
        mListView.setDividerHeight(0);
        mListView.setDivider(null);

        int maxShowRows = 10;

        mFunctionManager = new ExtendFunctionManager(ExtendFunctoinMessageManager.getInstance(), maxShowRows, getActivity(), true);

        if (mCells.size() == 0) {
            mCells.add(new HeaderCell(getActivity()));

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
                if (position == 0) {

                }
                /*
                if (position < 3) {
                    LogHelper.d(TAG, "on item click");
                    BaseListViewOnItemClickListener.onItemClickEffect(parent, view, position, id);
                    AlbumTypeCell cell = (AlbumTypeCell)mCells.get(position);
                    AlbumType albumType = cell.getAlbumType();
                    Intent i = new Intent(getActivity(), AlbumListActivity.class);
                    i.putExtra(AlbumListFragment.EXTRA_ALBUMTYPE, albumType);
                    startActivity(i);
                }*/
            }
        });
        //hasExecCreateViewCompleted = true;
        new GetFunctionMessageRequestTask().execute();
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



}
