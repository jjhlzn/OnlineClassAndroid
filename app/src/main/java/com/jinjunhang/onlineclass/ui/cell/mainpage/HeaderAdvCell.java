package com.jinjunhang.onlineclass.ui.cell.mainpage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

//import com.daimajia.slider.library.Indicators.PagerIndicator;
//import com.daimajia.slider.library.SliderLayout;
//import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.bumptech.glide.Glide;
import com.jinjunhang.framework.lib.LoadingAnimation;
import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.db.KeyValueDao;
import com.jinjunhang.onlineclass.model.Advertise;
import com.jinjunhang.onlineclass.service.GetMainPageAdsRequest;
import com.jinjunhang.onlineclass.service.GetMainPageAdsResponse;
import com.jinjunhang.onlineclass.service.GetTouTiaoResponse;
import com.jinjunhang.onlineclass.ui.activity.WebBrowserActivity;
import com.jinjunhang.onlineclass.ui.activity.mainpage.BottomTabLayoutActivity;
import com.jinjunhang.onlineclass.ui.cell.BaseListViewCell;
import com.jinjunhang.onlineclass.ui.fragment.mainpage.CustomSliderView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by jinjunhang on 17/1/6.
 */

public class HeaderAdvCell extends BaseListViewCell  {
    private static final String TAG = LogHelper.makeLogTag(HeaderAdvCell.class);

    private LoadingAnimation mLoading;
    private List<Advertise> mAdvertises;
    private Banner mSlider;
    private GetTouTiaoResponse mTouTiaoResp;
    private KeyValueDao dao;

    private View mView;

    public HeaderAdvCell(Activity activity, LoadingAnimation loading, String type) {
        super(activity);
        dao = KeyValueDao.getInstance(activity);
        this.mLoading = loading;
        mAdvertises = new ArrayList<>();
        mTouTiaoResp = new GetTouTiaoResponse();
    }

    public void updateAds() {
        new GetHeaderAdvTask().execute();
    }

    public void startPlay() {
        if (mSlider != null) {
            mSlider.startAutoPlay();
        }
    }

    public void stopPlay() {
        if (mSlider != null) {
            mSlider.stopAutoPlay();
        }
    }

    private void setSlider() {
        if (mSlider == null)
            return;
        List<String> imageUrls = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        for (Advertise adv : mAdvertises) {
            LogHelper.d(TAG, "imageUrl: " + adv.getImageUrl());
            imageUrls.add(adv.getImageUrl());
            titles.add(adv.getTitle());
        }
        mSlider.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Advertise adv = mAdvertises.get(position);
                String title = adv.getTitle();
                String clickUrl = (String)adv.getClickUrl();
                Intent i = new Intent(mActivity, WebBrowserActivity.class)
                        .putExtra(WebBrowserActivity.EXTRA_TITLE, title)
                        .putExtra(WebBrowserActivity.EXTRA_URL, clickUrl);
                mActivity.startActivity(i);
            }
        });
        mSlider.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        mSlider.setImages(imageUrls);
        mSlider.setBannerTitles(titles);
        mSlider.isAutoPlay(true);
        mSlider.setDelayTime(3000);
        mSlider.setIndicatorGravity(BannerConfig.CENTER);
        mSlider.start();
        mSlider.startAutoPlay();
    }


    @Override
    public ViewGroup getView() {
        if (mView == null) {
            mView = mActivity.getLayoutInflater().inflate(R.layout.list_item_mainpage_header, null);
            mSlider = mView.findViewById(R.id.slider);
            mSlider.setImageLoader(new GlideImageLoader());
            setSlider();
            return (LinearLayout) mView.findViewById(R.id.list_item_viewgroup);
        } {
            return (LinearLayout) mView.findViewById(R.id.list_item_viewgroup);
        }
    }

    private class GetHeaderAdvTask extends AsyncTask<Void, Void, GetMainPageAdsResponse> {

        @Override
        protected GetMainPageAdsResponse doInBackground(Void... voids) {
            GetMainPageAdsRequest request = new GetMainPageAdsRequest();
            //request.setType(mType);
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
                    //((BottomTabLayoutActivity)mActivity).showPopupAd();
                }
            } else {
                LogHelper.d(TAG, "reponse popad is null");
            }

            mAdvertises = advs;
            setSlider();
        }
    }

    class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            /**
             注意：
             1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
             2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
             传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
             切记不要胡乱强转！
             */

            //Glide 加载图片简单用法
            LogHelper.d(TAG, "load image: " + path);
            Glide.with(context).load(path).placeholder(R.drawable.rect_placeholder).into(imageView);

        }

        //提供createImageView 方法，如果不用可以不重写这个方法，主要是方便自定义ImageView的创建
        @Override
        public ImageView createImageView(Context context) {
            ImageView imageView = new ImageView(mActivity);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }

    }


}
