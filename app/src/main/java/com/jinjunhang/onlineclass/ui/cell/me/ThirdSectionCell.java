package com.jinjunhang.onlineclass.ui.cell.me;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.ServiceLinkManager;
import com.jinjunhang.onlineclass.ui.activity.WebBrowserActivity;
import com.jinjunhang.onlineclass.ui.cell.BaseListViewCell;

/**
 * Created by jjh on 2016-6-29.
 */
public class ThirdSectionCell extends BaseListViewCell {

    private String mVipEndDate = "";
    private String mAgentLevel = "";


    public void setVipEndDate(String vipEndDate) {
        mVipEndDate = vipEndDate;
    }

    public void setAgentLevel(String agentLevel) {
        mAgentLevel = agentLevel;
    }

    public ThirdSectionCell(Activity activity) {
        super(activity);
    }

    @Override
    public ViewGroup getView() {
        View v = mActivity.getLayoutInflater().inflate(R.layout.list_item_me_third_section, null);
        TextView vipEndDateTV = (TextView)v.findViewById(R.id.vipEndDate_label);
        TextView agentLevelTV = (TextView)v.findViewById(R.id.agentLevel_label);
        Button buyVipBtn = (Button)v.findViewById(R.id.buyVipBtn);
        Button upgradAgentBtn = (Button)v.findViewById(R.id.upgradeAgentBtn);
        if ("".equals(mVipEndDate)) {
            vipEndDateTV.setText("无");
            buyVipBtn.setText("购买");
        } else {
            vipEndDateTV.setText(mVipEndDate);
            buyVipBtn.setText("续费");
        }
        buyVipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, WebBrowserActivity.class)
                        .putExtra(WebBrowserActivity.EXTRA_TITLE, "VIP购买")
                        .putExtra(WebBrowserActivity.EXTRA_URL, ServiceLinkManager.ShenqingUrl());
                mActivity.startActivity(intent);
            }
        });
        agentLevelTV.setText(mAgentLevel);
        upgradAgentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, WebBrowserActivity.class)
                        .putExtra(WebBrowserActivity.EXTRA_TITLE, "代理升级")
                        .putExtra(WebBrowserActivity.EXTRA_URL, ServiceLinkManager.ShenqingUrl());
                mActivity.startActivity(intent);
            }
        });
        return (LinearLayout)v.findViewById(R.id.root_container);
    }



}
