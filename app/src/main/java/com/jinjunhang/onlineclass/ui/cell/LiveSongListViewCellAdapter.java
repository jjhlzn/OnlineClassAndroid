package com.jinjunhang.onlineclass.ui.cell;

import android.app.Activity;

import com.jinjunhang.onlineclass.model.Comment;
import com.jinjunhang.onlineclass.model.LiveSong;
import com.jinjunhang.onlineclass.model.Song;
import com.jinjunhang.onlineclass.ui.cell.comment.CommentCell;
import com.jinjunhang.onlineclass.ui.cell.comment.CommentHeaderCell;
import com.jinjunhang.onlineclass.ui.cell.comment.NoCommentCell;
import com.jinjunhang.onlineclass.ui.cell.player.PlayerCell;
import com.jinjunhang.onlineclass.ui.fragment.album.LiveSongFragment;
import com.jinjunhang.player.utils.LogHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzn on 16/7/23.
 */
public class LiveSongListViewCellAdapter extends SongListViewCellAdapter {
    private static final String TAG = LogHelper.makeLogTag(LiveSongListViewCellAdapter.class);

    private List<CommentCell> mCommentCells;
    private LiveSong mSong;
    private PlayerCell mPlayerCell;
    private SongAdvCell mSongAdvCell;
    private WideSectionSeparatorCell mSeparatorCell;
    private CommentHeaderCell mCommentHeaderCell;
    private NoCommentCell mNoCommentCell;

    public LiveSongListViewCellAdapter(Activity activity, LiveSong song, PlayerCell playerCell) {
        super(activity);
        mActivity = activity;
        mCommentCells = new ArrayList<CommentCell>();
        mSong = song;
        mPlayerCell = playerCell;
        mSeparatorCell = new WideSectionSeparatorCell(activity);
        mCommentHeaderCell = new CommentHeaderCell(activity);
        mNoCommentCell = new NoCommentCell(activity);
        recreateCells();
    }

    public void loadNewSong(LiveSong song) {
        mSong = song;
        mCommentCells = new ArrayList<CommentCell>();
        recreateCells();
    }

    private void recreateCells() {
        mCells = new ArrayList<>();
        mCells.add(mPlayerCell);
        mCells.add(mSeparatorCell);

        LogHelper.d(TAG, "recreateCells: hasAdvImage = " + mSong.hasAdvImage());
        if (mSong.hasAdvImage()) {
            if (mSongAdvCell == null) {
                mSongAdvCell = new SongAdvCell(mActivity, mSong);
            }
            mCells.add(mSongAdvCell);
            mCells.add(mSeparatorCell);
        }

        mCells.add(mCommentHeaderCell);
        if (mCommentCells.size() == 0) {
            mCells.add(mNoCommentCell);
        }

        for(int i = 0; i < LiveSongFragment.MAX_COMMENT_COUNT && i < mCommentCells.size(); i++) {
            CommentCell cell = mCommentCells.get(i);
            mCells.add(cell);
        }
    }

    public void addComments(List<Comment> comments) {
        if (comments.size() == 0 && !mSong.isSongAdvInfoChanged()) {
            LogHelper.d(TAG, "addComments exit");
            return;
        }

        List<CommentCell> newCommentCells = new ArrayList<>();
        for(int i = 0; i < LiveSongFragment.MAX_COMMENT_COUNT && i < comments.size(); i++) {
            CommentCell cell = new CommentCell(mActivity, comments.get(i));
            newCommentCells.add(cell);
        }

        int moreCount = LiveSongFragment.MAX_COMMENT_COUNT - comments.size();

        if (moreCount > 0) {
            for (int i = 0; i < moreCount && i < mCommentCells.size(); i++) {
                newCommentCells.add(mCommentCells.get(i));
            }
        }

        mCommentCells = newCommentCells;
        recreateCells();
        mSong.setSongAdvInfoChanged(false);

        notifyDataSetChanged();
    }

}
