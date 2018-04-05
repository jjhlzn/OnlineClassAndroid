package com.jinjunhang.onlineclass.ui.activity.search;

import android.support.v4.app.Fragment;

import com.jinjunhang.onlineclass.ui.activity.BaseMusicSingleFragmentActivity;
import com.jinjunhang.onlineclass.ui.fragment.search.SearchFragment;
import com.jinjunhang.onlineclass.ui.fragment.search.SearchResultFragment;

/**
 * Created by jinjunhang on 2018/4/4.
 */

public class SearchActivity extends BaseMusicSingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new SearchFragment();
    }
}
