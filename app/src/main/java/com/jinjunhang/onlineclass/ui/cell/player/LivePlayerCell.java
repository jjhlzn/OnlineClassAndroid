package com.jinjunhang.onlineclass.ui.cell.player;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.LiveSong;
import com.jinjunhang.onlineclass.model.Song;
import com.jinjunhang.onlineclass.ui.activity.WebBrowserActivity;
import com.jinjunhang.player.MusicPlayer;
import com.jinjunhang.player.utils.LogHelper;
import com.jinjunhang.player.utils.TimeUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jjh on 2016-7-2.
 */
public class LivePlayerCell extends PlayerCell {
    private static final  String  TAG = LogHelper.makeLogTag(LivePlayerCell.class);

    public LivePlayerCell(Activity activity) {
        super(activity);
    }


    @Override
    public ViewGroup getView() {
        ViewGroup v = super.getView();
        String listenerCount = ((LiveSong)mMusicPlayer.getCurrentPlaySong()).getListenPeople();
        LogHelper.d(TAG, "listenPeopleCount = " + listenerCount);
        mListenerCountLabel.setText(listenerCount);

        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int state = MusicPlayer.getInstance(mActivity).getState();
                LogHelper.d(TAG, "state = ", state);
                if (mMusicPlayer.isPlaying()) {
                    mMusicPlayer.pause();
                } else {
                    mMusicPlayer.play(mMusicPlayer.getSongs(), mMusicPlayer.getCurrentPlaySongIndex());
                    mMusicPlayer.resume();
                    scheduleSeekbarUpdate();
                }
                updatePlayButton();
            }
        });

        return v;
    }

    @Override
    protected void updateProgress() {
        //LogHelper.d(TAG, "updateProgress called");
        if (!mMusicPlayer.isPlaying()) {
            return;
        }
        LiveSong song = (LiveSong) mMusicPlayer.getCurrentPlaySong();

        int progress = (int)((double)song.getPlayedTimeInSec() / song.getTotalTimeInSec() * 1000);
        LogHelper.d(TAG, "startTime = " + song.getStartDateTime() + ", endTime = " + song.getEndDateTime());
        LogHelper.d(TAG, "leftTime = " + song.getTimeLeftInSec() + ", palyedTime = " + song.getPlayedTimeInSec() + ", totalTime = " + song.getTotalTimeInSec());
        LogHelper.d(TAG, "progress = " + progress / 10 + "%");
        mSeekbar.setProgress(progress );
        mPlayTimeTextView.setText(getNowTimeString());

    }

    @Override
    protected void setPlayButtons(View v) {
        super.setPlayButtons(v);
        mPlayTimeTextView.setText(getNowTimeString());
        LiveSong song = (LiveSong) mMusicPlayer.getCurrentPlaySong();
        mDurationTextView.setText(song.getEndTimeString());
    }

    protected String getNowTimeString() {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        return sdf.format(new Date());
    }

    @Override
    protected void setSeekbar(View v) {
        mSeekbar = (SeekBar) v.findViewById(R.id.player_seekbar);
        mSeekbar.setMax(0);
        mSeekbar.setMax(1000);
        //mSeekbar.setProgress(50);
        scheduleSeekbarUpdate();
    }


}
