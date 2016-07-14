package com.jinjunhang.onlineclass.ui.lib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.jinjunhang.player.ExoPlayerNotificationManager;
import com.jinjunhang.player.MediaNotificationManager;
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
        ExoPlayerNotificationManager notificationManager =  ExoPlayerNotificationManager.getInstance(context);
        String action = intent.getAction();
        LogHelper.d(TAG, "action = " + action );
        if(action.equalsIgnoreCase(ExoPlayerNotificationManager.ACTION_PLAY)){
            // do your stuff to play action;
            LogHelper.d(TAG, "AudioPlayerBroadcastReceiver#onReceive");
            if (musicPlayer.isPlaying()) {
                musicPlayer.pause();
            } else {
                musicPlayer.resume();
            }
            notificationManager.display();
        } else if (action.equalsIgnoreCase(ExoPlayerNotificationManager.ACTION_PREV)){
            musicPlayer.prev();

        } else if (action.equalsIgnoreCase(ExoPlayerNotificationManager.ACTION_NEXT)){
            musicPlayer.next();
        }
    }
}