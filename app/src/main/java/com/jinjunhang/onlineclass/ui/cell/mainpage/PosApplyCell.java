package com.jinjunhang.onlineclass.ui.cell.mainpage;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.Pos;
import com.jinjunhang.onlineclass.ui.activity.WebBrowserActivity;
import com.jinjunhang.onlineclass.ui.cell.BaseListViewCell;

public class PosApplyCell extends BaseListViewCell {

    private Pos mPos;

    public PosApplyCell(Activity activity, Pos pos) {
        super(activity);
        this.mPos = pos;
    }

    @Override
    public int getItemViewType() {
        return BaseListViewCell.POS_CELL;
    }

    public void setPos(Pos pos) {
        mPos = pos;
    }



    @Override
    public ViewGroup getView(View convertView) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mActivity.getLayoutInflater().inflate(R.layout.list_item_mainpage_pos, null).findViewById(R.id.container);
            convertView.setLayoutParams(new RelativeLayout.LayoutParams(Utils.getScreenWidth(mActivity), (int) ((float) Utils.getScreenWidth(mActivity) / 375.0 * 90)));
            viewHolder = new ViewHolder();
            convertView.setTag(viewHolder);

            viewHolder.imageView = convertView.findViewById(R.id.posImage);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        setView(viewHolder);
        return (ViewGroup)convertView;
    }

    private void setView(ViewHolder viewHolder) {
        if (mPos == null || mPos.getImageUrl().isEmpty()) {
            return;
        }

        Glide
                .with(mActivity)
                .load(mPos.getImageUrl())
                .placeholder(R.drawable.pos_placeholder)
                .into(viewHolder.imageView);

        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mActivity, WebBrowserActivity.class)
                        .putExtra(WebBrowserActivity.EXTRA_TITLE, mPos.getTitle())
                        .putExtra(WebBrowserActivity.EXTRA_URL, mPos.getClickUrl());
                mActivity.startActivity(i);
            }
        });

    }

    class ViewHolder {
        ImageView imageView;
    }
}
