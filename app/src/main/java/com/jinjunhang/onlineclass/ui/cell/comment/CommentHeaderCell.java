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
public class CommentHeaderCell  extends BaseListViewCell {

    private int mTotalCount;
    private TextView mHeaderTextView;

    public int getTotalCount() {
        return mTotalCount;
    }

    public void setTotalCount(int totalCount) {
        mTotalCount = totalCount;
    }

    public CommentHeaderCell(Activity activity) {
        super(activity);
    }

    @Override
    public ViewGroup getView() {
        View v = mActivity.getLayoutInflater().inflate(R.layout.list_item_comment_header, null);

        mHeaderTextView = (TextView)v.findViewById(R.id.comment_header_label);
        update();
        return (LinearLayout)v.findViewById(R.id.list_item_albumtype_viewgroup);
    }

    public void update() {
        mHeaderTextView.setText(String.format("听众点评   (%s)", mTotalCount+""));
    }
}
