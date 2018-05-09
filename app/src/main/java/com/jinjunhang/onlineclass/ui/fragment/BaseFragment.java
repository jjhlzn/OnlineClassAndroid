package com.jinjunhang.onlineclass.ui.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.ExoPlayer;
import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.ui.activity.BaseMusicSingleFragmentActivity;
import com.jinjunhang.player.MusicPlayer;
import com.jinjunhang.player.utils.StatusHelper;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by lzn on 16/6/18.
 */
public class BaseFragment extends android.support.v4.app.Fragment implements ExoPlayer.Listener  {
    private static  final String TAG = LogHelper.makeLogTag(BaseFragment.class);
    protected  MusicPlayer mMusicPlayer;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMusicPlayer = MusicPlayer.getInstance(getActivity().getApplicationContext());

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mMusicPlayer != null) {
            mMusicPlayer.removeListener(this);
            Utils.updateNavigationBarButton(getActivity());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMusicPlayer != null) {
            mMusicPlayer.addListener(this);
            Utils.updateNavigationBarButton(getActivity());
        }
        Utils.setNavigationBarMusicButton(getActivity());
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
         Utils.updateNavigationBarButton(getActivity());
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
