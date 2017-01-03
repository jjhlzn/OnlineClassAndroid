package com.jinjunhang.onlineclass.ui.lib;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDexApplication;

/**
 * Created by lzn on 16/6/27.
 */
public class CustomApplication extends MultiDexApplication
{

    private static Application instance;
    public static Application get() { return instance; }


    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = this;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}