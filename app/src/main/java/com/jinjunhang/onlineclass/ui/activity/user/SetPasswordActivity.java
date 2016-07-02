package com.jinjunhang.onlineclass.ui.activity.user;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jinjunhang.framework.controller.SingleFragmentActivity;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.ui.fragment.user.SetPasswordFragment;

/**
 * Created by jjh on 2016-7-2.
 */
public class SetPasswordActivity extends SingleFragmentActivity {

    @Override
    protected String getActivityTitle() {
        return "重设密码";
    }

    @Override
    protected Fragment createFragment() {
        return new SetPasswordFragment();
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
