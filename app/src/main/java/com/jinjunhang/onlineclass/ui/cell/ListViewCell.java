package com.jinjunhang.onlineclass.ui.cell;

import android.app.Activity;
import android.view.ViewGroup;

/**
 * Created by lzn on 16/6/20.
 */
public interface ListViewCell {

    boolean isSection();

    ViewGroup getView();

    void setActivity(Activity activity);

    void release();

}
