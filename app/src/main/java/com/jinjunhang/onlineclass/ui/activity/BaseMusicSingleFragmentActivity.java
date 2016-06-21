package com.jinjunhang.onlineclass.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jinjunhang.framework.controller.SingleFragmentActivity;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.ui.lib.BottomPlayerController;
import com.jinjunhang.player.MusicPlayer;

/**
 * Created by lzn on 16/6/17.
 */
public abstract class BaseMusicSingleFragmentActivity extends SingleFragmentActivity {

    protected BottomPlayerController mPlayerController;
    protected MusicPlayer mMusicPlayer = MusicPlayer.getInstance(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPlayerController = BottomPlayerController.getInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMusicPlayer.addListener(mPlayerController);
        mPlayerController.updateView();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMusicPlayer.removeListener(mPlayerController);
        mPlayerController.updateView();
    }

}
