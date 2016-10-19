package com.jinjunhang.onlineclass.ui.fragment.album;

import android.view.View;

import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.LiveSong;
import com.jinjunhang.onlineclass.ui.cell.LiveSongListViewCellAdapter;
import com.jinjunhang.onlineclass.ui.cell.player.LiveAudioPlayerCell;
import com.jinjunhang.onlineclass.ui.cell.player.PlayerCell;

/**
 * Created by lzn on 2016/10/18.
 */

public class LiveAudioFragment extends LiveSongFragment {

    private static final String TAG = LogHelper.makeLogTag(LiveAudioFragment.class);

    @Override
    protected PlayerCell createPlayerCell() {
        return new LiveAudioPlayerCell(getActivity());
    }

    @Override
    protected void createAdapter(View container) {
        View playListView = container.findViewById(R.id.play_list_view);
        mPlayerCell = createPlayerCell();
        mPlayerCell.setPlayerListView(playListView);

        LiveSong song = (LiveSong) mMusicPlayer.getCurrentPlaySong();

        mAdapter = new LiveSongListViewCellAdapter(getActivity(), song, mPlayerCell);
        mPlayerCell.setAdapter(mAdapter);
        LogHelper.d(TAG, "mAdapter.size = " + mAdapter.getCount());
    }
}
