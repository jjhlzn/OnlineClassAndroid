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
    public int getItemViewType() {
        return BaseListViewCell.ZHUANLAN_CELL;
    }

    private void setView(ViewHolder viewHolder) {
        viewHolder.nameLabel.setText(mZhuanLan.getName());
        viewHolder.latestLabel.setText(mZhuanLan.getLatest());
        viewHolder.updateTimeLabel.setText(mZhuanLan.getUpdateTime());
        viewHolder.descriptionLabel.setText(mZhuanLan.getDescription());
        viewHolder.priceInfoLabel.setText(mZhuanLan.getPriceInfo());
        Glide
                .with(mActivity)
                .load(mZhuanLan.getImageUrl())
                .placeholder(R.drawable.rect_placeholder)
                .into(viewHolder.imageView);
    }

    @Override
    public ViewGroup getView(View convertView) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mActivity.getLayoutInflater().inflate(R.layout.list_item_mainpage_zhuanlan, null);
            viewHolder = new ViewHolder();
            convertView.setTag(viewHolder);
            viewHolder.nameLabel = convertView.findViewById(R.id.nameLabel);
            viewHolder.latestLabel = (TextView) convertView.findViewById(R.id.latestLabel);
            viewHolder.updateTimeLabel = (TextView) convertView.findViewById(R.id.updateTimeLabel);
            viewHolder.descriptionLabel = (TextView) convertView.findViewById(R.id.descriptionLabel);
            viewHolder.priceInfoLabel = (TextView) convertView.findViewById(R.id.priceInfoLabel);
            viewHolder.imageView = convertView.findViewById(R.id.zhuanLanImage);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        setView(viewHolder);
        return (LinearLayout) convertView.findViewById(R.id.container);
    }

    @Override
    public ViewGroup getView() {
        return this.getView(null);
    }

    class ViewHolder {
        private TextView nameLabel;
        private TextView latestLabel;
        private TextView updateTimeLabel;
        private TextView descriptionLabel;
        private TextView priceInfoLabel;
        private ImageView imageView;
    }
}
