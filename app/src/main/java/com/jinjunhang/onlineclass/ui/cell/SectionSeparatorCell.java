package com.jinjunhang.onlineclass.ui.cell;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jinjunhang.onlineclass.R;

/**
 * Created by lzn on 16/6/20.
 */
public class SectionSeparatorCell extends BaseListViewCell {

    public SectionSeparatorCell(Activity activity) {
        super(activity);
    }

    @Override
    public boolean isSection() {
        return true;
    }

    @Override
    public ViewGroup getView() {
        View convertView = mActivity.getLayoutInflater().inflate(R.layout.list_item_separator, null);
        return (LinearLayout)convertView.findViewById(R.id.list_item_albumtype_viewgroup);
    }
}
