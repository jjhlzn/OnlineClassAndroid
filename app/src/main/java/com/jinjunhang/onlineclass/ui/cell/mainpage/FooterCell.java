package com.jinjunhang.onlineclass.ui.cell.mainpage;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.ui.cell.BaseListViewCell;

/**
 * Created by jinjunhang on 17/1/6.
 */

public class FooterCell extends BaseListViewCell {

    public FooterCell(Activity activity) {super(activity);}

    @Override
    public ViewGroup getView() {
        View view = mActivity.getLayoutInflater().inflate(R.layout.list_item_mainpage_footer, null);

        return (LinearLayout)view.findViewById(R.id.list_item_viewgroup);
    }

}
