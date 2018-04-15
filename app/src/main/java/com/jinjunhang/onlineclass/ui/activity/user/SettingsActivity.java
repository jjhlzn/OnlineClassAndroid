package com.jinjunhang.onlineclass.ui.activity.user;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jinjunhang.framework.controller.SingleFragmentActivity;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.ui.fragment.SettingsFragment;
import com.jinjunhang.onlineclass.ui.fragment.user.SetPasswordFragment;

/**
 * Created by jinjunhang on 2018/4/15.
 */

public class SettingsActivity extends SingleFragmentActivity {

    @Override
    protected String getActivityTitle() {
        return "设置";
    }

    @Override
    protected Fragment createFragment() {
        return new SettingsFragment();
    }

    @Override
    protected void createActionBar() {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View customView = getLayoutInflater().inflate(R.layout.actionbar_button, null);
        getSupportActionBar().setCustomView(customView);
        Toolbar parent = (Toolbar) customView.getParent();
        parent.setContentInsetsAbsolute(0, 0);
    }
}
