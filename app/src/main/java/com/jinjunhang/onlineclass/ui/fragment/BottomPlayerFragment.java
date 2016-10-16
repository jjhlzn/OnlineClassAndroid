package com.jinjunhang.onlineclass.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.jinjunhang.onlineclass.ui.lib.BottomPlayerController;
import com.jinjunhang.player.MusicPlayer;

/**
 * Created by lzn on 16/6/21.
 */
public class BottomPlayerFragment extends BaseFragment implements ExoPlayer.EventListener{
    protected BottomPlayerController mPlayerController;
    protected MusicPlayer mMusicPlayer = MusicPlayer.getInstance(getActivity());

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPlayerController = BottomPlayerController.getInstance(getActivity());
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

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
