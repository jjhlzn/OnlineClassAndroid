package com.jinjunhang.onlineclass;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * Created by lzn on 16/6/11.
 */
public class AlbumListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new AlbumListFragment();
    }

    @Override
    protected String getActivityTitle() {
        return "课程列表";
    }
}
