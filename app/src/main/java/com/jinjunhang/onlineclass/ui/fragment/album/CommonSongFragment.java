package com.jinjunhang.onlineclass.ui.fragment.album;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.framework.service.ServerResponse;
import com.jinjunhang.onlineclass.model.Comment;
import com.jinjunhang.onlineclass.service.GetSongCommentsRequest;
import com.jinjunhang.onlineclass.service.GetSongCommentsResponse;
import com.jinjunhang.onlineclass.ui.cell.WideSectionSeparatorCell;
import com.jinjunhang.onlineclass.ui.cell.comment.CommentCell;
import com.jinjunhang.onlineclass.ui.cell.ListViewCell;
import com.jinjunhang.onlineclass.ui.cell.comment.CommentHeaderCell;
import com.jinjunhang.onlineclass.ui.cell.comment.MoreCommentLinkCell;
import com.jinjunhang.onlineclass.ui.cell.comment.NoCommentCell;
import com.jinjunhang.onlineclass.ui.cell.player.PlayerCell;
import com.jinjunhang.onlineclass.ui.cell.SectionSeparatorCell;
import com.jinjunhang.player.MusicPlayer;
import com.jinjunhang.player.utils.LogHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzn on 16/6/13.
 */
public class CommonSongFragment extends BaseSongFragment implements MusicPlayer.MusicPlayerControlListener {
    private final static String TAG = LogHelper.makeLogTag(CommonSongFragment.class);
    private CommentHeaderCell mCommentHeaderCell;
    @Override
    protected PlayerCell createPlayerCell() {
        return new PlayerCell(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        mCommentHeaderCell = new CommentHeaderCell(getActivity());
        mCommentHeaderCell.setTotalCount(0);
        new GetSongCommentsTask().execute();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMusicPlayer.addMusicPlayerControlListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mMusicPlayer.removeMusicPlayerControlListener();
    }

    private class GetSongCommentsTask extends AsyncTask<Void, Void, GetSongCommentsResponse> {
        @Override
        protected GetSongCommentsResponse doInBackground(Void... params) {
            GetSongCommentsRequest req = new GetSongCommentsRequest();
            req.setSong(mMusicPlayer.getCurrentPlaySong());
            return new BasicService().sendRequest(req);
        }

        @Override
        protected void onPostExecute(GetSongCommentsResponse resp) {
            if (resp.getStatus() != ServerResponse.SUCCESS) {
                return;
            }
            List<Comment> comments = getTop5(resp.getResultSet());

            reCreateListViewCells(comments, resp.getTotalNumber());
        }
    }

    private List<Comment> getTop5(List<Comment> comments) {
        if (comments.size() <= 5) {
            return comments;
        }
        List<Comment> top5 = new ArrayList<>();
        for (int i = 0; i < 5; i ++) {
            top5.add(comments.get(i));
        }
        return top5;
    }

    private void reCreateListViewCells(List<Comment> comments, int totalCommentCount) {
        List<ListViewCell> cells = new ArrayList<>();
        cells.add(mPlayerCell);
        cells.add(new WideSectionSeparatorCell(getActivity()));
        cells.add(mCommentHeaderCell);
        mCommentHeaderCell.setTotalCount(totalCommentCount);

        if (totalCommentCount == 0) {
            cells.add(new NoCommentCell(getActivity()));
        } else {
            for (Comment comment : comments) {
                cells.add(new CommentCell(getActivity(), comment));
            }
        }

        if (totalCommentCount > 5) {
            MoreCommentLinkCell moreCell = new MoreCommentLinkCell(getActivity(), totalCommentCount);
            cells.add(moreCell);
        }

        this.mAdapter.setCells(cells);
        LogHelper.d(TAG, "reCreateListViewCells called");
    }

    @Override
    public void onClickNext() {
        new GetSongCommentsTask().execute();
    }

    @Override
    public void onClickPrev() {
        new GetSongCommentsTask().execute();
    }
}


