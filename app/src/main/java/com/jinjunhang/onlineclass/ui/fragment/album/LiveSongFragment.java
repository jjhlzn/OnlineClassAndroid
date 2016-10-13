package com.jinjunhang.onlineclass.ui.fragment.album;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jinjunhang.framework.lib.MyEmojiParse;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.framework.service.ServerResponse;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.db.LoginUserDao;
import com.jinjunhang.onlineclass.model.Comment;
import com.jinjunhang.onlineclass.model.LiveSong;
import com.jinjunhang.onlineclass.model.LoginUser;
import com.jinjunhang.onlineclass.model.ServiceLinkManager;
import com.jinjunhang.onlineclass.model.Song;
import com.jinjunhang.onlineclass.service.GetLiveCommentsRequest;
import com.jinjunhang.onlineclass.service.GetLiveCommentsResponse;
import com.jinjunhang.onlineclass.service.GetLiveListenerRequest;
import com.jinjunhang.onlineclass.service.GetLiveListenerResponse;
import com.jinjunhang.onlineclass.service.GetSongInfoRequest;
import com.jinjunhang.onlineclass.service.GetSongInfoResponse;
import com.jinjunhang.onlineclass.service.JoinRoomRequest;
import com.jinjunhang.onlineclass.service.SendLiveCommentRequest;
import com.jinjunhang.onlineclass.service.SendLiveCommentResponse;
import com.jinjunhang.onlineclass.ui.cell.LiveSongListViewCellAdapter;
import com.jinjunhang.onlineclass.ui.cell.player.LivePlayerCell;
import com.jinjunhang.onlineclass.ui.cell.player.PlayerCell;
import com.jinjunhang.framework.lib.LogHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by jjh on 2016-7-2.
 */
public class LiveSongFragment extends BaseSongFragment  {

    public final static int MAX_COMMENT_COUNT = 10;

    private final static String TAG = LogHelper.makeLogTag(LiveSongFragment.class);
    private String mLastCommentId = "-1";

    private int mUpdateChatCount = 0;

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
    private static final long CHAT_UPDATE_INTERNAL = 10000;
    private static final long CHAT_UPDATE_INITIAL_INTERVAL = 2000;

    //chat
    private Socket mSocket;

    public static String CHAT_MESSAGE_CMD = "chat message";
    public static String CHAT_JOIN_ROOM = "join room";

    private void initChat() {
        if (mSocket != null)
            return;
        try {
            mSocket = IO.socket(ServiceLinkManager.ChatServerUrl());
            setSocketEvent();
            mSocket.connect();
        }catch (Exception ex) {
            LogHelper.e(TAG, ex);
        }
    }

    private void setSocketEvent() {
        try {
            if (mSocket == null)
                return;
            mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    LogHelper.d(TAG, "socket connected");
                    JoinRoomRequest request = new JoinRoomRequest(mMusicPlayer.getCurrentPlaySong());
                    LogHelper.d(TAG, "send join room request");
                    mSocket.emit(CHAT_JOIN_ROOM, request.getRequestJson());
                }

            }).on(CHAT_MESSAGE_CMD, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    LogHelper.d(TAG, "get a new message");

                    final Comment comment = new Comment();
                    try {
                        JSONObject json = new JSONObject((String)args[0]);
                        comment.setContent(json.getString("content"));
                        comment.setTime(json.getString("time"));
                        comment.setId(json.getString("id"));
                        comment.setNickName(json.getString("name"));
                        comment.setUserId(json.getString("userId"));
                        Object isManager = json.get("isManager");
                        if (isManager instanceof Boolean) {
                            comment.setManager((Boolean)isManager);
                        }
                    } catch (Exception ex) {
                        LogHelper.e(TAG, ex);
                        return;
                    }
                    addCommentToList(comment);
                }
            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    LogHelper.d(TAG, "socket disconnected");
                }
            });
            mSocket.connect();
        }catch (Exception ex) {
            LogHelper.e(TAG, ex);
        }
    }

    private synchronized void addCommentToList(final Comment comment) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getAdapter().addComment(comment);
            }
        });
    }

    private void releaseChat() {
        if (mSocket != null) {
            mSocket.disconnect();
            mSocket.off();
        }
    }

    private void updateChat() {
        mUpdateChatCount++;


        if (mUpdateChatCount % 6 == 0) {
            new GetLiveListenerTask().execute();
        }

        if (mUpdateChatCount % 3 == 0) {
            new GetSongInfoTask().execute();
        }
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
        new GetLiveSongCommentsTask().execute();
        scheduleChatUpdate();
        return v;
    }

    @Override
    protected void createAdapter(View container) {
        View playListView = container.findViewById(R.id.play_list_view);
        mPlayerCell = createPlayerCell();
        mPlayerCell.setPlayerListView(playListView);

        LiveSong song = (LiveSong) mMusicPlayer.getCurrentPlaySong();

        mAdapter = new LiveSongListViewCellAdapter(getActivity(), song, mPlayerCell);
        mPlayerCell.setAdapter(mAdapter);
        LogHelper.d(TAG, "mAdapter.size = " + mAdapter.getCount());
    }

    @Override
    public void onResume() {
        super.onResume();
        initChat();
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
        releaseChat();
        stopChatUpdate();
    }


    private void addMoreComments(List<Comment> comments) {
        LiveSongListViewCellAdapter adapter = (LiveSongListViewCellAdapter)mAdapter;
        adapter.addComments(comments);
    }


    @Override
    protected void reloadNewSong() {
        super.reloadNewSong();
        getAdapter().loadNewSong((LiveSong)mMusicPlayer.getCurrentPlaySong());
        mLastCommentId = "-1";
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

                LogHelper.d(TAG, "comment0 = [" + comment + "], length = " + comment.length());
                String newComment = "";
                for(String each : mCommentChars) {
                    newComment += MyEmojiParse.convertToCheatCode(each);
                }
                comment = newComment;

                request.setComment(comment);
                request.setSong(song);
                request.setLastId(mLastCommentId);
                resetComment();
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
                return;
            }
            //commentCount += resp.getCommentList().size();
            if (resp.getCommentList().size() > 0) {
                mLastCommentId = resp.getCommentList().get(0).getId() + "";
            }

            List<Comment> comments = resp.getCommentList();
            addMoreComments(comments);
        }
    }

    /***
     * Send Live Comment Task
     */

    private class SendLiveCommentTask extends AsyncTask<SendLiveCommentRequest, Void, SendLiveCommentResponse> {
        private SendLiveCommentRequest mRequest;

        @Override
        protected SendLiveCommentResponse doInBackground(SendLiveCommentRequest... params) {
            if (!mSocket.connected()) {
                LogHelper.d(TAG, "reconect to socket");
                mSocket.off();
                mSocket = null;
                initChat();
            }

            mRequest = params[0];
            LogHelper.d(TAG, "send comment to socket");
            mSocket.emit(CHAT_MESSAGE_CMD, mRequest.getRequestJson(), new Ack() {
                @Override
                public void call(Object... args) {
                    LogHelper.d(TAG, "server received.");
                    final JSONObject resultJson = (JSONObject)args[0];
                    LogHelper.d(TAG, resultJson);
                    int status = -1;
                    try {
                        status = resultJson.getInt("status");
                        if (status != 0) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Utils.showErrorMessage(getActivity(), resultJson.getString("errorMessage"));
                                    }catch (JSONException ex) {
                                        LogHelper.e(TAG, ex);
                                    }
                                }
                            });
                            return;
                        }
                    } catch (JSONException ex) {
                        LogHelper.e(TAG, ex);
                        return;
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "发送成功", Toast.LENGTH_SHORT).show();

                            //make comment
                            final Comment comment = new Comment();
                            comment.setContent(mRequest.getComment());
                            Date now = new Date();
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                            comment.setTime(sdf.format(now));
                            comment.setId("1");
                            LoginUser user = LoginUserDao.getInstance(getActivity()).get();
                            comment.setNickName(user.getNickName());
                            comment.setUserId(user.getUserName());

                            addCommentToList(comment);

                            mCommentEditText.setText("");
                        }
                    });

                }
            });
            return null;
        }
    }


    private LiveSongListViewCellAdapter getAdapter() {
        return (LiveSongListViewCellAdapter)mAdapter;
    }

    private  class GetLiveListenerTask extends AsyncTask<Void ,Void, GetLiveListenerResponse> {
        @Override
        protected GetLiveListenerResponse doInBackground(Void... params) {
            GetLiveListenerRequest request = new GetLiveListenerRequest();
            request.setSong(mMusicPlayer.getCurrentPlaySong());
            return new BasicService().sendRequest(request);
        }

        @Override
        protected void onPostExecute(GetLiveListenerResponse resp) {
            super.onPostExecute(resp);
            if (!resp.isSuccess()) {
                return;
            }

            int listenerCount = resp.getListernerCount();
            LiveSong song = (LiveSong)mMusicPlayer.getCurrentPlaySong();
            if (song != null) {
                song.setListenPeople(listenerCount+"人");
            }
            mPlayerCell.getListenerCountLabel().setText(listenerCount+"人");
        }
    }

    private class GetSongInfoTask extends AsyncTask<Void, Void, GetSongInfoResponse> {
        @Override
        protected GetSongInfoResponse doInBackground(Void... params) {
            Song song = mMusicPlayer.getCurrentPlaySong();
            GetSongInfoRequest request = new GetSongInfoRequest(song);
            return new BasicService().sendRequest(request);
        }

        @Override
        protected void onPostExecute(GetSongInfoResponse resp) {
            super.onPostExecute(resp);
            if (resp.isSuccess()) {
                LiveSong song = (LiveSong)mMusicPlayer.getCurrentPlaySong();
                //if (song.getId().equals(resp.getSong().getId())) {
                    LiveSong newSong = (LiveSong) resp.getSong();

                    boolean hasChange = false;
                    if (newSong.hasAdvImage() != song.hasAdvImage() || newSong.getAdvImageUrl() != song.getAdvImageUrl() || newSong.getAdvUrl() != song.getAdvUrl()) {
                        hasChange = true;
                    }

                    if (newSong.getAdvText() != song.getAdvText()) {
                        hasChange = true;
                    }
                    LogHelper.d(TAG, "hasChange = " + hasChange);
                    if (!hasChange) {
                        return;
                    }

                    song.setHasAdvImage(newSong.hasAdvImage());
                    song.setAdvImageUrl(newSong.getAdvImageUrl());
                    song.setAdvUrl(newSong.getAdvUrl());
                    song.setAdvText(newSong.getAdvText());
                    getAdapter().notifyAdvChanged();
               // }
            }
        }
    }



}

