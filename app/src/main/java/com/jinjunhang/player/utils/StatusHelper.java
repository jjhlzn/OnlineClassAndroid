package com.jinjunhang.player.utils;

import android.media.session.PlaybackState;

import com.google.android.exoplayer2.Player;
import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.onlineclass.model.Song;
import com.jinjunhang.player.MusicPlayer;

/**
 * Created by lzn on 16/6/19.
 */
public class StatusHelper {

    private static final String TAG = LogHelper.makeLogTag(StatusHelper.class);


    public static boolean isPlayingForUI(MusicPlayer musicPlayer) {
        int state = musicPlayer.getState();
        boolean isPlayingForUI = musicPlayer.isPlaying()
                || (state == Player.STATE_BUFFERING);
       // LogHelper.d(TAG, "isPlayingForUI = " + isPlayingForUI);
        return isPlayingForUI;
    }

    public static boolean isPlayingLiveSongForUI(MusicPlayer musicPlayer) {
        int state = musicPlayer.getState();
        boolean isPlayingForUI = musicPlayer.isPlaying()
                || (state == Player.STATE_BUFFERING);
        // LogHelper.d(TAG, "isPlayingForUI = " + isPlayingForUI);
        if (isPlayingForUI) {
            Song song = musicPlayer.getCurrentPlaySong();
            LogHelper.d(TAG, "song.isLive = " + song.isLive());

            return musicPlayer.getCurrentPlaySong().isLive();
        } else {
            return false;
        }
    }
}
