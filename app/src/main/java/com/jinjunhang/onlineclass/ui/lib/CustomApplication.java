package com.jinjunhang.onlineclass.ui.lib;

import android.app.Application;
import android.content.Context;

import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;

/**
 * Created by lzn on 16/6/27.
 */
public class CustomApplication extends Application
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

    public DataSource.Factory buildDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultDataSourceFactory(this, bandwidthMeter,
                buildHttpDataSourceFactory(bandwidthMeter));
    }

    public HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultHttpDataSourceFactory("OnclineClassExoPlayer", bandwidthMeter);
    }
}