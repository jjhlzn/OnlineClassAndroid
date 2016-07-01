package com.jinjunhang.onlineclass.ui.lib;

import android.app.Application;
import android.content.Context;

import com.orm.SugarApp;
import com.orm.SugarContext;

/**
 * Created by lzn on 16/6/27.
 */
public class CustomApplication extends SugarApp
{

    private static Application instance;
    public static Application get() { return instance; }


    @Override
    public void onCreate()
    {
        super.onCreate();
        SugarContext.init(this);
        instance = this;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }
}