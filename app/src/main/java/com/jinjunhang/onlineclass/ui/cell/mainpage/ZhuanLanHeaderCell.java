package com.jinjunhang.onlineclass.ui.cell.mainpage;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.content.Intent;

import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.ui.activity.WebBrowserActivity;
import com.jinjunhang.onlineclass.ui.activity.ZhuanLanListActivity;
import com.jinjunhang.onlineclass.ui.cell.BaseListViewCell;

public class ZhuanLanHeaderCell extends BaseListViewCell {

    public ZhuanLanHeaderCell(Activity activity) {
        super(activity);
    }

    @Override
    public ViewGroup getView() {

        View view = mActivity.getLayoutInflater().inflate(R.layout.list_item_mainpage_zhuanlan_header, null);
        Button btn = (Button)view.findViewById(R.id.viewAllBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mActivity, ZhuanLanListActivity.class);
                mActivity.startActivity(i);
            }
        });
        return (RelativeLayout)view.findViewById(R.id.container);
    }
}
