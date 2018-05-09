package com.jinjunhang.onlineclass.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.jinjunhang.onlineclass.ui.lib.BottomPlayerController;
import com.jinjunhang.player.MusicPlayer;

/**
 * Created by lzn on 16/6/21.
 */
public class BaseMusicActivity extends AppCompatActivity {

    protected BottomPlayerController mPlayerController;
    protected MusicPlayer mMusicPlayer = MusicPlayer.getInstance(this.getApplicationContext());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPlayerController = BottomPlayerController.getInstance(this);
        getSupportActionBar().setElevation(0);
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
