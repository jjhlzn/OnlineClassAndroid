package com.jinjunhang.player;

import android.content.Context;
import android.net.Uri;

import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.util.Util;
import com.jinjunhang.onlineclass.model.Album;
import com.jinjunhang.onlineclass.model.Song;
import com.jinjunhang.player.playback.exo.player.DemoPlayer;
import com.jinjunhang.player.playback.exo.player.ExtractorRendererBuilder;
import com.jinjunhang.player.playback.exo.player.HlsRendererBuilder;
import com.jinjunhang.player.playback.exo.player.SmoothStreamingRendererBuilder;
import com.jinjunhang.player.playback.exo.player.SmoothStreamingTestMediaDrmCallback;
import com.jinjunhang.player.utils.LogHelper;
import com.jinjunhang.player.utils.StatusHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzn on 16/6/19.
 */
public class MusicPlayer implements ExoPlayer.Listener {
    private static final String TAG = LogHelper.makeLogTag(MusicPlayer.class);
    private static MusicPlayer instance;

    private Context context;
    private DemoPlayer player;
    private Song[] mSongs;
    private int currentIndex = -1;
    private MusicPlayerControlListener mControlListener;
    private int lastType = Util.TYPE_OTHER;
    private Album lastAlbum = null;

    public static MusicPlayer getInstance(Context context) {
        if (instance == null) {
            LogHelper.i(TAG, "create new MusicPlayer");
            instance = new MusicPlayer();
            instance.context = context;
            instance.player = new DemoPlayer();
            instance.player.addListener(instance);
            instance.player.addListener(ExoPlayerNotificationManager.getInstance(context));
        }
        return instance;
    }

    public List<Song> getSongs() {
        ArrayList<Song> result = new ArrayList<Song>();
        for(Song song : mSongs)
            result.add(song);
        return result;
    }

    public void addMusicPlayerControlListener(MusicPlayerControlListener listener) {
        mControlListener = listener;
    }

    public void removeMusicPlayerControlListener() {
        mControlListener = null;
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

    public void addListener(ExoPlayer.Listener listener) {
        player.addListener(listener);
    }

    public void removeListener(ExoPlayer.Listener listener) {
        player.removeListener(listener);
    }


    public int getState() {
        if (player == null) {
            return ExoPlayer.STATE_IDLE;
        }
        return  player.getPlaybackState();
    }


    public boolean isPlaying() {
        return getState() == ExoPlayer.STATE_READY && player.getPlayWhenReady();
    }

    public boolean isPlay(Song song) {
        if (StatusHelper.isPlayingForUI(this)) {
            if (getCurrentPlaySong().getId().equals(song.getId())) {
                return true;
            }
        }
        return false;
    }

    public boolean isPause() {
        return getState() == ExoPlayer.STATE_READY && !player.getPlayWhenReady();
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

    public void resume() {
        if (!isPause() || getCurrentPlaySong().isLive()) {
            Song song = mSongs[currentIndex];
            play(song);
        }

        player.setPlayWhenReady(true);
    }

    public boolean hasNext() {
        return currentIndex < mSongs.length - 1;
    }

    public void next() {
        if (hasNext()) {
            currentIndex++;
            Song song = mSongs[currentIndex];
            play(song);
            if (mControlListener != null) {
                mControlListener.onClickNext();
            }
        }
    }


    public boolean hasPrev() {
        return currentIndex >= 1;
    }
    public void prev() {

        if (hasPrev()) {
            Song song = mSongs[currentIndex--];
            play(song);
            if (mControlListener != null){
                mControlListener.onClickPrev();
            }
        }
    }


    public long getCurrentPosition() {
        return player.getCurrentPosition();
    }

    public long getDuration() {
        return player.getDuration();
    }

    public void seekTo(long position) {
        player.seekTo(position);
    }

    public Song getCurrentPlaySong() {
        if (mSongs == null) {
            return null;
        }
        return mSongs[currentIndex];
    }

    public int getCurrentPlaySongIndex() {
        if (mSongs == null) {
            return -1;
        }
        return currentIndex;
    }

    private void createPlayer(Song song) {
        int type = Util.TYPE_OTHER;
        if (song.isLive()) {
            type = Util.TYPE_HLS;
        }
        if (player == null) {
            player = new DemoPlayer(getRendererBuilder(Uri.parse(song.getUrl()), type));
            player.addListener(this);
            player.addListener(ExoPlayerNotificationManager.getInstance(context));
        } else {
            if (lastAlbum != null && lastAlbum.getId().equals(song.getAlbum().getId())) {
                player.setRendererBuilder(getRendererBuilder(Uri.parse(song.getUrl()), type));
            } else {
                player = new DemoPlayer(getRendererBuilder(Uri.parse(song.getUrl()), type));
                player.addListener(this);
                player.addListener(ExoPlayerNotificationManager.getInstance(context));
            }
        }
        lastAlbum = song.getAlbum();
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        LogHelper.d(TAG, "onPlayerStateChanged: playbackState = " + playbackState);
        if (playbackState == ExoPlayer.STATE_ENDED) {
            //自动播放下一首
            next();
        }
    }

    @Override
    public void onPlayWhenReadyCommitted() {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    public static interface MusicPlayerControlListener {
        void onClickNext();
        void onClickPrev();
    }
}

