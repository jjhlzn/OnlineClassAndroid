package com.jinjunhang.onlineclass.ui.cell.mainpage;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.ServiceLinkManager;
import com.jinjunhang.onlineclass.ui.activity.WebBrowserActivity;
import com.jinjunhang.onlineclass.ui.cell.BaseListViewCell;

public class FinanceToutiaoHeaderCell extends BaseListViewCell {


    public FinanceToutiaoHeaderCell(Activity activity) {
        super(activity);
    }

    @Override
    public ViewGroup getView(View convertView) {
        return this.getView();
    }

    @Override
    public ViewGroup getView() {

        View view = mActivity.getLayoutInflater().inflate(R.layout.list_item_mainpage_jr_tiaotiao_header, null);

        Button viewAllBtn = (Button)view.findViewById(R.id.viewAllBtn);
        viewAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mActivity, WebBrowserActivity.class)
                        .putExtra(WebBrowserActivity.EXTRA_TITLE, "金融宝典")
                        .putExtra(WebBrowserActivity.EXTRA_URL, ServiceLinkManager.JunHuoKuUrl());
                mActivity.startActivity(i);
            }
        });

        return (RelativeLayout)view.findViewById(R.id.container);
    }

}
