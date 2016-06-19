package com.jinjunhang.framework.controller;

import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.ExoPlayer;
import com.jinjunhang.player.MusicPlayer;

/**
 * Created by lzn on 16/6/18.
 */
public class BaseFragment extends android.support.v4.app.Fragment implements ExoPlayer.Listener {

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onPlayWhenReadyCommitted() {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onResume() {
        super.onResume();
        MusicPlayer.getInstance(getActivity()).addListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MusicPlayer.getInstance(getActivity()).removeListener(this);
    }
}
