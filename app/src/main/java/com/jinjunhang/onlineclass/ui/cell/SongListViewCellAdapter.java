package com.jinjunhang.onlineclass.ui.cell;

import android.app.Activity;

import com.jinjunhang.onlineclass.ui.fragment.album.LiveSongFragment;

import java.util.List;

/**
 * Created by jjh on 2016-7-4.
 */
public class SongListViewCellAdapter extends ListViewCellAdapter {

    public SongListViewCellAdapter(Activity activity, List<ListViewCell> cells) {
        super(activity, cells);
    }

    public SongListViewCellAdapter(Activity activity) {
        super(activity);
    }

    @Override
    public int getCount() {
        //if (super.getCount() > LiveSongFragment.MAX_COMMENT_COUNT) {
        //    return LiveSongFragment.MAX_COMMENT_COUNT;
        //}
        return super.getCount();
    }
}
