package com.jinjunhang.onlineclass.ui.fragment.album;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer.ExoPlayer;
import com.gyf.barlibrary.ImmersionBar;
import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.framework.service.ServerResponse;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.Album;
import com.jinjunhang.onlineclass.model.Comment;
import com.jinjunhang.onlineclass.model.LiveSong;
import com.jinjunhang.onlineclass.model.ServiceLinkManager;
import com.jinjunhang.onlineclass.service.GetAlbumSongsRequest;
import com.jinjunhang.onlineclass.service.GetAlbumSongsResponse;
import com.jinjunhang.onlineclass.service.GetLiveListenerRequest;
import com.jinjunhang.onlineclass.service.GetLiveListenerResponse;
import com.jinjunhang.onlineclass.service.GetZhuanLanAndTuijianCourseRequest;
import com.jinjunhang.onlineclass.service.GetZhuanLanAndTuijianCourseResponse;
import com.jinjunhang.onlineclass.ui.activity.WebBrowserActivity;
import com.jinjunhang.onlineclass.ui.activity.mainpage.BottomTabLayoutActivity;
import com.jinjunhang.onlineclass.ui.fragment.BaseFragment;
import com.jinjunhang.onlineclass.ui.fragment.album.player.ChatManager;
import com.jinjunhang.onlineclass.ui.lib.ShareManager;
import com.jinjunhang.player.MusicPlayer;
import com.jinjunhang.player.utils.StatusHelper;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by jjh on 2016-7-2.
 */
public class NewLiveSongFragment extends BaseFragment implements ExoPlayer.Listener {

    private final static String TAG = LogHelper.makeLogTag(NewLiveSongFragment.class);

    private boolean mInited;
    protected MusicPlayer mMusicPlayer;

    //private LoadingAnimation mLoadingView;
    private ImageButton mPlayButton;
    private Toolbar mToolbar;
    private Button couseOverViewBtn, signUpBtn;
    private TextView mListenerTextView;
    private TextView mPlayTextView;
    private ImageView mCourseImage;
    private CourseOverviewView mCourseOverviewView;

    private ChatManager mChatManager;
    protected ShareManager mShareManager;

    private int lastPlayerState = ExoPlayer.STATE_IDLE;
    private boolean hasSeekTo = false;

    //定时获取评论、回复播放
    private static final long CHAT_UPDATE_INTERNAL = 10000;
    private static final long CHAT_UPDATE_INITIAL_INTERVAL = 2000;
    private final Handler mHandler = new Handler();
    private ScheduledFuture<?> mScheduleFuture;
    private final ScheduledExecutorService mExecutorService = Executors.newSingleThreadScheduledExecutor();
    private final Runnable mUpdateChatTask = new Runnable() {
        @Override
        public void run() {
            updateChat();
        }
    };

    private float mLastAlpha = 0;

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

    private int mUpdateChatCount = 0;
    private void updateChat() {
        mUpdateChatCount++;
        if (mUpdateChatCount % 6 == 0) {
            new GetLiveListenerTask().execute();
        }
    }

    @Override
    protected boolean isNeedTopPadding() {
        return false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_fragment_live_player;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMusicPlayer = MusicPlayer.getInstance(getActivity());

        mView = super.onCreateView(inflater, container, savedInstanceState);

        ListView childListView = mView.findViewById(R.id.listView);
        mCourseOverviewView = new CourseOverviewView(getActivity(), childListView);

        //mLoadingView = new LoadingAnimation(getActivity());
        final ScrollView scrollView = mView.findViewById(R.id.scrollView);
        mListenerTextView = mView.findViewById(R.id.listenerCount);

        mPlayTextView = mView.findViewById(R.id.playTextView);
        mCourseImage = mView.findViewById(R.id.courseImage);

        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = scrollView.getScrollY(); // For ScrollView

                int height = 72;
                float alpha = 0;
                alpha = (float)scrollY / height;
                if (alpha > 1)
                    alpha = 1;

                if (Math.abs(alpha - mLastAlpha) > 0.0001)
                    updateToolBar(alpha);

                mLastAlpha = alpha;
            }
        });

        //把除了分享的toolbar设置好
        mToolbar = mView.findViewById(R.id.toolbar);
        setToolBar();

        if (!isInMainActivity()) {
            fetchData();
        }

        return mView;
    }

    private void setViewWithSongInfo(LiveSong song) {
        mChatManager = new ChatManager(NewLiveSongFragment.this);
        mChatManager.initChat();

        Glide.with(this)
                .load(song.getImageUrl())
                .into(mCourseImage);

        mListenerTextView.setText(song.getListenPeople());

        mShareManager = new ShareManager((AppCompatActivity)getActivity(), mView);
        mShareManager.setShareTitle(song.getShareTitle());
        mShareManager.setShareUrl(song.getShareUrl());
        mShareManager.setUseQrCodeImage(false);

        setToolBar();

        mPlayButton = mView.findViewById(R.id.playBtn);

        couseOverViewBtn = mView.findViewById(R.id.courseOverviewBtn);
        signUpBtn = mView.findViewById(R.id.SignupBtn);

        couseOverViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //buttonClicked(0);
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

        mCourseOverviewView.setChatManager(mChatManager);
        mCourseOverviewView.fetchData();

        setPlayerView(mView);

        mChatManager.setBottomCommentView(mView);
        //mChatManager.loadComments();

        scheduleChatUpdate();
    }

    public void fetchData() {

        if (getUserVisibleHint() && !mInited) {
            LogHelper.d(TAG, "fetch data");
            LiveSong song = (LiveSong) mMusicPlayer.getCurrentPlaySong();
            if (song == null) {
                loadSongInfo();
            } else {
                setViewWithSongInfo(song);
            }
            mInited = true;
        }
    }

    private void loadSongInfo() {
        new GetZhuanLanAndTuijianCoursesTask().execute();
    }

    private boolean isInMainActivity() {
        if (getActivity() instanceof BottomTabLayoutActivity) {
            return true;
        }
        return false;
    }

    private boolean isSetToolBarCalled = false;
    protected  void setToolBar() {

        ImageButton backBtn = mToolbar.findViewById(R.id.actionbar_back_button);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        if (isInMainActivity()) {
            backBtn.setVisibility(View.INVISIBLE);
        } else {
            backBtn.setVisibility(View.VISIBLE);
        }

        if (mInited) {
            ImageButton shareBtn = mToolbar.findViewById(R.id.actionbar_right_button);
            final ViewGroup shareView = mView.findViewById(R.id.share_view);
            shareBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    shareView.setVisibility(View.VISIBLE);
                }
            });

            View overlay = mView.findViewById(R.id.overlay_bg);
            overlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //shareView.setVisibility(View.INVISIBLE);
                }
            });
            //这段代码不能删除，否则按在空白的地方会把
            mView.findViewById(R.id.share_menu).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
        else {
            if (!isSetToolBarCalled) {
                ImmersionBar.setTitleBar(getActivity(), mToolbar);
                ImmersionBar.with(this).transparentStatusBar().init();
                isSetToolBarCalled = true;
            }
        }

        updateToolBar(0);
    }

    private void  updateToolBar(float alpha) {

        if (alpha < 0)
            alpha = 0;
        if (alpha > 1)
            alpha = 1;

        mToolbar.setBackgroundColor(ColorUtils.blendARGB(Color.TRANSPARENT, ContextCompat.getColor(getActivity(), R.color.colorPrimary), alpha));

        TextView textView = mToolbar.findViewById(R.id.actionbar_text);
        ImageButton backButton = mToolbar.findViewById(R.id.actionbar_back_button);
        ImageButton shareButton = mToolbar.findViewById(R.id.actionbar_right_button);

        if (mMusicPlayer.getCurrentPlaySong() != null)
            textView.setText(mMusicPlayer.getCurrentPlaySong().getName());
        else
            textView.setText("");

        if (alpha > 0.7) {
            textView.setTextColor(getResources().getColor(R.color.black));
            textView.setVisibility(View.VISIBLE);
            backButton.setImageDrawable(getResources().getDrawable(R.drawable.back));
            shareButton.setImageDrawable(getResources().getDrawable(R.drawable.share_black));
        } else {
            textView.setTextColor(getResources().getColor(R.color.colorPrimary));
            textView.setVisibility(View.INVISIBLE);
            backButton.setImageDrawable(getResources().getDrawable(R.drawable.back_white));
            shareButton.setImageDrawable(getResources().getDrawable(R.drawable.share_white));
        }

        if (alpha == 1) {
            ImmersionBar.with(this).statusBarDarkFont(true).init();
        } else {
            ImmersionBar.with(this).statusBarDarkFont(false).init();
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
        /*
        ViewGroup.LayoutParams params = mViewPager.getLayoutParams();
        if (index == 0)
            params.height= ((CourseOverviewFragment)mFragmensts[index]).getListViewHeightBasedOnChildren();
        else
            params.height= ((BeforeCoursesFragment)mFragmensts[index]).getListViewHeightBasedOnChildren();
        LogHelper.d(TAG, "set viewpager height = " + params.height);
        mViewPager.setLayoutParams(params); */
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mChatManager != null)
            mChatManager.initChat();
    }

    @Override
    public void onStop() {
        super.onStop();
        //mMusicPlayer.removeMusicPlayerControlListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mChatManager != null) {
            mChatManager.releaseChat();
            stopChatUpdate();
        }
    }

    protected void updatePlayButton() {
        LogHelper.d(TAG, "updatePlayButton called, state = " + mMusicPlayer.getState());

        int state = mMusicPlayer.getState();
        if (StatusHelper.isPlayingForUI(mMusicPlayer) ) {
            mPlayButton.setImageResource(R.drawable.icon_stop);
        } else {
            mPlayButton.setImageResource(R.drawable.icon_play);
        }


        if (state == ExoPlayer.STATE_BUFFERING || state == ExoPlayer.STATE_PREPARING) {
            //mBufferCircle.setVisibility(View.VISIBLE);
            //load_animations();
            mPlayTextView.setText("缓冲中");
        } else {
            /*
            if (mRotation != null) {
                mRotation.cancel();
            }
            mBufferCircle.setAnimation(null);
            mBufferCircle.setVisibility(View.INVISIBLE); */

            if (mMusicPlayer.isPlaying()) {
                mPlayTextView.setText("播放中");
            } else {
                mPlayTextView.setText("开始播放");
            }
        }

    }

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


    public synchronized void commentSentHandler(final Comment comment) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mCourseOverviewView.newCommentHanlder(comment);
            }
        });
    }

    public synchronized void commentReceiveHandler(final Comment comment) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mCourseOverviewView.newCommentHanlder(comment);
            }
        });

    }

    public void getCommentsHandler(List<Comment> comments) {
        mCourseOverviewView.setCommentsView(comments);
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
                song.setListenPeople(listenerCount+"");
            }
            mListenerTextView.setText(song.getListenPeople());
        }
    }

    public class GetAlbumSongsTask extends AsyncTask<GetAlbumSongsRequest, Void, GetAlbumSongsResponse> {

        private GetAlbumSongsRequest request;

        @Override
        protected GetAlbumSongsResponse doInBackground(GetAlbumSongsRequest... params) {
            //mLoadingView.show("");
            request = params[0];
            return new BasicService().sendRequest(request);
        }

        @Override
        protected void onPostExecute(GetAlbumSongsResponse resp) {
            super.onPostExecute(resp);
            if (resp.getStatus() == ServerResponse.NO_PERMISSION) {
                //mLoadingView.hide();
                Utils.showVipBuyMessage(getActivity(), resp.getErrorMessage());
                return;
            }

            if (resp.getStatus() == ServerResponse.NOT_PAY_COURSE_NO_PERMISSION) {
                //mLoadingView.hide();
                Utils.showErrorMessage(getActivity(), resp.getErrorMessage());
                return;
            }

            if (!resp.isSuccess()) {
               // mLoadingView.hide();
                LogHelper.e(TAG, resp.getErrorMessage());
                Utils.showErrorMessage(getActivity(), resp.getErrorMessage());
                return;
            }

            if (resp.getResultSet().size() >= 1) {
                LiveSong song = (LiveSong) resp.getResultSet().get(0);
                MusicPlayer musicPlayer = MusicPlayer.getInstance(getActivity());
                if (!musicPlayer.isPlay(song)) {
                    musicPlayer.pause();
                    musicPlayer.play(resp.getResultSet(), 0);
                    //mNotificationManager.display();
                }
                setViewWithSongInfo(song);
                return;
            } else {
               // mLoadingView.hide();
                Utils.showErrorMessage(getActivity(), "服务端出错");
                return;
            }
        }

    }

    private class GetZhuanLanAndTuijianCoursesTask extends AsyncTask<Void, Void, GetZhuanLanAndTuijianCourseResponse> {

        @Override
        protected GetZhuanLanAndTuijianCourseResponse doInBackground(Void... voids) {
            GetZhuanLanAndTuijianCourseRequest request = new GetZhuanLanAndTuijianCourseRequest();
            return new BasicService().sendRequest(request);
        }

        @Override
        protected void onPostExecute(final GetZhuanLanAndTuijianCourseResponse response) {
            super.onPostExecute(response);
            //mSwipeRefreshLayout.setRefreshing(false);

            if (!response.isSuccess()) {
                LogHelper.e(TAG, response.getErrorMessage());
                return;
            }

            List<Album> courses = response.getCourses();
            if (courses.size() > 0) {
                GetAlbumSongsRequest request = new GetAlbumSongsRequest();

                request.setAlbum(courses.get(0));
                request.setAlbum(courses.get(0));
                request.setPageIndex(0);
                request.setPageSize(10);
                new GetAlbumSongsTask().execute(request);
            }
        }
    }
}

