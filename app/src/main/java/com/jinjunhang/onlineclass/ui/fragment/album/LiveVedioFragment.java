package com.jinjunhang.onlineclass.ui.fragment.album;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.ui.cell.player.PlayerCell;
import com.jinjunhang.player.MusicPlayer;

/**
 * Created by lzn on 2016/10/13.
 */

public class LiveVedioFragment extends BaseSongFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_fragment_play_vedio, container, false);

        SimpleExoPlayerView playerView = (SimpleExoPlayerView)v.findViewById(R.id.player_view);

        mMusicPlayer = MusicPlayer.getInstance(getActivity());
        playerView.setPlayer(mMusicPlayer.getSimpleExoPlayer());

        return v;
    }


    @Override
    protected PlayerCell createPlayerCell() {
        return null;
    }

    @Override
    protected View.OnClickListener createSendOnClickListener() {
        return null;
    }
}
