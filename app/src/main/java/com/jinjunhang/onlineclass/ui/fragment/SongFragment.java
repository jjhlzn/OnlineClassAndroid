package com.jinjunhang.onlineclass.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.Song;
import com.jinjunhang.player.utils.LogHelper;

/**
 * Created by lzn on 16/6/13.
 */
public class SongFragment extends android.support.v4.app.Fragment {
    private final static String TAG = LogHelper.makeLogTag(SongFragment.class);

    public final static String EXTRA_SONG = "SongFragement_song";

    private Song mSong;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mSong = (Song)getActivity().getIntent().getSerializableExtra(EXTRA_SONG);

        View v = inflater.inflate(R.layout.activity_fragment_play_song, container, false);

        return v;
    }
}
