package com.jinjunhang.onlineclass.ui.cell;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daimajia.slider.library.SliderLayout;
import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.service.GetTuijianCoursesResponse;
import com.jinjunhang.onlineclass.ui.lib.ExtendFunctionManager;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzn on 16/6/20.
 */
public class CourseCell extends BaseListViewCell {

    private static final String TAG = LogHelper.makeLogTag(CourseCell.class);

    private ViewGroup mView;
    private GetTuijianCoursesResponse.TuijianCourse mCourse;
    private Activity mActivity;
    private List<ImageView> mStars;

    public CourseCell(Activity activity, GetTuijianCoursesResponse.TuijianCourse course) {
        super(activity);
        mCourse = course;
        mActivity = activity;
        mStars = new ArrayList<>();
    }

    public void setView(ViewGroup view) {
        mView = view;
    }


    @Override
    public ViewGroup getView() {
        View view = mActivity.getLayoutInflater().inflate(R.layout.list_item_mainpage_course, null);
        ViewGroup g = (ViewGroup)view.findViewById(R.id.list_item_course);
        TextView nameText = (TextView)view.findViewById(R.id.course_name_txt);
        TextView timeText = (TextView)view.findViewById(R.id.course_time_txt);
        TextView starText = (TextView)view.findViewById(R.id.starsText);
        nameText.setText(mCourse.getName());
        timeText.setText(mCourse.getDate());
        starText.setText(mCourse.getStars() + "");

        mStars.clear();
        mStars.add((ImageView)view.findViewById(R.id.star1));
        mStars.add((ImageView)view.findViewById(R.id.star2));
        mStars.add((ImageView)view.findViewById(R.id.star3));
        mStars.add((ImageView)view.findViewById(R.id.star4));
        mStars.add((ImageView)view.findViewById(R.id.star5));

        ImageView imageView = (ImageView)view.findViewById(R.id.course_image);
        Glide
            .with(mActivity)
            .load(mCourse.getImageUrl())
            .placeholder(R.drawable.course)
            .into(imageView);

        double stars = mCourse.getStars();
        for(int i = 0; i < 5; i++) {
            if (stars > 1) {
                mStars.get(i).setImageDrawable(mActivity.getDrawable(R.drawable.star1));
            } else if (stars <= 0) {
                mStars.get(i).setImageDrawable(mActivity.getDrawable(R.drawable.star2));
            } else {
                mStars.get(i).setImageDrawable(mActivity.getDrawable(R.drawable.star3));
            }
            stars -= 1;
        }


        return g;
    }
}
