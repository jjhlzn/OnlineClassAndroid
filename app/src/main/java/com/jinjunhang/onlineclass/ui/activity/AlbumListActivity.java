package com.jinjunhang.onlineclass.ui.activity;

import android.support.v4.app.Fragment;

import com.jinjunhang.onlineclass.ui.fragment.AlbumListFragment;

/**
 * Created by lzn on 16/6/11.
 */
public class AlbumListActivity extends BaseMusicSingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new AlbumListFragment();
    }

    @Override
    protected String getActivityTitle() {
        return "课程列表";
    }
}
