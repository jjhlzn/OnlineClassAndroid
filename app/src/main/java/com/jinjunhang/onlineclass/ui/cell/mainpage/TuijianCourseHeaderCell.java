package com.jinjunhang.onlineclass.ui.cell.mainpage;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.jinjunhang.framework.lib.LoadingAnimation;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.service.GetTouTiaoResponse;
import com.jinjunhang.onlineclass.ui.activity.user.QRImageActivity;
import com.jinjunhang.onlineclass.ui.cell.BaseListViewCell;

import java.util.ArrayList;

public class TuijianCourseHeaderCell extends BaseListViewCell {


    public TuijianCourseHeaderCell(Activity activity) {
        super(activity);
    }

    @Override
    public ViewGroup getView() {

        View view = mActivity.getLayoutInflater().inflate(R.layout.list_item_mainpage_tuijian_header, null);

        return (RelativeLayout)view.findViewById(R.id.container);
    }

}
