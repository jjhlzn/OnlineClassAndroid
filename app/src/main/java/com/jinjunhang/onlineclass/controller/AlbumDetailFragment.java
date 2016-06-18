package com.jinjunhang.onlineclass.controller;

import android.content.Intent;
import android.net.Uri;
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
import com.google.android.exoplayer.util.Util;
import com.jinjunhang.framework.controller.PagableController;
import com.jinjunhang.framework.controller.SingleFragmentActivity;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.framework.service.PagedServerResponse;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.Album;
import com.jinjunhang.onlineclass.model.Song;
import com.jinjunhang.onlineclass.service.GetAlbumSongsRequest;
import com.jinjunhang.uamp.utils.LogHelper;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzn on 16/6/12.
 */
public class AlbumDetailFragment extends android.support.v4.app.Fragment implements  SingleFragmentActivity.OnBackPressedListener,
        AbsListView.OnScrollListener {
    private static final String TAG = LogHelper.makeLogTag(AlbumDetailFragment.class);

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

        ((BaseMusicActivity)getActivity()).setActivityTitle(mAlbum.getName());

        ListView listView = (ListView)v.findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Song song = (Song)((mPagableController.getPagableArrayAdapter()).getItem(position));
                playMusic(song.getUrl());

                /*
                Song song = (Song)((mPagableController.getPagableArrayAdapter()).getItem(position));
                Intent i = new Intent(getActivity(), PlayerActivity.class)
                        .putExtra(PlayerActivity.CONTENT_TYPE_EXTRA, Util.TYPE_OTHER)
                        .putExtra(PlayerActivity.PROVIDER_EXTRA, "")
                        .putExtra(PlayerActivity.CONTENT_ID_EXTRA, song.getId())
                        .setData(Uri.parse(song.getUrl()));
                startActivity(i);*/

            }
        });

        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)v.findViewById(R.id.swipe_refresh_layout);

        mPagableController = new PagableController(getActivity(), listView);
        mPagableController.setSwipeRefreshLayout(swipeRefreshLayout);
        mPagableController.setPagableArrayAdapter(new AlbumDetailAdapter(mPagableController, new ArrayList<Song>()));
        mPagableController.setPagableRequestHandler(new AlbumDetailRequestHandler());
        mPagableController.setOnScrollListener(this);

        return v;
    }

    private void playMusic(String url) {
        //LogHelper.d(TAG, "onMediaItemSelected, mediaId=" + item.getMediaId());
        //if (item.isPlayable()) {
            getActivity().getSupportMediaController().getTransportControls().playFromMediaId(url, null);
        //} else if (item.isBrowsable()) {
         //   navigateToBrowser(item.getMediaId());
        //} else {
        //    LogHelper.w(TAG, "Ignoring MediaItem that is neither browsable nor playable: ",
         //           "mediaId=", item.getMediaId());
        //}
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
