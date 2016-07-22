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
    protected String getActivityTitle() {
        return "我的二维码";
    }

    @Override
    protected Fragment createFragment() {
        return new QRImageFragment();
    }
}
