package com.jinjunhang.onlineclass.ui.cell;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by jjh on 2016-7-3.
 */
public class ListViewCellAdapter extends ArrayAdapter<ListViewCell> {
    private List<ListViewCell> mCells;

    public ListViewCellAdapter(Activity activity, List<ListViewCell> cells) {
        super(activity, 0, cells);
        mCells = cells;
    }

    public void setCells(List<ListViewCell> cells) {
        mCells = cells;
        notifyDataSetChanged();
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
        return item.getView();
    }
}