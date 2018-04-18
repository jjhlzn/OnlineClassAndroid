package com.jinjunhang.onlineclass.ui.cell.player;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.ui.cell.BaseListViewCell;

public class CourseOverViewCell extends BaseListViewCell {

    private TextView mContentView;

    public CourseOverViewCell(Activity activity) {
        super(activity);
    }

    @Override
    public ViewGroup getView() {
        View v = mActivity.getLayoutInflater().inflate(R.layout.new_player_view_course_overview, null);
        mContentView = (TextView) v.findViewById(R.id.content);
        return (RelativeLayout)v.findViewById(R.id.container);
    }

    public int getMeasuredHeight() {
        return getView().getMeasuredHeight();
    }
}