package com.jinjunhang.onlineclass.ui.cell;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.AlbumType;
import com.jinjunhang.onlineclass.service.SearchResponse;

/**
 * Created by lzn on 16/6/20.
 */
public class SearchResultItemCell extends BaseListViewCell {

    private SearchResponse.SearchResult mSearchResult;
    private Activity mActivity;

    public SearchResultItemCell(Activity activity, SearchResponse.SearchResult result) {
        super(activity);
        this.mSearchResult = result;
        this.mActivity = activity;
    }

    @Override
    public boolean isSection() {
        return false;
    }

    @Override
    public ViewGroup getView() {
        View v = mActivity.getLayoutInflater().inflate(R.layout.list_item_search_result_item, null);

        ImageView image = (ImageView)v.findViewById(R.id.image);
        TextView name = (TextView)v.findViewById(R.id.name);
        TextView date = (TextView)v.findViewById(R.id.date);

        Glide
                .with(mActivity)
                .load(mSearchResult.getImage())
                .placeholder(R.drawable.course)
                .into(image);

        name.setText(mSearchResult.getContent());
        date.setText(mSearchResult.getDate());



        return (LinearLayout)v.findViewById(R.id.container);
    }

    @Override
    public Object getContent() {
        return "";
    }

    public SearchResponse.SearchResult getSearchResult() {
        return mSearchResult;
    }
}
