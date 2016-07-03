package com.jinjunhang.onlineclass.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.ExoPlayer;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.Song;
import com.jinjunhang.onlineclass.ui.cell.BaseListViewCell;
import com.jinjunhang.player.MusicPlayer;
import com.jinjunhang.player.utils.LogHelper;
import com.jinjunhang.player.utils.StatusHelper;
import com.jinjunhang.player.utils.TimeUtil;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by lzn on 16/6/20.
 */
public class WebBrowserActivity extends AppCompatActivity {

    private final static String TAG = LogHelper.makeLogTag(WebBrowserActivity.class);

    public final static String EXTRA_URL = "EXTRA_URL";
    public final static String EXTRA_TITLE = "EXTRA_TITLE";

    private String mUrl;
    private String mTitle;
    private WebView mWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_browser);

        mUrl = getIntent().getStringExtra(EXTRA_URL);
        mTitle = getIntent().getStringExtra(EXTRA_TITLE);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View customView = getLayoutInflater().inflate(R.layout.actionbar, null);
        ((TextView)customView.findViewById(R.id.actionbar_text)).setText(mTitle);
        getSupportActionBar().setCustomView(customView);
        Toolbar parent =(Toolbar) customView.getParent();
        parent.setContentInsetsAbsolute(0, 0);
        //设置返回按键
        ImageButton backButton = (ImageButton) getSupportActionBar().getCustomView().findViewById(R.id.actionbar_back_button);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mWebView = (WebView) findViewById(R.id.webview);

        mWebView.getSettings().setJavaScriptEnabled(true); // enable javascript
        mWebView.setWebViewClient(new MyWebViewClient());
        openURL();
    }

    /** Opens the URL in a browser */
    private void openURL() {
        mWebView.loadUrl(mUrl);
        mWebView.requestFocus();
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }


    /**
     * Created by lzn on 16/6/22.
     */
    public static class PlayerCell extends BaseListViewCell implements ExoPlayer.Listener {

        private ArrayAdapter mAdapter;

        private final static String TAG = LogHelper.makeLogTag(PlayerCell.class);
        private static final long PROGRESS_UPDATE_INTERNAL = 1000;
        private static final long PROGRESS_UPDATE_INITIAL_INTERVAL = 100;

        private ImageButton mPlayButton;
        private ImageButton mPrevButton;
        private ImageButton mNextButton;
        private SeekBar mSeekbar;
        private TextView mPlayTimeTextView;
        private TextView mDurationTextView;
        private ImageView mBufferCircle;
        private Animation mRotation;

        private MusicPlayer mMusicPlayer;
        private boolean mInited;

        private Song mSong;

        //更新播放进度
        private final Handler mHandler = new Handler();
        private ScheduledFuture<?> mScheduleFuture;
        private final ScheduledExecutorService mExecutorService = Executors.newSingleThreadScheduledExecutor();
        private final Runnable mUpdateProgressTask = new Runnable() {
            @Override
            public void run() {
                updateProgress();
            }
        };

        protected void reset() {
            mPlayTimeTextView.setText("00:00");
            mDurationTextView.setText("00:00");
            mSeekbar.setProgress(0);
        }

        public void setAdapter(ArrayAdapter adapter) {
            mAdapter = adapter;
        }

        @Override
        public ViewGroup getView() {

            View v = mActivity.getLayoutInflater().inflate(R.layout.player_view, null);
            mPlayButton = (ImageButton) v.findViewById(R.id.player_play_button);
            mPrevButton = (ImageButton) v.findViewById(R.id.player_prev_button);
            mNextButton = (ImageButton) v.findViewById(R.id.player_next_button);

            mPlayButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int state = MusicPlayer.getInstance(mActivity).getState();
                    LogHelper.d(TAG, "state = ", state);
                    if (mMusicPlayer.isPlaying()) {
                        mMusicPlayer.pause();
                    } else {
                        mMusicPlayer.resume();
                        scheduleSeekbarUpdate();
                    }
                    updatePlayButton();
                }
            });

            mPrevButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    updatePrevAndNextButton();
                    updatePlayButton();
                    reset();
                    mMusicPlayer.prev();
                }
            });

            mNextButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    updatePrevAndNextButton();
                    updatePlayButton();
                    reset();
                    mMusicPlayer.next();
                }
            });

            mBufferCircle = (ImageView) v.findViewById(R.id.player_buffer_image);
            mPlayTimeTextView = (TextView) v.findViewById(R.id.player_playTimeText);
            mDurationTextView = (TextView) v.findViewById(R.id.player_durationText);
            mSeekbar = (SeekBar) v.findViewById(R.id.player_seekbar);
            mSeekbar.setMax(0);
            mSeekbar.setMax(1000);
            //mSeekbar.setProgress(50);
            mSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    stopSeekbarUpdate();
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    //.seekTo(seekBar.getProgress());
                    long seekToSec = mMusicPlayer.getDuration() * seekBar.getProgress() / 1000;
                    mMusicPlayer.seekTo(seekToSec);
                    scheduleSeekbarUpdate();
                }
            });

            scheduleSeekbarUpdate();
            updatePlayButton();
            mInited = true;

            LogHelper.d(TAG, "playerCell make view");
            return (LinearLayout)v.findViewById(R.id.list_item_albumtype_viewgroup);
        }


        private void scheduleSeekbarUpdate() {
            stopSeekbarUpdate();
            if (!mExecutorService.isShutdown()) {
                mScheduleFuture = mExecutorService.scheduleAtFixedRate(
                        new Runnable() {
                            @Override
                            public void run() {
                                mHandler.post(mUpdateProgressTask);
                            }
                        }, PROGRESS_UPDATE_INITIAL_INTERVAL,
                        PROGRESS_UPDATE_INTERNAL, TimeUnit.MILLISECONDS);
            }
        }

        private void stopSeekbarUpdate() {
            if (mScheduleFuture != null) {
                mScheduleFuture.cancel(false);
            }
        }


        public PlayerCell(Activity activity) {
            super(activity);
            mMusicPlayer = MusicPlayer.getInstance(activity);
            mInited = false;
        }

        private void updatePlayButton() {
            int state = mMusicPlayer.getState();
            if (StatusHelper.isPlayingForUI(mMusicPlayer) ) {
                mPlayButton.setImageResource(R.drawable.icon_ios_music_pause);
            } else {
                mPlayButton.setImageResource(R.drawable.icon_ios_music_play);
            }

            if (state == ExoPlayer.STATE_BUFFERING || state == ExoPlayer.STATE_PREPARING) {
                mBufferCircle.setVisibility(View.VISIBLE);
                load_animations();

                LogHelper.d(TAG, "start buffer circle rotate");
            } else {
                if (mRotation != null) {
                    mRotation.cancel();
                }
                mBufferCircle.setAnimation(null);
                mBufferCircle.setVisibility(View.INVISIBLE);
                LogHelper.d(TAG, "stop buffer circle rot ate");
            }

        }

        private void updatePrevAndNextButton() {
            mNextButton.setEnabled(mMusicPlayer.hasNext());
            mPrevButton.setEnabled(mMusicPlayer.hasPrev());

        }


        void load_animations() {
            if (mRotation == null) {
                mRotation = AnimationUtils.loadAnimation(mActivity, R.anim.rotate);
                mRotation.setDuration(1600);
            }
            mBufferCircle.startAnimation(mRotation);
        }

        private void updateProgress() {
            //LogHelper.d(TAG, "updateProgress called");
            if (!mMusicPlayer.isPlaying()) {
                return;
            }
            long currentPosition = mMusicPlayer.getCurrentPosition();
            int progress = (int) ((double)currentPosition / mMusicPlayer.getDuration() * 1000);
            mSeekbar.setProgress(progress );

            long playTimeInSec = currentPosition / 1000;
            mPlayTimeTextView.setText(TimeUtil.secondsToString(playTimeInSec));
        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            LogHelper.d(TAG, "onPlayerStateChanged called, mInited = " + mInited);
            if (!mInited)
                return;
            updatePlayButton();
            updatePrevAndNextButton();
        }

        @Override
        public void onPlayWhenReadyCommitted() {

        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {

        }

        public Song getSong() {
            return mSong;
        }

        public void setSong(Song song) {
            mSong = song;
        }



    }
}
