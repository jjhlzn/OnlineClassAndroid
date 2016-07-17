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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jinjunhang.framework.controller.PagableController;
import com.jinjunhang.framework.controller.SingleFragmentActivity;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.framework.service.PagedServerResponse;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.AlbumType;
import com.jinjunhang.onlineclass.model.LiveSong;
import com.jinjunhang.onlineclass.ui.activity.BaseMusicSingleFragmentActivity;
import com.jinjunhang.onlineclass.model.Album;
import com.jinjunhang.onlineclass.model.Song;
import com.jinjunhang.onlineclass.service.GetAlbumSongsRequest;
import com.jinjunhang.onlineclass.ui.activity.album.SongActivity;
import com.jinjunhang.onlineclass.ui.fragment.BottomPlayerFragment;
import com.jinjunhang.player.ExoPlayerNotificationManager;
import com.jinjunhang.player.MusicPlayer;
import com.jinjunhang.player.utils.LogHelper;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzn on 16/6/12.
 */
public class AlbumDetailFragment extends BottomPlayerFragment implements  SingleFragmentActivity.OnBackPressedListener,
        AbsListView.OnScrollListener {
    private static final String TAG = LogHelper.makeLogTag(AlbumDetailFragment.class);

    public final static String EXTRA_ALBUM = "extra_album";

    private PagableController mPagableController;
    private Album mAlbum;
    private ExoPlayerNotificationManager mNotificationManager;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_fragment_albumdetail, container, false);

        mNotificationManager =  ExoPlayerNotificationManager.getInstance(getActivity());

        mAlbum = (Album) getActivity().getIntent().getSerializableExtra(EXTRA_ALBUM);


        ImageView imageView = (ImageView) v.findViewById(R.id.albumDtail_image);
        Glide.with(getActivity()).load(mAlbum.getImage()).into(imageView);

        ((TextView) v.findViewById(R.id.albumDetail_name)).setText(mAlbum.getName());
        ((TextView) v.findViewById(R.id.albumDetail_author)).setText(mAlbum.getAuthor());

        ((BaseMusicSingleFragmentActivity)getActivity()).setActivityTitle(mAlbum.getName());

        ListView listView = (ListView)v.findViewById(R.id.listView);
        //去掉列表的分割线
        listView.setDividerHeight(0);
        listView.setDivider(null);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Song song = (Song)((mPagableController.getPagableArrayAdapter()).getItem(position));
                MusicPlayer musicPlayer = MusicPlayer.getInstance(getActivity());
                if (!musicPlayer.isPlay(song)) {
                    musicPlayer.pause();
                    musicPlayer.play(mAlbum.getSongs(), position);
                    mNotificationManager.display();
                }
                Intent i = new Intent(getActivity(), SongActivity.class)
                        .putExtra(BaseSongFragment.EXTRA_SONG, song);
                startActivity(i);

            }
        });

        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)v.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2, R.color.refresh_progress_3);

        mPagableController = new PagableController(getActivity(), listView);
        if (mAlbum.isLive()) {
            mPagableController.setShowLoadCompleteTip(false);
        }
        mPagableController.setSwipeRefreshLayout(swipeRefreshLayout);
        mPagableController.setPagableArrayAdapter(new AlbumDetailAdapter(mPagableController, new ArrayList<Song>()));
        mPagableController.setPagableRequestHandler(new AlbumDetailRequestHandler());
        mPagableController.setOnScrollListener(this);

        mPlayerController.attachToView((FrameLayout)v.findViewById(R.id.fragmentContainer));
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            Song song = getItem(position);

            if (convertView == null) {
                if (song.isLive()) {
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_song_live, null);
                    ((TextView)convertView.findViewById(R.id.listen_people_label)).setText(((LiveSong)song).getListenPeople());
                } else {
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_song, null);
                }
            }


            RoundedImageView imageView = (RoundedImageView) convertView.findViewById(R.id.song_list_item_image);
            imageView.setOval(true);
            Glide.with(AlbumDetailFragment.this).load(song.getAlbum().getImage()).into(imageView);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Song song = (Song)((mPagableController.getPagableArrayAdapter()).getItem(position));
                    if (mMusicPlayer.isPlay(song)) {

                    } else {
                        MusicPlayer.getInstance(getActivity()).play(mAlbum.getSongs(), position);
                        notifyDataSetChanged();
                    }
                }
            });

            TextView nameTextView = (TextView) convertView.findViewById(R.id.song_list_item_name);
            nameTextView.setText(song.getName());

            TextView descTextView = (TextView) convertView.findViewById(R.id.song_list_item_desc);
            descTextView.setText(song.getDesc());

            ImageView playImageView = (ImageView) convertView.findViewById(R.id.song_list_item_playimage);
            if (mMusicPlayer.isPlay(song)) {
                LogHelper.d(TAG, "now player song.id = " + song.getId());
                playImageView.setImageResource(R.drawable.smallpause);
            } else {
                playImageView.setImageResource(R.drawable.smallplay);
            }

            return convertView;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPagableController.getPagableArrayAdapter().notifyDataSetChanged();
    }
}
