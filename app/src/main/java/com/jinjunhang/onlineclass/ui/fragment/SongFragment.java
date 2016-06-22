package com.jinjunhang.onlineclass.ui.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.exoplayer.ExoPlayer;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.framework.service.ServerResponse;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.Comment;
import com.jinjunhang.onlineclass.model.Song;
import com.jinjunhang.onlineclass.service.GetSongCommentsRequest;
import com.jinjunhang.onlineclass.service.GetSongCommentsResponse;
import com.jinjunhang.onlineclass.ui.cell.CommentCell;
import com.jinjunhang.onlineclass.ui.cell.ListViewCell;
import com.jinjunhang.onlineclass.ui.cell.PlayerCell;
import com.jinjunhang.onlineclass.ui.cell.SectionSeparatorCell;
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
        setTitle(mSong.getName());
        mMusicPlayer = MusicPlayer.getInstance(getActivity());

        View v = inflater.inflate(R.layout.activity_fragment_play_song, container, false);
        //让下拉刷新失效
        v.findViewById(R.id.swipe_refresh_layout).setEnabled(false);

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

        new GetSongCommentsTask().execute();
        return v;
    }


    private void createAdapter() {
        List<ListViewCell> cells = new ArrayList<>();
        mPlayerCell = new PlayerCell(getActivity());
        mPlayerCell.setSong(mSong);
        cells.add(mPlayerCell);

        cells.add(new SectionSeparatorCell(getActivity()));

        mAdapter = new SongFragmentAdapter(cells);
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

    private class GetSongCommentsTask extends AsyncTask<Void, Void, GetSongCommentsResponse> {
        @Override
        protected GetSongCommentsResponse doInBackground(Void... params) {
            GetSongCommentsRequest req = new GetSongCommentsRequest();
            req.setSong(mPlayerCell.getSong());
            return new BasicService().sendRequest(req);
        }

        @Override
        protected void onPostExecute(GetSongCommentsResponse resp) {
            if (resp.getStatus() != ServerResponse.SUCCESS) {
                return;
            }

            List<Comment> comments = getTop5(resp.getResultSet());

            updateListViewData(comments);
        }
    }

    private List<Comment> getTop5(List<Comment> comments) {
        if (comments.size() <= 5) {
            return comments;
        }
        List<Comment> top5 = new ArrayList<>();
        for (int i = 0; i < 5; i ++) {
            top5.add(comments.get(i));
        }
        return top5;
    }

    private void updateListViewData(List<Comment> comments) {
        List<ListViewCell> cells = new ArrayList<>();
        cells.add(mPlayerCell);
        cells.add(new SectionSeparatorCell(getActivity()));
        for (Comment comment : comments) {
            cells.add(new CommentCell(getActivity(), comment));
        }
        this.mAdapter.setCells(cells);
    }
}


