package com.jinjunhang.onlineclass.controller;

import android.support.v4.app.Fragment;

import com.jinjunhang.framework.controller.SingleFragmentActivity;

/**
 * Created by lzn on 16/6/13.
 */
public class SongActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new SongFragment();
    }
}
