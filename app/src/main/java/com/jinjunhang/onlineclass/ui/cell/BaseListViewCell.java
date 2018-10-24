package com.jinjunhang.onlineclass.ui.cell;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.jinjunhang.framework.lib.LogHelper;

/**
 * Created by lzn on 16/6/20.
 */
public abstract class BaseListViewCell implements ListViewCell {

    public final static int OTHER_CELL = 0;
    public final static int HEADER_CELL = 1;
    public final static int SEPARATOR_CELL = 2;
    public final static int EXTENDFUNCTION_CELL = 3;
    public final static int POS_CELL = 4;

    public final static int MAINPAGE_COURSE_CELL = 5;
    public final static int ZHUANLAN_HEADER_CELL = 6;
    public final static int ZHUANLAN_CELL = 7;
    public final static int QUESTION_CELL = 8;
    public final static int FINANCE_TOUTIAO_CELL = 9;
    public final static int JPK_HEADER_CELL = 10;
    public final static int MAINPAGE_COURSE_HEADER_CELL = 11;

    public final static int USER_FIRST_SECTION_CELL = 12;
    public final static int USER_SECOND_SECTION_CELL = 13;
    public final static int USER_COMMON_SECTION_CELL = 14;


    private  static final  String TAG = LogHelper.makeLogTag(BaseListViewCell.class);
    protected Activity mActivity;
    protected ViewGroup mParent;

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
    public ViewGroup getView(View convertView) {
        throw  new RuntimeException("not implemented");
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

    @Override
    public void setParentView(ViewGroup parent) {

    }

    protected void setTag(View view) {
        view.setTag(this.getClass().getSimpleName());
    }

    protected boolean canReuse(View convertView) {
        if (convertView != null)
            LogHelper.d(TAG, "simpleName: " + this.getClass().getSimpleName() + ", tag: " + convertView.getTag());
        return convertView != null && this.getClass().getSimpleName().equals(convertView.getTag());
    }

    @Override
    public int getItemViewType() {
        return 0;
    }
}
