package com.jinjunhang.onlineclass.ui.cell.mainpage;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.Pos;
import com.jinjunhang.onlineclass.ui.activity.WebBrowserActivity;
import com.jinjunhang.onlineclass.ui.activity.ZhuanLanListActivity;
import com.jinjunhang.onlineclass.ui.cell.BaseListViewCell;
import com.jinjunhang.onlineclass.ui.fragment.ZhuanLanListFragment;

public class PosApplyCell extends BaseListViewCell {

    private Pos mPos;

    public PosApplyCell(Activity activity, Pos pos) {
        super(activity);
        this.mPos = pos;
    }

    public void setPos(Pos pos) {
        mPos = pos;
    }

    @Override
    public ViewGroup getView() {

        View view = mActivity.getLayoutInflater().inflate(R.layout.list_item_mainpage_pos, null);

        if (mPos == null || mPos.getImageUrl().isEmpty()) {
            return (RelativeLayout)view.findViewById(R.id.container);
        }

        ImageView image = (ImageView)view.findViewById(R.id.posImage);
        Glide
                .with(mActivity)
                .load(mPos.getImageUrl())
                .placeholder(R.drawable.pos_placeholder)
                .into(image);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mActivity, WebBrowserActivity.class)
                        .putExtra(WebBrowserActivity.EXTRA_TITLE, mPos.getTitle())
                        .putExtra(WebBrowserActivity.EXTRA_URL, mPos.getClickUrl());
                mActivity.startActivity(i);
            }
        });
        return (RelativeLayout)view.findViewById(R.id.container);
    }
}
