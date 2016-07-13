package com.jinjunhang.player;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.NotificationCompat;

import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.Song;
import com.jinjunhang.onlineclass.ui.activity.MainActivity;
import com.jinjunhang.player.utils.LogHelper;

/**
 * Created by jjh on 2016-7-13.
 */
public class ExoPlayerNotificationManager {
    private Activity mActivity;
    private NotificationManager mNotificationManager;

    private static final String TAG = LogHelper.makeLogTag(ExoPlayerNotificationManager.class);

    public ExoPlayerNotificationManager(Activity activity) {
        this.mActivity = activity;
    }

    private void addPlayPauseAction(NotificationCompat.Builder builder) {
        LogHelper.d(TAG, "updatePlayPauseAction");
        String label;
        int icon;
        String pkg = mActivity.getPackageName();
        //PendingIntent intent =  PendingIntent.getBroadcast(mActivity, 1, new Intent("com.example.app.ACTION_PLAY").setPackage(pkg), PendingIntent.FLAG_CANCEL_CURRENT);

        Intent switchIntent = new Intent("com.example.app.ACTION_PLAY");
        PendingIntent pendingSwitchIntent = PendingIntent.getBroadcast(mActivity, 100, switchIntent, 0);
        /*
        if (mPlaybackState.getState() == PlaybackStateCompat.STATE_PLAYING) {
            label = mService.getString(R.string.label_pause);
            icon = R.drawable.uamp_ic_pause_white_24dp;
            intent = mPauseIntent;
        } else { */
            label ="test";
            icon = R.drawable.uamp_ic_play_arrow_white_48dp;
            //intent = mPlayIntent;
       // }
        builder.addAction(new NotificationCompat.Action(icon, label, pendingSwitchIntent));
    }

    public void display() {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mActivity);
        int playPauseButtonPosition = 0;


        addPlayPauseAction(notificationBuilder);

        // If skip to next action is enabled
        /*
        if ((mPlaybackState.getActions() & PlaybackStateCompat.ACTION_SKIP_TO_NEXT) != 0) {
            notificationBuilder.addAction(R.drawable.ic_skip_next_white_24dp,
                    mService.getString(R.string.label_next), mNextIntent);
        }*/

       // MediaDescriptionCompat description = mMetadata.getDescription();

        String fetchArtUrl = null;
        Bitmap art = null;

        /*
        if (description.getIconUri() != null) {
            // This sample assumes the iconUri will be a valid URL formatted String, but
            // it can actually be any valid Android Uri formatted String.
            // async fetch the album art icon
            String artUrl = description.getIconUri().toString();
            art = AlbumArtCache.getInstance().getBigImage(artUrl);
            if (art == null) {
                fetchArtUrl = artUrl;
                // use a placeholder art while the remote art is being downloaded
                art = BitmapFactory.decodeResource(mService.getResources(),
                        R.drawable.ic_default_art);
            }
        }*/

        Song song = MusicPlayer.getInstance(mActivity).getCurrentPlaySong();
        String songName = "";
        String author = "";
        if (song != null) {
            songName = song.getName();
            author = song.getAlbum().getAuthor();
        }

        notificationBuilder
                .setStyle(new NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(
                                new int[]{playPauseButtonPosition})  // show only play/pause in compact view
                       // .setMediaSession(mSessionToken)
                        )
                //.setColor(mNotificationColor)
                .setSmallIcon(R.drawable.ic_notification)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setUsesChronometer(true)
                //.setContentIntent(createContentIntent(description))
                .setContentTitle(songName)
                .setContentText(author)
                .setLargeIcon(art);

        /*
        if (mController != null && mController.getExtras() != null) {
            String castName = mController.getExtras().getString(MusicService.EXTRA_CONNECTED_CAST);
            if (castName != null) {
                String castInfo = mService.getResources()
                        .getString(R.string.casting_to_device, castName);
                notificationBuilder.setSubText(castInfo);
                notificationBuilder.addAction(R.drawable.ic_close_black_24dp,
                        mService.getString(R.string.stop_casting), mStopCastIntent);
            }
        }

        setNotificationPlaybackState(notificationBuilder);
        if (fetchArtUrl != null) {
            fetchBitmapFromURLAsync(fetchArtUrl, notificationBuilder);
        }*/

        mNotificationManager = (NotificationManager) mActivity.getSystemService(Context.NOTIFICATION_SERVICE);

   /* notificationID allows you to update the notification later on. */
        int notificationID = 100;
        mNotificationManager.notify(notificationID, notificationBuilder.build());

    }

    public void displayNotification() {
        LogHelper.i("Start", "notification");

   /* Invoking the default notification service */
        NotificationCompat.Builder  mBuilder = new NotificationCompat.Builder(mActivity);

        mBuilder.setStyle(new NotificationCompat.MediaStyle());
        mBuilder.setContentTitle("New Message");
        mBuilder.setContentText("You've received new message.");
        mBuilder.setTicker("New Message Alert!");
        mBuilder.setSmallIcon(R.drawable.log);

   /* Increase notification number every time a new notification arrives */
        //mBuilder.setNumber(++numMessages);

   /* Add Big View Specific Configuration */
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        String[] events = new String[6];
        events[0] = new String("This is first line....");
        events[1] = new String("This is second line...");
        events[2] = new String("This is third line...");
        events[3] = new String("This is 4th line...");
        events[4] = new String("This is 5th line...");
        events[5] = new String("This is 6th line...");

        // Sets a title for the Inbox style big view
        inboxStyle.setBigContentTitle("Big Title Details:");

        // Moves events into the big view
        for (int i=0; i < events.length; i++) {
            inboxStyle.addLine(events[i]);
        }

        mBuilder.setStyle(inboxStyle);

   /* Creates an explicit intent for an Activity in your app */
        Intent resultIntent = new Intent(mActivity, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mActivity);
        stackBuilder.addParentStack(MainActivity.class);

   /* Adds the Intent that starts the Activity to the top of the stack */
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);
        mNotificationManager = (NotificationManager) mActivity.getSystemService(Context.NOTIFICATION_SERVICE);

   /* notificationID allows you to update the notification later on. */
        int notificationID = 100;
        mNotificationManager.notify(notificationID, mBuilder.build());
    }

}
