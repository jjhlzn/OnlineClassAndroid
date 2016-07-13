package com.jinjunhang.onlineclass.ui.lib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.jinjunhang.player.MusicPlayer;
import com.jinjunhang.player.utils.LogHelper;

/**
 * Created by jjh on 2016-7-13.
 */
public class AudioPlayerBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = LogHelper.makeLogTag(AudioPlayerBroadcastReceiver.class);
    @Override
    public void onReceive(Context context, Intent intent) {
        MusicPlayer musicPlayer = MusicPlayer.getInstance(context);
        String action = intent.getAction();

        if(action.equalsIgnoreCase("com.example.app.ACTION_PLAY")){
            // do your stuff to play action;
            LogHelper.d(TAG, "AudioPlayerBroadcastReceiver#onReceive");
            if (musicPlayer.isPlaying()) {
                musicPlayer.pause();
            } else {
                musicPlayer.resume();
            }
        }
    }
}