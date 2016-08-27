package com.jinjunhang.onlineclass.ui.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.db.KeyValueDao;
import com.jinjunhang.onlineclass.model.AlbumType;
import com.jinjunhang.onlineclass.service.GetParameterInfoRequest;
import com.jinjunhang.onlineclass.service.GetParameterInfoResponse;
import com.jinjunhang.onlineclass.ui.activity.album.AlbumListActivity;
import com.jinjunhang.onlineclass.ui.cell.AdvImageCell;
import com.jinjunhang.onlineclass.ui.cell.AlbumTypeCell;
import com.jinjunhang.onlineclass.ui.cell.AlbumTypeCell2;
import com.jinjunhang.onlineclass.ui.lib.ExtendFunctionManager;
import com.jinjunhang.onlineclass.ui.cell.ListViewCell;
import com.jinjunhang.onlineclass.ui.cell.SectionSeparatorCell;
import com.jinjunhang.onlineclass.ui.fragment.album.AlbumListFragment;
import com.jinjunhang.onlineclass.ui.lib.BaseListViewOnItemClickListener;
import com.jinjunhang.player.utils.LogHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzn on 16/6/10.
 */
public class MainPageFragment extends android.support.v4.app.Fragment {

    private static final String TAG = LogHelper.makeLogTag(MainPageFragment.class);

    private List<AlbumType> mAlbumTypes = AlbumType.getAllAlbumType();
    private KeyValueDao mKeyValueDao;
    private List<ListViewCell> mCells = new ArrayList<>();
    private ExtendFunctionManager mFunctionManager;
    private AdvImageCell mAdvImageCell;

    private ListView mListView;
    private AlbumTypeAdapter mAlbumTypeAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_fragment_pushdownrefresh, container, false);
        mListView = (ListView) v.findViewById(R.id.listView);

        mKeyValueDao = KeyValueDao.getInstance(getActivity());

        //去掉列表的分割线
        mListView.setDividerHeight(0);
        mListView.setDivider(null);

        int maxShowRows = 10;

        mFunctionManager = new ExtendFunctionManager(maxShowRows, getActivity(), true);



        if (mCells.size() == 0) {
            int i = 0;
            List<String> descKeys = new ArrayList<>();
            descKeys.add(GetParameterInfoResponse.LIVE_DESCRIPTON);
            descKeys.add(GetParameterInfoResponse.VIP_DESCRIPTON);
            descKeys.add(GetParameterInfoResponse.BEFORE_DESCRIPTON);
            for (AlbumType albumType : mAlbumTypes) {
                AlbumTypeCell item = null;
                item = new AlbumTypeCell2(getActivity(), albumType, mKeyValueDao.getValue(descKeys.get(i), ""));
                mCells.add(item);
                i++;
            }

            mCells.add(new SectionSeparatorCell(getActivity()));


            int functionRowCount = mFunctionManager.getRowCount();
            for (i = 0; i < functionRowCount; i++) {
                mCells.add(mFunctionManager.getCell(i));
            }

            mAdvImageCell = new AdvImageCell(getActivity());
            mCells.add(mAdvImageCell);
        }



        mAlbumTypeAdapter = new AlbumTypeAdapter(mCells);
        mListView.setAdapter(mAlbumTypeAdapter);

        //不能下拉刷新
        v.findViewById(R.id.swipe_refresh_layout).setEnabled(false);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < 3) {
                    LogHelper.d(TAG, "on item click");
                    BaseListViewOnItemClickListener.onItemClickEffect(parent, view, position, id);
                    AlbumTypeCell cell = (AlbumTypeCell)mCells.get(position);
                    AlbumType albumType = cell.getAlbumType();
                    Intent i = new Intent(getActivity(), AlbumListActivity.class);
                    i.putExtra(AlbumListFragment.EXTRA_ALBUMTYPE, albumType);
                    startActivity(i);
                }
            }
        });

        new GetParameterTask().execute();

        return v;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdvImageCell != null) {
            mAdvImageCell.release();
        }
    }

    private class AlbumTypeAdapter extends ArrayAdapter<ListViewCell> {
        private List<ListViewCell> mViewCells;

        public AlbumTypeAdapter(List<ListViewCell> albumTypes) {
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

    private class GetParameterTask extends AsyncTask<Void, Void, GetParameterInfoResponse> {

        @Override
        protected GetParameterInfoResponse doInBackground(Void... params) {
            GetParameterInfoRequest request = new GetParameterInfoRequest();
            List<String> keys = new ArrayList<>();
            keys.add(GetParameterInfoResponse.LIVE_DESCRIPTON);
            keys.add(GetParameterInfoResponse.VIP_DESCRIPTON);
            keys.add(GetParameterInfoResponse.BEFORE_DESCRIPTON);
            LogHelper.d(TAG, "keys = " + keys);
            request.setKeywords(keys);
            return new BasicService().sendRequest(request);
        }

        @Override
        protected void onPostExecute(GetParameterInfoResponse resp) {
            super.onPostExecute(resp);
            if (!resp.isSuccess()) {
                LogHelper.e(TAG, resp.getErrorMessage());
                return;
            }

            updateCell(GetParameterInfoResponse.LIVE_DESCRIPTON, resp, (AlbumTypeCell2) mAlbumTypeAdapter.getItem(0));
            updateCell(GetParameterInfoResponse.VIP_DESCRIPTON, resp, (AlbumTypeCell2) mAlbumTypeAdapter.getItem(1));
            updateCell(GetParameterInfoResponse.BEFORE_DESCRIPTON, resp, (AlbumTypeCell2) mAlbumTypeAdapter.getItem(2));

            mAlbumTypeAdapter.notifyDataSetChanged();
        }

        private void updateCell(String key, GetParameterInfoResponse resp, AlbumTypeCell2 cell) {
            String description = resp.getValue(key, "");
            mKeyValueDao.saveOrUpdate(key, description);
            cell.setDescription(description);
        }
    }




}
