package com.jinjunhang.player;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.util.Util;
import com.jinjunhang.onlineclass.model.Song;
import com.jinjunhang.onlineclass.ui.lib.CustomApplication;
import com.jinjunhang.framework.lib.LogHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzn on 16/6/19.
 *
 * 该类是SimpleExoPlayerWrapper的包装类（代理类）。
 */
public class MusicPlayer implements ExoPlayer.EventListener {
    private static final String TAG = LogHelper.makeLogTag(MusicPlayer.class);
    private static MusicPlayer instance;

    private Context context;
    private Song[] mSongs;
    private int currentIndex = -1;
    private MusicPlayerControlListener mControlListener;

    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();


    private SimpleExoPlayer player;
    private DataSource.Factory mediaDataSourceFactory;
    private MappingTrackSelector trackSelector;

    private boolean shouldAutoPlay;
    private boolean shouldRestorePosition;
    private int playerWindow;
    private long playerPosition;

    private EventLogger eventLogger;
    private  Handler mainHandler; //= new Handler();


    public static MusicPlayer getInstance(Context context) {
        if (instance == null) {
            LogHelper.i(TAG, "create new MusicPlayer");
            instance = new MusicPlayer();
            instance.mainHandler = new Handler();
            instance.mediaDataSourceFactory = buildDataSourceFactory(true);
            instance.context = CustomApplication.get();;
            instance.initializePlayer();
        }
        return instance;
    }

    public SimpleExoPlayer getSimpleExoPlayer() {
        return player;
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
        LogHelper.d(TAG, "add listener: " + listener);
        if (listener == null) {
            return;
        }
        player.addListener(listener);
    }

    public void removeListener(ExoPlayer.EventListener listener) {
        //todo
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
        release();
        if (player == null) {
            player = createPlayer0(song);
        }
        /*
        else {
            //if (lastAlbum != null && lastAlbum.getId().equals(song.getAlbum().getId())) {
             //   LogHelper.d(TAG, "player.setRendererBuilder");
                //player.setRendererBuilder(getRendererBuilder(Uri.parse(song.getUrl()), type));
            //} else {
                //在创建之前先release
                LogHelper.d(TAG, "recreate player");
                player.release();
                player = createPlayer0(song);
            //}
        } */
    }

    private SimpleExoPlayer createPlayer0(Song song) {
        initializePlayer();
        player.addListener(this);
        player.addListener(ExoPlayerNotificationManager.getInstance(context));
        return player;
    }

    private void play(Song song) {
        LogHelper.d(TAG, "play(song) called");
        createPlayer(song);
        //player.prepare();
        setMediaSource(song);
        player.setPlayWhenReady(true);
    }

    private void setMediaSource(Song song) {
        //String url = "http://devimages.apple.com/samplecode/adDemo/ad.m3u8";
        //String url = "http://114.55.147.70:1935/live/jjh/playlist.m3u8";
        String url = song.getUrl();
        Uri[] uris = new Uri[1];
        uris[0] = Uri.parse(url);


        MediaSource[] mediaSources = new MediaSource[uris.length];
        for (int i = 0; i < uris.length; i++) {
            //// TODO: 2016/10/15   extension should be dynamic
            mediaSources[i] = buildMediaSource(uris[i], ".m3u8");
        }
        MediaSource mediaSource = mediaSources.length == 1 ? mediaSources[0]
                : new ConcatenatingMediaSource(mediaSources);
        player.prepare(mediaSource, !shouldRestorePosition);

    }

    private MediaSource buildMediaSource(Uri uri, String overrideExtension) {
        int type = Util.inferContentType(!TextUtils.isEmpty(overrideExtension) ? "." + overrideExtension
                : uri.getLastPathSegment());
        switch (type) {
            case C.TYPE_SS:
                return new SsMediaSource(uri, buildDataSourceFactory(false),
                        new DefaultSsChunkSource.Factory(mediaDataSourceFactory), mainHandler, eventLogger);
            case C.TYPE_DASH:
                return new DashMediaSource(uri, buildDataSourceFactory(false),
                        new DefaultDashChunkSource.Factory(mediaDataSourceFactory), mainHandler, eventLogger);
            case C.TYPE_HLS:
                return new HlsMediaSource(uri, mediaDataSourceFactory, mainHandler, eventLogger);
            case C.TYPE_OTHER:
                return new ExtractorMediaSource(uri, mediaDataSourceFactory, new DefaultExtractorsFactory(),
                        mainHandler, eventLogger);
            default: {
                throw new IllegalStateException("Unsupported type: " + type);
            }
        }
    }

    /**
     * Returns a new DataSource factory.
     *
     * @param useBandwidthMeter Whether to set {@link #BANDWIDTH_METER} as a listener to the new
     *     DataSource factory.
     * @return A new DataSource factory.
     */
    private static DataSource.Factory buildDataSourceFactory(boolean useBandwidthMeter) {
        return ((CustomApplication)CustomApplication.get()).buildDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null);
    }


    private void initializePlayer() {

        if (player == null) {
            boolean preferExtensionDecoders = false;
            DrmSessionManager<FrameworkMediaCrypto> drmSessionManager = null;


            eventLogger = new EventLogger();
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveVideoTrackSelection.Factory(BANDWIDTH_METER);
            trackSelector = new DefaultTrackSelector(mainHandler, videoTrackSelectionFactory);
            //trackSelector.addListener(this);
            trackSelector.addListener(eventLogger);
            //trackSelectionHelper = new TrackSelectionHelper(trackSelector, videoTrackSelectionFactory);
            player = ExoPlayerFactory.newSimpleInstance(context, trackSelector, new DefaultLoadControl(),
                    drmSessionManager, preferExtensionDecoders);
            //player.addListener(this);
            //player.addListener(eventLogger);
            //player.setAudioDebugListener(eventLogger);
            //player.setVideoDebugListener(eventLogger);
            //player.setId3Output(eventLogger);
            //simpleExoPlayerView.setPlayer(player);
            if (shouldRestorePosition) {
                if (playerPosition == C.TIME_UNSET) {
                    player.seekToDefaultPosition(playerWindow);
                } else {
                    player.seekTo(playerWindow, playerPosition);
                }
            }
            player.setPlayWhenReady(shouldAutoPlay);
            //debugViewHelper = new DebugTextViewHelper(player, debugTextView);
            //debugViewHelper.start();
            //playerNeedsSource = true;
            player.addListener(this);
        }

    }

    public void release() {
        if (player != null) {
            shouldAutoPlay = player.getPlayWhenReady();
            shouldRestorePosition = false;
            Timeline timeline = player.getCurrentTimeline();
            if (timeline != null) {
                playerWindow = player.getCurrentWindowIndex();
                Timeline.Window window = timeline.getWindow(playerWindow, new Timeline.Window());
                if (!window.isDynamic) {
                    shouldRestorePosition = true;
                    playerPosition = window.isSeekable ? player.getCurrentPosition() : C.TIME_UNSET;
                }
            }
            player.removeListener(this);
            player.release();
            player = null;
            trackSelector = null;
            eventLogger = null;
        }
    }


    /************************************************************************/

    public interface MusicPlayerControlListener {
        void onClickNext();
        void onClickPrev();
    }
}

