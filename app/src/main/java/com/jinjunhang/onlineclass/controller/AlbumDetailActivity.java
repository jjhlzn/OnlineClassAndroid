package com.jinjunhang.onlineclass.controller;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.jinjunhang.framework.controller.SingleFragmentActivity;

/**
 * Created by lzn on 16/6/10.
 */
public class AlbumDetailActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new AlbumDetailFragment();
    }


}
