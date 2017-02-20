package com.jinjunhang.onlineclass.ui.cell.mainpage;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.ui.cell.BaseListViewCell;
import com.sunfusheng.marqueeview.MarqueeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jinjunhang on 17/2/18.
 */

public class CourseNotifyCell extends BaseListViewCell {

    private List<String> mCourseNotifies = new ArrayList<>();

    public CourseNotifyCell(Activity activity) {super(activity);}

    public void setCourseNotify(List<String> courseNotifies) {
        mCourseNotifies = courseNotifies;
    }

    @Override
    public ViewGroup getView() {
        View v = mActivity.getLayoutInflater().inflate(R.layout.list_item_mainpage_course_notify, null);

        MarqueeView advTextView = (MarqueeView)v.findViewById(R.id.course_notifyt_text);

        if (mCourseNotifies.size() == 0) {
            advTextView.setVisibility(View.INVISIBLE);
        } else {
            advTextView.startWithList(mCourseNotifies);
            advTextView.setVisibility(View.VISIBLE);
        }

        return (LinearLayout)v.findViewById(R.id.list_item_viewgroup);
    }

}
