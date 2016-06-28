package com.jinjunhang.framework.lib;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jinjunhang.onlineclass.R;

/**
 * Created by lzn on 16/6/27.
 */
public class LoadingAnimation {
    private Activity mContext;
    private LinearLayout mLoading;

    public LoadingAnimation(Activity context) {
        mContext = context;
    }

    public void show(String message) {
        mLoading = (LinearLayout)mContext.getLayoutInflater().inflate(R.layout.loading_animation, null);
        mLoading.setGravity(Gravity.CENTER);

        ViewGroup container = (ViewGroup) mContext.findViewById(R.id.fragmentContainer);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(100, 100);

        container.addView(mLoading);

    }

    public void hide() {
        ViewGroup container = (ViewGroup) mContext.findViewById(R.id.fragmentContainer);
        container.removeView(mLoading);
    }

}
