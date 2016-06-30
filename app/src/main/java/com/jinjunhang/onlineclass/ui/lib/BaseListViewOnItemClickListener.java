package com.jinjunhang.onlineclass.ui.lib;

import android.content.Intent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;

import com.jinjunhang.onlineclass.model.Album;
import com.jinjunhang.onlineclass.ui.activity.AlbumDetailActivity;
import com.jinjunhang.onlineclass.ui.fragment.AlbumDetailFragment;

/**
 * Created by jjh on 2016-6-30.
 */
public class BaseListViewOnItemClickListener implements AdapterView.OnItemClickListener {

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Animation animation1 = new AlphaAnimation(0.7f, 0.3f);
        animation1.setDuration(500);
        view.startAnimation(animation1);

    }

    public static void onItemClickEffect(AdapterView<?> parent, View view, int position, long id) {
        Animation animation1 = new AlphaAnimation(0.7f, 0.3f);
        animation1.setDuration(500);
        view.startAnimation(animation1);

    }
}
