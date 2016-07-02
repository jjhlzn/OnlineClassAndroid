package com.jinjunhang.onlineclass.ui.fragment.album;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.framework.service.ServerResponse;
import com.jinjunhang.onlineclass.model.Comment;
import com.jinjunhang.onlineclass.service.GetSongCommentsRequest;
import com.jinjunhang.onlineclass.service.GetSongCommentsResponse;
import com.jinjunhang.onlineclass.ui.cell.CommentCell;
import com.jinjunhang.onlineclass.ui.cell.ListViewCell;
import com.jinjunhang.onlineclass.ui.cell.player.PlayerCell;
import com.jinjunhang.onlineclass.ui.cell.SectionSeparatorCell;
import com.jinjunhang.player.utils.LogHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzn on 16/6/13.
 */
public class CommonSongFragment extends BaseSongFragment {
    private final static String TAG = LogHelper.makeLogTag(CommonSongFragment.class);
    @Override
    protected PlayerCell createPlayerCell() {
        return new PlayerCell(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        new GetSongCommentsTask().execute();
        return v;
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


