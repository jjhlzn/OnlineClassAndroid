package com.jinjunhang.onlineclass.ui.fragment.album;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.framework.service.ServerResponse;
import com.jinjunhang.onlineclass.db.LoginUserDao;
import com.jinjunhang.onlineclass.model.Comment;
import com.jinjunhang.onlineclass.model.LoginUser;
import com.jinjunhang.onlineclass.model.Song;
import com.jinjunhang.onlineclass.service.GetLiveCommentsRequest;
import com.jinjunhang.onlineclass.service.GetLiveCommentsResponse;
import com.jinjunhang.onlineclass.service.GetSongCommentsRequest;
import com.jinjunhang.onlineclass.service.GetSongCommentsResponse;
import com.jinjunhang.onlineclass.service.SendCommentRequest;
import com.jinjunhang.onlineclass.service.SendCommentResponse;
import com.jinjunhang.onlineclass.ui.cell.ListViewCell;
import com.jinjunhang.onlineclass.ui.cell.WideSectionSeparatorCell;
import com.jinjunhang.onlineclass.ui.cell.comment.CommentCell;
import com.jinjunhang.onlineclass.ui.cell.comment.CommentHeaderCell;
import com.jinjunhang.onlineclass.ui.cell.comment.LiveCommentHeaderCell;
import com.jinjunhang.onlineclass.ui.cell.comment.MoreCommentLinkCell;
import com.jinjunhang.onlineclass.ui.cell.comment.NoCommentCell;
import com.jinjunhang.onlineclass.ui.cell.player.PlayerCell;
import com.jinjunhang.onlineclass.ui.lib.CustomApplication;
import com.jinjunhang.player.MusicPlayer;
import com.jinjunhang.player.utils.LogHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jjh on 2016-7-2.
 */
public class LiveSongFragment extends BaseSongFragment implements MusicPlayer.MusicPlayerControlListener {

    private final static String TAG = LogHelper.makeLogTag(CommonSongFragment.class);
    private LiveCommentHeaderCell mCommentHeaderCell;
    private String mLastCommentId = "-1";
    @Override
    protected PlayerCell createPlayerCell() {
        return new PlayerCell(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        mCommentHeaderCell = new LiveCommentHeaderCell(getActivity());
        new GetLiveSongCommentsTask().execute();
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

    private class GetLiveSongCommentsTask extends AsyncTask<Void, Void, GetLiveCommentsResponse> {
        @Override
        protected GetLiveCommentsResponse doInBackground(Void... params) {
            GetLiveCommentsRequest req = new GetLiveCommentsRequest();
            req.setSong(mMusicPlayer.getCurrentPlaySong());
            req.setLastId(mLastCommentId);
            return new BasicService().sendRequest(req);
        }

        @Override
        protected void onPostExecute(GetLiveCommentsResponse resp) {
            if (resp.getStatus() != ServerResponse.SUCCESS) {
                return;
            }
            List<Comment> comments = resp.getCommentList();
            reCreateListViewCells(comments, comments.size());
        }
    }

    private void reCreateListViewCells(List<Comment> comments, int totalCommentCount) {
        List<ListViewCell> cells = new ArrayList<>();
        cells.add(mPlayerCell);
        cells.add(new WideSectionSeparatorCell(getActivity()));
        cells.add(mCommentHeaderCell);

        if (totalCommentCount == 0) {
            cells.add(new NoCommentCell(getActivity()));
        } else {
            for (Comment comment : comments) {
                cells.add(new CommentCell(getActivity(), comment));
            }
        }

        this.mAdapter.setCells(cells);
        LogHelper.d(TAG, "reCreateListViewCells called");
    }

    @Override
    public void onClickNext() {
        new GetLiveSongCommentsTask().execute();
    }

    @Override
    public void onClickPrev() {
        new GetLiveSongCommentsTask().execute();
    }

    @Override
    protected View.OnClickListener createSendOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment =  mCommentEditText.getText().toString();

                if (comment.trim().length() == 0)  {
                    Utils.showErrorMessage(getActivity(), "不能发送空评论");
                    return;
                }

                Song song = mMusicPlayer.getCurrentPlaySong();
                SendCommentRequest request = new SendCommentRequest();
                request.setComment(comment);
                request.setSong(song);
                closeCommentWindow();
                new SendCommentTask().execute(request);
            }
        };
    }


    private class SendCommentTask extends AsyncTask<SendCommentRequest, Void, SendCommentResponse> {
        private SendCommentRequest mRequest;

        @Override
        protected SendCommentResponse doInBackground(SendCommentRequest... params) {
            mRequest = params[0];
            return new BasicService().sendRequest(mRequest);
        }

        @Override
        protected void onPostExecute(SendCommentResponse resp) {
            super.onPostExecute(resp);
            if (!resp.isSuccess()) {
                Utils.showErrorMessage(getActivity(), resp.getErrorMessage());
                return;
            }

            Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT);
            mCommentEditText.setText("");
            Comment comment = new Comment();
            comment.setTime("刚刚");
            comment.setContent(mRequest.getComment());

            LoginUser loginUser = LoginUserDao.getInstance(CustomApplication.get()).get();
            comment.setUserId(loginUser.getUserName());
            comment.setNickName(loginUser.getNickName());
            CommentCell cell = new CommentCell(getActivity(), comment);
            mAdapter.getCells().add(3, cell);
            mAdapter.notifyDataSetChanged();
        }
    }

}
