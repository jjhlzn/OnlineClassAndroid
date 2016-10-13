package com.jinjunhang.onlineclass.ui.cell.player;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.Advertise;
import com.jinjunhang.onlineclass.model.LiveSong;
import com.jinjunhang.onlineclass.model.ServiceLinkManager;
import com.jinjunhang.onlineclass.ui.activity.WebBrowserActivity;
import com.jinjunhang.framework.lib.LogHelper;
import com.sunfusheng.marqueeview.MarqueeView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by jjh on 2016-7-2.
 */
public class LivePlayerCell extends PlayerCell {
    private static final  String  TAG = LogHelper.makeLogTag(LivePlayerCell.class);

    public LivePlayerCell(Activity activity) {
        super(activity);
    }

    @Override
    public ViewGroup getView() {
        LogHelper.d(TAG, "LivePlayer.getView() called");

        ViewGroup v = super.getView();
        LiveSong song = (LiveSong) mMusicPlayer.getCurrentPlaySong();

        Button applyButton = (Button) v.findViewById(R.id.player_apply_button);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mActivity, WebBrowserActivity.class)
                        .putExtra(WebBrowserActivity.EXTRA_TITLE, "我要报名")
                        .putExtra(WebBrowserActivity.EXTRA_URL, ServiceLinkManager.MyAgentUrl());
                mActivity.startActivity(i);
            }
        });
        if (song.hasAdvImage()){
            applyButton.setVisibility(View.VISIBLE);
            v.findViewById(R.id.player_hand).setVisibility(View.VISIBLE);
        } else {
            applyButton.setVisibility(View.INVISIBLE);
            v.findViewById(R.id.player_hand).setVisibility(View.INVISIBLE);
        }

        MarqueeView advTextView = (MarqueeView)v.findViewById(R.id.player_adv_text);
        if (song.getAdvText() == null || song.getAdvText().trim().equals("")) {
            advTextView.startWithText("欢迎大家收听");
        } else {
            advTextView.startWithText(song.getAdvText());
        }

        //advTextView.setSelected(true);

        String listenerCount = ((LiveSong)mMusicPlayer.getCurrentPlaySong()).getListenPeople();
        mListenerCountLabel.setText(listenerCount);

        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogHelper.d(TAG, "LivePlayerCell play button pressed");
                if (mMusicPlayer.isPlaying()) {
                    mMusicPlayer.pause();
                } else {
                    //mMusicPlayer.play(mMusicPlayer.getSongs(), mMusicPlayer.getCurrentPlaySongIndex());
                    LogHelper.d(TAG, "LivePlayerCell resume");
                    mMusicPlayer.resume();
                    scheduleSeekbarUpdate();
                }
                updatePlayButton();
            }
        });

        return v;
    }

    @Override
    protected void loadArtImage(View v) {
        LiveSong song = (LiveSong) mMusicPlayer.getCurrentPlaySong();
        if (song == null) {
            return;
        }
        SliderLayout sliderShow = (SliderLayout)v.findViewById(R.id.player_song_image_adv);

        List<Advertise> advs = song.getImageAdvs();

        if (advs.size() == 0) {
            Advertise adv = new Advertise();
            adv.setImageUrl(ServiceLinkManager.DefaultAdvImageUrl());
            adv.setClickUrl("");
            advs.add(adv);
        }

        for(final Advertise adv : advs){
            LogHelper.d(TAG, "advimage = " + adv.getImageUrl() + ", title = " + adv.getTitle());
            if (adv.getImageUrl() == null || "".equals(adv.getImageUrl().trim())) {
                continue;
            }
            DefaultSliderView textSliderView = new DefaultSliderView(mActivity);
            textSliderView.image(adv.getImageUrl()).setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(BaseSliderView slider) {
                    if (adv.getClickUrl().equals("")) {
                        return;
                    }
                    Intent i = new Intent(mActivity, WebBrowserActivity.class)
                            .putExtra(WebBrowserActivity.EXTRA_TITLE, adv.getTitle())
                            .putExtra(WebBrowserActivity.EXTRA_URL, adv.getClickUrl());
                    mActivity.startActivity(i);
                }
            });
            sliderShow.addSlider(textSliderView);
        }

        if (advs.size() == 1) {
            sliderShow.stopAutoCycle();
            sliderShow.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
        } else {

            long duration = 500;
            if (duration < song.getScrollRate() * 1000) {
                duration = song.getScrollRate() * 1000;
            }

            if (song.getScrollRate() == 0) {
                duration = 5000;
            }

            sliderShow.setDuration(duration);
        }
    }

    @Override
    protected void updateProgress() {
        if (!mMusicPlayer.isPlaying()) {
            return;
        }
        LiveSong song = (LiveSong) mMusicPlayer.getCurrentPlaySong();
        int progress = (int)((double)song.getPlayedTimeInSec() / song.getTotalTimeInSec() * 1000);
        mSeekbar.setProgress(progress );
        if (mPlayTimeTextView != null)
            mPlayTimeTextView.setText(getNowTimeString());
    }

    @Override
    protected void setPlayButtons(View v) {
        super.setPlayButtons(v);
        mPlayTimeTextView.setText(getNowTimeString());
        LiveSong song = (LiveSong) mMusicPlayer.getCurrentPlaySong();
        mDurationTextView.setText(song.getEndTimeString());
    }

    protected String getNowTimeString() {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        return sdf.format(new Date());
    }

    @Override
    protected void setSeekbar(View v) {
        mSeekbar = (SeekBar) v.findViewById(R.id.player_seekbar);
        mSeekbar.setMax(0);
        mSeekbar.setMax(1000);
        updateProgress();
        scheduleSeekbarUpdate();
    }


}
