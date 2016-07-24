package com.jinjunhang.onlineclass.ui.cell.comment;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.data5tream.emojilib.EmojiParser;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.Comment;
import com.jinjunhang.onlineclass.service.ServiceConfiguration;
import com.jinjunhang.onlineclass.ui.cell.BaseListViewCell;
import com.jinjunhang.player.utils.LogHelper;
import com.makeramen.roundedimageview.RoundedImageView;

import com.ppi.emoji.EmojiTextView;
import com.ppi.emoji.emojiParser;

/**
 * Created by lzn on 16/6/22.
 */
public class CommentCell extends BaseListViewCell {
    private static final String TAG = LogHelper.makeLogTag(CommentCell.class);

    private Comment mComment;


    public CommentCell(Activity activity, Comment comment) {
        super(activity);
        mComment = comment;
    }

    @Override
    public ViewGroup getView() {
        View v = mActivity.getLayoutInflater().inflate(R.layout.list_item_comment, null);

        RoundedImageView image = ((RoundedImageView)v.findViewById(R.id.comment_user_image));
        image.setOval(true);

        //Glide.with(mActivity).load(ServiceConfiguration.GetUserProfileImage(mComment.getUserId())).into(image);
        //Log.d(TAG, "url = " + ServiceConfiguration.GetUserProfileImage(mComment.getUserId()));
        Glide
                .with(mActivity)
                .load(ServiceConfiguration.GetUserProfileImage(mComment.getUserId()))
                .centerCrop()
                .placeholder(R.drawable.smallusericon)
                .crossFade()
                .into(image);


        ((TextView)v.findViewById(R.id.comment_username)).setText(mComment.getNickName());
        ((TextView)v.findViewById(R.id.comment_date)).setText(mComment.getTime());

        EmojiTextView  contentView = (EmojiTextView)v.findViewById(R.id.comment_content);
        contentView.setText(EmojiParser.parseEmojis(mComment.getContent()));

        return (LinearLayout)v.findViewById(R.id.list_item_albumtype_viewgroup);
    }
}
