package com.jinjunhang.onlineclass.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.exoplayer.ExoPlayer;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.Song;
import com.jinjunhang.onlineclass.ui.cell.ListViewCell;
import com.jinjunhang.onlineclass.ui.cell.PlayerCell;
import com.jinjunhang.player.MusicPlayer;
import com.jinjunhang.player.utils.LogHelper;
import com.jinjunhang.player.utils.StatusHelper;
import com.jinjunhang.player.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by lzn on 16/6/13.
 */
public class SongFragment extends BaseFragment {
    private final static String TAG = LogHelper.makeLogTag(SongFragment.class);
    public final static String EXTRA_SONG = "SongFragement_song";

    private Song mSong;

    private MusicPlayer mMusicPlayer;


    private ListView mListView;
    private SongFragmentAdapter mAdapter;
    private PlayerCell mPlayerCell;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        mSong = (Song)getActivity().getIntent().getSerializableExtra(EXTRA_SONG);
        mMusicPlayer = MusicPlayer.getInstance(getActivity());

        View v = inflater.inflate(R.layout.activity_fragment_pushdownrefresh, container, false);
        //让下拉刷新失效
        v.findViewById(R.id.swipe_refresh_layout).setEnabled(false);

        mListView = (ListView) v.findViewById(R.id.listView);
        //去掉列表的分割线
        mListView.setDividerHeight(0);
        mListView.setDivider(null);

        List<ListViewCell> cells = new ArrayList<>();
        mPlayerCell = new PlayerCell(getActivity());
        cells.add(mPlayerCell);

        mAdapter = new SongFragmentAdapter(cells);
        mListView.setAdapter(mAdapter);

        return v;
    }


    @Override
    public void onResume() {
        super.onResume();
        mMusicPlayer.addListener(mPlayerCell);
    }

    @Override
    public void onStop() {
        super.onStop();
        mMusicPlayer.removeListener(mPlayerCell);
    }

    private class SongFragmentAdapter extends ArrayAdapter<ListViewCell> {
        private List<ListViewCell> mCells;

        public SongFragmentAdapter(List<ListViewCell> cells) {
            super(getActivity(), 0, cells);
            mCells = cells;
        }

        @Override
        public int getCount() {
            return mCells.size();
        }

        @Override
        public ListViewCell getItem(int position) {
            return mCells.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ListViewCell item = getItem(position);
            return item.getView();
        }
    }
}


