package com.jinjunhang.onlineclass.ui.activity.user;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.jinjunhang.framework.controller.SingleFragmentActivity;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.ui.fragment.user.QRImageFragment;

/**
 * Created by jjh on 2016-7-1.
 */
public class QRImageActivity extends SingleFragmentActivity {
    @Override
    protected void createActionBar() {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View customView = getLayoutInflater().inflate(R.layout.actionbar, null);
        ImageButton rightButton = (ImageButton) customView.findViewById(R.id.actionbar_right_button);
        rightButton.setImageResource(R.drawable.share);
        rightButton.setVisibility(View.VISIBLE);

        getSupportActionBar().setCustomView(customView);
        Toolbar parent = (Toolbar) customView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

    }

    @Override
    protected String getActivityTitle() {
        return "我的二维码";
    }

    @Override
    protected Fragment createFragment() {
        return new QRImageFragment();
    }
}
