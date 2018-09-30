package com.jinjunhang.framework.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.onlineclass.R;


/**
 * Created by lzn on 16/3/19.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = LogHelper.makeLogTag(BaseActivity.class);

    protected ImmersionBar mImmersionBar;

    protected void setContentView() {
        setContentView(R.layout.activity_fragment);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView();

        //初始化沉浸式
        if (isImmersionBarEnabled())
            initImmersionBar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null)
            mImmersionBar.destroy();  //在BaseActivity里销毁
    }


    /**
     * 是否可以使用沉浸式
     * Is immersion bar enabled boolean.
     *
     * @return the boolean
     */
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    protected void initImmersionBar() {
        //在BaseActivity里初始化
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();
    }



}
