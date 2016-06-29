package com.jinjunhang.onlineclass.ui.activity;

import android.support.v4.app.Fragment;

import com.jinjunhang.onlineclass.ui.fragment.SearchResultFragment;

/**
 * Created by lzn on 16/6/29.
 */
public class SearchResultActivity extends BaseMusicSingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new SearchResultFragment();
    }
}
