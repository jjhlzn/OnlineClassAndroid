package com.jinjunhang.onlineclass.ui.cell;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.jinjunhang.player.utils.LogHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jjh on 2016-7-3.
 */
public class ListViewCellAdapter extends ArrayAdapter<ListViewCell> {
    private static final String TAG = LogHelper.makeLogTag(ListViewCellAdapter.class);
    protected List<ListViewCell> mCells;
    protected Activity mActivity;

    public ListViewCellAdapter(Activity activity, List<ListViewCell> cells) {
        super(activity, 0, cells);
        mCells = cells;
        mActivity = activity;
    }

    public ListViewCellAdapter(Activity activity) {
        this(activity, new ArrayList<ListViewCell>());

    }

    public void setCells(List<ListViewCell> cells) {
        mCells = cells;
        notifyDataSetChanged();
    }

    public List<ListViewCell> getCells() {
        return mCells;
    }

    @Override
    public int getCount() {
        return mCells.size();
    }

    @Override
    public ListViewCell getItem(int position) {
        return mCells.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListViewCell item = getItem(position);
        LogHelper.d(TAG, "position = " + position);
        return item.getView();
    }
}