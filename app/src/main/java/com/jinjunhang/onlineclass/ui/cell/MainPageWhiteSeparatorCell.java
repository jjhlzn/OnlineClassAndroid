package com.jinjunhang.onlineclass.ui.cell;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.onlineclass.R;

import org.w3c.dom.Text;

/**
 * Created by lzn on 16/6/20.
 */
public class MainPageWhiteSeparatorCell extends BaseListViewCell {
    private static final String TAG = LogHelper.makeLogTag(MainPageWhiteSeparatorCell.class);

    public MainPageWhiteSeparatorCell(Activity activity) {
        super(activity);
    }

    @Override
    public boolean isSection() {
        return true;
    }


    private int getHeight() {
        int screenWidth =   Utils.getScreenWidth(mActivity);

        int result = 20;
        if (screenWidth >= 1440) {
            result = 126;
        }
        return result;
    }

    @Override
    public ViewGroup getView() {
        return makeView();
    }


    private ViewGroup makeView() {

        LinearLayout layout = new LinearLayout(mActivity);
        layout.setOrientation(LinearLayout.VERTICAL);

        int width = Utils.getScreenWidth(mActivity);
        ViewGroup.LayoutParams params =  new AbsListView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        // Changes the height and width to the specified *pixels*
        int cellHeight = getHeight();
        LogHelper.d(TAG, "seperator height: ", getHeight());
        params.width = width;
        params.height = cellHeight;

        layout.setLayoutParams(params);
        layout.setBackgroundColor(Color.rgb(255, 255, 255)); //white
        return layout;
    }
}
