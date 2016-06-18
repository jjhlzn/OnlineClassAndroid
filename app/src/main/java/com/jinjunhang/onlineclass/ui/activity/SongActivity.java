package com.jinjunhang.onlineclass.ui.activity;

import android.support.v4.app.Fragment;

import com.jinjunhang.framework.controller.SingleFragmentActivity;
import com.jinjunhang.onlineclass.ui.fragment.SongFragment;

/**
 * Created by lzn on 16/6/13.
 */
public class SongActivity extends BaseMusicActivity {

    @Override
    protected Fragment createFragment() {
        return new SongFragment();
    }




}
