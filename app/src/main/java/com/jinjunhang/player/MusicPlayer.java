package com.jinjunhang.player;

import android.content.Context;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.jinjunhang.onlineclass.model.Album;
import com.jinjunhang.onlineclass.model.Song;
import com.jinjunhang.player.playback.exo.player.SimpleExoPlayerWrapper;
import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.player.utils.StatusHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzn on 16/6/19.
 *
 * 该类是DemoPlayer的包装类（代理类）。
 */
public class MusicPlayer implements ExoPlayer.EventListener {
    private static final String TAG = LogHelper.makeLogTag(MusicPlayer.class);
    private static MusicPlayer instance;

    private Context context;
    private SimpleExoPlayerWrapper player;
    private Song[] mSongs;
    private int currentIndex = -1;
    private MusicPlayerControlListener mControlListener;
    private Album lastAlbum = null;

    public static MusicPlayer getInstance(Context context) {
        if (instance == null) {
            LogHelper.i(TAG, "create new MusicPlayer");
            instance = new MusicPlayer();
            instance.context = context;
            instance.player = new SimpleExoPlayerWrapper();
            instance.player.addListener(instance);
            instance.player.addListener(ExoPlayerNotificationManager.getInstance(context));
        }
        return instance;
    }

    public SimpleExoPlayer getSimpleExoPlayer() {
        return player.getPlayer();
    }

    /**
     * 获取当前在播的歌曲列表
     * @return
     */
    public List<Song> getSongs() {
        ArrayList<Song> result = new ArrayList<Song>();
        for(Song song : mSongs)
            result.add(song);
        return result;
    }

    /**
     * 加入播放器的状态监听器，用于接收播放器的状态改变
     * @param listener
     */
    public void addListener(ExoPlayer.EventListener listener) {
        //// TODO: 2016/10/15  
        //player.addListener(listener);
    }

    public void removeListener(ExoPlayer.EventListener listener) {
        //todo
        //player.removeListener(listener);
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

    /**
     * 播放歌曲列表的第startIndex首歌
     * @param songs
     * @param startIndex
     */
    public void play(List<Song> songs, int startIndex) {
        LogHelper.d(TAG, "play(list, index) called");
        mSongs = new Song[songs.size()];
        mSongs =   songs.toArray(mSongs);
        currentIndex = startIndex;
        Song song = mSongs[currentIndex];
        play(song);
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

    /**
     * 获取当前在播放的歌曲
     * @return
     */
    public Song getCurrentPlaySong() {
        if (mSongs == null) {
            return null;
        }
        return mSongs[currentIndex];
    }


    /***
     * 加入MusicPlayerController监听器，用于收到上一首和下一首的的通知
     * @param listener
     */
    public void addMusicPlayerControlListener(MusicPlayerControlListener listener) {
        mControlListener = listener;
    }

    public void removeMusicPlayerControlListener() {
        mControlListener = null;
    }



    /***************************  实现ExoPlayer.Listener接口    ***************************/
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        LogHelper.d(TAG, "onPlayerStateChanged: playbackState = " + playbackState);
        if (playbackState == ExoPlayer.STATE_ENDED) {
            //自动播放下一首
            next();
        }
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }
    /*************************************************************************************/


    /***************************  private method   ***************************/

    private void createPlayer(Song song) {
        LogHelper.d(TAG, "createPlayer() called");
        if (player == null) {
            player = createPlayer0(song);
        } else {
            //if (lastAlbum != null && lastAlbum.getId().equals(song.getAlbum().getId())) {
             //   LogHelper.d(TAG, "player.setRendererBuilder");
                //player.setRendererBuilder(getRendererBuilder(Uri.parse(song.getUrl()), type));
            //} else {
                //在创建之前先release
                LogHelper.d(TAG, "recreate player");
                player.release();
                player = createPlayer0(song);
            //}
        }
        lastAlbum = song.getAlbum();
    }

    private SimpleExoPlayerWrapper createPlayer0(Song song) {
        SimpleExoPlayerWrapper player = new SimpleExoPlayerWrapper(song);
        player.addListener(this);
        player.addListener(ExoPlayerNotificationManager.getInstance(context));
        return player;
    }

    private void play(Song song) {
        LogHelper.d(TAG, "play(song) called");
        createPlayer(song);
        player.prepare();
        player.setPlayWhenReady(true);
    }


    /************************************************************************/

    public interface MusicPlayerControlListener {
        void onClickNext();
        void onClickPrev();
    }
}

