package com.jinjunhang.onlineclass.ui.activity.album;

import android.support.v4.app.Fragment;

import com.jinjunhang.onlineclass.model.Song;
import com.jinjunhang.onlineclass.ui.activity.BaseMusicSingleFragmentActivity;
import com.jinjunhang.onlineclass.ui.fragment.album.BaseSongFragment;
import com.jinjunhang.onlineclass.ui.fragment.album.CommonSongFragment;
import com.jinjunhang.onlineclass.ui.fragment.album.LiveSongFragment;

/**
 * Created by lzn on 16/6/13.
 */
public class SongActivity extends BaseMusicSingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        Song song = (Song)getIntent().getSerializableExtra(BaseSongFragment.EXTRA_SONG);
        if (song.isLive()) {
            return new LiveSongFragment();
        } else {
            return new CommonSongFragment();
        }
    }
}
