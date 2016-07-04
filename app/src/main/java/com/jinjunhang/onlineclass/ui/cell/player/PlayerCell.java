package com.jinjunhang.onlineclass.ui.cell.player;

import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
 * Created by lzn on 16/6/22.
 */
public class PlayerCell extends BaseListViewCell implements ExoPlayer.Listener {

    private ArrayAdapter mAdapter;

    private final static String TAG = LogHelper.makeLogTag(PlayerCell.class);
    private static final long PROGRESS_UPDATE_INTERNAL = 1000;
    private static final long PROGRESS_UPDATE_INITIAL_INTERVAL = 100;

    protected ImageButton mPlayButton;
    protected ImageButton mPrevButton;
    protected ImageButton mNextButton;
    protected SeekBar mSeekbar;
    protected TextView mPlayTimeTextView;
    protected TextView mDurationTextView;
    protected ImageView mBufferCircle;
    protected Animation mRotation;
    protected TextView mListenerCountLabel;

    protected MusicPlayer mMusicPlayer;
    private boolean mInited;

    //private Song mSong;

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

    public TextView getListenerCountLabel() {
        return mListenerCountLabel;
    }

    @Override
    public ViewGroup getView() {
        Song song = mMusicPlayer.getCurrentPlaySong();
        int playerView;
        View v;
        if (song.isLive()) {
            playerView = R.layout.player_view_live;
            v = mActivity.getLayoutInflater().inflate(playerView, null);
            mListenerCountLabel = (TextView) v.findViewById(R.id.listen_people_label);
        } else {
            playerView = R.layout.player_view;
            v = mActivity.getLayoutInflater().inflate(playerView, null);
        }

        setPlayButtons(v);
        setSeekbar(v);

        mInited = true;

        LogHelper.d(TAG, "playerCell make view");
        return (LinearLayout)v.findViewById(R.id.list_item_albumtype_viewgroup);
    }

    protected void setPlayButtons(View v) {
        mPlayButton = (ImageButton) v.findViewById(R.id.player_play_button);
        mPrevButton = (ImageButton) v.findViewById(R.id.player_prev_button);
        mNextButton = (ImageButton) v.findViewById(R.id.player_next_button);

        mBufferCircle = (ImageView) v.findViewById(R.id.player_buffer_image);
        mPlayTimeTextView = (TextView) v.findViewById(R.id.player_playTimeText);
        mDurationTextView = (TextView) v.findViewById(R.id.player_durationText);

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

        updatePlayButton();
    }

    protected void setSeekbar(View v) {
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
    }


    protected void scheduleSeekbarUpdate() {
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

    protected void updateProgress() {
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

    @Override
    public void release() {
        super.release();
        stopSeekbarUpdate();
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



}