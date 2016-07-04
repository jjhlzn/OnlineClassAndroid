package com.jinjunhang.onlineclass.ui.activity.album;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.jinjunhang.onlineclass.model.Song;
import com.jinjunhang.onlineclass.ui.activity.BaseMusicSingleFragmentActivity;
import com.jinjunhang.onlineclass.ui.fragment.album.BaseSongFragment;
import com.jinjunhang.onlineclass.ui.fragment.album.CommonSongFragment;
import com.jinjunhang.onlineclass.ui.fragment.album.LiveSongFragment;
import com.jinjunhang.player.utils.LogHelper;

/**
 * Created by lzn on 16/6/13.
 */
public class SongActivity extends BaseMusicSingleFragmentActivity {

    private final static String TAG = LogHelper.makeLogTag(SongActivity.class);

    @Override
    protected Fragment createFragment() {
        Song song = (Song)getIntent().getSerializableExtra(BaseSongFragment.EXTRA_SONG);
       // return new SongFragment();

        if (song.isLive()) {
            return new LiveSongFragment();
        } else {
            return new CommonSongFragment();
        }
    }
   
}