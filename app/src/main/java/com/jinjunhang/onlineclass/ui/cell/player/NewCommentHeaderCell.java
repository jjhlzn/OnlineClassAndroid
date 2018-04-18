package com.jinjunhang.onlineclass.ui.cell.player;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.LiveSong;
import com.jinjunhang.onlineclass.ui.activity.WebBrowserActivity;
import com.jinjunhang.onlineclass.ui.cell.BaseListViewCell;

public class NewCommentHeaderCell extends BaseListViewCell {

    public NewCommentHeaderCell(Activity activity) {
        super(activity);
    }

    @Override
    public ViewGroup getView() {
        View v = mActivity.getLayoutInflater().inflate(R.layout.new_player_view_comment_header, null);


        return (RelativeLayout)v.findViewById(R.id.container);
    }
}
