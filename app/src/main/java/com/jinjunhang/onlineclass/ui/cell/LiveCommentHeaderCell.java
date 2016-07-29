package com.jinjunhang.onlineclass.ui.cell;

import android.app.Activity;

import com.jinjunhang.onlineclass.ui.cell.comment.CommentHeaderCell;

/**
 * Created by lzn on 16/7/28.
 */
public class LiveCommentHeaderCell extends CommentHeaderCell {

    public LiveCommentHeaderCell(Activity activity) {
        super(activity);
    }

    @Override
    public void update() {
        mHeaderTextView.setText(String.format("在线聊天"));
    }
}
