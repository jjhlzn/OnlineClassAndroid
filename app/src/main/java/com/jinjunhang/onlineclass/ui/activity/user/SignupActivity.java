package com.jinjunhang.onlineclass.ui.activity.user;

import android.support.v4.app.Fragment;

import com.jinjunhang.framework.controller.SingleFragmentActivity;
import com.jinjunhang.onlineclass.ui.fragment.user.SignupFragment;

/**
 * Created by lzn on 16/6/28.
 */
public class SignupActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new SignupFragment();
    }

    @Override
    protected String getActivityTitle() {
        return "注册";
    }
}
