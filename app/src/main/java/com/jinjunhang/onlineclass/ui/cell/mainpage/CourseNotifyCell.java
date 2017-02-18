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

    public CourseNotifyCell(Activity activity) {super(activity);}

    @Override
    public ViewGroup getView() {
        View v = mActivity.getLayoutInflater().inflate(R.layout.list_item_mainpage_course_notify, null);

        MarqueeView advTextView = (MarqueeView)v.findViewById(R.id.course_notifyt_text);
        String advContent = "434324234";
        advTextView.startWithText(advContent);

        if ("".equals(advContent)) {
            //advTextView.setBackgroundColor(0x000000);
            advTextView.setVisibility(View.INVISIBLE);
        } else {
            //advTextView.setBackgroundColor(0xefeef3);
            advTextView.setVisibility(View.VISIBLE);
        }

        return (LinearLayout)v.findViewById(R.id.list_item_viewgroup);
    }

}
