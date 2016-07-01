package com.jinjunhang.onlineclass.ui.activity.user;

import android.support.v4.app.Fragment;

import com.jinjunhang.framework.controller.SingleFragmentActivity;
import com.jinjunhang.onlineclass.ui.fragment.user.PersonalInfoFragment;

/**
 * Created by jjh on 2016-7-1.
 */
public class PersonalInfoActivity extends SingleFragmentActivity {

    @Override
    protected boolean isNeedPushDownFresh() {
        return false;
    }

    @Override
    protected Fragment createFragment() {
        return new PersonalInfoFragment();
    }
}
