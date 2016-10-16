/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jinjunhang.player.playback.exo.player;

import android.content.Context;
import android.content.Intent;
import android.media.MediaCodec.CryptoException;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Surface;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.drm.FrameworkMediaDrm;
import com.google.android.exoplayer2.drm.HttpMediaDrmCallback;
import com.google.android.exoplayer2.drm.StreamingDrmSessionManager;
import com.google.android.exoplayer2.drm.UnsupportedDrmException;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer.DecoderInitializationException;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil.DecoderQueryException;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector.MappedTrackInfo;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelections;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.DebugTextViewHelper;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;
import com.jinjunhang.onlineclass.model.Song;
import com.jinjunhang.onlineclass.ui.lib.CustomApplication;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A wrapper around {@link ExoPlayer} that provides a higher level interface. It can be prepared
 * with one of a number of {@link} classes to suit different use cases (e.g. DASH,
 * SmoothStreaming and so on).
 */
public class SimpleExoPlayerWrapper
        implements
        ExoPlayer.EventListener,
        DefaultBandwidthMeter.EventListener,
        StreamingDrmSessionManager.EventListener
{



    // Constants pulled into this class for convenience.
    public static final int STATE_IDLE = ExoPlayer.STATE_IDLE;
    //public static final int STATE_PREPARING = ExoPlayer.STATE_PREPARING;
    public static final int STATE_BUFFERING = ExoPlayer.STATE_BUFFERING;
    public static final int STATE_READY = ExoPlayer.STATE_READY;
    public static final int STATE_ENDED = ExoPlayer.STATE_ENDED;

    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();


    private SimpleExoPlayer player;
    private DataSource.Factory mediaDataSourceFactory;
    private final Handler mainHandler;
    private MappingTrackSelector trackSelector;

    private boolean shouldAutoPlay;
    private boolean shouldRestorePosition;
    private int playerWindow;
    private long playerPosition;


    private int rendererBuildingState;
    private int lastReportedPlaybackState;
    private boolean lastReportedPlayWhenReady;

    private EventLogger eventLogger;

    private Context mContext;


    public SimpleExoPlayer getPlayer() {
        return player;
    }

    public SimpleExoPlayerWrapper() {
        mainHandler = new Handler();
        mContext = CustomApplication.get();
        mediaDataSourceFactory = buildDataSourceFactory(true);
        initializePlayer();
        /*
        player = ExoPlayer.Factory.newInstance(RENDERER_COUNT, 1000, 5000);
        player.addListener(this);
        mainHandler = new Handler();
        listeners = new CopyOnWriteArrayList<>();
        lastReportedPlaybackState = STATE_IDLE;
        rendererBuildingState = RENDERER_BUILDING_STATE_IDLE;
        // Disable text initially.
        player.setSelectedTrack(TYPE_TEXT, TRACK_DISABLED);
        */
    }

    public SimpleExoPlayerWrapper(Song song) {
        mainHandler = new Handler();
        mContext = CustomApplication.get();
        mediaDataSourceFactory = buildDataSourceFactory(true);
        initializePlayer();
        setMediaSource(song);
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
            player = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector, new DefaultLoadControl(),
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
        }

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
    private DataSource.Factory buildDataSourceFactory(boolean useBandwidthMeter) {
        return ((CustomApplication)CustomApplication.get()).buildDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null);
    }

    public void addListener(ExoPlayer.EventListener listener) {
        player.addListener(listener);
    }

    public void removeListener(ExoPlayer.EventListener listener) {
        player.removeListener(listener);
    }


    public void prepare() {
        /*
        if (rendererBuildingState == RENDERER_BUILDING_STATE_BUILT) {
            player.stop();
        }
        rendererBuilder.cancel();
        videoFormat = null;
        videoRenderer = null;
        rendererBuildingState = RENDERER_BUILDING_STATE_BUILDING;
        maybeReportPlayerState();
        rendererBuilder.buildRenderers(this); */
    }

    public void setPlayWhenReady(boolean playWhenReady) {
        player.setPlayWhenReady(playWhenReady);
    }

    public void seekTo(long positionMs) {
        player.seekTo(positionMs);
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
            player.release();
            player = null;
            trackSelector = null;
            eventLogger = null;
        }
    }

    public int getPlaybackState() {
        int playerState = player.getPlaybackState();
        return playerState;
    }

    public long getCurrentPosition() {
        return player.getCurrentPosition();
    }

    public long getDuration() {
        return player.getDuration();
    }

    public boolean getPlayWhenReady() {
        return player.getPlayWhenReady();
    }

    @Override
    public void onDrmKeysLoaded() {
        // Do nothing.
    }

    @Override
    public void onDrmSessionManagerError(Exception e) {

    }

    @Override
    public void onBandwidthSample(int elapsedMs, long bytes, long bitrate) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }
}
