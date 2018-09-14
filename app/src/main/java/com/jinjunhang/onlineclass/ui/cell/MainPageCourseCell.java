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
import com.jinjunhang.onlineclass.service.GetTuijianCoursesResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzn on 16/6/20.
 */
public class MainPageCourseCell extends BaseListViewCell {

    private static final String TAG = LogHelper.makeLogTag(MainPageCourseCell.class);

    protected ViewGroup mView;
    protected Album mCourse;
    protected Activity mActivity;
    private List<ImageView> mStars;

    public MainPageCourseCell(Activity activity, Album course) {
        super(activity);
        mCourse = course;
        mActivity = activity;
        mStars = new ArrayList<>();
    }

    public Album getCourse() {
        return mCourse;
    }

    public void setView(ViewGroup view) {
        mView = view;
    }


    @Override
    public ViewGroup getView() {
        View view = mActivity.getLayoutInflater().inflate(R.layout.list_item_mainpage_course, null);
        ViewGroup g = (ViewGroup)view.findViewById(R.id.list_item_course);
        setCourseView(view);
        return g;
    }

    protected void setCourseView(View view) {
        /*
        TextView nameText = (TextView)view.findViewById(R.id.course_name_txt);
        TextView timeText = (TextView)view.findViewById(R.id.course_time_txt);
        TextView starText = (TextView)view.findViewById(R.id.starsText);
        nameText.setText(mCourse.getName());
        timeText.setText(mCourse.getDate());
        String starsStr = mCourse.getStars() + "";
        if (starsStr.length() >= 3) {
            starsStr = starsStr.substring(0, 3);
        }
        starText.setText(starsStr);

        mStars.clear();
        mStars.add((ImageView)view.findViewById(R.id.star1));
        mStars.add((ImageView)view.findViewById(R.id.star2));
        mStars.add((ImageView)view.findViewById(R.id.star3));
        mStars.add((ImageView)view.findViewById(R.id.star4));
        mStars.add((ImageView)view.findViewById(R.id.star5)); */

        ImageView imageView = (ImageView)view.findViewById(R.id.course_image);
        Glide
                .with(mActivity)
                .load(mCourse.getImage())
                .placeholder(R.drawable.rect_placeholder)
                .into(imageView);

        /*
        double stars = mCourse.getStars();
        for(int i = 0; i < 5; i++) {
            if (stars >= 1) {
                mStars.get(i).setImageDrawable(mActivity.getDrawable(R.drawable.star1));
            } else if (stars <= 0) {
                mStars.get(i).setImageDrawable(mActivity.getDrawable(R.drawable.star2));
            } else {
                mStars.get(i).setImageDrawable(mActivity.getDrawable(R.drawable.star3));
            }
            stars -= 1;
        } */
    }
}
