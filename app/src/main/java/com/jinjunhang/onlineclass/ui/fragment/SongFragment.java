package com.jinjunhang.onlineclass.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.exoplayer.ExoPlayer;
import com.jinjunhang.framework.controller.BaseFragment;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.Song;
import com.jinjunhang.player.MusicPlayer;
import com.jinjunhang.player.utils.LogHelper;

/**
 * Created by lzn on 16/6/13.
 */
public class SongFragment extends BaseFragment {
    private final static String TAG = LogHelper.makeLogTag(SongFragment.class);

    public final static String EXTRA_SONG = "SongFragement_song";

    private Song mSong;

    private ImageButton mPlayButton;
    private ImageButton mPrevButton;
    private ImageButton mNextButton;
    private MusicPlayer mMusicPlayer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        mSong = (Song)getActivity().getIntent().getSerializableExtra(EXTRA_SONG);
        mMusicPlayer = MusicPlayer.getInstance(getActivity());

        View v = inflater.inflate(R.layout.activity_fragment_play_song, container, false);
        mPlayButton = (ImageButton) v.findViewById(R.id.player_play_button);
        mPrevButton = (ImageButton) v.findViewById(R.id.player_prev_button);
        mNextButton = (ImageButton) v.findViewById(R.id.player_next_button);

        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int state = MusicPlayer.getInstance(getActivity()).getState();
                LogHelper.d(TAG, "state = ", state);
                if (mMusicPlayer.isPlaying()) {
                    mMusicPlayer.pause();
                } else {
                    mMusicPlayer.resume();
                }

            }
        });

        mPrevButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mMusicPlayer.prev();
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mMusicPlayer.next();
            }
        });

        return v;
    }
}
