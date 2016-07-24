package com.jinjunhang.onlineclass.ui.cell;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.LiveSong;
import com.jinjunhang.onlineclass.ui.activity.WebBrowserActivity;
import com.jinjunhang.onlineclass.ui.activity.album.CommentListActivity;
import com.jinjunhang.onlineclass.ui.fragment.album.CommentListFragment;
import com.jinjunhang.player.MusicPlayer;

/**
 * Created by lzn on 16/7/22.
 */
public class SongAdvCell extends BaseListViewCell {

    private LiveSong mLiveSong;

    public SongAdvCell(Activity activity, LiveSong song) {
        super(activity);
        mLiveSong = song;
    }

    @Override
    public ViewGroup getView() {
        View v = mActivity.getLayoutInflater().inflate(R.layout.list_item_song_adv, null);

        ImageView songAdvImage = (ImageView)v.findViewById(R.id.songAdvImage);
        Glide.with(mActivity).load(mLiveSong.getAdvImageUrl()).into(songAdvImage);

        songAdvImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mActivity, WebBrowserActivity.class)
                        .putExtra(WebBrowserActivity.EXTRA_TITLE, "")
                        .putExtra(WebBrowserActivity.EXTRA_URL, mLiveSong.getAdvUrl());

                mActivity.startActivity(i);
            }
        });

        return (LinearLayout)v.findViewById(R.id.list_item_albumtype_viewgroup);
    }
}
