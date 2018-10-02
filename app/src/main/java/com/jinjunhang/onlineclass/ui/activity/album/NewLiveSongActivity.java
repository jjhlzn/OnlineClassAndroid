package com.jinjunhang.onlineclass.ui.activity.album;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.onlineclass.model.Song;
import com.jinjunhang.onlineclass.ui.activity.BaseMusicSingleFragmentActivity;
import com.jinjunhang.onlineclass.ui.activity.MainActivity;
import com.jinjunhang.onlineclass.ui.fragment.album.BaseSongFragment;
import com.jinjunhang.onlineclass.ui.fragment.album.CommonSongFragment;
import com.jinjunhang.onlineclass.ui.fragment.album.LiveSongFragment;
import com.jinjunhang.onlineclass.ui.fragment.album.NewLiveSongFragment;
import com.jinjunhang.player.MusicPlayer;

/**
 * Created by lzn on 16/6/13.
 */
public class NewLiveSongActivity extends BaseMusicSingleFragmentActivity {

    protected boolean hasActionBar() {
        return false;
    }


    @Override
    protected Fragment createFragment() {
        return new NewLiveSongFragment();

    }
   
}
