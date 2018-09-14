package com.jinjunhang.onlineclass.ui.cell.mainpage;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.ZhuanLan;
import com.jinjunhang.onlineclass.ui.cell.BaseListViewCell;

public class ZhuanLanCell extends BaseListViewCell {

    private ZhuanLan mZhuanLan;

    public ZhuanLanCell(Activity activity, ZhuanLan zhuanLan) {
        super(activity);
        mZhuanLan = zhuanLan;
    }

    public ZhuanLan getZhuanLan() {
        return mZhuanLan;
    }

    @Override
    public ViewGroup getView() {

        View v = mActivity.getLayoutInflater().inflate(R.layout.list_item_mainpage_zhuanlan, null);

        TextView nameLabel = (TextView)v.findViewById(R.id.nameLabel);
        TextView latestLabel = (TextView)v.findViewById(R.id.latestLabel);
        TextView updateTimeLabel = (TextView)v.findViewById(R.id.updateTimeLabel);
        TextView descriptionLabel = (TextView)v.findViewById(R.id.descriptionLabel);
        TextView priceInfoLabel = (TextView)v.findViewById(R.id.priceInfoLabel);
        ImageView imageView = (ImageView)v.findViewById(R.id.zhuanLanImage);

        nameLabel.setText(mZhuanLan.getName());
        latestLabel.setText(mZhuanLan.getLatest());
        updateTimeLabel.setText(mZhuanLan.getUpdateTime());
        descriptionLabel.setText(mZhuanLan.getDescription());
        priceInfoLabel.setText(mZhuanLan.getPriceInfo());
        Glide
                .with(mActivity)
                .load(mZhuanLan.getImageUrl())
                .placeholder(R.drawable.rect_placeholder)
                .into(imageView);

        return (LinearLayout)v.findViewById(R.id.container);
    }

}
