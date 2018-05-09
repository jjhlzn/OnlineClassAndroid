package com.jinjunhang.onlineclass.ui.fragment.mainpage;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jinjunhang.framework.lib.LoadingAnimation;
import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.framework.service.ServerResponse;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.Album;
import com.jinjunhang.onlineclass.model.LiveSong;
import com.jinjunhang.onlineclass.model.ZhuanLan;
import com.jinjunhang.onlineclass.service.GetAlbumSongsRequest;
import com.jinjunhang.onlineclass.service.GetAlbumSongsResponse;
import com.jinjunhang.onlineclass.service.GetTuijianCoursesRequest;
import com.jinjunhang.onlineclass.service.GetTuijianCoursesResponse;
import com.jinjunhang.onlineclass.service.GetZhuanLanAndTuijianCourseRequest;
import com.jinjunhang.onlineclass.service.GetZhuanLanAndTuijianCourseResponse;
import com.jinjunhang.onlineclass.ui.activity.WebBrowserActivity;
import com.jinjunhang.onlineclass.ui.activity.album.NewLiveSongActivity;
import com.jinjunhang.onlineclass.ui.cell.BaseListViewCell;
import com.jinjunhang.onlineclass.ui.cell.MainPageCourseCell;
import com.jinjunhang.onlineclass.ui.cell.ListViewCell;
import com.jinjunhang.onlineclass.ui.cell.SectionSeparatorCell;
import com.jinjunhang.onlineclass.ui.cell.mainpage.HeaderAdvCell;
import com.jinjunhang.onlineclass.ui.cell.mainpage.TuijianCourseHeaderCell;
import com.jinjunhang.onlineclass.ui.cell.mainpage.ZhuanLanCell;
import com.jinjunhang.onlineclass.ui.cell.mainpage.ZhuanLanHeaderCell;
import com.jinjunhang.onlineclass.ui.lib.ExtendFunctionManager;
import com.jinjunhang.onlineclass.ui.lib.ExtendFunctoinVariableInfoManager;
import com.jinjunhang.player.ExoPlayerNotificationManager;
import com.jinjunhang.player.MusicPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jinjunhang on 2018/3/31.
 */

public class Page implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = LogHelper.makeLogTag(Page.class);

    public View v;
    private LoadingAnimation mLoading;
    private ListView mListView;
    private ExtendFunctionManager mFunctionManager;
    private MainPageAdapter mMainPageAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<ListViewCell> mCells = new ArrayList<>();
    private String mType;
    private HeaderAdvCell mHeaderAdvCell;
    private List<Album> mCourses;
    private List<ZhuanLan> mZhuanLans;
    private FragmentActivity mActivity;
    private boolean mIsLoading;
    private ExoPlayerNotificationManager mNotificationManager;

    public Page(FragmentActivity activity, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, String type) {
        mNotificationManager = ExoPlayerNotificationManager.getInstance(activity);
        mActivity = activity;
        mZhuanLans = new ArrayList<>();
        mCourses = new ArrayList<>();
        this.mType = type;

        v = inflater.inflate(R.layout.activity_fragment_pushdownrefresh, container, false);

        mListView = (ListView) v.findViewById(R.id.listView);

        mLoading = new LoadingAnimation(activity, (ViewGroup)v.findViewById(R.id.fragmentContainer));

        //去掉列表的分割线
        mListView.setDividerHeight(0);
        mListView.setDivider(null);

        int maxShowRows = 10;

        mFunctionManager = new ExtendFunctionManager(ExtendFunctoinVariableInfoManager.getInstance(), maxShowRows,
                activity, false, type);

        if (mCells.size() == 0) {
            mHeaderAdvCell = new HeaderAdvCell(activity, mLoading, mType);
            mCells.add(mHeaderAdvCell);

            //mCells.add(new CourseNotifyCell(activity));
            mCells.add(new SectionSeparatorCell(activity));

            int functionRowCount = mFunctionManager.getRowCount();
            for (int i = 0; i < functionRowCount; i++) {
                mCells.add(mFunctionManager.getCell(i));
            }
            mCells.add(new SectionSeparatorCell(activity));
        }

        mHeaderAdvCell.updateAds();
        mHeaderAdvCell.updateTouTiao();

        mMainPageAdapter = new MainPageAdapter(activity, mCells);
        mListView.setAdapter(mMainPageAdapter);

        //可以下拉刷新
        mSwipeRefreshLayout =  (SwipeRefreshLayout)v.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < 5) {
                    return;
                }

                BaseListViewCell cell = (BaseListViewCell) mMainPageAdapter.getItem(position);
                if (cell instanceof ZhuanLanCell) {
                    ZhuanLanCell zhuanLanCell = (ZhuanLanCell)cell;
                    ZhuanLan zhuanLan = zhuanLanCell.getZhuanLan();

                    Intent i = new Intent(mActivity, WebBrowserActivity.class)
                            .putExtra(WebBrowserActivity.EXTRA_TITLE, zhuanLan.getName())
                            .putExtra(WebBrowserActivity.EXTRA_URL, zhuanLan.getUrl());
                    mActivity.startActivity(i);

                } else if (cell instanceof MainPageCourseCell) {
                    MainPageCourseCell courseCell = (MainPageCourseCell)cell;
                    Album album = courseCell.getCourse();
                    if (!album.isReady()) {
                        Utils.showErrorMessage(mActivity, "该课程未上线，敬请期待！");
                        return;
                    }

                    mLoading.show("");

                    GetAlbumSongsRequest request = new GetAlbumSongsRequest();
                    request.setAlbum(album);
                    request.setPageIndex(0);
                    request.setPageSize(200);
                    new GetAlbumSongsTask().execute(request);
                }


            }
        });

        new GetZhuanLanAndTuijianCoursesTask().execute();
    }

    private void setZhuanLanAndCoursesView() {
        if (mCells.size() > 5) {
            int size = mCells.size();
            for(int i = 0; i < size - 5; i++) {
                mCells.remove(5);
            }
        }
        mCells.add(new ZhuanLanHeaderCell(mActivity));
        for(ZhuanLan zhuanLan : mZhuanLans) {
            mCells.add(new ZhuanLanCell(mActivity, zhuanLan));
        }
        mCells.add(new TuijianCourseHeaderCell(mActivity));
        for(Album course : mCourses) {
            mCells.add(new MainPageCourseCell(mActivity, course));
        }
        mMainPageAdapter.notifyDataSetChanged();
    }

    public void onStopHandler() {
        mLoading.hide();
    }

    @Override
    public void onRefresh() {
        new GetZhuanLanAndTuijianCoursesTask().execute();
    }

    private class MainPageAdapter extends ArrayAdapter<ListViewCell> {
        private List<ListViewCell> mViewCells;

        public MainPageAdapter(Activity activity, List<ListViewCell> cells) {
            super(activity, 0, cells);
            mViewCells = cells;
        }

        @Override
        public int getCount() {
            return mViewCells.size();
        }

        @Override
        public ListViewCell getItem(int position) {
            return mViewCells.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ListViewCell item = getItem(position);
            return item.getView();
        }
    }

    private class GetZhuanLanAndTuijianCoursesTask extends AsyncTask<Void, Void, GetZhuanLanAndTuijianCourseResponse> {

        @Override
        protected GetZhuanLanAndTuijianCourseResponse doInBackground(Void... voids) {
            GetZhuanLanAndTuijianCourseRequest request = new GetZhuanLanAndTuijianCourseRequest();
            return new BasicService().sendRequest(request);
        }

        @Override
        protected void onPostExecute(final GetZhuanLanAndTuijianCourseResponse response) {
            super.onPostExecute(response);
            mSwipeRefreshLayout.setRefreshing(false);
            if (!response.isSuccess()) {
                LogHelper.e(TAG, response.getErrorMessage());
                return;
            }

            mCourses = response.getCourses();
            mZhuanLans = response.getZhuanLans();
            setZhuanLanAndCoursesView();
        }
    }


    public class GetAlbumSongsTask extends AsyncTask<GetAlbumSongsRequest, Void, GetAlbumSongsResponse> {

        private GetAlbumSongsRequest request;

        @Override
        protected GetAlbumSongsResponse doInBackground(GetAlbumSongsRequest... params) {
            request = params[0];
            return new BasicService().sendRequest(request);
        }

        @Override
        protected void onPostExecute(GetAlbumSongsResponse resp) {
            super.onPostExecute(resp);
            if (resp.getStatus() == ServerResponse.NO_PERMISSION) {
                mLoading.hide();
                Utils.showVipBuyMessage(mActivity, resp.getErrorMessage());
                return;
            }

            if (resp.getStatus() == ServerResponse.NOT_PAY_COURSE_NO_PERMISSION) {
                mLoading.hide();
                Utils.showErrorMessage(mActivity, resp.getErrorMessage());
                return;
            }

            if (!resp.isSuccess()) {
                mLoading.hide();
                LogHelper.e(TAG, resp.getErrorMessage());
                Utils.showErrorMessage(mActivity, resp.getErrorMessage());
                return;
            }

            if (resp.getResultSet().size() >= 1) {
                LiveSong song = (LiveSong) resp.getResultSet().get(0);
                LogHelper.d(TAG, "mActivity: " + mActivity);
                MusicPlayer musicPlayer = MusicPlayer.getInstance(mActivity.getApplicationContext());
                if (!musicPlayer.isPlay(song)) {
                    musicPlayer.pause();
                    musicPlayer.play(resp.getResultSet(), 0);
                    mNotificationManager.display();
                }
                Intent i = new Intent(mActivity, NewLiveSongActivity.class);
                mActivity.startActivity(i);
                return;
            } else {
                mLoading.hide();
                Utils.showErrorMessage(mActivity, "服务端出错");
                return;
            }

        }
    }


}