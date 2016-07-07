package com.jinjunhang.onlineclass.ui.fragment.album;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.jinjunhang.framework.controller.SingleFragmentActivity;
import com.jinjunhang.framework.service.PagedServerResponse;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.ui.activity.album.AlbumDetailActivity;
import com.jinjunhang.onlineclass.ui.activity.MainActivity;
import com.jinjunhang.onlineclass.model.Album;
import com.jinjunhang.onlineclass.model.AlbumType;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.onlineclass.service.GetAlbumsRequest;
import com.jinjunhang.framework.controller.PagableController;
import com.jinjunhang.onlineclass.ui.fragment.BottomPlayerFragment;
import com.jinjunhang.onlineclass.ui.lib.BaseListViewOnItemClickListener;
import com.jinjunhang.player.utils.LogHelper;

import java.util.ArrayList;

/**
 * Created by lzn on 16/6/10.
 *
 * 改Activity加载对应类型的所有的Album，需要有分页支持
 *
 */
public class AlbumListFragment extends BottomPlayerFragment implements  SingleFragmentActivity.OnBackPressedListener,
        AbsListView.OnScrollListener {
    public final static String EXTRA_ALBUMTYPE = "extra_albumtype";

    private final static String TAG = "AlbumListFragment";

    private PagableController mPagableController;

    private AlbumType mAlbumType;
    private ListView mListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_fragment_pushdownrefresh, container, false);

        mListView = (ListView) v.findViewById(R.id.listView);
        //去掉列表的分割线
        mListView.setDividerHeight(0);
        mListView.setDivider(null);

        mListView.setOnItemClickListener(new BaseListViewOnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                super.onItemClick(parent, view, position, id);

                Album album = (Album) (mPagableController.getPagableArrayAdapter().getItem(position));
                Intent i = new Intent(getActivity(), AlbumDetailActivity.class);
                i.putExtra(AlbumDetailFragment.EXTRA_ALBUM, album);
                startActivity(i);
            }
        });

        mAlbumType = (AlbumType)getActivity().getIntent().getSerializableExtra(EXTRA_ALBUMTYPE);


        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)v.findViewById(R.id.swipe_refresh_layout);

        //设置PagableController
        mPagableController = new PagableController(getActivity(), mListView);
        LogHelper.d(TAG, "mAlbumType = " + mAlbumType.getName());
        if (mAlbumType.getName().equals(AlbumType.LiveAlbumType.getName()) || mAlbumType.getName().equals(AlbumType.VipAlbumType.getName())) {
            mPagableController.setShowLoadCompleteTip(false);
        }
        LogHelper.d(TAG, "isShowLoadCompleteTip = " + mPagableController.isShowLoadCompleteTip());
        AlbumListAdapter adapter = new AlbumListAdapter(mPagableController, new ArrayList<Album>());
        mPagableController.setSwipeRefreshLayout(swipeRefreshLayout);
        mPagableController.setPagableArrayAdapter(adapter);
        mPagableController.setPagableRequestHandler(new AlbumListHanlder());
        mPagableController.setOnScrollListener(this);

        //((FrameLayout)v.findViewById(R.id.fragmentContainer)).addView(mPlayerController.getView());
        mPlayerController.attachToView((FrameLayout)v.findViewById(R.id.fragmentContainer));
        return v;
    }


    @Override
    public void doBack() {
        Intent i = new Intent(getActivity(), MainActivity.class);
        i.putExtra(MainActivity.EXTRA_TAB, 1);
        startActivity(i);
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
         //此方法不需要实现
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        mPagableController.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
    }

    private class AlbumListHanlder implements PagableController.PagableRequestHandler {
        @Override
        public PagedServerResponse handle() {
            GetAlbumsRequest request = new GetAlbumsRequest(mAlbumType);
            request.setPageIndex(mPagableController.getPageIndex());
            return new BasicService().sendRequest(request);
        }
    }





}


