package com.jinjunhang.onlineclass.ui.cell.me;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.ui.cell.BaseListViewCell;


/**
 * Created by jjh on 2016-6-29.
 */
public class CommonCell extends BaseListViewCell {
    private LineRecord mRecord;
    private TextView mOtherInfoLable;
    public CommonCell(Activity activity, LineRecord record) {
        super(activity);
        mRecord = record;
    }

    @Override
    public ViewGroup getView() {
        View v = mActivity.getLayoutInflater().inflate(R.layout.list_item_me_common_section, null);
        TextView title = (TextView) v.findViewById(R.id.title_label);
        title.setText(mRecord.getTitle());

        mOtherInfoLable = (TextView) v.findViewById(R.id.otherInfo_label);
        updateView();

        ImageView image = (ImageView) v.findViewById(R.id.icon_image);
        image.setImageResource(mRecord.getImage()
        );

        RelativeLayout layout = (RelativeLayout)v.findViewById(R.id.root_container);
        //layout.setMinimumHeight(40);
        return layout;
    }

    @Override
    public void onClick() {
        mRecord.getListener().onClick(this);
    }

    public LineRecord getRecord() {
        return mRecord;
    }

    public void updateView() {
        mOtherInfoLable.setText(mRecord.getOtherInfo());
    }
}
