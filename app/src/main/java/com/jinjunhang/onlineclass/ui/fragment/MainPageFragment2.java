package com.jinjunhang.onlineclass.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.db.KeyValueDao;
import com.jinjunhang.onlineclass.model.AlbumType;
import com.jinjunhang.onlineclass.ui.activity.album.AlbumListActivity;
import com.jinjunhang.onlineclass.ui.cell.AdvImageCell;
import com.jinjunhang.onlineclass.ui.cell.AlbumTypeCell;
import com.jinjunhang.onlineclass.ui.cell.ListViewCell;
import com.jinjunhang.onlineclass.ui.cell.mainpage.FooterCell;
import com.jinjunhang.onlineclass.ui.cell.mainpage.HeaderCell;
import com.jinjunhang.onlineclass.ui.fragment.album.AlbumListFragment;
import com.jinjunhang.onlineclass.ui.lib.BaseListViewOnItemClickListener;
import com.jinjunhang.onlineclass.ui.lib.ExtendFunctionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzn on 16/6/10.
 */
public class MainPageFragment2 extends android.support.v4.app.Fragment {

    private static final String TAG = LogHelper.makeLogTag(MainPageFragment2.class);

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
            mCells.add(new HeaderCell(getActivity()));

            int functionRowCount = mFunctionManager.getRowCount();
            for (int i = 0; i < functionRowCount; i++) {
                mCells.add(mFunctionManager.getCell(i));
            }

            mCells.add(new FooterCell(getActivity()));
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

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
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



}
