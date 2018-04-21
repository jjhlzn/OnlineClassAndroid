package com.jinjunhang.onlineclass.ui.cell;

import android.app.Activity;

import com.jinjunhang.onlineclass.model.Comment;
import com.jinjunhang.onlineclass.model.LiveSong;
import com.jinjunhang.onlineclass.ui.cell.comment.CommentCell;
import com.jinjunhang.onlineclass.ui.cell.comment.CommentHeaderCell;
import com.jinjunhang.onlineclass.ui.cell.comment.NoCommentCell;
import com.jinjunhang.onlineclass.ui.cell.player.PlayerCell;
import com.jinjunhang.onlineclass.ui.fragment.album.LiveSongFragment;
import com.jinjunhang.framework.lib.LogHelper;

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
        mCommentHeaderCell = new LiveCommentHeaderCell(activity);
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

        if (mCommentCells.size() == 0) {
            mCells.add(mNoCommentCell);
        }

        for(int i = 0; i < LiveSongFragment.MAX_COMMENT_COUNT && i < mCommentCells.size(); i++) {
            CommentCell cell = mCommentCells.get(i);
            mCells.add(cell);
        }


    }

    public void addComments(List<Comment> comments) {
        if (comments.size() == 0) {
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
        notifyDataSetChanged();
    }

    public void addComment(Comment comment) {
        if (comment == null) {
            return;
        }

        List<CommentCell> newCommentCells = new ArrayList<>();
        newCommentCells.add(new CommentCell(mActivity, comment));

        for (int i = 0; i < LiveSongFragment.MAX_COMMENT_COUNT - 1 && i < mCommentCells.size(); i++) {
            newCommentCells.add(mCommentCells.get(i));
        }

        mCommentCells = newCommentCells;
        recreateCells();
        notifyDataSetChanged();
    }

    public void notifyAdvChanged() {
        mSong.setSongAdvInfoChanged(false);
        recreateCells();
        notifyDataSetChanged();
    }

}
