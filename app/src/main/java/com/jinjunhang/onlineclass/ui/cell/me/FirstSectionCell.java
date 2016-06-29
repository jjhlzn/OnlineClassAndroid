package com.jinjunhang.onlineclass.ui.cell.me;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.data5tream.emojilib.EmojiParser;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.Comment;
import com.jinjunhang.onlineclass.ui.cell.BaseListViewCell;
import com.jinjunhang.onlineclass.ui.cell.CommentCell;
import com.makeramen.roundedimageview.RoundedImageView;
import com.ppi.emoji.EmojiTextView;

/**
 * Created by jjh on 2016-6-29.
 */
public class FirstSectionCell extends BaseListViewCell {

    public FirstSectionCell(Activity activity) {
        super(activity);
    }

    @Override
    public ViewGroup getView() {
        View v = mActivity.getLayoutInflater().inflate(R.layout.list_item_me_first_section, null);

        return (LinearLayout)v.findViewById(R.id.root_container);
    }
}
