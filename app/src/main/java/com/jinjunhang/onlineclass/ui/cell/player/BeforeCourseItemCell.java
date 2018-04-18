package com.jinjunhang.onlineclass.ui.cell.player;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.BeforeCourse;
import com.jinjunhang.onlineclass.ui.cell.BaseListViewCell;

public class BeforeCourseItemCell extends BaseListViewCell {

    private BeforeCourse mBeforeCourse;

    public BeforeCourseItemCell(Activity activity, BeforeCourse beforeCourse) {
        super(activity);
        this.mBeforeCourse = beforeCourse;
    }

    @Override
    public ViewGroup getView() {
        View v = mActivity.getLayoutInflater().inflate(R.layout.new_player_view_before_course_item, null);
        TextView sequence = (TextView) v.findViewById(R.id.number);
        TextView title = (TextView) v.findViewById(R.id.title);
        TextView time = (TextView) v.findViewById(R.id.time);

        sequence.setText(mBeforeCourse.getSequence()+".");
        title.setText(mBeforeCourse.getTitle());
        time.setText(mBeforeCourse.getTime());

        return (RelativeLayout)v.findViewById(R.id.container);
    }

    public int getMeasuredHeight() {
        return getView().getMeasuredHeight();
    }
}