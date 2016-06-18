package com.jinjunhang.uamp.playback;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;

import com.jinjunhang.uamp.model.MusicProvider;
import com.jinjunhang.uamp.model.MusicProviderSource;
import com.jinjunhang.uamp.playback.exo.player.DemoPlayer;
import com.jinjunhang.uamp.playback.exo.player.ExtractorRendererBuilder;
import com.jinjunhang.uamp.playback.exo.player.HlsRendererBuilder;
import com.jinjunhang.uamp.playback.exo.player.SmoothStreamingRendererBuilder;
import com.jinjunhang.uamp.playback.exo.player.SmoothStreamingTestMediaDrmCallback;
import com.google.android.exoplayer.util.Util;
import com.jinjunhang.uamp.utils.LogHelper;
import com.jinjunhang.uamp.utils.MediaIDHelper;

import java.io.IOException;

/**
 * Created by lzn on 16/6/17.
 */
public class ExoPlayback implements Playback, AudioManager.OnAudioFocusChangeListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnSeekCompleteListener {

    private static final String TAG = LogHelper.makeLogTag(ExoPlayback.class);

    private DemoPlayer player;
    private boolean playerNeedsPrepare;

    private Context context;
    private int contentType;
    private Uri contentUri;

    private final MusicProvider mMusicProvider;



    public ExoPlayback(Context context, MusicProvider musicProvider) {
        this.context = context;
        mMusicProvider = musicProvider;
    }

    private void preparePlayer(boolean playWhenReady) {
       // if (player == null) {
            player = new DemoPlayer(getRendererBuilder());
            playerNeedsPrepare = true;
        //}
        if (playerNeedsPrepare) {
            player.prepare();
            playerNeedsPrepare = false;
        }
    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
        }
    }

    private DemoPlayer.RendererBuilder getRendererBuilder() {
        String userAgent = Util.getUserAgent(context, "ExoPlayerDemo");
        switch (contentType) {
            case Util.TYPE_SS:
                return new SmoothStreamingRendererBuilder(context, userAgent, contentUri.toString(), new SmoothStreamingTestMediaDrmCallback());
            //case Util.TYPE_DASH:
             //   return new DashRendererBuilder(this, userAgent, contentUri.toString(), new WidevineTestMediaDrmCallback(contentId, provider));
            case Util.TYPE_HLS:
                return new HlsRendererBuilder(context, userAgent, contentUri.toString());
            case Util.TYPE_OTHER:
                return new ExtractorRendererBuilder(context, userAgent, contentUri);
            default:
                throw new IllegalStateException("Unsupported type: " + contentType);
        }
    }

    @Override
    public void onAudioFocusChange(int focusChange) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop(boolean notifyListeners) {

    }

    @Override
    public void setState(int state) {

    }

    @Override
    public int getState() {
        return 0;
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public boolean isPlaying() {
        if (player == null) {
            return false;
        }
        return player.getPlaybackState() == PlaybackState.STATE_PLAYING;
    }

    @Override
    public int getCurrentStreamPosition() {
        return 0;
    }

    @Override
    public void setCurrentStreamPosition(int pos) {

    }

    @Override
    public void updateLastKnownStreamPosition() {

    }

    public String getUrl(MediaSessionCompat.QueueItem item) {
        //String mediaId = item.getDescription().getMediaId();

        MediaMetadataCompat track = mMusicProvider.getMusic(item.getDescription().getMediaId());

        String source = track.getString(MusicProviderSource.CUSTOM_METADATA_TRACK_SOURCE);

        LogHelper.d(TAG, "source = " + source);

        return source;
    }

    @Override
    public void play(MediaSessionCompat.QueueItem item) {
        //mPlayOnFocusGain = true;
        //tryToGetAudioFocus();
        //registerAudioNoisyReceiver();
        releasePlayer();
        contentType = Util.TYPE_OTHER;
        contentUri = Uri.parse(getUrl(item));
        //contentUri = Uri.parse("http://114.215.171.93:1935/live/myStream2/playlist.m3u8");
        preparePlayer(true);
        player.setPlayWhenReady(true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void seekTo(int position) {

    }

    @Override
    public void setCurrentMediaId(String mediaId) {

    }

    @Override
    public String getCurrentMediaId() {
        return null;
    }

    @Override
    public void setCallback(Callback callback) {

    }
}
