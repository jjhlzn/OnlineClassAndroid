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

    private final static String TAG = LogHelper.makeLogTag(NewLiveSongActivity.class);

    protected boolean hasActionBar() {
        return false;
    }


    @Override
    protected Fragment createFragment() {
        return new NewLiveSongFragment();
        /*
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

        if (song.isLive()) {
            return new LiveSongFragment();
        } else {
            return new CommonSongFragment();
        }*/
    }
   
}
