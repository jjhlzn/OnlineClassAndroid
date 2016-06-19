package com.jinjunhang.player.utils;

import android.media.session.PlaybackState;

import com.google.android.exoplayer.ExoPlayer;
import com.jinjunhang.player.MusicPlayer;

/**
 * Created by lzn on 16/6/19.
 */
public class StatusHelper {

    public static int convertExo2Media(int state, int playWhenReady) {
        int resultState;
        switch (state) {
            case ExoPlayer.STATE_IDLE:
                resultState = PlaybackState.STATE_NONE;
                break;
            case ExoPlayer.STATE_BUFFERING:
                resultState = PlaybackState.STATE_BUFFERING;
                break;
            case ExoPlayer.STATE_PREPARING:
                //resultState = PlaybackState.STATE
                break;
            case ExoPlayer.STATE_ENDED:
                break;
            case ExoPlayer.STATE_READY:
                break;
        }
        return 0;
    }

    public static boolean isPlayingForUI(MusicPlayer musicPlayer) {
        int state = musicPlayer.getState();
        return musicPlayer.isPlaying() || (state == ExoPlayer.STATE_BUFFERING) || (state == ExoPlayer.STATE_PREPARING);
    }
}
