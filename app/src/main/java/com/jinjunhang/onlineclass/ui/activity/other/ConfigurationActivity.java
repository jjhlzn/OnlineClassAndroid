package com.jinjunhang.onlineclass.ui.activity.other;

import android.support.v4.app.Fragment;

import com.jinjunhang.framework.controller.SingleFragmentActivity;
import com.jinjunhang.onlineclass.ui.fragment.other.ConfigurationFragment;

/**
 * Created by lzn on 16/5/7.
 */
public class ConfigurationActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new ConfigurationFragment();
    }

    @Override
    protected String getActivityTitle() {
        return "服务器配置";
    }
}
