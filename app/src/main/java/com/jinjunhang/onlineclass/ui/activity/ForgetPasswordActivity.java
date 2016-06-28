package com.jinjunhang.onlineclass.ui.activity;

import android.support.v4.app.Fragment;

import com.jinjunhang.framework.controller.SingleFragmentActivity;
import com.jinjunhang.onlineclass.ui.fragment.ForgetPasswordFragment;

/**
 * Created by lzn on 16/6/28.
 */
public class ForgetPasswordActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new ForgetPasswordFragment();
    }

    @Override
    protected String getActivityTitle() {
        return "忘记密码";
    }
}
