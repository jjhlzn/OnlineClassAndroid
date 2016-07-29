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
public class AlbumTypeCell2 extends AlbumTypeCell {

    private String mDescription = "";

    public AlbumTypeCell2(Activity activity, AlbumType albumType) {
        super(activity, albumType);
    }

    public AlbumTypeCell2(Activity activity, AlbumType albumType, String description) {
        super(activity, albumType);
        mDescription = description;
    }

    public AlbumType getAlbumType() {
        return mAlbumType;
    }

    public void setAlbumType(AlbumType albumType) {
        mAlbumType = albumType;
    }

    @Override
    public boolean isSection() {
        return false;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    @Override
    public ViewGroup getView() {
        View convertView = mActivity.getLayoutInflater().inflate(R.layout.list_item_albumtype2, null);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.albumType_list_item_image);
        imageView.setImageResource(mAlbumType.getImage());

        TextView nameTextView = (TextView) convertView.findViewById(R.id.albumType_list_item_name);
        nameTextView.setText(mAlbumType.getName());

        TextView descriptionTextView = (TextView) convertView.findViewById(R.id.albumType_list_item_detail);
        descriptionTextView.setText(mDescription);

        return (LinearLayout)convertView.findViewById(R.id.list_item_albumtype_viewgroup);
    }

    @Override
    public Object getContent() {
        return mAlbumType;
    }
}
