package com.jinjunhang.onlineclass.ui.activity.user;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.ui.activity.BaseMusicSingleFragmentActivity;
import com.jinjunhang.onlineclass.ui.activity.MainActivity;
import com.jinjunhang.onlineclass.ui.fragment.user.UserProfilePhotoFragment;

/**
 * Created by jjh on 2016-6-30.
 */
public class UserProfilePhotoActivity extends BaseMusicSingleFragmentActivity {
    private static final String TAG = LogHelper.makeLogTag(UserProfilePhotoActivity.class);

    private UserProfilePhotoFragment mFragment;

    @Override
    protected String getActivityTitle() {
        return "头像";
    }

    @Override
    protected Fragment createFragment() {
        mFragment = new UserProfilePhotoFragment();
        return mFragment;
    }

    @Override
    public void onBackPressed() {
        LogHelper.d(TAG, "onBackPressed");
        Intent intent = new Intent();
        setResult( MainActivity.REQUEST_ME_UPDATE_USER_IAMGE,intent);
        finish();



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
        button.setImageResource(R.drawable.more);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFragment.onActivityResult(requestCode, resultCode, data);
    }
}
