package com.jinjunhang.onlineclass.ui.cell.mainpage;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.jinjunhang.onlineclass.db.KeyValueDao;
import com.jinjunhang.onlineclass.model.Advertise;
import com.jinjunhang.onlineclass.model.LiveSong;
import com.jinjunhang.onlineclass.model.ServiceLinkManager;
import com.jinjunhang.onlineclass.model.Song;
import com.jinjunhang.onlineclass.service.GetHeaderAdvResponse;
import com.jinjunhang.onlineclass.service.GetMainPageAdsRequest;
import com.jinjunhang.onlineclass.service.GetMainPageAdsResponse;
import com.jinjunhang.onlineclass.service.GetSongInfoRequest;
import com.jinjunhang.onlineclass.service.GetSongInfoResponse;
import com.jinjunhang.onlineclass.service.GetTouTiaoRequest;
import com.jinjunhang.onlineclass.service.GetTouTiaoResponse;
import com.jinjunhang.onlineclass.ui.activity.WebBrowserActivity;
import com.jinjunhang.onlineclass.ui.activity.album.AlbumListActivity;
import com.jinjunhang.onlineclass.ui.activity.album.SongActivity;
import com.jinjunhang.onlineclass.ui.activity.mainpage.BottomTabLayoutActivity;
import com.jinjunhang.onlineclass.ui.activity.user.QRImageActivity;
import com.jinjunhang.onlineclass.ui.cell.BaseListViewCell;
import com.jinjunhang.onlineclass.ui.fragment.album.BaseSongFragment;
import com.jinjunhang.onlineclass.ui.fragment.mainpage.CustomSliderView;
import com.jinjunhang.onlineclass.ui.fragment.user.QRImageFragment;
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
    private TextView mToutiaoTextView;
    private ImageButton mFriendBtn;
    private LoadingAnimation mLoading;
    private List<Advertise> mAdvertises;
    private SliderLayout mSlider;
    private GetTouTiaoResponse mTouTiaoResp;
    private String mType = "";
    private KeyValueDao dao;

    public HeaderAdvCell(Activity activity, LoadingAnimation loading, String type) {
        super(activity);
        dao = KeyValueDao.getInstance(activity);
        this.mLoading = loading;
        mAdvertises = new ArrayList<>();
        mTouTiaoResp = new GetTouTiaoResponse();
        this.mType = type;
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        //Toast.makeText(mActivity,slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
        String title = (String)slider.getBundle().get("title");
        String clickUrl = (String)slider.getBundle().get("clickUrl");
        Intent i = new Intent(mActivity, WebBrowserActivity.class)
                .putExtra(WebBrowserActivity.EXTRA_TITLE, title)
                .putExtra(WebBrowserActivity.EXTRA_URL, clickUrl);
        mActivity.startActivity(i);
    }

    public void updateAds() {
        new GetHeaderAdvTask().execute();
    }

    public void updateTouTiao() {
        new GetToutiaoTask().execute();
    }

    private void setSlider() {
        for (Advertise adv : mAdvertises) {
            CustomSliderView textSliderView = new CustomSliderView(mActivity);
            // initialize a SliderLayout
            LogHelper.d(TAG, "imageUrl: " + adv.getImageUrl());
            textSliderView
                    ///.description(name)
                    .image(adv.getImageUrl())
                    //.image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(HeaderAdvCell.this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle().putString("title",adv.getTitle());
            textSliderView.getBundle().putString("clickUrl",adv.getClickUrl());

            mSlider.addSlider(textSliderView);
        }

        mSlider.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);

        mSlider.setPresetIndicator(SliderLayout.PresetIndicators.Right_Bottom);
        mSlider.setDuration(4000);
    }

    private void setTouTiao() {
        mToutiaoTextView.setText(mTouTiaoResp.getContent());
        LogHelper.d(TAG, "content = " + mTouTiaoResp.getContent());
        mToutiaoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogHelper.d(TAG, "clickUrl: " + mTouTiaoResp.getClickUrl());
                Intent i = new Intent(mActivity, WebBrowserActivity.class)
                        .putExtra(WebBrowserActivity.EXTRA_TITLE, mTouTiaoResp.getTitle())
                        .putExtra(WebBrowserActivity.EXTRA_URL, mTouTiaoResp.getClickUrl());
                mActivity.startActivity(i);
            }
        });
    }

    @Override
    public ViewGroup getView() {

        View view = mActivity.getLayoutInflater().inflate(R.layout.list_item_mainpage_header, null);
        mSlider = (SliderLayout)view.findViewById(R.id.slider);
        mToutiaoTextView = (TextView)view.findViewById(R.id.toutiaoTxt);
        mFriendBtn = (ImageButton) view.findViewById(R.id.haoyou_btn);
        mFriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mActivity, QRImageActivity.class);
                mActivity.startActivity(i);
            }
        });
        setSlider();
        setTouTiao();
        return (LinearLayout)view.findViewById(R.id.list_item_viewgroup);
    }

    private class GetHeaderAdvTask extends AsyncTask<Void, Void, GetMainPageAdsResponse> {

        @Override
        protected GetMainPageAdsResponse doInBackground(Void... voids) {
            GetMainPageAdsRequest request = new GetMainPageAdsRequest();
            request.setType(mType);
            return new BasicService().sendRequest(request);
        }


        @Override
        protected void onPostExecute(GetMainPageAdsResponse response) {
            super.onPostExecute(response);

            if (!response.isSuccess()) {
                LogHelper.e(TAG, response.getErrorMessage());
                return;
            }
            List<Advertise> advs = response.getAdvertises();
            if (response.getPopupAd() != null && !"".equals(response.getPopupAd().getImageUrl())) {
                LogHelper.d(TAG, "reponse popad is not null");
                Advertise popAd = response.getPopupAd();
                String cacheImageUrl = dao.getValue(KeyValueDao.KEY_POPUPAD_IMAGEURL, "");
                if (  !popAd.getImageUrl().equals(cacheImageUrl) ) {
                    dao.saveOrUpdate(KeyValueDao.KEY_POPUPAD_IMAGEURL, popAd.getImageUrl());
                    dao.saveOrUpdate(KeyValueDao.KEY_POPUPAD_CLICKURL, popAd.getClickUrl());
                    dao.saveOrUpdate(KeyValueDao.KEY_POPUPAD_TITLE, popAd.getTitle());
                    ((BottomTabLayoutActivity)mActivity).showPopupAd();
                }
            } else {
                LogHelper.d(TAG, "reponse popad is null");
            }

            mAdvertises = advs;
            setSlider();
        }
    }

    private class GetToutiaoTask extends AsyncTask<Void, Void, GetTouTiaoResponse> {

        @Override
        protected GetTouTiaoResponse doInBackground(Void... voids) {
            GetTouTiaoRequest request = new GetTouTiaoRequest();
            return new BasicService().sendRequest(request);
        }

        @Override
        protected void onPostExecute(final GetTouTiaoResponse response) {
            super.onPostExecute(response);

            if (!response.isSuccess()) {
                LogHelper.e(TAG, response.getErrorMessage());
                return;
            }

            mTouTiaoResp = response;

            setTouTiao();

        }
    }
}
