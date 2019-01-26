package com.jinjunhang.onlineclass.ui.fragment.album.player;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.onlineclass.db.LoginUserDao;
import com.jinjunhang.onlineclass.model.Comment;
import com.jinjunhang.onlineclass.model.LoginUser;
import com.jinjunhang.onlineclass.service.SendLiveCommentRequest;
import com.jinjunhang.onlineclass.service.SendLiveCommentResponse;
import com.jinjunhang.onlineclass.ui.fragment.album.NewLiveSongFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.socket.client.Ack;
import io.socket.client.Socket;

public class SendLiveCommentTask extends AsyncTask<SendLiveCommentRequest, Void, SendLiveCommentResponse> {

    private final String TAG = LogHelper.makeLogTag(SendLiveCommentTask.class);

    private SendLiveCommentRequest mRequest;
    private Socket mSocket;
    private NewLiveSongFragment mFragment;
    private ChatManager mChatManager;

    public SendLiveCommentTask(NewLiveSongFragment fragment, ChatManager chatManager) {
        mFragment = fragment;
        mChatManager = chatManager;
        mSocket = chatManager.getSocket();
    }

    @Override
    protected SendLiveCommentResponse doInBackground(SendLiveCommentRequest... params) {
        if (!mSocket.connected()) {
            LogHelper.d(TAG, "reconect to socket");
            mSocket.off();
            mSocket = null;
            mChatManager.initChat();
            mSocket = mChatManager.getSocket();
        }

        mRequest = params[0];
        LogHelper.d(TAG, "send comment to socket");
        LogHelper.d(TAG, "msocket = " + mSocket);
        mSocket.emit(ChatManager.CHAT_MESSAGE_CMD, mRequest.getRequestJson(), new Ack() {
            @Override
            public void call(Object... args) {
                LogHelper.d(TAG, "server received.");
                final JSONObject resultJson = (JSONObject)args[0];
                LogHelper.d(TAG, resultJson);
                int status = -1;
                try {
                    status = resultJson.getInt("status");
                    if (status != 0) {
                        mFragment.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Utils.showErrorMessage(mFragment.getActivity(), resultJson.getString("errorMessage"));
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

                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mFragment.getActivity(), "发送成功", Toast.LENGTH_SHORT).show();

                        //make comment
                        final Comment comment = new Comment();
                        comment.setContent(mRequest.getComment());
                        Date now = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                        comment.setTime(sdf.format(now));
                        comment.setId("1");
                        LoginUser user = LoginUserDao.getInstance(mFragment.getActivity()).get();
                        comment.setNickName(user.getNickName());
                        comment.setUserId(user.getUserName());

                        mFragment.commentSentHandler(comment);
                    }
                });

            }
        });
        return null;
    }
}
