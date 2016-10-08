package com.jinjunhang.onlineclass.ui.fragment.album;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.jinjunhang.framework.lib.LoadingAnimation;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.service.PagedServerResponse;
import com.jinjunhang.framework.service.ServerResponse;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.LiveSong;
import com.jinjunhang.onlineclass.model.ServiceLinkManager;
import com.jinjunhang.onlineclass.service.GetAlbumSongsRequest;
import com.jinjunhang.onlineclass.service.GetAlbumSongsResponse;
import com.jinjunhang.onlineclass.ui.activity.WebBrowserActivity;
import com.jinjunhang.onlineclass.ui.activity.album.AlbumDetailActivity;
import com.jinjunhang.onlineclass.ui.activity.MainActivity;
import com.jinjunhang.onlineclass.model.Album;
import com.jinjunhang.onlineclass.model.AlbumType;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.onlineclass.service.GetAlbumsRequest;
import com.jinjunhang.framework.controller.PagableController;
import com.jinjunhang.onlineclass.ui.activity.album.SongActivity;
import com.jinjunhang.onlineclass.ui.fragment.BottomPlayerFragment;
import com.jinjunhang.onlineclass.ui.lib.BaseListViewOnItemClickListener;
import com.jinjunhang.player.ExoPlayerNotificationManager;
import com.jinjunhang.player.MusicPlayer;
import com.jinjunhang.player.utils.LogHelper;

import java.util.ArrayList;

/**
 * Created by lzn on 16/6/10.
 *
 * 改Activity加载对应类型的所有的Album，需要有分页支持
 *
 */
public class AlbumListFragment extends BottomPlayerFragment implements  SingleFragmentActivity.OnBackPressedListener,
        AbsListView.OnScrollListener, PagableController.PagableErrorResponseHandler {
    public final static String EXTRA_ALBUMTYPE = "extra_albumtype";

    private final static String TAG = "AlbumListFragment";

    private PagableController mPagableController;

    private AlbumType mAlbumType;
    private ListView mListView;
    private LoadingAnimation mLoading;
    private ExoPlayerNotificationManager mNotificationManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_fragment_pushdownrefresh, container, false);

        mNotificationManager = ExoPlayerNotificationManager.getInstance(getActivity());
        mLoading = new LoadingAnimation(getActivity());
        mListView = (ListView) v.findViewById(R.id.listView);
        //去掉列表的分割线
        mListView.setDividerHeight(0);
        mListView.setDivider(null);

        mListView.setOnItemClickListener(new BaseListViewOnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Album album = (Album) (mPagableController.getPagableArrayAdapter().getItem(position));

                if (!album.isReady()) {
                    Utils.showErrorMessage(getActivity(), "该课程未上线，敬请期待！");
                    return;
                }

                GetAlbumSongsRequest request = new GetAlbumSongsRequest();
                request.setAlbum(album);
                request.setPageIndex(0);
                request.setPageSize(200);
                new GetAlbumSongsTask().execute(request);
            }
        });

        mAlbumType = (AlbumType)getActivity().getIntent().getSerializableExtra(EXTRA_ALBUMTYPE);


        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)v.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2, R.color.refresh_progress_3);

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
        mPagableController.setErrorResponseHanlder(this);
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


    @Override
    public void handle(PagedServerResponse resp) {
        if (resp.getStatus() == ServerResponse.NO_PERMISSION) {
            showVipBuyMessage(getActivity(), resp.getErrorMessage());
        } else {
            Utils.showMessage(getActivity(), resp.getErrorMessage());
        }
    }

    private void showVipBuyMessage(Context context, String message) {
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(context);
        dlgAlert.setMessage(message);
        dlgAlert.setPositiveButton("购买", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(getActivity(), WebBrowserActivity.class)
                        .putExtra(WebBrowserActivity.EXTRA_TITLE, "课程购买")
                        .putExtra(WebBrowserActivity.EXTRA_URL, ServiceLinkManager.MyAgentUrl());
                getActivity().startActivity(i);
            }
        });
        dlgAlert.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dlgAlert.setCancelable(false);

        dlgAlert.create().show();
    }

    private class GetAlbumSongsTask extends AsyncTask<GetAlbumSongsRequest, Void, GetAlbumSongsResponse> {

        private GetAlbumSongsRequest request;
        @Override
        protected GetAlbumSongsResponse doInBackground(GetAlbumSongsRequest... params) {
            request = params[0];
            return new BasicService().sendRequest(request);
        }

        @Override
        protected void onPostExecute(GetAlbumSongsResponse resp) {
            super.onPostExecute(resp);
            mLoading.hide();
            if (resp.getStatus() == ServerResponse.NO_PERMISSION) {
                showVipBuyMessage(getActivity(), resp.getErrorMessage());
                return;
            }

            if (!resp.isSuccess()) {
                LogHelper.e(TAG, resp.getErrorMessage());
                Utils.showErrorMessage(getActivity(), resp.getErrorMessage());
                return;
            }

            if (resp.getResultSet().size() == 1) {
                LiveSong song = (LiveSong)resp.getResultSet().get(0);
                MusicPlayer musicPlayer = MusicPlayer.getInstance(getActivity());
                if (!musicPlayer.isPlay(song)) {
                    musicPlayer.pause();
                    musicPlayer.play(resp.getResultSet(), 0);
                    mNotificationManager.display();
                }
                Intent i = new Intent(getActivity(), SongActivity.class)
                        .putExtra(BaseSongFragment.EXTRA_SONG, song);
                startActivity(i);
                return;
            }

            Album album = request.getAlbum();
            Intent i = new Intent(getActivity(), AlbumDetailActivity.class);
            i.putExtra(AlbumDetailFragment.EXTRA_ALBUM, album);
            startActivity(i);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoading.show("");
        }
    }
}


