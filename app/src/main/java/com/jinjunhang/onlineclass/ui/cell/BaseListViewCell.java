package com.jinjunhang.onlineclass.ui.cell;

import android.app.Activity;
import android.view.ViewGroup;

/**
 * Created by lzn on 16/6/20.
 */
public abstract class BaseListViewCell implements ListViewCell {

    protected Activity mActivity;

    public BaseListViewCell(Activity activity) {
        mActivity = activity;
    }

    private BaseListViewCell() {}

    @Override
    public boolean isSection() {
        return false;
    }

    @Override
    public ViewGroup getView() {
        return null;
    }

    @Override
    public void setActivity(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void release() {

    }

    @Override
    public Object getContent() {
        return null;
    }

    @Override
    public void onClick() {

    }
}
