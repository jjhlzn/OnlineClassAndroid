package com.jinjunhang.onlineclass.ui.fragment.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.ui.fragment.BaseFragment;

/**
 * Created by jinjunhang on 2018/4/7.
 */

public class ShareImageFragment extends BaseFragment {

    private final String TAG = LogHelper.makeLogTag(ShareImageFragment.class);

    private ImageView mImageView;
    private String mUrl = "";
    private boolean mIsInited = false;

    public void setUrl(String url) {
        mUrl = url;
    }

    public boolean isInited() {
        return mIsInited;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.share_image_item, container, false);
        if (getArguments() != null)
            mUrl = getArguments().getString("url");
        mImageView = (ImageView)v.findViewById(R.id.share_image);

        //if (!"".equals(mUrl)) {
            loadImage(mUrl);
       // }

        mIsInited = true;
        return v;
    }

    public void loadImage(String url) {
        LogHelper.d(TAG, "loadImage, url = " + url);
        Glide
            .with(this)
            .load(url)
           // .placeholder(R.drawable.footer_ditu)
            .into(mImageView);
    }
}
