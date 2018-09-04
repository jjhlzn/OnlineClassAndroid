package com.jinjunhang.onlineclass.ui.cell.mainpage;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.ui.cell.BaseListViewCell;

public class FinanceToutiaoHeaderCell extends BaseListViewCell {


    public FinanceToutiaoHeaderCell(Activity activity) {
        super(activity);
    }

    @Override
    public ViewGroup getView() {

        View view = mActivity.getLayoutInflater().inflate(R.layout.list_item_mainpage_jr_tiaotiao_header, null);

        return (RelativeLayout)view.findViewById(R.id.container);
    }

}
