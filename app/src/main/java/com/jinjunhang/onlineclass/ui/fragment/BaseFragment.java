package com.jinjunhang.onlineclass.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.ExoPlayer;
import com.gyf.barlibrary.ImmersionBar;
import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.ui.activity.BaseMusicSingleFragmentActivity;
import com.jinjunhang.player.MusicPlayer;

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
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMusicPlayer != null) {
            mMusicPlayer.addListener(this);
            LogHelper.d(TAG, "onResume(): add music player listener -> " + this);
        }
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
         if (getUserVisibleHint()) {
             Utils.updateNavigationBarButton(getActivity());
         }
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
