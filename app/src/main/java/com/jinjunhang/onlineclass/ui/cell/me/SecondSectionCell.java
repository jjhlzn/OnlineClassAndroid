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
public class SecondSectionCell extends BaseListViewCell {

    private String mJiFen;
    private String mChaiFu;
    private String mTeamPeople;
    private TextView mJifenLabel;
    private TextView mChaiFuLabel;
    private TextView mTeamPeopleLabel;

    public void setJiFen(String jiFen) {
        mJiFen = jiFen;
    }

    public void setChaiFu(String chaiFu) {
        mChaiFu = chaiFu;
    }

    public void setTeamPeople(String teamPeople) {
        mTeamPeople = teamPeople;
    }

    public SecondSectionCell(Activity activity) {
        super(activity);
    }

    @Override
    public ViewGroup getView() {
        View v = mActivity.getLayoutInflater().inflate(R.layout.list_item_me_second_section, null);

        mJifenLabel = (TextView) v.findViewById(R.id.jifen_label);
        mChaiFuLabel = (TextView) v.findViewById(R.id.caifu_label);
        mTeamPeopleLabel = (TextView) v.findViewById(R.id.tuandui_label);


        mJifenLabel.setText(mJiFen);
        mChaiFuLabel.setText(mChaiFu);
        mTeamPeopleLabel.setText(mTeamPeople);

        Button tixianBtn = (Button)v.findViewById(R.id.tixianBtn);
        tixianBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mActivity, WebBrowserActivity.class)
                        .putExtra(WebBrowserActivity.EXTRA_TITLE, "提现")
                        .putExtra(WebBrowserActivity.EXTRA_URL,  ServiceLinkManager.MyExchangeUrl());
                mActivity.startActivity(i);
            }
        });


        updateView();

        return (LinearLayout)v.findViewById(R.id.root_container);
    }

    public void updateView() {
        mJifenLabel.setText(mJiFen);
        mChaiFuLabel.setText(mChaiFu);
        mTeamPeopleLabel.setText(mTeamPeople);
    }


}
