package com.jinjunhang.onlineclass.ui.activity.user;

import android.support.v4.app.Fragment;

import com.jinjunhang.framework.controller.SingleFragmentActivity;
import com.jinjunhang.onlineclass.ui.fragment.user.SetPhoneFragment;

public class SetPhoneActivity extends SingleFragmentActivity {

    @Override
    protected boolean hasActionBar() {
        return false;
    }

    @Override
    protected Fragment createFragment() {
        return new SetPhoneFragment();
    }
}
