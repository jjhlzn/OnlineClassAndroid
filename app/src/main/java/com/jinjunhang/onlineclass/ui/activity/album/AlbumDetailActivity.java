package com.jinjunhang.onlineclass.ui.activity.album;

import android.support.v4.app.Fragment;

import com.jinjunhang.onlineclass.ui.activity.BaseMusicSingleFragmentActivity;
import com.jinjunhang.onlineclass.ui.fragment.album.AlbumDetailFragment;

/**
 * Created by lzn on 16/6/10.
 */
public class AlbumDetailActivity extends BaseMusicSingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new AlbumDetailFragment();
    }


}
