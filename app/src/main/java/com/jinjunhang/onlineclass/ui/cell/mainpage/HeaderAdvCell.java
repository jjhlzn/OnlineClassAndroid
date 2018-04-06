package com.jinjunhang.onlineclass.ui.cell.mainpage;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.jinjunhang.framework.lib.LoadingAnimation;
import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.framework.service.ServerResponse;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.db.HeaderAdvManager;
import com.jinjunhang.onlineclass.model.LiveSong;
import com.jinjunhang.onlineclass.model.Song;
import com.jinjunhang.onlineclass.service.GetHeaderAdvResponse;
import com.jinjunhang.onlineclass.service.GetSongInfoRequest;
import com.jinjunhang.onlineclass.service.GetSongInfoResponse;
import com.jinjunhang.onlineclass.ui.activity.album.AlbumListActivity;
import com.jinjunhang.onlineclass.ui.activity.album.SongActivity;
import com.jinjunhang.onlineclass.ui.cell.BaseListViewCell;
import com.jinjunhang.onlineclass.ui.fragment.album.BaseSongFragment;
import com.jinjunhang.onlineclass.ui.fragment.mainpage.CustomSliderView;
import com.jinjunhang.player.ExoPlayerNotificationManager;
import com.jinjunhang.player.MusicPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jinjunhang on 17/1/6.
 */

public class HeaderAdvCell extends BaseListViewCell implements BaseSliderView.OnSliderClickListener {
    private static final String TAG = LogHelper.makeLogTag(HeaderAdvCell.class);

    private ImageView mImageView;
    private LoadingAnimation mLoading;

    public HeaderAdvCell(Activity activity, LoadingAnimation loading) {
        super(activity);
        this.mLoading = loading;
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(mActivity,slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public ViewGroup getView() {
        View view = mActivity.getLayoutInflater().inflate(R.layout.list_item_mainpage_header, null);


        SliderLayout slider = (SliderLayout)view.findViewById(R.id.slider);

        HashMap<String,String> url_maps = new HashMap<String, String>();
        url_maps.put("Hannibal", "http://imageprocess.yitos.net/images/public/20160910/99381473502384338.jpg");
        url_maps.put("Big Bang Theory", "http://imageprocess.yitos.net/images/public/20160910/77991473496077677.jpg");
        url_maps.put("House of Cards", "http://imageprocess.yitos.net/images/public/20160906/1291473163104906.jpg");

        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Hannibal",R.drawable.actionbar_bg);
        file_maps.put("Big Bang Theory",R.drawable.actionbar_bg);
        file_maps.put("House of Cards",R.drawable.actionbar_bg);
        file_maps.put("Game of Thrones", R.drawable.actionbar_bg);

        for(String name : url_maps.keySet()){
            CustomSliderView textSliderView = new CustomSliderView(mActivity);
            // initialize a SliderLayout
            textSliderView
                    ///.description(name)
                    .image(url_maps.get(name))
                    //.image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            slider.addSlider(textSliderView);
        }
        slider.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);

        slider.setPresetIndicator(SliderLayout.PresetIndicators.Right_Bottom);
        slider.setDuration(4000);



        return (LinearLayout)view.findViewById(R.id.list_item_viewgroup);
    }

    /*
    private class GetSongInfoTask extends AsyncTask<Void, Void, GetSongInfoResponse> {
        @Override
        protected GetSongInfoResponse doInBackground(Void... params) {
            Song song = new Song();
            HeaderAdvManager manager = HeaderAdvManager.getInstance();
            final GetHeaderAdvResponse.HeaderAdvImage adv = manager.getHeaderAdv();
            if (GetHeaderAdvResponse.HeaderAdvImage.TYPE_SONG.equals(adv.getType())) {
                song.setId(adv.getParams().get(GetHeaderAdvResponse.HeaderAdvImage.PARAM_KEY_SONG));
                GetSongInfoRequest request = new GetSongInfoRequest(song);
                return new BasicService().sendRequest(request);
            } else {
                GetSongInfoResponse resp = new GetSongInfoResponse();
                resp.setStatus(ServerResponse.FAIL);
                resp.setErrorMessage("");
                return resp;
            }
        }

        @Override
        protected void onPostExecute(GetSongInfoResponse resp) {
            super.onPostExecute(resp);
            mLoading.hide();
            if (resp.getStatus() == ServerResponse.NO_PERMISSION) {
                Utils.showVipBuyMessage(mActivity, resp.getErrorMessage());
                return;
            }

            if (resp.isSuccess()) {
                MusicPlayer musicPlayer = MusicPlayer.getInstance(mActivity);
                ExoPlayerNotificationManager notificationManager = ExoPlayerNotificationManager.getInstance(mActivity);
                LiveSong song = (LiveSong) resp.getSong();
                List<Song> songs = new ArrayList<>();
                songs.add(song);
                if (!musicPlayer.isPlay(song)) {
                    musicPlayer.pause();
                    musicPlayer.play(songs, 0);
                    notificationManager.display();
                }
                Intent i = new Intent(mActivity, SongActivity.class)
                        .putExtra(BaseSongFragment.EXTRA_SONG, song);
                mActivity.startActivity(i);
            } else {
                Utils.showErrorMessage(mActivity, resp.getErrorMessage());
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoading.show("");
        }
    } */
}
