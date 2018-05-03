package com.jinjunhang.player.utils;

import android.media.session.PlaybackState;

import com.google.android.exoplayer.ExoPlayer;
import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.player.MusicPlayer;

/**
 * Created by lzn on 16/6/19.
 */
public class StatusHelper {

    private static final String TAG = LogHelper.makeLogTag(StatusHelper.class);


    public static boolean isPlayingForUI(MusicPlayer musicPlayer) {
        int state = musicPlayer.getState();
        boolean isPlayingForUI = musicPlayer.isPlaying()
                || (state == ExoPlayer.STATE_BUFFERING) || (state == ExoPlayer.STATE_PREPARING);
       // LogHelper.d(TAG, "isPlayingForUI = " + isPlayingForUI);
        return isPlayingForUI;
    }
}
