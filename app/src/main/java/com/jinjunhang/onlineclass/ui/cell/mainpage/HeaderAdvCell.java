package com.jinjunhang.onlineclass.ui.cell.mainpage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jinjunhang.framework.lib.LoadingAnimation;
import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.db.KeyValueDao;
import com.jinjunhang.onlineclass.model.Advertise;
import com.jinjunhang.onlineclass.service.GetMainPageAdsRequest;
import com.jinjunhang.onlineclass.service.GetMainPageAdsResponse;
import com.jinjunhang.onlineclass.ui.activity.WebBrowserActivity;
import com.jinjunhang.onlineclass.ui.activity.mainpage.BottomTabLayoutActivity;
import com.jinjunhang.onlineclass.ui.cell.BaseListViewCell;
import com.jinjunhang.onlineclass.ui.fragment.mainpage.MainPageFragment;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by jinjunhang on 17/1/6.
 */

public class HeaderAdvCell extends BaseListViewCell  {
    private static final String TAG = LogHelper.makeLogTag(HeaderAdvCell.class);

    private List<Advertise> mAdvertises;
    private ViewHolder mViewHolder;

    private boolean isNeedUpdate = true;

    public void setAdvertises(List<Advertise> advertises) {
        mAdvertises = advertises;
        isNeedUpdate = true;
    }

    public HeaderAdvCell(Activity activity) {
        super(activity);
        mAdvertises = new ArrayList<>();
    }

    public void startPlay() {
        if (mViewHolder != null && mViewHolder.slider != null) {
            mViewHolder.slider.startAutoPlay();
        }
    }

    public void stopPlay() {
        if (mViewHolder != null && mViewHolder.slider != null) {
            mViewHolder.slider.stopAutoPlay();
        }
    }


    private void setSlider(ViewHolder viewHolder) {
        if (mAdvertises.size() == 0)
            return;

        //LogHelper.d(TAG, "isInited = " + viewHolder.isInited + ", isNeedUpdate = " + isNeedUpdate);
        if (viewHolder.isInited && !isNeedUpdate)
            return;

        Banner mSlider = viewHolder.slider;
        updateSlider(viewHolder);
        mSlider.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);

        mSlider.setOffscreenPageLimit(8);
        mSlider.isAutoPlay(true);
        mSlider.setDelayTime(3000);
        mSlider.setIndicatorGravity(BannerConfig.CENTER);

        mSlider.start();
        mSlider.startAutoPlay();

        viewHolder.isInited = true;
        isNeedUpdate = false;
    }

    private  void updateSlider(ViewHolder viewHolder) {
        Banner mSlider = viewHolder.slider;
        List<String> imageUrls = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        for (Advertise adv : mAdvertises) {
            //LogHelper.d(TAG, "url: " + adv.getImageUrl());
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

        if (imageUrls.size() != 0) {
            mSlider.update(imageUrls, titles);
        } else {
            mSlider.setImages(imageUrls);
            mSlider.setBannerTitles(titles);
        }
    }

    @Override
    public int getItemViewType() {
        return BaseListViewCell.HEADER_CELL;
    }


    private View recreateCell() {
        View convertView = mActivity.getLayoutInflater().inflate(R.layout.list_item_mainpage_header, null).findViewById(R.id.list_item_viewgroup);
        //设置Banner的显示比例
        convertView.setLayoutParams(new RelativeLayout.LayoutParams(Utils.getScreenWidth(mActivity), (int) ((float) Utils.getScreenWidth(mActivity) / 375.0 * 224)));
        Banner mSlider = convertView.findViewById(R.id.slider);
        ViewHolder viewHolder = new ViewHolder();
        convertView.setTag(viewHolder);
        viewHolder.slider = mSlider;
        viewHolder.slider.setImageLoader(new GlideImageLoader());
        return convertView;
    }

    //TODO： 不能下拉刷新
    @Override
    public ViewGroup getView(View convertView) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = recreateCell();
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }


        viewHolder.mAdvertises = mAdvertises;
        //Log.d(TAG, "viewHolder.mAdvertises.size() = " + viewHolder.mAdvertises.size());
        mViewHolder = viewHolder;
        setSlider(viewHolder);

        return (ViewGroup) convertView;
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
           // LogHelper.d(TAG, "load image: " + path);
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

    class ViewHolder {
        Banner slider;
        boolean isInited;
        List<Advertise> mAdvertises;
    }


}
