package com.jinjunhang.onlineclass.ui.cell.me;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.ui.cell.BaseListViewCell;
import com.makeramen.roundedimageview.RoundedImageView;


/**
 * Created by jjh on 2016-6-29.
 */
public class CommonCell extends BaseListViewCell {
    private LineRecord mRecord;
    public CommonCell(Activity activity, LineRecord record) {
        super(activity);
        mRecord = record;
    }

    @Override
    public ViewGroup getView() {
        View v = mActivity.getLayoutInflater().inflate(R.layout.list_item_me_common_section, null);
        TextView title = (TextView) v.findViewById(R.id.title_label);
        title.setText(mRecord.getmTitle());

        TextView otherInfo = (TextView) v.findViewById(R.id.otherInfo_label);
        otherInfo.setText(mRecord.getmOtherInfo());

        ImageView image = (ImageView) v.findViewById(R.id.icon_image);
        image.setImageResource(mRecord.getmImage()
        );
        return (LinearLayout)v.findViewById(R.id.root_container);
    }

    @Override
    public void onClick() {
        mRecord.getmListener().onClick(this);
    }

    public LineRecord getmRecord() {
        return mRecord;
    }
}
