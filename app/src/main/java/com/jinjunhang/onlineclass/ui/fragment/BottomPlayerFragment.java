package com.jinjunhang.onlineclass.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

//import com.google.android.exoplayer.ExoPlaybackException;
//import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;
import com.jinjunhang.onlineclass.ui.lib.BottomPlayerController;
import com.jinjunhang.player.MusicPlayer;



/**
 * Created by lzn on 16/6/21.
 */
public class BottomPlayerFragment extends BaseFragment implements Player.EventListener{
    protected BottomPlayerController mPlayerController;
    protected MusicPlayer mMusicPlayer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MusicPlayer mMusicPlayer = MusicPlayer.getInstance(getActivity().getApplicationContext());
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
        /*
        if (mMusicPlayer != null) {
            mMusicPlayer.addListener(mPlayerController);
            mPlayerController.updateView();
        } */
    }

    @Override
    public void onPause() {
        super.onPause();
        /*
        if (mMusicPlayer != null) {
            mMusicPlayer.removeListener(mPlayerController);
            mPlayerController.updateView();
        } */
    }
}
