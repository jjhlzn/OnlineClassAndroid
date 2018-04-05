package com.jinjunhang.onlineclass.ui.cell;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;

import com.daimajia.slider.library.SliderLayout;
import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.ui.lib.ExtendFunctionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzn on 16/6/20.
 */
public class CourseCell extends BaseListViewCell {

    private static final String TAG = LogHelper.makeLogTag(CourseCell.class);

    private ViewGroup mView;


    public CourseCell(Activity activity) {
        super(activity);
    }

    public void setView(ViewGroup view) {
        mView = view;
    }


    @Override
    public ViewGroup getView() {
        View view = mActivity.getLayoutInflater().inflate(R.layout.list_item_mainpage_course, null);

        ViewGroup g = (ViewGroup)view.findViewById(R.id.list_item_course);
        return g;
    }
}
