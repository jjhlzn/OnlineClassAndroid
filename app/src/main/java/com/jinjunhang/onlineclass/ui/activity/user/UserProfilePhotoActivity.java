package com.jinjunhang.onlineclass.ui.activity.user;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.ui.activity.BaseMusicSingleFragmentActivity;
import com.jinjunhang.onlineclass.ui.fragment.user.UserProfilePhotoFragment;

/**
 * Created by jjh on 2016-6-30.
 */
public class UserProfilePhotoActivity extends BaseMusicSingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new UserProfilePhotoFragment();
    }

    @Override
    protected void createActionBar() {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View customView = getLayoutInflater().inflate(R.layout.actionbar, null);
        getSupportActionBar().setCustomView(customView);
        Toolbar parent = (Toolbar) customView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        ImageButton button = (ImageButton) customView.findViewById(R.id.actionbar_right_button);
        button.setVisibility(View.VISIBLE);
        button.setImageResource(R.drawable.back);
    }
}
