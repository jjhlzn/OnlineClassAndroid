package com.jinjunhang.onlineclass.ui.fragment.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.jinjunhang.framework.controller.PagableController;
import com.jinjunhang.framework.controller.SingleFragmentActivity;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.framework.service.PagedServerResponse;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.Album;
import com.jinjunhang.onlineclass.service.SearchRequest;
import com.jinjunhang.onlineclass.ui.activity.album.AlbumDetailActivity;
import com.jinjunhang.onlineclass.ui.activity.MainActivity;
import com.jinjunhang.onlineclass.ui.fragment.BottomPlayerFragment;
import com.jinjunhang.onlineclass.ui.fragment.album.AlbumDetailFragment;
import com.jinjunhang.onlineclass.ui.fragment.album.AlbumListAdapter;

import java.util.ArrayList;

/**
 * Created by lzn on 16/6/29.
 */
public class SearchResultFragment extends BottomPlayerFragment implements  SingleFragmentActivity.OnBackPressedListener,
        AbsListView.OnScrollListener {
    public final static String EXTRA_ALBUMTYPE = "extra_albumtype";
    public final static String EXTRA_KEYWORD = "EXTRA_Keyword";

    private final static String TAG = "SearchResultFragment";

    private PagableController mPagableController;

    private ListView mListView;
    private String mKeyword;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_fragment_pushdownrefresh;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        mKeyword = getActivity().getIntent().getStringExtra(EXTRA_KEYWORD);

        mListView = (ListView) v.findViewById(R.id.listView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Album album = (Album) (mPagableController.getPagableArrayAdapter().getItem(position));

                Intent i = new Intent(getActivity(), AlbumDetailActivity.class);
                i.putExtra(AlbumDetailFragment.EXTRA_ALBUM, album);
                startActivity(i);
            }
        });


        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)v.findViewById(R.id.swipe_refresh_layout);

        //设置PagableController
        mPagableController = new PagableController(getActivity(), mListView);
        AlbumListAdapter adapter = new AlbumListAdapter(mPagableController, new ArrayList<Album>());
        mPagableController.setSwipeRefreshLayout(swipeRefreshLayout);
        mPagableController.setPagableArrayAdapter(adapter);
        mPagableController.setPagableRequestHandler(new SearchCouseHanlder());
        mPagableController.setOnScrollListener(this);

        //((FrameLayout)v.findViewById(R.id.fragmentContainer)).addView(mPlayerController.getView());
        mPlayerController.attachToView((FrameLayout)v.findViewById(R.id.fragmentContainer));
        return v;
    }


    @Override
    public void doBack() {
        Intent i = new Intent(getActivity(), MainActivity.class);
        i.putExtra(MainActivity.EXTRA_TAB, 1);
        startActivity(i);
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //此方法不需要实现
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        mPagableController.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
    }

    private class SearchCouseHanlder implements PagableController.PagableRequestHandler {
        @Override
        public PagedServerResponse handle() {
            SearchRequest request = new SearchRequest();
            request.setKeyword(mKeyword);
            request.setPageIndex(mPagableController.getPageIndex());
            return new BasicService().sendRequest(request);
        }
    }





}