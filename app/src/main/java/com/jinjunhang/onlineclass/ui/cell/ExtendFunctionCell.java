package com.jinjunhang.onlineclass.ui.cell;

import android.app.Activity;
import android.view.ViewGroup;

/**
 * Created by lzn on 16/6/20.
 */
public class ExtendFunctionCell extends BaseListViewCell {

    private ViewGroup mView;

    public ExtendFunctionCell(Activity activity) {
        super(activity);
    }

    public void setView(ViewGroup view) {
        mView = view;
    }

    @Override
    public boolean isSection() {
        return false;
    }

    @Override
    public ViewGroup getView() {
        return mView;
    }
}
