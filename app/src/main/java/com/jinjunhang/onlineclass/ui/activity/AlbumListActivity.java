package com.jinjunhang.onlineclass.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.jinjunhang.onlineclass.R;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
}
