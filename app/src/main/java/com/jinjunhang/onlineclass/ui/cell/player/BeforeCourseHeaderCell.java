package com.jinjunhang.onlineclass.ui.cell.player;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.ui.cell.BaseListViewCell;

public class BeforeCourseHeaderCell extends BaseListViewCell {

    public BeforeCourseHeaderCell(Activity activity) {
        super(activity);
    }

    @Override
    public ViewGroup getView() {
        View v = mActivity.getLayoutInflater().inflate(R.layout.new_player_view_before_course_header, null);


        return (RelativeLayout)v.findViewById(R.id.container);
    }
}
