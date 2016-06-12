package com.jinjunhang.onlineclass.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jinjunhang.framework.controller.PagableController;
import com.jinjunhang.framework.controller.SingleFragmentActivity;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.framework.service.PagedServerResponse;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.Album;
import com.jinjunhang.onlineclass.model.Song;
import com.jinjunhang.onlineclass.service.GetAlbumSongsRequest;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzn on 16/6/12.
 */
public class AlbumDetailFragment extends android.support.v4.app.Fragment implements  SingleFragmentActivity.OnBackPressedListener,
        AbsListView.OnScrollListener {
    public final static String EXTRA_ALBUM = "extra_album";

    private PagableController mPagableController;
    private Album mAlbum;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_fragment_albumdetail, container, false);
        mAlbum = (Album) getActivity().getIntent().getSerializableExtra(EXTRA_ALBUM);

        ImageView imageView = (ImageView) v.findViewById(R.id.albumDtail_image);
        Glide.with(getActivity()).load(mAlbum.getImage()).into(imageView);

        ((TextView) v.findViewById(R.id.albumDetail_name)).setText(mAlbum.getName());
        ((TextView) v.findViewById(R.id.albumDetail_author)).setText(mAlbum.getAuthor());

        ((SingleFragmentActivity)getActivity()).setActivityTitle(mAlbum.getName());

        ListView listView = (ListView)v.findViewById(R.id.listView);

        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)v.findViewById(R.id.swipe_refresh_layout);

        mPagableController = new PagableController(getActivity(), listView);
        mPagableController.setSwipeRefreshLayout(swipeRefreshLayout);
        mPagableController.setPagableArrayAdapter(new AlbumDetailAdapter(mPagableController, new ArrayList<Song>()));
        mPagableController.setPagableRequestHandler(new AlbumDetailRequestHandler());
        mPagableController.setOnScrollListener(this);

        return v;
    }

    @Override
    public void doBack() {

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mPagableController.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
    }

    class AlbumDetailRequestHandler implements PagableController.PagableRequestHandler {
        @Override
        public PagedServerResponse handle() {
            GetAlbumSongsRequest request = new GetAlbumSongsRequest();
            request.setAlbum(mAlbum);
            return new BasicService().sendRequest(request);
        }
    }

    class AlbumDetailAdapter extends PagableController.PagableArrayAdapter<Song> {

        public AlbumDetailAdapter(PagableController controller, List<Song> dataSet) {
            super(controller, dataSet);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_song, null);
            }

            Song song = getItem(position);
            RoundedImageView imageView = (RoundedImageView) convertView.findViewById(R.id.song_list_item_image);
            imageView.setOval(true);
            Glide.with(AlbumDetailFragment.this).load(song.getAlbum().getImage()).into(imageView);

            TextView nameTextView = (TextView) convertView.findViewById(R.id.song_list_item_name);
            nameTextView.setText(song.getName());

            TextView descTextView = (TextView) convertView.findViewById(R.id.song_list_item_desc);
            descTextView.setText(song.getDesc());

            return convertView;
        }
    }
}
