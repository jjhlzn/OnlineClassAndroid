package com.jinjunhang.onlineclass.ui.fragment.album;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jinjunhang.framework.controller.PagableController;
import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.Album;
import com.jinjunhang.onlineclass.model.AlbumType;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by lzn on 16/6/29.
 */
public class AlbumListAdapter extends PagableController.PagableArrayAdapter<Album> {
    private final String TAG = LogHelper.makeLogTag(AlbumListAdapter.class);
    private Activity mActivity;

    public AlbumListAdapter(PagableController pagableController, List<Album> dataSet){
        super(pagableController, dataSet);
        mActivity = pagableController.getActivity();
    }

    private  boolean isDummyAlbumType(Album album) {
        return album.getAlbumType().getTypeCode().equals(AlbumType.DummyAlbumType.getTypeCode());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Album album = getItem(position);

        if (isDummyAlbumType(album)) {
            convertView = mActivity.getLayoutInflater().inflate(R.layout.list_item_albums_separator, null);
            ((TextView)convertView.findViewById(R.id.albums_separator_text)).setText(album.getName());
        }  else if (album.isLive()) {
            convertView = mActivity.getLayoutInflater().inflate(R.layout.list_item_album_live, null);
            ((TextView)convertView.findViewById(R.id.listen_people_label)).setText(album.getListenCount());
            TextView descTextView = ((TextView)convertView.findViewById(R.id.album_list_item_desc));

            if (album.hasPlayTimeDesc()) {
                descTextView.setTextColor(mActivity.getResources().getColor(R.color.red));
                descTextView.setText(album.getPlayTimeDesc());
            } else {
                descTextView.setText(album.getDesc());
            }

            if (!album.isReady()) {
                ((ImageView)convertView.findViewById(R.id.user_image)).setImageResource(R.drawable.user1_1);
            }
            if (album.isPlaying()) {
                ((TextView)convertView.findViewById(R.id.album_list_playing_desc)).setVisibility(View.VISIBLE);
            } else {
                ((TextView)convertView.findViewById(R.id.album_list_playing_desc)).setVisibility(View.INVISIBLE);
            }

        } else {
            convertView = mActivity.getLayoutInflater().inflate(R.layout.list_item_album, null);
            TextView authorTextView = (TextView) convertView.findViewById(R.id.album_list_item_author);
            authorTextView.setText(album.getAuthor());

            TextView listenTextView = (TextView) convertView.findViewById(R.id.album_list_item_listernCountAndCount);
            listenTextView.setText(album.getListenCount()+ ", " + album.getCount() + "集");
        }


        if (AlbumType.DummyAlbumType.getTypeCode().equals(album.getAlbumType().getTypeCode())) {
            return convertView;
        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.album_list_item_image);
        if (imageView != null)
            Glide.with(mActivity).load(album.getImage()).into(imageView);

        TextView nameTextView = (TextView) convertView.findViewById(R.id.album_list_item_name);
        if (nameTextView != null)
            nameTextView.setText(album.getName());



        return convertView;
    }
}