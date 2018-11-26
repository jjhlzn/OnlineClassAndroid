package com.jinjunhang.onlineclass.ui.cell.mainpage;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.FinanceToutiao;
import com.jinjunhang.onlineclass.model.LearnFinanceItem;
import com.jinjunhang.onlineclass.model.LiveSong;
import com.jinjunhang.onlineclass.model.Song;
import com.jinjunhang.onlineclass.ui.activity.WebBrowserActivity;
import com.jinjunhang.onlineclass.ui.cell.BaseListViewCell;
import com.jinjunhang.player.MusicPlayer;

import java.util.ArrayList;
import java.util.List;

public class LearnFinanceCell extends BaseListViewCell {

    private static String TAG = LogHelper.makeLogTag(LearnFinanceCell.class);

    private LearnFinanceItem mLearnFinanceItem;
    private Activity mActivity;

    public LearnFinanceCell(Activity activity, LearnFinanceItem item) {
        super(activity);
        mActivity = activity;
        this.mLearnFinanceItem = item;
    }

    private Song getSong() {
        Song song = new Song();
        song.setId(mLearnFinanceItem.getSongId());
        song.setUrl(mLearnFinanceItem.getAudioUrl());
        LogHelper.d(TAG, mLearnFinanceItem.getAudioUrl());
        return song;
    }

    private List<Song> getSongs() {
        List<Song> songs = new ArrayList<>();
        songs.add(getSong());
        return songs;
    }

    @Override
    public int getItemViewType() {
        return BaseListViewCell.LEARN_FINANCE_CELL;
    }

    private void setView(ViewHolder viewHolder) {
        viewHolder.titleTextView.setText(mLearnFinanceItem.getTitle());

        final MusicPlayer musicPlayer = MusicPlayer.getInstance(mActivity);

        viewHolder.superView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Song song = musicPlayer.getCurrentPlaySong();

                if (song != null && song.getId().equals(getSong().getId())) {
                    if (musicPlayer.isPlay(getSong())) {
                        musicPlayer.pause();
                    } else {
                        musicPlayer.resume();
                    }
                } else {
                    musicPlayer.play(getSongs(), 0);
                }


            }
        });

    }

    @Override
    public ViewGroup getView(View convertView) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mActivity.getLayoutInflater().inflate(R.layout.list_item_mainpage_learn_finance, null).findViewById(R.id.container);

            viewHolder = new ViewHolder();
            viewHolder.superView = (RelativeLayout)convertView;
            convertView.setTag(viewHolder);

            viewHolder. titleTextView =  convertView.findViewById(R.id.toutiaoTitle);
            viewHolder.tagImage =  convertView.findViewById(R.id.indexImage);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        setView(viewHolder);
        return (RelativeLayout)convertView;
    }

    class ViewHolder {
        TextView titleTextView;
        ImageView tagImage;
        RelativeLayout superView;
    }
}
