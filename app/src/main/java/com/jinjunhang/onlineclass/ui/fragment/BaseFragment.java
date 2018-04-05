package com.jinjunhang.onlineclass.ui.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.ExoPlayer;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.ui.activity.BaseMusicSingleFragmentActivity;
import com.jinjunhang.onlineclass.ui.lib.BottomPlayerController;
import com.jinjunhang.player.MusicPlayer;

/**
 * Created by lzn on 16/6/18.
 */
public class BaseFragment extends android.support.v4.app.Fragment implements ExoPlayer.Listener  {


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
    public void onPlayWhenReadyCommitted() {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    protected void setTitle(String title) {
        ((BaseMusicSingleFragmentActivity)getActivity()).setActivityTitle(title);
    }

    public void changeActionBar() {

    }

    public void initView() {

    }


    protected void setLightStatusBar(View view, Activity activity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            activity.getWindow().setStatusBarColor(Color.WHITE);
        }
    }

}
