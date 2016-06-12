package com.jinjunhang.onlineclass.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jinjunhang.framework.controller.SingleFragmentActivity;
import com.jinjunhang.framework.service.PagedServerResponse;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.Album;
import com.jinjunhang.onlineclass.model.AlbumType;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.onlineclass.service.GetAlbumsRequest;
import com.jinjunhang.framework.controller.PagableController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzn on 16/6/10.
 *
 * 改Activity加载对应类型的所有的Album，需要有分页支持
 *
 */
public class AlbumListFragment extends android.support.v4.app.Fragment implements  SingleFragmentActivity.OnBackPressedListener,
        AbsListView.OnScrollListener {
    public final static String EXTRA_ALBUMTYPE = "extra_albumtype";

    private final static String TAG = "AlbumListFragment";

    private PagableController mPagableController;

    private AlbumType mAlbumType;
    private ListView mListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_fragment_pushdownrefresh, container, false);

        mListView = (ListView) v.findViewById(R.id.listView);

        mAlbumType = (AlbumType)getActivity().getIntent().getSerializableExtra(EXTRA_ALBUMTYPE);

        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)v.findViewById(R.id.swipe_refresh_layout);

        //设置PagableController
        mPagableController = new PagableController(getActivity(), mListView);
        AlbumListAdapter adapter = new AlbumListAdapter(mPagableController, new ArrayList<Album>());
        mPagableController.setSwipeRefreshLayout(swipeRefreshLayout);
        mPagableController.setPagableArrayAdapter(adapter);
        mPagableController.setPagableRequestHandler(new AlbumListHanlder());
        mPagableController.setOnScrollListener(this);

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

    private class AlbumListHanlder implements PagableController.PagableRequestHandler {
        @Override
        public PagedServerResponse handle() {
            GetAlbumsRequest request = new GetAlbumsRequest(mAlbumType);
            return new BasicService().sendRequest(request);
        }
    }

    private class AlbumListAdapter extends PagableController.PagableArrayAdapter<Album> {
        public AlbumListAdapter(PagableController pagableController, List<Album> dataSet){
            super(pagableController, dataSet);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_album, null);
            }

            Album album = getItem(position);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.album_list_item_image);
            Glide.with(AlbumListFragment.this).load(album.getImage()).into(imageView);


            TextView nameTextView = (TextView) convertView.findViewById(R.id.album_list_item_name);
            nameTextView.setText(album.getName());

            TextView authorTextView = (TextView) convertView.findViewById(R.id.album_list_item_author);
            authorTextView.setText(album.getAuthor());

            TextView listenTextView = (TextView) convertView.findViewById(R.id.album_list_item_listernCountAndCount);
            listenTextView.setText(album.getListenCount()+ ", " + album.getCount() + "集");

            return convertView;
        }
    }



}


