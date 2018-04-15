package com.jinjunhang.onlineclass.ui.cell.me;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.ui.cell.BaseListViewCell;

/**
 * Created by jjh on 2016-6-29.
 */
public class ThirdSectionCell extends BaseListViewCell {




    public ThirdSectionCell(Activity activity) {
        super(activity);
    }

    @Override
    public ViewGroup getView() {
        View v = mActivity.getLayoutInflater().inflate(R.layout.list_item_me_third_section, null);

        return (LinearLayout)v.findViewById(R.id.root_container);
    }



}
