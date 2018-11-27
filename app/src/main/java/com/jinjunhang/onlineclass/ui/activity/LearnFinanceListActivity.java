package com.jinjunhang.onlineclass.ui.activity;

import android.support.v4.app.Fragment;

import com.jinjunhang.framework.controller.SingleFragmentActivity;
import com.jinjunhang.onlineclass.ui.fragment.LearnFinanceListFragment;

public class LearnFinanceListActivity extends SingleFragmentActivity {

    @Override
    protected boolean hasActionBar() {
        return false;
    }

    @Override
    protected Fragment createFragment() {
        return new LearnFinanceListFragment();
    }
}
