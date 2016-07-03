package com.jinjunhang.onlineclass.ui.fragment.album;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.Song;
import com.jinjunhang.onlineclass.ui.cell.ListViewCell;
import com.jinjunhang.onlineclass.ui.cell.WideSectionSeparatorCell;
import com.jinjunhang.onlineclass.ui.cell.player.PlayerCell;
import com.jinjunhang.onlineclass.ui.cell.SectionSeparatorCell;
import com.jinjunhang.onlineclass.ui.fragment.BaseFragment;
import com.jinjunhang.player.MusicPlayer;
import com.jinjunhang.player.utils.LogHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jjh on 2016-7-2.
 */
public abstract class BaseSongFragment  extends BaseFragment {
    private final static String TAG = LogHelper.makeLogTag(BaseSongFragment.class);
    public final static String EXTRA_SONG = "SongFragement_song";

    protected Song mSong;
    protected MusicPlayer mMusicPlayer;

    protected ListView mListView;
    protected SongFragmentAdapter mAdapter;
    List<ListViewCell> mCells;
    protected PlayerCell mPlayerCell;

    abstract protected  PlayerCell createPlayerCell();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        mSong = (Song)getActivity().getIntent().getSerializableExtra(EXTRA_SONG);
        setTitle(mSong.getName());
        mMusicPlayer = MusicPlayer.getInstance(getActivity());

        View v = inflater.inflate(R.layout.activity_fragment_play_song, container, false);

        View commentTip = v.findViewById(R.id.bottom_comment_tip);
        final View commentWindow = v.findViewById(R.id.bottom_comment);
        final EditText editText = (EditText) v.findViewById(R.id.comment_edittext);
        commentTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentWindow.setVisibility(View.VISIBLE);
                editText.requestFocus();
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        mListView = (ListView) v.findViewById(R.id.listView);

        //去掉列表的分割线
        mListView.setDividerHeight(0);
        mListView.setDivider(null);

        createAdapter();
        mListView.setAdapter(mAdapter);

        //设置发送按钮
        TextView sendButton = (TextView)v.findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogHelper.d(TAG, "send button pressed");
            }
        });

        //设置emoji切换按钮

        Utils.setupUI4HideKeybaord(v, getActivity());
        return v;
    }

    private void createAdapter() {
        mCells = new ArrayList<>();
        mPlayerCell = createPlayerCell();
        mPlayerCell.setSong(mSong);
        mCells.add(mPlayerCell);
        mCells.add(new WideSectionSeparatorCell(getActivity()));

        mAdapter = new SongFragmentAdapter(mCells);
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

    protected class SongFragmentAdapter extends ArrayAdapter<ListViewCell> {
        private List<ListViewCell> mCells;

        public SongFragmentAdapter(List<ListViewCell> cells) {
            super(getActivity(), 0, cells);
            mCells = cells;
        }

        public void setCells(List<ListViewCell> cells) {
            mCells = cells;
            notifyDataSetChanged();
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

