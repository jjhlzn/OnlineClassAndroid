package com.jinjunhang.onlineclass.ui.fragment.album;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jinjunhang.framework.controller.PagableController;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.framework.service.PagedServerResponse;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.Album;
import com.jinjunhang.onlineclass.model.Comment;
import com.jinjunhang.onlineclass.model.Song;
import com.jinjunhang.onlineclass.service.GetSongCommentsRequest;
import com.jinjunhang.onlineclass.ui.activity.album.AlbumDetailActivity;
import com.jinjunhang.onlineclass.ui.cell.comment.CommentCell;
import com.jinjunhang.onlineclass.ui.fragment.BaseFragment;
import com.jinjunhang.onlineclass.ui.lib.BaseListViewOnItemClickListener;
import com.jinjunhang.framework.lib.LogHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jjh on 2016-7-3.
 */
public class CommentListFragment extends BaseFragment implements AbsListView.OnScrollListener {
    public static final String EXTRA_SONG = "EXTRA_SONG";
    private static final String TAG = LogHelper.makeLogTag(CommentListFragment.class);

    private PagableController mPagableController;

    private ListView mListView;
    private Song mSong;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_fragment_pushdownrefresh, container, false);

        mListView = (ListView) v.findViewById(R.id.listView);

        //去掉列表的分割线
        mListView.setDividerHeight(0);
        mListView.setDivider(null);

        mListView.setOnItemClickListener(new BaseListViewOnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                super.onItemClick(parent, view, position, id);

                Album album = (Album) (mPagableController.getPagableArrayAdapter().getItem(position));
                Intent i = new Intent(getActivity(), AlbumDetailActivity.class);
                i.putExtra(AlbumDetailFragment.EXTRA_ALBUM, album);
                startActivity(i);
            }
        });

        mSong = (Song) getActivity().getIntent().getSerializableExtra(EXTRA_SONG);

        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)v.findViewById(R.id.swipe_refresh_layout);

        //设置PagableController
        mPagableController = new PagableController(getActivity(), mListView);
        LogHelper.d(TAG, "Song = " + mSong.getName());

        CommentListAdpater adapter = new CommentListAdpater(mPagableController, new ArrayList<Comment>());

       // mPagableController.setSwipeRefreshLayout(swipeRefreshLayout);
        mPagableController.setPagableArrayAdapter(adapter);
        mPagableController.setPagableRequestHandler(new CommentListHanler());
        mPagableController.setOnScrollListener(this);

        return v;
    }

    private class CommentListAdpater extends PagableController.PagableArrayAdapter<Comment> {
        public CommentListAdpater(PagableController pagableController, List<Comment> dataSet) {
            super(pagableController, dataSet);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Comment item = getItem(position);
            CommentCell cell = new CommentCell(getActivity(), item);
            return cell.getView();
        }
    }

    private class CommentListHanler implements PagableController.PagableRequestHandler {
        @Override
        public PagedServerResponse handle() {
            GetSongCommentsRequest req = new GetSongCommentsRequest();
            req.setSong(mSong);
            req.setPageIndex(mPagableController.getPageIndex());
            return new BasicService().sendRequest(req);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mPagableController.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
    }
}
