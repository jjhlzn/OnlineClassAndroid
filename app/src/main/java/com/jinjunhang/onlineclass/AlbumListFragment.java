package com.jinjunhang.onlineclass;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jinjunhang.onlineclass.lib.DownloadImageTask;
import com.jinjunhang.onlineclass.lib.Utils;
import com.jinjunhang.onlineclass.model.Album;
import com.jinjunhang.onlineclass.model.AlbumType;
import com.jinjunhang.onlineclass.service.BasicService;
import com.jinjunhang.onlineclass.service.GetAlbumsRequest;
import com.jinjunhang.onlineclass.service.GetAlbumsResponse;
import com.jinjunhang.onlineclass.service.ServerResponse;
import com.jinjunhang.ui.lib.PagableController;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Exchanger;

/**
 * Created by lzn on 16/6/10.
 *
 * 改Activity加载对应类型的所有的Album，需要有分页支持
 *
 */
public class AlbumListFragment extends android.support.v4.app.Fragment implements  SingleFragmentActivity.OnBackPressedListener,
        AbsListView.OnScrollListener, SwipeRefreshLayout.OnRefreshListener {
    public final static String EXTRA_ALBUMTYPE = "extra_albumtype";

    private final static String TAG = "AlbumListActivity";

    private PagableController mPagableController;

    private AlbumType mAlbumType;
    private ListView mListView;
    private AlbumListAdapter mAlbumListAdapter;

    //用于load more
    private View mFooterView;
    private boolean mIsLoading;
    private boolean mMoreDataAvailable;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_fragment_pushdownrefresh, container, false);

        mListView = (ListView) v.findViewById(R.id.listView);
        mAlbumListAdapter = new AlbumListAdapter(new ArrayList<Album>());
        mListView.setAdapter(mAlbumListAdapter);

        mAlbumType = (AlbumType)getActivity().getIntent().getSerializableExtra(EXTRA_ALBUMTYPE);
        mIsLoading = false;
        mMoreDataAvailable = true;

        setFootView();
        return v;
    }


    @Override
    public void doBack() {
        Intent i = new Intent(getActivity(), MainActivity.class);
        i.putExtra(MainActivity.EXTRA_TAB, 1);
        startActivity(i);
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
         //此方法不需要实现
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if (!mMoreDataAvailable)
            return;

        int lastInScreen = firstVisibleItem + visibleItemCount;

        if (!mIsLoading) {
            if (lastInScreen == totalItemCount) {
                Log.d(TAG, "start loading more");
                new GetAlbumsTask().execute();
            }
        }
    }

    private void setFootView() {
        mFooterView = LayoutInflater.from(getActivity()).inflate(R.layout.loading_view, null);

        resetFootView();
        mListView.addFooterView(mFooterView, null, false);
        mListView.setOnScrollListener(this);
    }

    private void resetFootView() {
        if (!mMoreDataAvailable) {
            mFooterView.findViewById(R.id.loading_progressbar).setVisibility(View.GONE);
            if (mAlbumListAdapter.getCount() > 0)
                ((TextView)mFooterView.findViewById(R.id.loading_message)).setText("已加载全部数据");
            else {
                ((TextView) mFooterView.findViewById(R.id.loading_message)).setText("没有找到任何数据");
                ((TextView) mFooterView.findViewById(R.id.loading_message)).setTextSize(15);
            }
        }
    }


    private class GetAlbumsTask extends AsyncTask<Void, Void, GetAlbumsResponse> {

        @Override
        protected GetAlbumsResponse doInBackground(Void... params) {
            GetAlbumsRequest request = new GetAlbumsRequest(mAlbumType);
            return new BasicService().sendRequest(request);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mIsLoading = true;
        }

        @Override
        protected void onPostExecute(GetAlbumsResponse resp) {
            super.onPostExecute(resp);

            if (resp.getStatus() != ServerResponse.SUCCESS) {
                Log.e(TAG, "resp return error, status = " + resp.getStatus() + ", errorMessage = " + resp.getErrorMessage());
                Utils.showMessage(getActivity(), resp.getErrorMessage());
                return;
            }

            mAlbumListAdapter.addMoreAlbums(resp.getResultSet());

            if (resp.getTotalNumber() <= mAlbumListAdapter.getCount()) {
                mMoreDataAvailable = false;
            } else {
                mMoreDataAvailable = true;
            }

            mIsLoading = false;
            Log.d(TAG, "loading complete");

            resetFootView();

        }
    }

    private class AlbumListAdapter extends ArrayAdapter<Album> {
        private List<Album> mAlbums;

        public AlbumListAdapter(List<Album> albums) {
            super(getActivity(), 0, albums);
            mAlbums = albums;
        }

        public void addMoreAlbums(List<Album> albums) {
            mAlbums.addAll(albums);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mAlbums.size();
        }

        @Override
        public Album getItem(int position) {
            return mAlbums.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_album, null);
            }

            Album album = getItem(position);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.album_list_item_image);
            Glide.with(AlbumListFragment.this).load(album.getImage()).into(imageView);


            TextView nameTextView = (TextView) convertView.findViewById(R.id.album_list_item_name);
            nameTextView.setText(album.getName());

            TextView authorTextView = (TextView) convertView.findViewById(R.id.album_list_item_author);
            authorTextView.setText(album.getAuthor());

            TextView listenTextView = (TextView) convertView.findViewById(R.id.album_list_item_listernCountAndCount);
            listenTextView.setText(album.getListenCount()+ ", " + album.getCount() + "集");

            return convertView;
        }
    }
}


