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
    public int getItemViewType() {
        return BaseListViewCell.MAINPAGE_COURSE_CELL;
    }

    @Override
    public ViewGroup getView(View convertView) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mActivity.getLayoutInflater().inflate(R.layout.list_item_mainpage_course, null);
            viewHolder = new ViewHolder();
            convertView.setTag(viewHolder);
            viewHolder.imageView = convertView.findViewById(R.id.course_image);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        setCourseView(viewHolder);
        return convertView.findViewById(R.id.list_item_course);
    }

    @Override
    public ViewGroup getView() {
        return this.getView(null);
    }

    protected void setCourseView(ViewHolder viewHodler) {


        Glide
                .with(mActivity)
                .load(mCourse.getImage())
                .placeholder(R.drawable.rect_placeholder)
                .into(viewHodler.imageView);


    }

    class ViewHolder {
        ImageView imageView;
    }
}
