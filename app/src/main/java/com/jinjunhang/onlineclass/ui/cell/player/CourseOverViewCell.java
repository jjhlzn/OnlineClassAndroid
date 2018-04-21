package com.jinjunhang.onlineclass.ui.cell.player;


import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinjunhang.onlineclass.model.Course;

import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.ui.cell.BaseListViewCell;

public class CourseOverViewCell extends BaseListViewCell {

    private Course mCourse;
    private TextView mContentView;

    public CourseOverViewCell(Activity activity, Course course) {
        super(activity);
        mCourse = course;
    }

    public void setCourse(Course course) {
        mCourse = course;
    }

    @Override
    public ViewGroup getView() {
        View v = mActivity.getLayoutInflater().inflate(R.layout.new_player_view_course_overview, null);
        mContentView = (TextView) v.findViewById(R.id.content);
        mContentView.setText(mCourse.getIntroduction());
        return (RelativeLayout)v.findViewById(R.id.container);
    }

    public int getMeasuredHeight() {
        return getView().getMeasuredHeight();
    }
}