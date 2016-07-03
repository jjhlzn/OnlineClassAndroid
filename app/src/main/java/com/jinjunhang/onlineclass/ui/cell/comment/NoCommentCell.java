package com.jinjunhang.onlineclass.ui.cell.comment;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.ui.cell.BaseListViewCell;

/**
 * Created by jjh on 2016-7-3.
 */
public class NoCommentCell extends BaseListViewCell {

    public NoCommentCell(Activity activity) {
        super(activity);
    }

    @Override
    public ViewGroup getView() {
        View v = mActivity.getLayoutInflater().inflate(R.layout.list_item_comment_not_any, null);

        return (LinearLayout)v.findViewById(R.id.list_item_albumtype_viewgroup);
    }
}
