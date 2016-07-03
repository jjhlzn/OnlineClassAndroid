package com.jinjunhang.onlineclass.ui.activity.album;

import android.support.v4.app.Fragment;

import com.jinjunhang.onlineclass.ui.activity.BaseMusicSingleFragmentActivity;
import com.jinjunhang.onlineclass.ui.fragment.album.CommentListFragment;

/**
 * Created by jjh on 2016-7-3.
 */
public class CommentListActivity extends BaseMusicSingleFragmentActivity {

    @Override
    protected String getActivityTitle() {
        return "所有评论";
    }

    @Override
    protected Fragment createFragment() {
        return new CommentListFragment();
    }
}
