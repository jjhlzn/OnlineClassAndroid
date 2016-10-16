package com.jinjunhang.onlineclass.ui.activity.album;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.jinjunhang.onlineclass.model.Song;
import com.jinjunhang.onlineclass.ui.activity.BaseMusicSingleFragmentActivity;
import com.jinjunhang.onlineclass.ui.activity.MainActivity;
import com.jinjunhang.onlineclass.ui.fragment.album.BaseSongFragment;
import com.jinjunhang.onlineclass.ui.fragment.album.CommonSongFragment;
import com.jinjunhang.onlineclass.ui.fragment.album.LiveSongFragment;
import com.jinjunhang.onlineclass.ui.fragment.album.LiveVedioFragment;
import com.jinjunhang.player.MusicPlayer;
import com.jinjunhang.framework.lib.LogHelper;

/**
 * Created by lzn on 16/6/13.
 */
public class SongActivity extends BaseMusicSingleFragmentActivity {

    private final static String TAG = LogHelper.makeLogTag(SongActivity.class);

    @Override
    protected Fragment createFragment() {
        Song song = (Song)getIntent().getSerializableExtra(BaseSongFragment.EXTRA_SONG);
       // return new SongFragment();
        if (song == null) {  //从通知栏过来
           song = MusicPlayer.getInstance(this).getCurrentPlaySong();
        }

        if (song == null) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            return null;
        }

        return new LiveVedioFragment();
        /*
        if (song.isLive()) {
            return new LiveSongFragment();
        } else {
            return new CommonSongFragment();
        } */
    }
   
}
