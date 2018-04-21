package com.jinjunhang.onlineclass.ui.cell.player;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.Course;
import com.jinjunhang.onlineclass.ui.cell.BaseListViewCell;

public class BeforeCourseItemCell extends BaseListViewCell {

    private Course mCourse;

    public BeforeCourseItemCell(Activity activity, Course course) {
        super(activity);
        this.mCourse = course;
    }

    @Override
    public ViewGroup getView() {
        View v = mActivity.getLayoutInflater().inflate(R.layout.new_player_view_before_course_item, null);
        TextView sequence = (TextView) v.findViewById(R.id.number);
        TextView title = (TextView) v.findViewById(R.id.title);
        TextView time = (TextView) v.findViewById(R.id.time);

        sequence.setText(mCourse.getSequence()+".");
        title.setText(mCourse.getTitle());
        time.setText(mCourse.getTime());

        return (RelativeLayout)v.findViewById(R.id.container);
    }

    public int getMeasuredHeight() {
        return getView().getMeasuredHeight();
    }
}