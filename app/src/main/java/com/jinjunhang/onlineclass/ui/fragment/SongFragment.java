package com.jinjunhang.onlineclass.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.exoplayer.ExoPlayer;
import com.jinjunhang.framework.controller.BaseFragment;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.Song;
import com.jinjunhang.player.MusicPlayer;
import com.jinjunhang.player.utils.LogHelper;
import com.jinjunhang.player.utils.TimeUtil;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by lzn on 16/6/13.
 */
public class SongFragment extends BaseFragment {
    private final static String TAG = LogHelper.makeLogTag(SongFragment.class);
    private static final long PROGRESS_UPDATE_INTERNAL = 1000;
    private static final long PROGRESS_UPDATE_INITIAL_INTERVAL = 100;
    public final static String EXTRA_SONG = "SongFragement_song";

    private Song mSong;

    private ImageButton mPlayButton;
    private ImageButton mPrevButton;
    private ImageButton mNextButton;
    private SeekBar mSeekbar;
    private TextView mPlayTimeTextView;
    private TextView mDurationTextView;
    private ImageView mBufferCircle;


    private MusicPlayer mMusicPlayer;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        mSong = (Song)getActivity().getIntent().getSerializableExtra(EXTRA_SONG);
        mMusicPlayer = MusicPlayer.getInstance(getActivity());

        View v = inflater.inflate(R.layout.activity_fragment_play_song, container, false);
        mPlayButton = (ImageButton) v.findViewById(R.id.player_play_button);
        mPrevButton = (ImageButton) v.findViewById(R.id.player_prev_button);
        mNextButton = (ImageButton) v.findViewById(R.id.player_next_button);

        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int state = MusicPlayer.getInstance(getActivity()).getState();
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
                mMusicPlayer.prev();
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                updatePrevAndNextButton();
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
        return v;
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


    private void updateProgress() {
        //LogHelper.d(TAG, "updateProgress called, isPlaying = " + mMusicPlayer.isPlaying()
         //       + ", duration = " + mMusicPlayer.getDuration()
        //        + ", curretnPosition = " + mMusicPlayer.getCurrentPosition());
        if (!mMusicPlayer.isPlaying()) {
            return;
        }
        long currentPosition = mMusicPlayer.getCurrentPosition();
        int progress = (int) ((double)currentPosition / mMusicPlayer.getDuration() * 1000);
        //LogHelper.d(TAG, "progress = " + progress);
        mSeekbar.setProgress(progress );

        long playTimeInSec = currentPosition / 1000;
        mPlayTimeTextView.setText(TimeUtil.secondsToString(playTimeInSec));
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
       // super.onPlayerStateChanged(playWhenReady, playbackState);
        updatePlayButton();
        updatePrevAndNextButton();
        if (playbackState == ExoPlayer.STATE_READY) {
            mDurationTextView.setText( TimeUtil.secondsToString(mMusicPlayer.getDuration() / 1000) );
        }
    }


    private void updatePlayButton() {
        if (mMusicPlayer.isPlaying()) {
            mPlayButton.setImageResource(R.drawable.icon_ios_music_pause);
        } else {
            mPlayButton.setImageResource(R.drawable.icon_ios_music_play);
        }

        if (mMusicPlayer.getState() == ExoPlayer.STATE_BUFFERING) {
            mBufferCircle.setVisibility(View.VISIBLE);
        } else {
            mBufferCircle.setVisibility(View.INVISIBLE);
        }

    }

    private void updatePrevAndNextButton() {
        mNextButton.setEnabled(mMusicPlayer.hasNext());
        mPrevButton.setEnabled(mMusicPlayer.hasPrev());
    }
}

//06-19 15:11:43.237 12710-12710/com.jinjunhang.onlineclass D/uamp_SongFragment:
// updateProgress called, isPlaying = true, duration = 233090, curretnPosition = 58519

