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
import com.jinjunhang.onlineclass.model.FinanceToutiao;
import com.jinjunhang.onlineclass.model.LiveSong;
import com.jinjunhang.onlineclass.model.ZhuanLan;
import com.jinjunhang.onlineclass.service.GetAlbumSongsRequest;
import com.jinjunhang.onlineclass.service.GetAlbumSongsResponse;
import com.jinjunhang.onlineclass.service.GetExtendFunctionInfoRequest;
import com.jinjunhang.onlineclass.service.GetExtendFunctionInfoResponse;
import com.jinjunhang.onlineclass.service.GetFinanceToutiaoRequest;
import com.jinjunhang.onlineclass.service.GetFinanceToutiaoResponse;
import com.jinjunhang.onlineclass.service.GetTuijianCoursesRequest;
import com.jinjunhang.onlineclass.service.GetTuijianCoursesResponse;
import com.jinjunhang.onlineclass.service.GetZhuanLanAndTuijianCourseRequest;
import com.jinjunhang.onlineclass.service.GetZhuanLanAndTuijianCourseResponse;
import com.jinjunhang.onlineclass.ui.activity.WebBrowserActivity;
import com.jinjunhang.onlineclass.ui.activity.album.NewLiveSongActivity;
import com.jinjunhang.onlineclass.ui.cell.BaseListViewCell;
import com.jinjunhang.onlineclass.ui.cell.ExtendFunctionCell;
import com.jinjunhang.onlineclass.ui.cell.MainPageCourseCell;
import com.jinjunhang.onlineclass.ui.cell.ListViewCell;
import com.jinjunhang.onlineclass.ui.cell.SectionSeparatorCell;
import com.jinjunhang.onlineclass.ui.cell.mainpage.FinanceToutiaoCell;
import com.jinjunhang.onlineclass.ui.cell.mainpage.FinanceToutiaoHeaderCell;
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
    //private List<ListViewCell> mCells = new ArrayList<>();
    private PageCells mPageCells = new PageCells();
    private String mType;
    private HeaderAdvCell mHeaderAdvCell;
    private List<Album> mCourses;
    private List<ZhuanLan> mZhuanLans;
    private List<FinanceToutiao> mToutiaos = new ArrayList<>();
    private FragmentActivity mActivity;
    private boolean mIsLoading;
    private ExoPlayerNotificationManager mNotificationManager;


    class PageCells {
       private HeaderAdvCell mHeaderAdvCell;
       private List<ExtendFunctionCell> mFuncCells = new ArrayList<>();
       private ZhuanLanHeaderCell mZhuanLanHeaderCell;
       private List<ZhuanLanCell> mZhuanLanCells = new ArrayList<>();
       private TuijianCourseHeaderCell mTuijianCourseHeaderCell;
       private List<MainPageCourseCell> mMainPageCourseCells = new ArrayList<>();
       private FinanceToutiaoHeaderCell mFinanceToutiaoHeaderCell;
       private List<FinanceToutiaoCell> mFinanceToutiaoCells = new ArrayList<>();

       private List<ListViewCell> cells = new ArrayList<>();
       private boolean hasUpdate = true;

       public List<ListViewCell> getCells() {
           if (!hasUpdate) {
               hasUpdate = false;
               return cells;
           }

           cells = new ArrayList<>();
           cells.add(mHeaderAdvCell);
           cells.add(new SectionSeparatorCell(mActivity));
           for (ExtendFunctionCell cell : mFuncCells) {
               cells.add(cell);
           }
           cells.add(new SectionSeparatorCell(mActivity));
           cells.add(mFinanceToutiaoHeaderCell);
           for(FinanceToutiaoCell cell : mFinanceToutiaoCells) {
               cells.add(cell);
           }
           cells.add(new SectionSeparatorCell(mActivity));
           cells.add(mZhuanLanHeaderCell);
           for (ZhuanLanCell cell : mZhuanLanCells) {
               cells.add(cell);
           }
           cells.add(new SectionSeparatorCell(mActivity));
           cells.add(mTuijianCourseHeaderCell);
           for (MainPageCourseCell cell : mMainPageCourseCells) {
               cells.add(cell);
           }


           return cells;
       }
     }

    public Page(FragmentActivity activity, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, String type) {
        mNotificationManager = ExoPlayerNotificationManager.getInstance(activity);
        mActivity = activity;
        mZhuanLans = new ArrayList<>();
        mCourses = new ArrayList<>();
        this.mType = type;

        v = inflater.inflate(R.layout.activity_fragment_pushdownrefresh_mainpage, container, false);

        mListView = (ListView) v.findViewById(R.id.listView);

        mLoading = new LoadingAnimation(activity, (ViewGroup)v.findViewById(R.id.fragmentContainer));

        //去掉列表的分割线
        mListView.setDividerHeight(0);
        mListView.setDivider(null);


        mFunctionManager = ExtendFunctionManager.getInstance(mActivity);

        mHeaderAdvCell = new HeaderAdvCell(activity, mLoading, mType);
        mPageCells.mHeaderAdvCell = mHeaderAdvCell;
        mPageCells.mZhuanLanHeaderCell = new ZhuanLanHeaderCell(activity);
        mPageCells.mTuijianCourseHeaderCell = new TuijianCourseHeaderCell(activity);
        mPageCells.mFinanceToutiaoHeaderCell = new FinanceToutiaoHeaderCell(activity);



        mHeaderAdvCell.updateAds();
        mHeaderAdvCell.updateTouTiao();

        mMainPageAdapter = new MainPageAdapter(activity, mPageCells);
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
        new GetFunctionInfoRequestTask().execute();
        new GetFinanceToutiaoTask().execute();
    }

    private void setFunctionCellView() {
        int functionRowCount = mFunctionManager.getRowCount();
        mPageCells.mFuncCells.clear();
        for (int i = 0; i < functionRowCount; i++) {
            mPageCells.mFuncCells.add(mFunctionManager.getCell(i, true));
        }
        mPageCells.hasUpdate = true;
        mMainPageAdapter.notifyDataSetChanged();
    }

    private void setZhuanLanAndCoursesView() {
        mPageCells.mZhuanLanCells.clear();
        for(ZhuanLan zhuanLan : mZhuanLans) {
            mPageCells.mZhuanLanCells.add(new ZhuanLanCell(mActivity, zhuanLan));
        }
        mPageCells.mMainPageCourseCells.clear();
        for(Album course : mCourses) {
            mPageCells.mMainPageCourseCells.add(new MainPageCourseCell(mActivity, course));
        }
        mPageCells.hasUpdate = true;
        mMainPageAdapter.notifyDataSetChanged();
    }

    private  void setFinanceToutiaosView() {
        mPageCells.mFinanceToutiaoCells.clear();
        for(FinanceToutiao toutiao : mToutiaos) {
            mPageCells.mFinanceToutiaoCells.add(new FinanceToutiaoCell(mActivity, toutiao));
        }
        mPageCells.hasUpdate = true;
        mMainPageAdapter.notifyDataSetChanged();
    }

    public void onStopHandler() {
        mLoading.hide();
    }

    @Override
    public void onRefresh() {
        new GetZhuanLanAndTuijianCoursesTask().execute();
        new GetFunctionInfoRequestTask().execute();
        new GetFinanceToutiaoTask().execute();
    }

    private class MainPageAdapter extends ArrayAdapter<ListViewCell> {
        private PageCells mViewCells;

        public MainPageAdapter(Activity activity, PageCells cells) {
            super(activity, 0, cells.getCells());
            mViewCells = cells;
        }

        @Override
        public int getCount() {
            return mViewCells.getCells().size();
        }

        @Override
        public ListViewCell getItem(int position) {
            return mViewCells.getCells().get(position);
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

    private class GetFunctionInfoRequestTask extends AsyncTask<Void, Void, GetExtendFunctionInfoResponse> {

        @Override
        protected GetExtendFunctionInfoResponse doInBackground(Void... voids) {
            GetExtendFunctionInfoRequest request = new GetExtendFunctionInfoRequest();
            return new BasicService().sendRequest(request);
        }

        @Override
        protected void onPostExecute(GetExtendFunctionInfoResponse response) {
            super.onPostExecute(response);

            if (!response.isSuccess()){
                LogHelper.e(TAG, response.getStatus(), response.getErrorMessage());
                return;
            }

            List<GetExtendFunctionInfoResponse.ExtendFunctionInfo> functions = response.getFunctions();
            if (functions.size() == 0) {
                return;
            }

            List<ExtendFunctionManager.ExtendFunction> funcs = new ArrayList<>();


            for (GetExtendFunctionInfoResponse.ExtendFunctionInfo function : functions) {
                ExtendFunctionManager.ExtendFunction func = mFunctionManager.makeExtendFunction(function.getImageUrl(), function.getName(),
                        function.getCode(),
                        function.getClickUrl(), function.getAction(), function.hasMessage());
                funcs.add(func);
            }
            mFunctionManager.setFunctions(funcs);
            setFunctionCellView();

            LogHelper.d(TAG, "notify adpter data changed");

        }
    }

    private class GetFinanceToutiaoTask extends AsyncTask<Void, Void, GetFinanceToutiaoResponse> {
        protected GetFinanceToutiaoResponse doInBackground(Void... voids) {
            GetFinanceToutiaoRequest request = new GetFinanceToutiaoRequest();
            return new BasicService().sendRequest(request);
        }

        protected void onPostExecute(GetFinanceToutiaoResponse response) {
            super.onPostExecute(response);

            if (!response.isSuccess()){
                LogHelper.e(TAG, response.getStatus(), response.getErrorMessage());
                return;
            }

            List<FinanceToutiao> toutiaos = response.getToutiaos();
            if (toutiaos.size() == 0) {
                return;
            }
            mToutiaos = toutiaos;
            setFinanceToutiaosView();
        }

    }


}