package com.jinjunhang.onlineclass.ui.cell;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.service.GetTuijianCoursesResponse;

import java.util.ArrayList;

/**
 * Created by jinjunhang on 2018/4/15.
 */

public class LiveCourseCell extends MainPageCourseCell {
    public LiveCourseCell(Activity activity, GetTuijianCoursesResponse.Course course) {
        super(activity, course);
    }

    @Override
    public ViewGroup getView() {
        View view = mActivity.getLayoutInflater().inflate(R.layout.list_item_live_course, null);
        ViewGroup g = (ViewGroup)view.findViewById(R.id.list_item_course);
        setCourseView(view);

        TextView status = (TextView)view.findViewById(R.id.status);
        TextView listener = (TextView)view.findViewById(R.id.listenerCount);
        TextView liveTime = (TextView)view.findViewById(R.id.liveTime);

        status.setText(mCourse.getStatus());
        listener.setText(mCourse.getListenerCount()+"");
        liveTime.setText(mCourse.getLiveTime());

        if ("正在直播".equals(mCourse.getStatus())) {
            status.setTextColor(0xBE1520);
        } else {
            status.setTextColor(0x000000);
        }

        if (mCourse.getListenerCount() == 0) {
            listener.setTextColor(0x000000);
        } else {
            listener.setTextColor(0xBE1520);
        }
        return g;
    }
}
