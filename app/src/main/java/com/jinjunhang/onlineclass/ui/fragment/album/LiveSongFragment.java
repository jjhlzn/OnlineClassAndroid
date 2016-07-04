package com.jinjunhang.onlineclass.ui.fragment.album;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.exoplayer.ExoPlayer;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.framework.service.ServerResponse;
import com.jinjunhang.onlineclass.db.LoginUserDao;
import com.jinjunhang.onlineclass.model.Comment;
import com.jinjunhang.onlineclass.model.LoginUser;
import com.jinjunhang.onlineclass.model.Song;
import com.jinjunhang.onlineclass.service.GetLiveCommentsRequest;
import com.jinjunhang.onlineclass.service.GetLiveCommentsResponse;
import com.jinjunhang.onlineclass.service.SendCommentRequest;
import com.jinjunhang.onlineclass.service.SendCommentResponse;
import com.jinjunhang.onlineclass.service.SendLiveCommentRequest;
import com.jinjunhang.onlineclass.service.SendLiveCommentResponse;
import com.jinjunhang.onlineclass.ui.cell.ListViewCell;
import com.jinjunhang.onlineclass.ui.cell.WideSectionSeparatorCell;
import com.jinjunhang.onlineclass.ui.cell.comment.CommentCell;
import com.jinjunhang.onlineclass.ui.cell.comment.LiveCommentHeaderCell;
import com.jinjunhang.onlineclass.ui.cell.comment.NoCommentCell;
import com.jinjunhang.onlineclass.ui.cell.player.LivePlayerCell;
import com.jinjunhang.onlineclass.ui.cell.player.PlayerCell;
import com.jinjunhang.onlineclass.ui.lib.CustomApplication;
import com.jinjunhang.player.MusicPlayer;
import com.jinjunhang.player.utils.LogHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by jjh on 2016-7-2.
 */
public class LiveSongFragment extends BaseSongFragment implements MusicPlayer.MusicPlayerControlListener {

    public final static int MAX_COMMENT_COUNT = 200;

    private final static String TAG = LogHelper.makeLogTag(CommonSongFragment.class);
    private LiveCommentHeaderCell mCommentHeaderCell;
    private String mLastCommentId = "-1";
    private int commentCount = 0;

    private boolean isUpdatingChat = false;

    //定时获取评论、回复播放
    private final Handler mHandler = new Handler();
    private ScheduledFuture<?> mScheduleFuture;
    private final ScheduledExecutorService mExecutorService = Executors.newSingleThreadScheduledExecutor();
    private final Runnable mUpdateChatTask = new Runnable() {
        @Override
        public void run() {
            updateChat();
        }
    };
    private static final long CHAT_UPDATE_INTERNAL = 5000;
    private static final long CHAT_UPDATE_INITIAL_INTERVAL = 2000;

    private void updateChat() {
        if (isUpdatingChat)
            return;

        int state = mMusicPlayer.getState();
        LogHelper.d(TAG, "musicPlayer.state = " + state + ", currentIndex = " + mMusicPlayer.getCurrentPlaySongIndex());
        if (state == ExoPlayer.STATE_BUFFERING || state == ExoPlayer.STATE_IDLE ) {
            mMusicPlayer.play(mMusicPlayer.getSongs(),  mMusicPlayer.getCurrentPlaySongIndex());
        }

        new GetLiveSongCommentsTask().execute();
    }

    private void stopChatUpdate() {
        if (mScheduleFuture != null) {
            mScheduleFuture.cancel(false);
        }
    }

    protected void scheduleChatUpdate() {
        stopChatUpdate();
        if (!mExecutorService.isShutdown()) {
            mScheduleFuture = mExecutorService.scheduleAtFixedRate(
                    new Runnable() {
                        @Override
                        public void run() {
                            mHandler.post(mUpdateChatTask);
                        }
                    }, CHAT_UPDATE_INITIAL_INTERVAL,
                    CHAT_UPDATE_INTERNAL, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    protected PlayerCell createPlayerCell() {
        return new LivePlayerCell(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        mCommentHeaderCell = new LiveCommentHeaderCell(getActivity());
        new GetLiveSongCommentsTask().execute();
        scheduleChatUpdate();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopChatUpdate();
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

        List<ListViewCell> oldComments = mAdapter.getCells();
        LogHelper.d(TAG, "oldComment.count = " + oldComments.size());

        if (oldComments.size() > 2) {
            oldComments.remove(0);
            oldComments.remove(0);
            oldComments.remove(0);
            if (commentCount == 0) {
                oldComments.remove(0);
            }
            cells.addAll(oldComments);
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
                SendLiveCommentRequest request = new SendLiveCommentRequest();
                request.setComment(comment);
                request.setSong(song);
                request.setLastId(mLastCommentId);
                closeCommentWindow();
                new SendLiveCommentTask().execute(request);
            }
        };
    }


    /***
     * Get Live Comments Task
     */
    private class GetLiveSongCommentsTask extends AsyncTask<Void, Void, GetLiveCommentsResponse> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isUpdatingChat = true;
        }

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
                isUpdatingChat = false;
                return;
            }
            commentCount += resp.getCommentList().size();
            List<Comment> comments = resp.getCommentList();
            reCreateListViewCells(comments, comments.size());

            if (resp.getCommentList().size() > 0) {
                mLastCommentId = resp.getCommentList().get(0).getId() + "";
            }
            isUpdatingChat = false;
        }
    }

    /***
     * Send Live Comment Task
     */
    private class SendLiveCommentTask extends AsyncTask<SendLiveCommentRequest, Void, SendLiveCommentResponse> {
        private SendLiveCommentRequest mRequest;

        @Override
        protected SendLiveCommentResponse doInBackground(SendLiveCommentRequest... params) {
            mRequest = params[0];
            return new BasicService().sendRequest(mRequest);
        }

        @Override
        protected void onPostExecute(SendLiveCommentResponse resp) {
            super.onPostExecute(resp);
            if (!resp.isSuccess()) {
                Utils.showErrorMessage(getActivity(), resp.getErrorMessage());
                return;
            }
            commentCount += resp.getCommentList().size();
            if (resp.getCommentList().size() > 0) {
                mLastCommentId = resp.getCommentList().get(0).getId() + "";
            }

            List<Comment> newComments =  resp.getCommentList();
            reCreateListViewCells(newComments, newComments.size());

            Toast.makeText(getActivity(), "发送成功", Toast.LENGTH_SHORT).show();
            mCommentEditText.setText("");

            mAdapter.notifyDataSetChanged();
        }
    }

    private  class Get

}
