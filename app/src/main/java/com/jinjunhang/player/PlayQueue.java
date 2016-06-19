package com.jinjunhang.player;

import com.jinjunhang.onlineclass.model.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzn on 16/6/19.
 */
public class PlayQueue {

    private List<Song> mSongs;
    private int currentIndex = -1;

    public PlayQueue() {
        this(new ArrayList<Song>());
    }

    public PlayQueue(List<Song> songs) {
        mSongs = songs;
    }




}
