package com.jinjunhang.onlineclass.ui.cell;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lzn on 16/6/20.
 */
public interface ListViewCell {

    @Deprecated
    void setParentView(ViewGroup parent);

    Object getContent();

    @Deprecated
    boolean isSection();

    @Deprecated
    ViewGroup getView();

    ViewGroup getView( View convertView);

    void setActivity(Activity activity);

    void release();

    void onClick();

    int getItemViewType();

}
