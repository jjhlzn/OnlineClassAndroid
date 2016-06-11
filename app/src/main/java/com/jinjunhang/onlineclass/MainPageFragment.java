package com.jinjunhang.onlineclass;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jinjunhang.onlineclass.model.Album;
import com.jinjunhang.onlineclass.model.AlbumType;

import java.util.List;

/**
 * Created by lzn on 16/6/10.
 */
public class MainPageFragment extends android.support.v4.app.Fragment {


    private static final String TAG = "MainPageFragment";
    private List<AlbumType> mAlbumTypes = AlbumType.getAllAlbumType();

    private ListView mListView;
    private AlbumTypeAdapter mAlbumTypeAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_fragment_pushdownrefresh, container, false);

        //不能下拉刷新
        v.findViewById(R.id.swipe_refresh_layout).setEnabled(false);

        mListView = (ListView) v.findViewById(R.id.listView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AlbumType albumType = mAlbumTypeAdapter.getItem(position);

                Intent i = new Intent(getActivity(), AlbumListActivity.class);
                i.putExtra(AlbumListFragment.EXTRA_ALBUMTYPE, albumType);
                startActivity(i);
            }
        });
        return v;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAlbumTypeAdapter = new AlbumTypeAdapter(mAlbumTypes);
        mListView.setAdapter(mAlbumTypeAdapter);
    }


    private class AlbumTypeAdapter extends ArrayAdapter<AlbumType> {
        private List<AlbumType> mAlbumTypes;

        public AlbumTypeAdapter(List<AlbumType> albumTypes) {
            super(getActivity(), 0, albumTypes);
            mAlbumTypes = albumTypes;
        }

        @Override
        public int getCount() {
            return mAlbumTypes.size();
        }

        @Override
        public AlbumType getItem(int position) {
            return mAlbumTypes.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_albumtype, null);
            }

            AlbumType albumType = getItem(position);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.albumType_list_item_image);
            imageView.setImageResource(albumType.getImage());

            TextView nameTextView = (TextView) convertView.findViewById(R.id.albumType_list_item_name);
            nameTextView.setText(albumType.getName());

            return convertView;
        }
    }




}
