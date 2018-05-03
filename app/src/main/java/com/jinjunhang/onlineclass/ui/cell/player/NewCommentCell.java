package com.jinjunhang.onlineclass.ui.cell.player;

import android.app.Activity;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.github.data5tream.emojilib.EmojiParser;
import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.Comment;
import com.jinjunhang.onlineclass.service.ServiceConfiguration;
import com.jinjunhang.onlineclass.ui.cell.BaseListViewCell;
import com.makeramen.roundedimageview.RoundedImageView;
import com.ppi.emoji.EmojiTextView;

public class NewCommentCell extends BaseListViewCell {

    private final static String TAG = LogHelper.makeLogTag(NewCommentCell.class);


    private Comment mComment;
    public NewCommentCell(Activity activity, Comment comment) {
        super(activity);
        this.mComment = comment;
    }

    @Override
    public ViewGroup getView() {
        View v = mActivity.getLayoutInflater().inflate(R.layout.new_player_view_comment, null);
        LinearLayout container = (LinearLayout)v.findViewById(R.id.container);

        final RoundedImageView image = ((RoundedImageView)v.findViewById(R.id.comment_user_image));
        image.setOval(true);
        image.setBorderWidth(0.5f);
        image.setBorderColor(mActivity.getResources().getColor(R.color.ccl_grey600));



        Glide.with(mActivity)
                .load(ServiceConfiguration.GetUserProfileImage(mComment.getUserId()))
                .asBitmap()
                .centerCrop()
                .placeholder(R.drawable.comment_user)
                .into(new BitmapImageViewTarget(image) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(mActivity.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        image.setImageDrawable(circularBitmapDrawable);
                    }
                });
        /*
        if (mComment.isManager()) {
            image.setImageResource(R.drawable.user2_0);
            ((TextView)v.findViewById(R.id.comment_username)).setTextColor(mActivity.getResources().getColor(R.color.colorPrimary));
        } */

        String nickName = mComment.getNickName();
        if ("".equals(nickName)) {
            nickName = "匿名";
        }
        ((TextView)v.findViewById(R.id.comment_username)).setText(nickName);
        ((TextView)v.findViewById(R.id.comment_date)).setText(mComment.getTime());

        EmojiTextView contentView = (EmojiTextView)v.findViewById(R.id.comment_content);
        contentView.setText(EmojiParser.parseEmojis(mComment.getContent()));

        return container;
    }
}
