package com.jinjunhang.player;

import android.content.Context;
import android.net.Uri;
import android.support.v4.media.session.MediaSessionCompat;

import com.google.android.exoplayer.util.Util;
import com.jinjunhang.onlineclass.model.Song;
import com.jinjunhang.player.playback.Playback;
import com.jinjunhang.player.playback.exo.player.DemoPlayer;
import com.jinjunhang.player.playback.exo.player.ExtractorRendererBuilder;
import com.jinjunhang.player.playback.exo.player.HlsRendererBuilder;
import com.jinjunhang.player.playback.exo.player.SmoothStreamingRendererBuilder;
import com.jinjunhang.player.playback.exo.player.SmoothStreamingTestMediaDrmCallback;
import com.jinjunhang.player.utils.LogHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzn on 16/6/19.
 */
public class MusicPlayer  {
    private static final String TAG = LogHelper.makeLogTag(MusicPlayer.class);
    private static MusicPlayer instance;

    private Context context;
    private DemoPlayer player;
    private Song[] mSongs;
    private int currentIndex = -1;

    public static MusicPlayer getInstance(Context context) {
        if (instance == null) {
            LogHelper.i(TAG, "create new MusicPlayer");
            instance = new MusicPlayer();
            instance.context = context;
        }
        return instance;
    }


    private DemoPlayer.RendererBuilder getRendererBuilder(Uri contentUri, int contentType) {
        String userAgent = Util.getUserAgent(context, "ExoPlayerDemo");
        switch (contentType) {
            case Util.TYPE_SS:
                return new SmoothStreamingRendererBuilder(context, userAgent, contentUri.toString(), new SmoothStreamingTestMediaDrmCallback());
            case Util.TYPE_HLS:
                return new HlsRendererBuilder(context, userAgent, contentUri.toString());
            case Util.TYPE_OTHER:
                return new ExtractorRendererBuilder(context, userAgent, contentUri);
            default:
                throw new IllegalStateException("Unsupported type: " + contentType);
        }
    }



    public void start() {

    }


    public void stop() {

    }


    public void setState(int state) {

    }


    public int getState() {
        return  player.getPlaybackState();
    }


    public boolean isPlaying() {
        return false;
    }



    public void play(List<Song> songs, int startIndex) {
        mSongs = new Song[songs.size()];
        mSongs =   songs.toArray(mSongs);
        currentIndex = startIndex;
        Song song = mSongs[currentIndex];
        play(song);
    }

    private void play(Song song) {
        createPlayer(song);
        player.prepare();
        player.setPlayWhenReady(true);
    }


    public void pause() {
        player.setPlayWhenReady(false);
    }


    public void seekTo(int position) {

    }


    public Song getCurrentPlaySong() {
        return mSongs[currentIndex];
    }

    private void createPlayer(Song song) {
        if (player != null) {
            player.release();
        }
        player = new DemoPlayer(getRendererBuilder(Uri.parse(song.getUrl()), Util.TYPE_OTHER));
    }

}
