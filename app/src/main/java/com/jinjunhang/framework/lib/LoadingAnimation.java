package com.jinjunhang.framework.lib;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jinjunhang.onlineclass.R;

/**
 * Created by lzn on 16/6/27.
 */
public class LoadingAnimation {
    private Activity mContext;
    private ViewGroup mLoading;
    private ViewGroup mFather;
    private boolean mIsShow;

    public Activity getActivity() {
        return mContext;
    }

    public LoadingAnimation(Activity context) {
        mContext = context;
    }

    public LoadingAnimation(Activity context, ViewGroup father) {
        mContext = context;
        this.mFather = father;
    }

    public boolean isShow() {
        return mIsShow;
    }

    public void show(String message) {
        mIsShow  = true;
        mLoading = (ViewGroup)mContext.getLayoutInflater().inflate(R.layout.loading_animation, null);

        mLoading.findViewById(R.id.overlay_bg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ViewGroup container = null;
        if (mFather != null) {
            container = mFather;
        } else  {
            container = (ViewGroup) mContext.findViewById(R.id.fragmentContainer);
            if (container == null) {
                container = (ViewGroup) mContext.findViewById(R.id.rootView);
            }
        }

        container.addView(mLoading);

    }

    public void hide() {
        mIsShow = false;
        if (mFather != null) {
            mFather.removeView(mLoading);
        } else {
            ViewGroup container = (ViewGroup) mContext.findViewById(R.id.fragmentContainer);
            if (container == null) {
                container = (ViewGroup) mContext.findViewById(R.id.rootView);
            }
            container.removeView(mLoading);
        }


    }

}
