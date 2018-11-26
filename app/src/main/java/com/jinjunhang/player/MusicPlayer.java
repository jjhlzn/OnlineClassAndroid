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
import com.jinjunhang.framework.lib.LogHelper;
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
    private Album lastAlbum = null;

    public static MusicPlayer getInstance(Context context) {
        if (instance == null) {
            LogHelper.i(TAG, "create new MusicPlayer");
            instance = new MusicPlayer();
            instance.context = context;
            instance.player = new DemoPlayer();
            instance.addListener(instance);
            instance.addListener(ExoPlayerNotificationManager.getInstance(context));
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
        LogHelper.d(TAG, "context: " + context);
        String userAgent = Util.getUserAgent(context, "ExoPlayerDemo");
        switch (contentType) {
            //case Util.TYPE_SS:
            //    return new SmoothStreamingRendererBuilder(context, userAgent, contentUri.toString(), new SmoothStreamingTestMediaDrmCallback());
            case Util.TYPE_HLS:
                return new HlsRendererBuilder(context, userAgent, contentUri.toString());
            case Util.TYPE_OTHER:
                return new ExtractorRendererBuilder(context, userAgent, contentUri);
            default:
                throw new IllegalStateException("Unsupported type: " + contentType);
        }
    }

    private List<ExoPlayer.Listener> mListeners = new ArrayList<>();

    public void addListener(ExoPlayer.Listener listener) {
        player.addListener(listener);
        mListeners.add(listener);
    }

    public void removeListener(ExoPlayer.Listener listener) {
        player.removeListener(listener);
        mListeners.remove(listener);

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
        LogHelper.d(TAG, "play(list, index) called");
        mSongs = new Song[songs.size()];
        mSongs =   songs.toArray(mSongs);
        currentIndex = startIndex;
        Song song = mSongs[currentIndex];
        play(song);
    }

    private void play(Song song) {
        LogHelper.d(TAG, "play(song) called");
        createPlayer(song);
        player.prepare();
        player.setPlayWhenReady(true);
    }


    public void pause() {
        LogHelper.d(TAG, "pause");
        player.setPlayWhenReady(false);
    }

    public void resume() {
        LogHelper.d(TAG, "resume");
        if (getCurrentPlaySong().isLive()) {
            int state = getState();
            if (isPause() || state == ExoPlayer.STATE_BUFFERING || state == ExoPlayer.STATE_READY) {
                LogHelper.d(TAG, "resume from pause");
                player.seekTo(-1);
                player.setPlayWhenReady(true);
            } else {
                LogHelper.d(TAG, "resume not from pause");
                LogHelper.d(TAG, "resume: recreate player");
                Song song = mSongs[currentIndex];
                play(song);
            }
        } else {
            if (isPause()) {
                if (getCurrentPlaySong().isLive()){
                    //跳到最远
                   player.seekTo(-1);
                }
                player.setPlayWhenReady(true);
            } else {
                Song song = mSongs[currentIndex];
                play(song);
            }
        }
    }

    public boolean hasNext() {
        return currentIndex < mSongs.length - 1;
    }

    public void next() {
        LogHelper.d("next called");
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

    private boolean isMp3(Song song) {
        return song.getUrl() != null && song.getUrl().endsWith(".mp3");
    }

    private void createPlayer(Song song) {
        LogHelper.d(TAG, "createPlayer() called");
        int type = Util.TYPE_OTHER;
        if (isMp3(song)) {
            type = Util.TYPE_OTHER;
        } else if (song.isLive()) {
            type = Util.TYPE_HLS;
        }

        LogHelper.d(TAG, "type = " + type );
        if (player == null) {
            player = new DemoPlayer(getRendererBuilder(Uri.parse(song.getUrl()), type));
            player.addListener(this);
            player.addListener(ExoPlayerNotificationManager.getInstance(context));
        } else {
            if (lastAlbum != null && lastAlbum.getId().equals(song.getAlbum().getId())) {
                LogHelper.d(TAG, "player.setRendererBuilder");
                //player.setRendererBuilder(getRendererBuilder(Uri.parse(song.getUrl()), type));
            } else {
                //在创建之前先release
                LogHelper.d(TAG, "recreate player");
                player.release();
                player = new DemoPlayer(getRendererBuilder(Uri.parse(song.getUrl()), type));

                //player.addListener(ExoPlayerNotificationManager.getInstance(context));
                for (ExoPlayer.Listener item : mListeners ) {
                    player.addListener(item);
                }
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

