package com.jinjunhang.onlineclass.ui.cell;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.data5tream.emojilib.EmojiParser;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.Comment;
import com.makeramen.roundedimageview.RoundedImageView;

import com.ppi.emoji.EmojiTextView;
import com.ppi.emoji.emojiParser;

/**
 * Created by lzn on 16/6/22.
 */
public class CommentCell extends BaseListViewCell {

    private Comment mComment;


    public CommentCell(Activity activity, Comment comment) {
        super(activity);
        mComment = comment;
    }

    @Override
    public ViewGroup getView() {
        View v = mActivity.getLayoutInflater().inflate(R.layout.list_item_comment, null);

        ((RoundedImageView)v.findViewById(R.id.comment_user_image)).setOval(true);

        ((TextView)v.findViewById(R.id.comment_username)).setText(mComment.getUserId());
        ((TextView)v.findViewById(R.id.comment_date)).setText(mComment.getTime());

        EmojiTextView  contentView = (EmojiTextView)v.findViewById(R.id.comment_content);
        contentView.setText(EmojiParser.parseEmojis(mComment.getContent()));


        return (LinearLayout)v.findViewById(R.id.list_item_albumtype_viewgroup);
    }
}
