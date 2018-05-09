package com.jinjunhang.onlineclass.ui.cell.comment;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.ui.activity.album.CommentListActivity;
import com.jinjunhang.onlineclass.ui.cell.BaseListViewCell;
import com.jinjunhang.onlineclass.ui.fragment.album.CommentListFragment;
import com.jinjunhang.player.MusicPlayer;
import com.jinjunhang.framework.lib.LogHelper;

/**
 * Created by jjh on 2016-7-3.
 */
public class MoreCommentLinkCell extends BaseListViewCell {
    private static final String TAG = LogHelper.makeLogTag(MoreCommentLinkCell.class);

    private int mTotalCount;
    private TextView mMoreCommentLink;

    public int getTotalCount() {
        return mTotalCount;
    }

    public void setTotalCount(int totalCount) {
        mTotalCount = totalCount;
    }

    public MoreCommentLinkCell(Activity activity) {
        super(activity);
    }

    public MoreCommentLinkCell(Activity activity, int totalCount) {
        super(activity);
        setTotalCount(totalCount);
    }

    @Override
    public ViewGroup getView() {
        View v = mActivity.getLayoutInflater().inflate(R.layout.list_item_comment_more, null);

        mMoreCommentLink = (TextView)v.findViewById(R.id.comment_more_button);
        mMoreCommentLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //LogHelper.d(TAG, "more button clicked");
                Intent i = new Intent(mActivity, CommentListActivity.class);
                i.putExtra(CommentListFragment.EXTRA_SONG, MusicPlayer.getInstance(mActivity.getApplicationContext()).getCurrentPlaySong());
                mActivity.startActivity(i);
            }
        });
        update();
        return (LinearLayout)v.findViewById(R.id.list_item_albumtype_viewgroup);
    }

    public void update() {
        mMoreCommentLink.setText(String.format("查看全部%s条评论 >", mTotalCount+""));
    }
}
