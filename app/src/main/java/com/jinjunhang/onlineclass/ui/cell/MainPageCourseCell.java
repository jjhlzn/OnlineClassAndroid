package com.jinjunhang.onlineclass.ui.cell;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.Album;

/**
 * Created by lzn on 16/6/20.
 */
public class MainPageCourseCell extends BaseListViewCell {

    private static final String TAG = LogHelper.makeLogTag(MainPageCourseCell.class);

    protected View mView;
    protected Album mCourse;
    protected Activity mActivity;

    public MainPageCourseCell(Activity activity, Album course) {
        super(activity);
        mCourse = course;
        mActivity = activity;
    }

    public Album getCourse() {
        return mCourse;
    }


    @Override
    public ViewGroup getView() {
        if (mView == null) {
            mView = mActivity.getLayoutInflater().inflate(R.layout.list_item_mainpage_course, null);
            setCourseView(mView);
            return mView.findViewById(R.id.list_item_course);
        } else {
            return mView.findViewById(R.id.list_item_course);
        }
    }

    protected void setCourseView(View view) {

        ImageView imageView = view.findViewById(R.id.course_image);
        Glide
                .with(mActivity)
                .load(mCourse.getImage())
                .placeholder(R.drawable.rect_placeholder)
                .into(imageView);


    }
}
