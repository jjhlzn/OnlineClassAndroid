package com.jinjunhang.onlineclass.controller.activity;

import android.support.v4.app.Fragment;

import com.jinjunhang.framework.controller.SingleFragmentActivity;
import com.jinjunhang.onlineclass.controller.fragment.SongFragment;

/**
 * Created by lzn on 16/6/13.
 */
public class SongActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new SongFragment();
    }
}
