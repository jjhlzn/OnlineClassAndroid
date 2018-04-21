package com.jinjunhang.onlineclass.ui.fragment.album;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer.ExoPlayer;
import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.framework.lib.MyEmojiParse;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.db.LoginUserDao;
import com.jinjunhang.onlineclass.model.Comment;
import com.jinjunhang.onlineclass.model.LoginUser;
import com.jinjunhang.onlineclass.model.ServiceLinkManager;
import com.jinjunhang.onlineclass.model.Song;
import com.jinjunhang.onlineclass.service.JoinRoomRequest;
import com.jinjunhang.onlineclass.service.SendLiveCommentRequest;
import com.jinjunhang.onlineclass.service.SendLiveCommentResponse;
import com.jinjunhang.onlineclass.ui.activity.WebBrowserActivity;
import com.jinjunhang.onlineclass.ui.fragment.BaseFragment;
import com.jinjunhang.onlineclass.ui.lib.EmojiKeyboard;
import com.jinjunhang.player.MusicPlayer;
import com.jinjunhang.player.utils.StatusHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by jjh on 2016-7-2.
 */
public class NewLiveSongFragment extends BaseFragment implements ExoPlayer.Listener {

    public final static int MAX_COMMENT_COUNT = 10;

    private final static String TAG = LogHelper.makeLogTag(NewLiveSongFragment.class);

    private boolean mInited;
    protected MusicPlayer mMusicPlayer;

    private ImageButton mPlayButton;

    private ViewPager mViewPager;
    private BaseFragment[] mFragmensts;
    private MyPagerAdapter mMyPagerAdapter;
    private Button couseOverViewBtn, otherCourseBtn, signUpBtn;

    public int getCurrentSelectPage() {
        return mViewPager.getCurrentItem();
    }

    //chat
    private Socket mSocket;
    public static String CHAT_MESSAGE_CMD = "chat message";
    public static String CHAT_JOIN_ROOM = "join room";



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMusicPlayer = MusicPlayer.getInstance(getActivity());

        View v = inflater.inflate(R.layout.activity_fragment_live_player, container, false);

        ImageButton backBtn = (ImageButton)v.findViewById(R.id.back_button);
        LogHelper.d(TAG, "backBtn = " + backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getActivity().onBackPressed();
                getActivity().finish();
            }
        });

        mPlayButton  = (ImageButton)v.findViewById(R.id.playBtn);

        couseOverViewBtn = (Button)v.findViewById(R.id.courseOverviewBtn);
        otherCourseBtn = (Button)v.findViewById(R.id.otherCoursesBtn);
        signUpBtn = (Button)v.findViewById(R.id.SignupBtn);


        couseOverViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonClicked(0);
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), WebBrowserActivity.class)
                        .putExtra(WebBrowserActivity.EXTRA_TITLE, "我要报名")
                        .putExtra(WebBrowserActivity.EXTRA_URL, ServiceLinkManager.MyAgentUrl2());
                getActivity().startActivity(i);
            }
        });

        otherCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonClicked(1);
            }
        });

        mFragmensts = DataGenerator.getFragments(getActivity(), "", this);
        mViewPager = (ViewPager)v.findViewById(R.id.viewpager);
        mMyPagerAdapter = new MyPagerAdapter(getActivity().getSupportFragmentManager());
        mViewPager.setAdapter(mMyPagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                buttonClicked(position);
                resetViewPagerHeight(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        buttonClicked(0);
        setBottomCommentView(v);

        setPlayerView(v);

        mInited = true;
        return v;
    }

    private void buttonClicked(int selectedIndex) {
        if (selectedIndex == 0) {
            couseOverViewBtn.setTextColor(getActivity().getResources().getColor(R.color.tab_selected_color));
            otherCourseBtn.setTextColor(getActivity().getResources().getColor(R.color.black));
            mViewPager.setCurrentItem(0);
        } else {
            couseOverViewBtn.setTextColor(getActivity().getResources().getColor(R.color.black));
            otherCourseBtn.setTextColor(getActivity().getResources().getColor(R.color.tab_selected_color));
            mViewPager.setCurrentItem(1);
        }
    }

    private void setPlayerView(View v) {
        ImageButton playBtn = (ImageButton)v.findViewById(R.id.playBtn);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogHelper.d(TAG, "PlayerCell play pressed");
                int state = MusicPlayer.getInstance(getActivity()).getState();
                LogHelper.d(TAG, "state = ", state);
                if (mMusicPlayer.isPlaying()) {
                    mMusicPlayer.pause();
                } else {
                    mMusicPlayer.resume();
                }
                updatePlayButton();
            }
        });
        updatePlayButton();
    }


    protected View mCommentTip;
    protected View mCommentWindow;
    protected EditText mCommentEditText;
    private ViewGroup mEmojiKeyBoardView;
    private ImageButton keyboardSwitchButton;

    protected List<String> mCommentChars;
    private String oldCommentString;
    private void setBottomCommentView(View v) {
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
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
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
        final View emojiKeyboard = new EmojiKeyboard(getActivity(), mCommentEditText).getView();
        mEmojiKeyBoardView.addView(emojiKeyboard);


        keyboardSwitchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (mEmojiKeyBoardView.getVisibility() == View.VISIBLE) {
                    //Utils.hideSoftKeyboard(getActivity());
                    LogHelper.d(TAG, "emojiboard will hide");
                    mEmojiKeyBoardView.setVisibility(View.GONE);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mCommentWindow.getLayoutParams();
                    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    mCommentEditText.requestFocus();
                    imm.showSoftInput(mCommentEditText, InputMethodManager.SHOW_IMPLICIT);
                    //设置为数字键盘
                    mCommentEditText.setRawInputType(InputType.TYPE_CLASS_NUMBER);
                    keyboardSwitchButton.setImageResource(R.drawable.keyboard_emoji);
                } else {
                    imm.hideSoftInputFromWindow(mCommentEditText.getWindowToken(), 0);
                    LogHelper.d(TAG, "emojiboard will show");

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mEmojiKeyBoardView.setVisibility(View.VISIBLE);
                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mCommentWindow.getLayoutParams();
                            params.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                            keyboardSwitchButton.setImageResource(R.drawable.keybaord_keyboard);
                        }
                    }, 100);
                }
            }
        });
    }

    protected View.OnClickListener createSendOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment =  mCommentEditText.getText().toString();

                if (comment.trim().length() == 0)  {
                    Utils.showErrorMessage(getActivity(), "不能发送空评论");
                    return;
                }

                //Song song = mMusicPlayer.getCurrentPlaySong();
                Song song = new Song();
                SendLiveCommentRequest request = new SendLiveCommentRequest();

                //LogHelper.d(TAG, "comment0 = [" + comment + "], length = " + comment.length());
                String newComment = "";
                for(String each : mCommentChars) {
                    newComment += MyEmojiParse.convertToCheatCode(each);
                }
                comment = newComment;

                request.setComment(comment);
                request.setSong(song);
                CourseOverviewFragment fragement = (CourseOverviewFragment)mFragmensts[0];
                request.setLastId(fragement.mLastCommentId);
                resetComment();
                closeCommentWindow();
                new SendLiveCommentTask().execute(request);
            }
        };
    }

    protected void closeCommentWindow() {
        Utils.hideSoftKeyboard(getActivity());
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
    

    public void resetViewPagerHeight(int index) {
        ViewGroup.LayoutParams params = mViewPager.getLayoutParams();
        if (index == 0)
            params.height= ((CourseOverviewFragment)mFragmensts[index]).getListViewHeightBasedOnChildren();
        else
            params.height= ((BeforeCoursesFragment)mFragmensts[index]).getListViewHeightBasedOnChildren();
        params.height = (int)(1.4 * params.height);
        LogHelper.d(TAG, "set viewpager height = " + params.height);
        mViewPager.setLayoutParams(params);
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmensts[position];
        }

        @Override
        public int getCount() {
            return mFragmensts.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "";
        }
    }


    public static class DataGenerator {

        public static BaseFragment[] getFragments(Activity activity, String from, NewLiveSongFragment fragment){
            BaseFragment fragments[] = new BaseFragment[2];
            try {

                fragments[0] = CourseOverviewFragment.class.newInstance();
                ((CourseOverviewFragment)fragments[0]).mSongFragment = fragment;
                fragments[1] = BeforeCoursesFragment.class.newInstance();
                ((BeforeCoursesFragment)fragments[1]).mSongFragment = fragment;
            }catch (Exception  ex) {
                LogHelper.e("DataGenerator", ex);
            }
            return fragments;
        }
    }

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

    private void releaseChat() {
        if (mSocket != null) {
            mSocket.disconnect();
            mSocket.off();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initChat();
        //mMusicPlayer.addMusicPlayerControlListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        //mMusicPlayer.removeMusicPlayerControlListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseChat();
        //stopChatUpdate();
    }


    protected void updatePlayButton() {
        int state = mMusicPlayer.getState();
        if (StatusHelper.isPlayingForUI(mMusicPlayer) ) {
            mPlayButton.setImageResource(R.drawable.icon_stop);
        } else {
            mPlayButton.setImageResource(R.drawable.icon_play);
        }

        /*
        if (state == ExoPlayer.STATE_BUFFERING || state == ExoPlayer.STATE_PREPARING) {
            mBufferCircle.setVisibility(View.VISIBLE);
            load_animations();
        } else {
            if (mRotation != null) {
                mRotation.cancel();
            }
            mBufferCircle.setAnimation(null);
            mBufferCircle.setVisibility(View.INVISIBLE);
        }*/
    }

    private int lastPlayerState = ExoPlayer.STATE_IDLE;
    private boolean hasSeekTo = false;
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (!mInited)
            return;
        updatePlayButton();

        //TODO: 如何测试这段代码
        if (mMusicPlayer.getCurrentPlaySong() != null && mMusicPlayer.getCurrentPlaySong().isLive()) {
            if (playbackState == ExoPlayer.STATE_READY && playWhenReady && lastPlayerState == ExoPlayer.STATE_BUFFERING && !hasSeekTo) {
                hasSeekTo = true;
                MusicPlayer.getInstance(getActivity()).seekTo(-1);
                LogHelper.e(TAG, "lastPlayerState = " + lastPlayerState + ", playbackState = " + playbackState + ", playWhenReady = " + playWhenReady);
            } else if (playbackState == ExoPlayer.STATE_READY && playWhenReady && lastPlayerState == ExoPlayer.STATE_BUFFERING) {
                hasSeekTo = false;
            }
            lastPlayerState = playbackState;
        }
    }


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
                    Song song = new Song();
                    //JoinRoomRequest request = new JoinRoomRequest(mMusicPlayer.getCurrentPlaySong());
                    JoinRoomRequest request = new JoinRoomRequest(song);
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

    private void addCommentToList(Comment comment) {
        //
    }
}

