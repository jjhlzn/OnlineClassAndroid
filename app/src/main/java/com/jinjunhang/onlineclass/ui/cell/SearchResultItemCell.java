package com.jinjunhang.onlineclass.ui.cell;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.AlbumType;

/**
 * Created by lzn on 16/6/20.
 */
public class SearchResultItemCell extends BaseListViewCell {


    public SearchResultItemCell(Activity activity) {
        super(activity);

    }


    @Override
    public boolean isSection() {
        return false;
    }

    @Override
    public ViewGroup getView() {
        View convertView = mActivity.getLayoutInflater().inflate(R.layout.list_item_search_result_item, null);

        return (LinearLayout)convertView.findViewById(R.id.container);
    }

    @Override
    public Object getContent() {
        return "";
    }
}
