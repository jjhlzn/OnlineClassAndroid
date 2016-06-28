package com.jinjunhang.onlineclass;

import com.orm.SugarApp;
import com.orm.SugarContext;

/**
 * Created by lzn on 16/6/27.
 */
public class CustomApplication extends SugarApp
{

    @Override
    public void onCreate()
    {
        super.onCreate();
        SugarContext.init(this);

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }
}