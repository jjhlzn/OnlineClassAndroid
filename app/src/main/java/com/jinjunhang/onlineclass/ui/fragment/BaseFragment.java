package com.jinjunhang.onlineclass.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.ui.activity.BaseMusicSingleFragmentActivity;
import com.jinjunhang.onlineclass.ui.lib.BottomPlayerController;
import com.jinjunhang.player.MusicPlayer;

/**
 * Created by lzn on 16/6/18.
 */
public class BaseFragment extends android.support.v4.app.Fragment implements ExoPlayer.EventListener  {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MusicPlayer.getInstance(getActivity()).addListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        MusicPlayer.getInstance(getActivity()).removeListener(this);
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

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

    protected void setTitle(String title) {
        ((BaseMusicSingleFragmentActivity)getActivity()).setActivityTitle(title);
    }

}
