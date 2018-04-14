package com.jinjunhang.onlineclass.ui.cell;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.onlineclass.R;

/**
 * Created by jinjunhang on 2018/4/11.
 */

public class LiveCourseCell extends BaseListViewCell {

    private static final String TAG = LogHelper.makeLogTag(CourseCell.class);

    private ViewGroup mView;


    public LiveCourseCell(Activity activity) {
        super(activity);
    }

    public void setView(ViewGroup view) {
        mView = view;
    }


    @Override
    public ViewGroup getView() {
        View view = mActivity.getLayoutInflater().inflate(R.layout.list_item_live_course, null);

        ViewGroup g = (ViewGroup)view.findViewById(R.id.list_item_course);
        return g;
    }
}
