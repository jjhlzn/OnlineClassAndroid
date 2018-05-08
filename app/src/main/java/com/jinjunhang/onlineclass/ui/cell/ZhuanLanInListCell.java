package com.jinjunhang.onlineclass.ui.cell;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.ZhuanLan;

public class ZhuanLanInListCell extends BaseListViewCell {

    private ZhuanLan mZhuanLan;

    public ZhuanLanInListCell(Activity activity, ZhuanLan zhuanLan) {
        super(activity);
        mZhuanLan = zhuanLan;
    }

    public ZhuanLan getZhuanLan() {
        return mZhuanLan;
    }

    @Override
    public ViewGroup getView() {

        View v = mActivity.getLayoutInflater().inflate(R.layout.list_item_inlist_zhuanlan, null);

        TextView nameLabel = (TextView)v.findViewById(R.id.nameLabel);
        TextView latestLabel = (TextView)v.findViewById(R.id.latestLabel);
        TextView authorLabel = (TextView)v.findViewById(R.id.authorLabel);
        TextView priceInfoLabel = (TextView)v.findViewById(R.id.priceInfoLabel);
        TextView dingyueLabel = (TextView)v.findViewById(R.id.dingyueLabel);
        ImageView imageView = (ImageView)v.findViewById(R.id.zhuanLanImage);

        nameLabel.setText(mZhuanLan.getName());
        latestLabel.setText(mZhuanLan.getLatest());
        authorLabel.setText(mZhuanLan.getAuthor() + "   "  + mZhuanLan.getAuthorTitle());
        dingyueLabel.setText(mZhuanLan.getDingYue()+"人订阅");
        priceInfoLabel.setText(mZhuanLan.getPriceInfo());
        Glide
                .with(mActivity)
                .load(mZhuanLan.getImageUrl())
                .placeholder(R.drawable.course)
                .into(imageView);

        return (LinearLayout)v.findViewById(R.id.container);
    }

}
