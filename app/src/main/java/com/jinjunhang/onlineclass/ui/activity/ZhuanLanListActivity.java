package com.jinjunhang.onlineclass.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.jinjunhang.framework.controller.SingleFragmentActivity;
import com.jinjunhang.onlineclass.ui.fragment.ZhuanLanListFragment;
import com.jinjunhang.onlineclass.ui.fragment.album.AlbumListFragment;

public class ZhuanLanListActivity extends SingleFragmentActivity {

    @Override
    protected boolean hasActionBar() {
        return false;
    }

    @Override
    protected Fragment createFragment() {
        return new ZhuanLanListFragment();
    }

    @Override
    protected String getActivityTitle() {
        return "专栏列表";
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
}