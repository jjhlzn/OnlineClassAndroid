package com.jinjunhang.onlineclass.ui.activity.other;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.jinjunhang.onlineclass.ui.activity.BaseMusicSingleFragmentActivity;
import com.jinjunhang.onlineclass.ui.fragment.other.ExtendFunctionFragment;

/**
 * Created by jjh on 2016-7-5.
 */
public class ExtendFunctionActivity extends BaseMusicSingleFragmentActivity {

    @Override
    protected boolean isNeedPushDownFresh() {
        return false;
    }

    @Override
    protected String getActivityTitle() {
        return "功能";
    }

    @Override
    protected Fragment createFragment() {
        return new ExtendFunctionFragment();
    }
}
