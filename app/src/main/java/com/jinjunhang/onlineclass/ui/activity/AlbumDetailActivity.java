package com.jinjunhang.onlineclass.ui.activity;

import android.support.v4.app.Fragment;

import com.jinjunhang.onlineclass.ui.fragment.AlbumDetailFragment;

/**
 * Created by lzn on 16/6/10.
 */
public class AlbumDetailActivity extends BaseMusicActivity {

    @Override
    protected Fragment createFragment() {
        return new AlbumDetailFragment();
    }


}
