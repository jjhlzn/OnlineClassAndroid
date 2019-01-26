package com.jinjunhang.onlineclass.ui.fragment.album.player;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.framework.lib.MyEmojiParse;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.framework.service.ServerResponse;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.Comment;
import com.jinjunhang.onlineclass.model.ServiceLinkManager;
import com.jinjunhang.onlineclass.model.Song;
import com.jinjunhang.onlineclass.service.GetLiveCommentsRequest;
import com.jinjunhang.onlineclass.service.GetLiveCommentsResponse;
import com.jinjunhang.onlineclass.service.JoinRoomRequest;
import com.jinjunhang.onlineclass.service.SendLiveCommentRequest;
//import com.jinjunhang.onlineclass.ui.fragment.album.CourseOverviewFragment;
import com.jinjunhang.onlineclass.ui.fragment.album.NewLiveSongFragment;
import com.jinjunhang.onlineclass.ui.lib.EmojiKeyboard;
import com.jinjunhang.player.MusicPlayer;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ChatManager {

    private Socket mSocket;
    private final String TAG = LogHelper.makeLogTag(ChatManager.class);
    private NewLiveSongFragment mFragment;
    private MusicPlayer mMusicPlayer;

    //chat
    public static String CHAT_MESSAGE_CMD = "chat message";
    public static String CHAT_JOIN_ROOM = "join room";

    public ChatManager(NewLiveSongFragment fragment) {
        mCommentChars = new ArrayList<>();
        mFragment = fragment;
        mMusicPlayer = MusicPlayer.getInstance(mFragment.getActivity());
    }


    public Socket getSocket() {
        return mSocket;
    }

    public void initChat() {
        LogHelper.d(TAG, "initChat called");
        if (mSocket != null) {
            LogHelper.d(TAG, "socket is not null");
            return;
        }
        try {
            LogHelper.d(TAG, "create and set socket");
            mSocket = IO.socket(ServiceLinkManager.ChatServerUrl());
            setSocketEvent();
            mSocket.connect();
        }catch (Exception ex) {
            LogHelper.e(TAG, ex);
        }
    }

    public void releaseChat() {
        LogHelper.d("releaseChat called");
        if (mSocket != null) {
            mSocket.disconnect();
            mSocket.off();
            mSocket = null;
        }
    }

    private void setSocketEvent() {
        try {
            if (mSocket == null)
                return;
            mSocket.off();
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
                    mFragment.commentReceiveHandler(comment);
                }
            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    LogHelper.d(TAG, "socket disconnected");
                }
            });
        }catch (Exception ex) {
            LogHelper.e(TAG, ex);
        }
    }

    protected View mCommentTip;
    protected View mCommentWindow;
    protected EditText mCommentEditText;
    private ViewGroup mEmojiKeyBoardView;
    private ImageButton keyboardSwitchButton;

    protected List<String> mCommentChars;
    private String oldCommentString = "";
    public void setBottomCommentView(View v) {


        //设置emoji切换按钮
        keyboardSwitchButton = (ImageButton) v.findViewById(R.id.emojikeyboard_switch_button);

        //control comment editor
        mCommentTip = v.findViewById(R.id.bottom_comment_tip);
        mCommentTip.setVisibility(View.VISIBLE);
        mCommentWindow = v.findViewById(R.id.bottom_comment);
        mCommentEditText = (EditText) v.findViewById(R.id.comment_edittext);
        mCommentTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCommentWindow.setVisibility(View.VISIBLE);
                mCommentEditText.requestFocus();
                keyboardSwitchButton.setImageResource(R.drawable.keyboard_emoji);
                InputMethodManager imm = (InputMethodManager) mFragment.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mCommentEditText, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        mCommentEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmojiKeyBoardView.setVisibility(View.GONE);
                keyboardSwitchButton.setImageResource(R.drawable.keyboard_emoji);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mCommentWindow.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            }
        });

        mCommentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try {
                    if (s.length() > oldCommentString.length()) {
                        mCommentChars.add(s.subSequence(start + before, s.length()).toString());
                        LogHelper.d(TAG, "add char = " + s.subSequence(start + before, s.length()).toString());
                    } else {
                        if (mCommentChars.size() > 0) {
                            mCommentChars.remove(mCommentChars.size() - 1);
                            LogHelper.d(TAG, "remove char");
                        }
                    }
                    oldCommentString = s.toString();
                }catch (Exception ex){
                    LogHelper.e(TAG, ex);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        TextView sendButton = (TextView) v.findViewById(R.id.send_button);
        sendButton.setOnClickListener(createSendOnClickListener());

        mEmojiKeyBoardView = (ViewGroup) v.findViewById(R.id.emojikeyboard);
        final View emojiKeyboard = new EmojiKeyboard(mFragment.getActivity(), mCommentEditText).getView();
        mEmojiKeyBoardView.addView(emojiKeyboard);


        keyboardSwitchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) mFragment.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (mEmojiKeyBoardView.getVisibility() == View.VISIBLE) { //显示文字键盘
                    //Utils.hideSoftKeyboard(getActivity());
                    LogHelper.d(TAG, "emojiboard will hide");
                    mEmojiKeyBoardView.setVisibility(View.GONE);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mCommentWindow.getLayoutParams();
                    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    mCommentEditText.requestFocus();
                    imm.showSoftInput(mCommentEditText, InputMethodManager.SHOW_IMPLICIT);
                    keyboardSwitchButton.setImageResource(R.drawable.keyboard_emoji);
                    mCommentTip.setVisibility(View.VISIBLE);
                } else {        //显示Emoji键盘
                    imm.hideSoftInputFromWindow(mCommentEditText.getWindowToken(), 0);
                    LogHelper.d(TAG, "emojiboard will show");

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mEmojiKeyBoardView.setVisibility(View.VISIBLE);
                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mCommentWindow.getLayoutParams();
                            params.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                            //mCommentEditText.setVisibility(View.INVISIBLE);
                            keyboardSwitchButton.setImageResource(R.drawable.keybaord_keyboard);
                            mCommentTip.setVisibility(View.INVISIBLE);
                        }
                    }, 100);
                }
            }
        });
        resetComment();
    }

    protected View.OnClickListener createSendOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment =  mCommentEditText.getText().toString();

                if (comment.trim().length() == 0)  {
                    Utils.showErrorMessage(mFragment.getActivity(), "不能发送空评论");
                    return;
                }

                Song song = mMusicPlayer.getCurrentPlaySong();
                //Song song = new Song();
                SendLiveCommentRequest request = new SendLiveCommentRequest();

                //LogHelper.d(TAG, "comment0 = [" + comment + "], length = " + comment.length());
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
                new SendLiveCommentTask( mFragment, ChatManager.this).execute(request);

                mCommentTip.setVisibility(View.VISIBLE);
            }
        };
    }



    protected void closeCommentWindow() {
        Utils.hideSoftKeyboard(mFragment.getActivity());
        mCommentWindow.setVisibility(View.INVISIBLE);

        mEmojiKeyBoardView.setVisibility(View.GONE);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mCommentWindow.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
    }

    protected void resetComment() {
        mCommentEditText.setText("");
        mCommentChars = new ArrayList<>();
        oldCommentString = "";
    }

    private String mLastCommentId = "-1";
    private class GetLiveSongCommentsTask extends AsyncTask<GetLiveCommentsRequest, Void, GetLiveCommentsResponse> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected GetLiveCommentsResponse doInBackground(GetLiveCommentsRequest... params) {
            GetLiveCommentsRequest req = params[0];
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
            mFragment.getCommentsHandler(comments);
        }
    }

    public void loadComments() {
        GetLiveCommentsRequest req = new GetLiveCommentsRequest();
        Song song = mMusicPlayer.getCurrentPlaySong();
        req.setSong(song);
        req.setLastId(mLastCommentId);
        new GetLiveSongCommentsTask().execute(req);
    }
}
