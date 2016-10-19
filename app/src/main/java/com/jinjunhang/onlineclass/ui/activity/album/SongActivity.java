package com.jinjunhang.onlineclass.ui.activity.album;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.jinjunhang.onlineclass.model.Song;
import com.jinjunhang.onlineclass.ui.activity.BaseMusicSingleFragmentActivity;
import com.jinjunhang.onlineclass.ui.activity.MainActivity;
import com.jinjunhang.onlineclass.ui.fragment.album.BaseSongFragment;
import com.jinjunhang.onlineclass.ui.fragment.album.CommonSongFragment;
import com.jinjunhang.onlineclass.ui.fragment.album.LiveAudioFragment;
import com.jinjunhang.onlineclass.ui.fragment.album.LiveSongFragment;
import com.jinjunhang.onlineclass.ui.fragment.album.LiveVideoFragment;
import com.jinjunhang.player.MusicPlayer;
import com.jinjunhang.framework.lib.LogHelper;

/**
 * Created by lzn on 16/6/13.
 */
public class SongActivity extends BaseMusicSingleFragmentActivity {

    private final static String TAG = LogHelper.makeLogTag(SongActivity.class);
    private Song song;

    @Override
    protected Fragment createFragment() {
        song = (Song)getIntent().getSerializableExtra(BaseSongFragment.EXTRA_SONG);

        if (song == null) {  //从通知栏过来
           song = MusicPlayer.getInstance(this).getCurrentPlaySong();
        }

        if (song == null) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            return null;
        }


        if (song.getSongType().getName().equals(Song.SongType.Video.getName())) {

            return new LiveVideoFragment();
        } else {
            if (song.isLive()) {
                return new LiveAudioFragment();
            } else {
                return new CommonSongFragment();
            }
        }
    }

    @Override
    protected boolean hasActionBar() {
        if (song != null)  {
            if (song.getSongType().getName().equals(Song.SongType.Video.getName())) {
                return false;
            }
        }

        return super.hasActionBar();
    }
}
