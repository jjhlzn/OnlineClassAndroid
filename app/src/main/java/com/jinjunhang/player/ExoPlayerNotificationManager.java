package com.jinjunhang.player;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.NotificationCompat;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.ExoPlayer;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.Song;
import com.jinjunhang.player.utils.LogHelper;

/**
 * Created by jjh on 2016-7-13.
 */
public class ExoPlayerNotificationManager implements ExoPlayer.Listener {
    public static final String ACTION_PLAY = "com.jinjunhang.onlineclass.ACTION_PLAY";
    public static final String ACTION_PREV = "com.jinjunhang.onlineclass.ACTION_PLAY";
    public static final String ACTION_NEXT = "com.jinjunhang.onlineclass.ACTION_PLAY";

    private final int NOTIFICATION_ID = 100;

    private Context mContext;
    private NotificationManager mNotificationManager;

    private static final String TAG = LogHelper.makeLogTag(ExoPlayerNotificationManager.class);

    private static ExoPlayerNotificationManager instance;
    public  static ExoPlayerNotificationManager getInstance(Context context) {
        if (instance == null) {
            instance = new ExoPlayerNotificationManager(context);
        }
        return instance;
    }

    private ExoPlayerNotificationManager(Context context) {
        this.mContext = context;
    }

    private void addPlayPauseAction(NotificationCompat.Builder builder) {
        LogHelper.d(TAG, "updatePlayPauseAction");
        String label;
        int icon;
        Intent switchIntent = new Intent(ACTION_PLAY);
        PendingIntent pendingSwitchIntent = PendingIntent.getBroadcast(mContext, 100, switchIntent, 0);
        MusicPlayer musicPlayer = MusicPlayer.getInstance(mContext);

        if (musicPlayer.isPlaying()) {
            label = "暂停";
            icon = R.drawable.icon_ios_music_pause;
        } else {
            label = "播放";
            icon = R.drawable.icon_ios_music_play;
        }

        builder.addAction(new NotificationCompat.Action(icon, label, pendingSwitchIntent));
    }

    private void addPrevButton(NotificationCompat.Builder builder) {
        String label;
        int icon;
        Intent switchIntent = new Intent(ACTION_PREV);
        PendingIntent pendingSwitchIntent = PendingIntent.getBroadcast(mContext, 100, switchIntent, 0);

        label = "上一首";
        icon = R.drawable.icon_ios_music_backward;

        builder.addAction(new NotificationCompat.Action(icon, label, pendingSwitchIntent));
    }

    private void addNextButton(NotificationCompat.Builder builder) {
        String label;
        int icon;
        Intent switchIntent = new Intent(ACTION_NEXT);
        PendingIntent pendingSwitchIntent = PendingIntent.getBroadcast(mContext, 100, switchIntent, 0);

        label = "下一首";
        icon = R.drawable.icon_ios_music_forward;

        builder.addAction(new NotificationCompat.Action(icon, label, pendingSwitchIntent));
    }

    public void display() {
        new GetImageTask().execute();
    }

    private void fetchBitmapFromURLAsync(final String bitmapUrl,
                                         final NotificationCompat.Builder builder) {
        AlbumArtCache.getInstance().fetch(bitmapUrl, new AlbumArtCache.FetchListener() {
            @Override
            public void onFetched(String artUrl, Bitmap bitmap, Bitmap icon) {
                    // If the media is still the same, update the notification:
                    LogHelper.d(TAG, "fetchBitmapFromURLAsync: set bitmap to ", artUrl);
                    builder.setLargeIcon(bitmap);
                    mNotificationManager.notify(NOTIFICATION_ID, builder.build());

            }
        });
    }

    public void display0() {
        final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext);
        int playPauseButtonPosition = 1;

        addPrevButton(notificationBuilder);
        addPlayPauseAction(notificationBuilder);
        addNextButton(notificationBuilder);

        String fetchArtUrl = null;
        Bitmap art = null;


        Song song = MusicPlayer.getInstance(mContext).getCurrentPlaySong();
        String songName = "";
        String author = "";
        if (song != null) {
            songName = song.getName();
            author = song.getAlbum().getAuthor();
        }


        notificationBuilder
                .setStyle(new NotificationCompat.MediaStyle())
                .setSmallIcon(R.drawable.ic_notification)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setUsesChronometer(true)
                .setContentTitle(songName)
                .setContentText(author)
                .setColor(Color.RED)
                .setShowWhen(false);

        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);


        notificationBuilder.setLargeIcon(art);
        final Notification notification = notificationBuilder.build();
        notification.flags |= Notification.FLAG_NO_CLEAR;
        mNotificationManager.notify(NOTIFICATION_ID, notification);

        fetchArtUrl = song.getAlbum().getImage();
        LogHelper.d(TAG, "imageUrl = " + fetchArtUrl);
        fetchBitmapFromURLAsync(fetchArtUrl, notificationBuilder);

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        display();
    }

    @Override
    public void onPlayWhenReadyCommitted() {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    private class GetImageTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            display0();
            return null;
        }
    }
}
