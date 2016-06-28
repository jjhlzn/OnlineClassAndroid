package com.jinjunhang.onlineclass.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.jinjunhang.onlineclass.db.LoginUserDao;

/**
 * Created by lzn on 16/6/19.
 */
public class LaunchActivity extends Activity {

    private LoginUserDao mLoginUserDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLoginUserDao = LoginUserDao.getInstance(this);

        Intent i;
        if (mLoginUserDao.get() == null) {
            i = new Intent(this, LoginActivity.class);
        } else {
            i = new Intent(this, MainActivity.class);
        }
        startActivity(i);
    }
}
