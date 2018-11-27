package com.jinjunhang.player;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;

//import com.google.android.exoplayer.ExoPlaybackException;
//import com.google.android.exoplayer.ExoPlayer;
//import com.google.android.exoplayer.util.Util;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.drm.FrameworkMediaDrm;
import com.google.android.exoplayer2.offline.FilteringManifestParser;
import com.google.android.exoplayer2.offline.StreamKey;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.manifest.DashManifestParser;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.hls.playlist.DefaultHlsPlaylistParserFactory;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifestParser;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.DebugTextViewHelper;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.util.EventLogger;
import com.google.android.exoplayer2.util.Util;
import com.jinjunhang.onlineclass.model.Album;
import com.jinjunhang.onlineclass.model.Song;
import com.jinjunhang.onlineclass.ui.lib.CustomApplication;
import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.player.utils.StatusHelper;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzn on 16/6/19.
 */
public class MusicPlayer implements Player.EventListener {
    private static final String TAG = LogHelper.makeLogTag(MusicPlayer.class);
    private static MusicPlayer instance;

    private Context context;
    private SimpleExoPlayer player;
    private Song[] mSongs;
    private int currentIndex = -1;
    private MusicPlayerControlListener mControlListener;
    private Album lastAlbum = null;


    TrackSelection.Factory trackSelectionFactory = new AdaptiveTrackSelection.Factory();
    private DataSource.Factory dataSourceFactory;
    //private FrameworkMediaDrm mediaDrm;
    private MediaSource mediaSource;
    private DefaultTrackSelector trackSelector;
    private DefaultTrackSelector.Parameters trackSelectorParameters;
    //private DebugTextViewHelper debugViewHelper;
    private TrackGroupArray lastSeenTrackGroupArray;

    public static MusicPlayer getInstance(Context context) {
        if (instance == null) {
            LogHelper.i(TAG, "create new MusicPlayer");
            instance = new MusicPlayer();
            instance.context = context;
            instance.trackSelectorParameters = new DefaultTrackSelector.ParametersBuilder().build();
            instance.dataSourceFactory = buildDataSourceFactory();
            //instance.player = new DemoPlayer();
            //instance.addListener(instance);
            //instance.addListener(ExoPlayerNotificationManager.getInstance(context));
        }
        return instance;
    }

    public List<Song> getSongs() {
        ArrayList<Song> result = new ArrayList<Song>();
        for(Song song : mSongs)
            result.add(song);
        return result;
    }

    /** Returns a new DataSource factory. */
    private static DataSource.Factory buildDataSourceFactory() {
        return ((CustomApplication) CustomApplication.get()).buildDataSourceFactory();
    }

    public void addMusicPlayerControlListener(MusicPlayerControlListener listener) {
        mControlListener = listener;
    }

    public void removeMusicPlayerControlListener() {
        mControlListener = null;
    }


    private List<Player.EventListener> mListeners = new ArrayList<>();

    public void addListener(Player.EventListener listener) {
        Assert.assertTrue(listener != null);
        if (player != null && !mListeners.contains(player)) {
            player.addListener(listener);
            mListeners.add(listener);
        }
    }

    public void removeListener(Player.EventListener  listener) {
        if (player != null && listener != null) {
            player.removeListener(listener);
            mListeners.remove(listener);
        }
    }

    public int getState() {
        if (player == null) {
            return Player.STATE_IDLE;
        }
        return  player.getPlaybackState();
    }

    public boolean hasError() {
        return player.getPlaybackError() != null;
    }

    public boolean isPlaying() {
        return getState() == Player.STATE_READY && player.getPlayWhenReady();
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
        return getState() == Player.STATE_READY && !player.getPlayWhenReady();
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
        //player.prepare();
        //player.setPlayWhenReady(true);
    }


    private void setBeforeListeners(Player player) {
        for(int i = 0; i < mListeners.size(); i++) {
            player.addListener(mListeners.get(i));
        }
    }

    private void initializePlayer(Song song) {
        if (player != null) {
            player.release();
        }

        Uri[] uris;
        LogHelper.d(TAG, song.getUrl());
        uris = new Uri[]  { Uri.parse(song.getUrl())};

        if (!Util.checkCleartextTrafficPermitted(uris)) {
            LogHelper.e(TAG, "checkCleartextTrafficPermitted return false");
            return;
        }

        boolean preferExtensionDecoders = false;
        @DefaultRenderersFactory.ExtensionRendererMode int extensionRendererMode =
                ((CustomApplication) CustomApplication.get()).useExtensionRenderers()
                        ? (preferExtensionDecoders ? DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER
                        : DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON)
                        : DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF;
        DefaultRenderersFactory renderersFactory =
                new DefaultRenderersFactory(context, extensionRendererMode);

        trackSelector = new DefaultTrackSelector(trackSelectionFactory);
        trackSelector.setParameters(trackSelectorParameters);
        lastSeenTrackGroupArray = null;

        DefaultDrmSessionManager<FrameworkMediaCrypto> drmSessionManager = null;
        player =
                ExoPlayerFactory.newSimpleInstance(
                        /* context= */ context, renderersFactory, trackSelector, drmSessionManager);
        setBeforeListeners(player);
        player.addListener(this);
        player.setPlayWhenReady(true);
        player.addAnalyticsListener(new EventLogger(trackSelector));
        //playerView.setPlayer(player);

        //player.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
        //playerView.setPlaybackPreparer(this);

        //TODO: 关掉控制器
        //playerView.setUseController(false);
        //debugViewHelper = new DebugTextViewHelper(player, debugTextView);
        //debugRootView.setVisibility(View.INVISIBLE);
        //debugViewHelper.start();

        MediaSource[] mediaSources = new MediaSource[uris.length];
        for (int i = 0; i < uris.length; i++) {
            mediaSources[i] = buildMediaSource(uris[i]);
        }
        mediaSource =
                mediaSources.length == 1 ? mediaSources[0] : new ConcatenatingMediaSource(mediaSources);

        player.prepare(mediaSource);
        //player.prepare(mediaSource, !haveStartPosition, false);
        player.setPlayWhenReady(true);
    }

    private MediaSource buildMediaSource(Uri uri) {
        return buildMediaSource(uri, null);
    }

    @SuppressWarnings("unchecked")
    private MediaSource buildMediaSource(Uri uri, @Nullable String overrideExtension) {
        @C.ContentType int type = Util.inferContentType(uri, overrideExtension);
        switch (type) {
            case C.TYPE_DASH:
                return new DashMediaSource.Factory(dataSourceFactory)
                        .setManifestParser(
                                new FilteringManifestParser<>(new DashManifestParser(), getOfflineStreamKeys(uri)))
                        .createMediaSource(uri);
            case C.TYPE_SS:
                return new SsMediaSource.Factory(dataSourceFactory)
                        .setManifestParser(
                                new FilteringManifestParser<>(new SsManifestParser(), getOfflineStreamKeys(uri)))
                        .createMediaSource(uri);
            case C.TYPE_HLS:
                return new HlsMediaSource.Factory(dataSourceFactory)
                        .setPlaylistParserFactory(
                                new DefaultHlsPlaylistParserFactory(getOfflineStreamKeys(uri)))
                        .createMediaSource(uri);
            case C.TYPE_OTHER:
                return new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
            default: {
                throw new IllegalStateException("Unsupported type: " + type);
            }
        }
    }

    private List<StreamKey> getOfflineStreamKeys(Uri uri) {

        return   ((CustomApplication)CustomApplication.get()).getDownloadTracker().getOfflineStreamKeys(uri);
    }


    public void pause() {
        LogHelper.d(TAG, "pause");
        if (player != null) {
            player.setPlayWhenReady(false);
        }
    }

    private boolean isLive(Song song) {
        return song.getUrl() != null && song.getUrl().endsWith(".m3u8");
    }

    public void resume() {
        LogHelper.d(TAG, "resume");
        if ( isLive(getCurrentPlaySong()) ) {
            int state = getState();
            if (isPause() || state == Player.STATE_BUFFERING || state == Player.STATE_READY) {
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
                if (isLive(getCurrentPlaySong())){
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



    private void createPlayer(Song song) {
        initializePlayer(song);
        /*
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
        */
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
    public void onPlayerError(ExoPlaybackException error) {

    }

    public static interface MusicPlayerControlListener {
        void onClickNext();
        void onClickPrev();
    }
}

