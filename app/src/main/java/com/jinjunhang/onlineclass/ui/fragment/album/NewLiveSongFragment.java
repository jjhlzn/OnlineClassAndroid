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
import com.jinjunhang.onlineclass.ui.fragment.album.player.ChatManager;
import com.jinjunhang.onlineclass.ui.fragment.album.player.SendLiveCommentTask;
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


    private ChatManager mChatManager;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMusicPlayer = MusicPlayer.getInstance(getActivity());
        mChatManager = new ChatManager(NewLiveSongFragment.this);

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
        setPlayerView(v);

        mChatManager.setBottomCommentView(v);
        //mChatManager.loadComments();

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
                ((CourseOverviewFragment)fragments[0]).setChatManager(fragment.mChatManager);
                fragments[1] = BeforeCoursesFragment.class.newInstance();
                ((BeforeCoursesFragment)fragments[1]).mSongFragment = fragment;
            }catch (Exception  ex) {
                LogHelper.e("DataGenerator", ex);
            }
            return fragments;
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        mChatManager.initChat();
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
        mChatManager.releaseChat();
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


    public synchronized void commentSentHandler(Comment comment) {

    }

    public synchronized void commentReceiveHandler(final Comment comment) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((CourseOverviewFragment)mFragmensts[0]).newCommentHanlder(comment);
            }
        });

    }

    public void getCommentsHandler(List<Comment> comments) {
        ((CourseOverviewFragment)mFragmensts[0]).setCommentsView(comments);
    }
}

