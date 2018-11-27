package com.jinjunhang.onlineclass.ui.cell.mainpage;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.ServiceLinkManager;
import com.jinjunhang.onlineclass.ui.activity.LearnFinanceListActivity;
import com.jinjunhang.onlineclass.ui.activity.WebBrowserActivity;
import com.jinjunhang.onlineclass.ui.cell.BaseListViewCell;

public class LearnFinanceHeaderCell extends BaseListViewCell {


    public LearnFinanceHeaderCell(Activity activity) {
        super(activity);
    }

    @Override
    public ViewGroup getView(View convertView) {
        return this.getView();
    }

    @Override
    public int getItemViewType() {
        return BaseListViewCell.LEARN_FINANCE_HEADER_CELL;
    }

    @Override
    public ViewGroup getView() {

        View view = mActivity.getLayoutInflater().inflate(R.layout.list_item_mainpage_learn_finance_header, null);

        Button viewAllBtn = (Button)view.findViewById(R.id.viewAllBtn);
        viewAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mActivity, LearnFinanceListActivity.class);
                mActivity.startActivity(i);
            }
        });

        return (RelativeLayout)view.findViewById(R.id.container);
    }

}
