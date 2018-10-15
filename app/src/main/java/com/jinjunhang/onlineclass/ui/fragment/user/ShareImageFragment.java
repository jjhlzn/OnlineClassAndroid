package com.jinjunhang.onlineclass.ui.fragment.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.ui.fragment.BaseFragment;

/**
 * Created by jinjunhang on 2018/4/7.
 */

public class ShareImageFragment extends BaseFragment {

    private final String TAG = LogHelper.makeLogTag(ShareImageFragment.class);

    private ImageView mImageView;
    private String mUrl = "";

    public void setUrl(String url) {
        mUrl = url;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.share_image_item, container, false);
        if (getArguments() != null) {
            mUrl = getArguments().getString("url");
        }
        mImageView = v.findViewById(R.id.share_image);

        int width = Utils.getScreenWidth(getActivity()) - QRImageFragment.PageInterWidth * 2;
        int height = (int)( (Utils.getScreenWidth(getActivity()) - QRImageFragment.PageInterWidth * 2 )* 1.8);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)mImageView.getLayoutParams();
        params.height = height;
        params.width = width;

        LogHelper.d(TAG, "width = " + params.width + ", height = " + params.height);

        mImageView.setLayoutParams(params);
        loadImage(mUrl);
        return v;
    }

    public void loadImage(String url) {
        LogHelper.d(TAG, "loadImage, url = " + url);
        Glide
            .with(this)
            .load(url)
            .placeholder(R.drawable.rect_placeholder)
            .into(mImageView);
    }
}
