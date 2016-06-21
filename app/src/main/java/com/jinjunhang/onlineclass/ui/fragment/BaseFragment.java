package com.jinjunhang.onlineclass.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.ExoPlayer;
import com.jinjunhang.onlineclass.ui.lib.BottomPlayerController;
import com.jinjunhang.player.MusicPlayer;

/**
 * Created by lzn on 16/6/18.
 */
public class BaseFragment extends android.support.v4.app.Fragment implements ExoPlayer.Listener {

    protected BottomPlayerController mPlayerController;
    protected MusicPlayer mMusicPlayer = MusicPlayer.getInstance(getActivity());

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPlayerController = new BottomPlayerController(getActivity());
    }

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
        mMusicPlayer.addListener(mPlayerController);
        mPlayerController.updateView();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMusicPlayer.removeListener(mPlayerController);
        mPlayerController.updateView();
    }
}
