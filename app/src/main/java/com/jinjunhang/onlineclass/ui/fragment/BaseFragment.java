package com.jinjunhang.onlineclass.ui.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.ExoPlayer;
import com.gyf.barlibrary.ImmersionBar;
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
    protected ImmersionBar mImmersionBar;
    protected View mView;

    protected boolean isNeedTopPadding() {
        return true;
    }

    protected int getLayoutId() {
        return R.layout.activity_fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = inflater.inflate(getLayoutId(), container, false);
        if (isNeedTopPadding()) {
            float scale = getResources().getDisplayMetrics().density;
            int dpAsPixels = (int) (72 * scale + 0.5f);

            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mView.getLayoutParams();
            params.topMargin = dpAsPixels;
        }

        mMusicPlayer = MusicPlayer.getInstance(getActivity().getApplicationContext());
        if (isImmersionBarEnabled()) {
            initImmersionBar();
            if (isCompatibleActionBar()) {
                ImmersionBar.with(this).statusBarColor(R.color.white).statusBarDarkFont(true).init();
            }
        }
        return mView;
    }

    protected boolean isCompatibleActionBar() {
        return true;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mMusicPlayer != null) {
            mMusicPlayer.removeListener(this);
            //Utils.updateNavigationBarButton(getActivity());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMusicPlayer != null) {
            mMusicPlayer.addListener(this);
            //Utils.updateNavigationBarButton(getActivity());
        }
        //Utils.updateNavigationBarButton(getActivity());
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

    public Toolbar getToolBar() {
        return null;
    }

    protected void setLightStatusBar(View view, Activity activity) {

        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            activity.getWindow().setStatusBarColor(Color.WHITE);
        } */
    }

    public void updateMusicBtnState() {
        LogHelper.d(TAG, "action = " + getActivity() + ", updateMusicBtnState called");
        Utils.updateNavigationBarButton(getActivity());
    }


    /**
     * 是否在Fragment使用沉浸式
     *
     * @return the boolean
     */
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    /**
     * 初始化沉浸式
     */
    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.keyboardEnable(true).navigationBarWithKitkatEnable(false).init();
    }

}
