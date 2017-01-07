package com.jinjunhang.onlineclass.ui.activity.album;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.jinjunhang.onlineclass.ui.activity.BaseMusicSingleFragmentActivity;
import com.jinjunhang.onlineclass.ui.fragment.album.AlbumListFragment;

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
        return "直播课程";
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
}
