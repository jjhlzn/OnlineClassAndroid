package com.jinjunhang.onlineclass.ui.cell.player;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.data5tream.emojilib.EmojiParser;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.Comment;
import com.jinjunhang.onlineclass.ui.cell.BaseListViewCell;
import com.makeramen.roundedimageview.RoundedImageView;
import com.ppi.emoji.EmojiTextView;

public class NewCommentCell extends BaseListViewCell {

    private Comment mComment;
    public NewCommentCell(Activity activity, Comment comment) {
        super(activity);
        this.mComment = comment;

    }

    @Override
    public ViewGroup getView() {
        View v = mActivity.getLayoutInflater().inflate(R.layout.new_player_view_comment, null);

        RoundedImageView image = ((RoundedImageView)v.findViewById(R.id.comment_user_image));
        image.setOval(true);
        image.setBorderWidth(0.5f);
        image.setBorderColor(mActivity.getResources().getColor(R.color.ccl_grey600));

        if (mComment.isManager()) {
            image.setImageResource(R.drawable.user2_0);
            ((TextView)v.findViewById(R.id.comment_username)).setTextColor(mActivity.getResources().getColor(R.color.colorPrimary));
        }


        ((TextView)v.findViewById(R.id.comment_username)).setText(mComment.getNickName());
        ((TextView)v.findViewById(R.id.comment_date)).setText(mComment.getTime());

        EmojiTextView contentView = (EmojiTextView)v.findViewById(R.id.comment_content);
        contentView.setText(EmojiParser.parseEmojis(mComment.getContent()));

        return (LinearLayout)v.findViewById(R.id.container);
    }
}
