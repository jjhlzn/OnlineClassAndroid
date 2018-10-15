package com.jinjunhang.onlineclass.ui.fragment.mainpage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.gyf.barlibrary.ImmersionBar;
import com.jinjunhang.framework.lib.LoadingAnimation;
import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.db.KeyValueDao;
import com.jinjunhang.onlineclass.model.Advertise;
import com.jinjunhang.onlineclass.model.Album;
import com.jinjunhang.onlineclass.model.Answer;
import com.jinjunhang.onlineclass.model.FinanceToutiao;
import com.jinjunhang.onlineclass.model.Pos;
import com.jinjunhang.onlineclass.model.Question;
import com.jinjunhang.onlineclass.model.ZhuanLan;
import com.jinjunhang.onlineclass.service.GetExtendFunctionInfoRequest;
import com.jinjunhang.onlineclass.service.GetExtendFunctionInfoResponse;
import com.jinjunhang.onlineclass.service.GetFinanceToutiaoRequest;
import com.jinjunhang.onlineclass.service.GetFinanceToutiaoResponse;
import com.jinjunhang.onlineclass.service.GetMainPageAdsRequest;
import com.jinjunhang.onlineclass.service.GetMainPageAdsResponse;
import com.jinjunhang.onlineclass.service.GetQuestionRequest;
import com.jinjunhang.onlineclass.service.GetQuestionResponse;
import com.jinjunhang.onlineclass.service.GetZhuanLanAndTuijianCourseRequest;
import com.jinjunhang.onlineclass.service.GetZhuanLanAndTuijianCourseResponse;
import com.jinjunhang.onlineclass.ui.activity.WebBrowserActivity;
import com.jinjunhang.onlineclass.ui.activity.mainpage.BottomTabLayoutActivity;
import com.jinjunhang.onlineclass.ui.activity.search.SearchActivity;
import com.jinjunhang.onlineclass.ui.cell.BaseListViewCell;
import com.jinjunhang.onlineclass.ui.cell.ExtendFunctionCell;
import com.jinjunhang.onlineclass.ui.cell.ListViewCell;
import com.jinjunhang.onlineclass.ui.cell.MainPageCourseCell;
import com.jinjunhang.onlineclass.ui.cell.SectionSeparatorCell;
import com.jinjunhang.onlineclass.ui.cell.mainpage.FinanceToutiaoCell;
import com.jinjunhang.onlineclass.ui.cell.mainpage.FinanceToutiaoHeaderCell;
import com.jinjunhang.onlineclass.ui.cell.mainpage.HeaderAdvCell;
import com.jinjunhang.onlineclass.ui.cell.mainpage.JpkHeaderCell;
import com.jinjunhang.onlineclass.ui.cell.mainpage.PosApplyCell;
import com.jinjunhang.onlineclass.ui.cell.mainpage.QuestionCell;
import com.jinjunhang.onlineclass.ui.cell.mainpage.QuestionHeaderCell;
import com.jinjunhang.onlineclass.ui.cell.mainpage.TuijianCourseHeaderCell;
import com.jinjunhang.onlineclass.ui.cell.mainpage.ZhuanLanCell;
import com.jinjunhang.onlineclass.ui.cell.mainpage.ZhuanLanHeaderCell;
import com.jinjunhang.onlineclass.ui.fragment.BaseFragment;
import com.jinjunhang.onlineclass.ui.fragment.QuestionAnswerFragment;
import com.jinjunhang.onlineclass.ui.lib.ExtendFunctionManager;
import com.jinjunhang.player.ExoPlayerNotificationManager;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnMultiPurposeListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jinjunhang on 2018/3/21.
 */

public class MainPageFragment extends BaseFragment  {

    static {
        ClassicsHeader.REFRESH_HEADER_PULLING = "下拉可以刷新";
        ClassicsHeader.REFRESH_HEADER_REFRESHING = "正在刷新...";
        ClassicsHeader.REFRESH_HEADER_LOADING = "正在加载...";
        ClassicsHeader.REFRESH_HEADER_RELEASE = "释放立即刷新";
        ClassicsHeader.REFRESH_HEADER_FINISH = "刷新完成";
        ClassicsHeader.REFRESH_HEADER_FAILED = "刷新失败";
        ClassicsHeader.REFRESH_HEADER_SECONDARY = "释放进入二楼";
        //ClassicsHeader.REFRESH_HEADER_LASTTIME = "上次更新 M-d HH:mm";
        //ClassicsHeader.REFRESH_HEADER_LASTTIME = "'Last update' M-d HH:mm";
    }

    private  static final String TAG = LogHelper.makeLogTag(MainPageFragment.class);

    private Page mPage;

    private boolean isIntied;
    private View v;
    private Toolbar mToolBar;

    private  ImmersionBar mImmersionBar;

    @Override
    protected boolean isCompatibleActionBar() {
        return false;
    }

    @Override
    protected boolean isNeedTopMargin() {
        return false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_main;
    }

    @Override
    public Toolbar getToolBar() {
        return mToolBar;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = super.onCreateView(inflater, container, savedInstanceState);


        mPage = new Page(getActivity(), this,v);
        mToolBar = mPage.mToolbar;

        if (!isIntied) {
            setToolBar();
            isIntied = true;
        }
        return v;
    }

    public void setToolBar() {

        TextView searchField = mToolBar.findViewById(R.id.searchBar);
        searchField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), SearchActivity.class);
                getContext().startActivity(i);
            }
        });

        Utils.updateNavigationBarButton(getActivity(), mPage.mToolbarAlpha);

    }

    @Override
    public void updateMusicBtnState() {
        if (mPage != null) {
            Utils.updateNavigationBarButton(getActivity(), mPage.mToolbarAlpha);
        }
    }

    @Override
    public void onStop() {
        LogHelper.d(TAG, "onStop() called");
        super.onStop();
        mPage.onStopHandler();
    }

    @Override
    public void onResume() {
        LogHelper.d(TAG, "onResume() called");
        super.onResume();
        mPage.onStartHandler();
    }

    @Override
    public void onPause() {
        LogHelper.d(TAG, "onPause() called");
        super.onPause();
        mPage.onStopHandler();
    }

    public void startBannerPlay() {
        if (mPage != null) {
            mPage.onStartHandler();
            mPage.updateToolBar(mPage.mToolbarAlpha);
        }
    }

    public void stopBannerPlay() {
        if (mPage != null)
            mPage.onStopHandler();
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (getUserVisibleHint()) {
            Utils.updateNavigationBarButton(getActivity(), mPage.mToolbarAlpha);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogHelper.d(TAG, "onActivityResult called");
        if (requestCode == QuestionAnswerFragment.REQUEST_QUESTION && data != null) {
            boolean isSuccess = data.getBooleanExtra(QuestionAnswerFragment.EXTRA_IS_SUCCESS, false);
            if (isSuccess) {
                Answer answer = (Answer)data.getSerializableExtra(QuestionAnswerFragment.EXTRA_ANSWER);
                if(answer != null)
                    mPage.refreshListView(answer);
            }
        }
    }


    class Page  implements  Serializable {
        private  final String TAG = LogHelper.makeLogTag(Page.class);

        public View v;
        private LoadingAnimation mLoading;
        private ListView mListView;
        private ExtendFunctionManager mFunctionManager;
        private MainPageAdapter mMainPageAdapter;
        private RefreshLayout mSwipeRefreshLayout;
        private PageCells mPageCells = new PageCells();
        private HeaderAdvCell mHeaderAdvCell;
        private List<Album> mCourses;
        private List<ZhuanLan> mZhuanLans;
        private List<ZhuanLan> mJpks;
        private List<Advertise> mAdvertises;
        private Pos mPos;
        private List<FinanceToutiao> mToutiaos = new ArrayList<>();
        private List<Question> mQuestions = new ArrayList<>();
        private FragmentActivity mActivity;
        private Fragment mFragment;

        //ToolBar相关的
        private Toolbar mToolbar;
        private TextView mSearchBar;
        private TextView mTitleView;
        private float mToolbarAlpha = 0;

        private ExoPlayerNotificationManager mNotificationManager;
        private ImmersionBar mImmersionBar;
        private float mLastAlpha = 0;
        private KeyValueDao dao;



        class PageCells implements Serializable {
            private HeaderAdvCell mHeaderAdvCell;
            private List<ExtendFunctionCell> mFuncCells = new ArrayList<>();
            private ZhuanLanHeaderCell mZhuanLanHeaderCell;
            private List<ZhuanLanCell> mZhuanLanCells = new ArrayList<>();
            private JpkHeaderCell mJpkHeaderCell;
            private List<ZhuanLanCell> mJpkCells = new ArrayList<>();
            private TuijianCourseHeaderCell mTuijianCourseHeaderCell;
            private List<MainPageCourseCell> mMainPageCourseCells = new ArrayList<>();
            private FinanceToutiaoHeaderCell mFinanceToutiaoHeaderCell;
            private List<FinanceToutiaoCell> mFinanceToutiaoCells = new ArrayList<>();
            private QuestionHeaderCell mQuestionHeaderCell;
            private List<QuestionCell> mQuestionCells = new ArrayList<>();
            private PosApplyCell mPosApplyCell;

            private List<ListViewCell> cells = new ArrayList<>();
            private boolean hasUpdate = true;  //表示是否已经有更新，如果有更新，需要重新创建cells； 否则就直接返回cells

            public List<ListViewCell> getCells() {
                //LogHelper.d(TAG, "cells.count = " + cells.size());
                if (!hasUpdate) {
                    return cells;
                }

                LogHelper.d(TAG, "recreate cells");
                hasUpdate = false;
                cells = new ArrayList<>();
                cells.add(mHeaderAdvCell);

                for (ExtendFunctionCell cell : mFuncCells) {
                    cells.add(cell);
                }

                if (mPos != null){
                    cells.add(new SectionSeparatorCell(mActivity));
                    cells.add(mPosApplyCell);
                }

                if (mMainPageCourseCells.size() > 0) {
                    cells.add(new SectionSeparatorCell(mActivity));
                    cells.add(mTuijianCourseHeaderCell);
                    for (MainPageCourseCell cell : mMainPageCourseCells) {
                        cells.add(cell);
                    }
                }

                if (mFinanceToutiaoCells.size() > 0) {
                    cells.add(new SectionSeparatorCell(mActivity));
                    cells.add(mFinanceToutiaoHeaderCell);
                    for (FinanceToutiaoCell cell : mFinanceToutiaoCells) {
                        cells.add(cell);
                    }
                }

                if (mJpkCells.size() > 0) {
                    cells.add(new SectionSeparatorCell(mActivity));
                    cells.add(mJpkHeaderCell);
                    for (ZhuanLanCell cell : mJpkCells) {
                        cells.add(cell);
                    }
                }

                if (mZhuanLanCells.size() > 0) {
                    cells.add(new SectionSeparatorCell(mActivity));
                    cells.add(mZhuanLanHeaderCell);
                    for (ZhuanLanCell cell : mZhuanLanCells) {
                        cells.add(cell);
                    }
                }

                if (mQuestionCells.size() > 0) {
                    cells.add(new SectionSeparatorCell(mActivity));
                    cells.add(mQuestionHeaderCell);
                    for (QuestionCell cell : mQuestionCells) {
                        cells.add(cell);
                    }
                }



                return cells;
            }
        }

        public Page(FragmentActivity activity, final Fragment fragment, View view) {
            mNotificationManager = ExoPlayerNotificationManager.getInstance(activity);
            mActivity = activity;
            mFragment = fragment;
            mZhuanLans = new ArrayList<>();
            mCourses = new ArrayList<>();

            dao = KeyValueDao.getInstance(activity);
            v = view;
            mToolbar = v.findViewById(R.id.toolbar);
            mTitleView = v.findViewById(R.id.title);
            mSearchBar = v.findViewById(R.id.searchBar);
            mListView = v.findViewById(R.id.listView);
            mListView.setVerticalScrollBarEnabled(false);
            mListView.setFocusable(true);

            mLoading = new LoadingAnimation(activity, (ViewGroup)v.findViewById(R.id.fragmentContainer));

            //去掉列表的分割线
            mListView.setDividerHeight(0);
            mListView.setDivider(null);



            mFunctionManager = ExtendFunctionManager.getInstance(mActivity);

            mHeaderAdvCell = new HeaderAdvCell(activity);
            mPageCells.mHeaderAdvCell = mHeaderAdvCell;
            mPageCells.mZhuanLanHeaderCell = new ZhuanLanHeaderCell(activity);
            mPageCells.mJpkHeaderCell = new JpkHeaderCell(activity);
            mPageCells.mTuijianCourseHeaderCell = new TuijianCourseHeaderCell(activity);
            mPageCells.mFinanceToutiaoHeaderCell = new FinanceToutiaoHeaderCell(activity);
            mPageCells.mQuestionHeaderCell = new QuestionHeaderCell(activity);
            mPageCells.mPosApplyCell = new PosApplyCell(mActivity, mPos);


            mMainPageAdapter = new MainPageAdapter(activity, mPageCells);
            mListView.setAdapter(mMainPageAdapter);

            //可以下拉刷新
            mSwipeRefreshLayout =  (RefreshLayout)v.findViewById(R.id.swipe_refresh_layout);
            ClassicsHeader header = new ClassicsHeader(mActivity);
            header.setEnableLastTime(false);

            mSwipeRefreshLayout.setRefreshHeader(header);
            mSwipeRefreshLayout.setOnMultiPurposeListener(new OnMultiPurposeListener() {
                @Override
                public void onHeaderMoving(RefreshHeader refreshHeader, boolean b, float v, int i, int i1, int i2) {
                    if ( v > 0) {
                        mToolbar.setVisibility(View.GONE);
                    } else {
                        mToolbar.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onHeaderReleased(RefreshHeader refreshHeader, int i, int i1) {
                }

                @Override
                public void onHeaderStartAnimator(RefreshHeader refreshHeader, int i, int i1) {

                }

                @Override
                public void onHeaderFinish(RefreshHeader refreshHeader, boolean b) {
                    //mToolbar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFooterMoving(RefreshFooter refreshFooter, boolean b, float v, int i, int i1, int i2) {

                }

                @Override
                public void onFooterReleased(RefreshFooter refreshFooter, int i, int i1) {

                }

                @Override
                public void onFooterStartAnimator(RefreshFooter refreshFooter, int i, int i1) {

                }

                @Override
                public void onFooterFinish(RefreshFooter refreshFooter, boolean b) {

                }

                @Override
                public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

                }

                @Override
                public void onRefresh(@NonNull final RefreshLayout refreshLayout) {


                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            new GetHeaderAdvTask().execute();
                            new GetZhuanLanAndTuijianCoursesTask().execute();
                            new GetFunctionInfoRequestTask().execute();
                            //new GetFinanceToutiaoTask().execute();
                            new GetQuestionsTask().execute();
                            refreshLayout.finishRefresh(3000/*,false*/);//传入false表示刷新失败
                        }
                    });

                }

                @Override
                public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState refreshState, @NonNull RefreshState refreshState1) {
                    if (refreshState == RefreshState.PullDownToRefresh) {
                        mToolbar.setVisibility(View.GONE);
                    }
                    if (refreshState == RefreshState.PullDownCanceled) {
                        mToolbar.setVisibility(View.VISIBLE);
                    }
                    if (refreshState == RefreshState.RefreshFinish) {
                        mToolbar.setVisibility(View.VISIBLE);
                    }
                }
            });


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
                        ((BottomTabLayoutActivity)(getActivity())).switchToZhiboTab();
                    }
                }
            });

            mImmersionBar = ImmersionBar.with(fragment);
            mImmersionBar.statusBarColorTransformEnable(false).init();
            ImmersionBar.setTitleBar(activity, mToolbar);

            mToolbar.setBackgroundColor(ColorUtils.blendARGB(Color.TRANSPARENT
                    , ContextCompat.getColor(mActivity, R.color.colorPrimary), 0));


            mListView.setOnScrollListener(new AbsListView.OnScrollListener(){
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                    if (mListView.getChildAt(0) != null) {

                        View c = mListView.getChildAt(0); //this is the first visible row
                        int scrollY = -c.getTop();

                        int top = scrollY;
                        int height = 72;
                        float alpha = 0;
                        if (firstVisibleItem > 0) {
                            alpha = 1;
                        } else {
                            alpha = (float)(top) / height;
                            if (alpha > 1)
                                alpha = 1;
                        }
                        mToolbarAlpha = alpha;
                        if (Math.abs(alpha - mLastAlpha) > 0.0001)
                            updateToolBar(mToolbarAlpha);

                        mLastAlpha = alpha;
                    }
                }
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }


            });

            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    new GetHeaderAdvTask().execute();
                    new GetZhuanLanAndTuijianCoursesTask().execute();
                    new GetFunctionInfoRequestTask().execute();
                    new GetFinanceToutiaoTask().execute();
                    new GetQuestionsTask().execute();
                }
            });

            updateToolBar(mToolbarAlpha);
        }

        private void  updateToolBar(float alpha) {

            mToolbar.setBackgroundColor(ColorUtils.blendARGB(Color.TRANSPARENT, ContextCompat.getColor(mActivity, R.color.colorPrimary), alpha));
            if (Utils.isWhiteImage(alpha)) {
                mTitleView.setVisibility(View.INVISIBLE);
                mSearchBar.setVisibility(View.VISIBLE);
            } else {
                mTitleView.setVisibility(View.VISIBLE);
                mSearchBar.setVisibility(View.INVISIBLE);
            }

            Utils.updateNavigationBarButton(getActivity(), alpha);

            if (alpha == 1) {
                ImmersionBar.with(mFragment).statusBarDarkFont(true).init();
            } else {
                ImmersionBar.with(mFragment).statusBarDarkFont(false).init();
            }

        }


        private void addAnswer(Answer answer) {
            for (Question question : mQuestions) {
                if (question.getId().equals(answer.getQuestion().getId())) {
                    question.getAnswers().add(answer);
                    break;
                }
            }
        }

        public void refreshListView(Answer answer) {
            addAnswer(answer);
            mMainPageAdapter.notifyDataSetChanged();
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
            mPageCells.mJpkCells.clear();
            for(ZhuanLan jpk : mJpks) {
                mPageCells.mJpkCells.add(new ZhuanLanCell(mActivity, jpk));
            }
            mPageCells.mMainPageCourseCells.clear();
            for(Album course : mCourses) {
                mPageCells.mMainPageCourseCells.add(new MainPageCourseCell(mActivity, course));
            }
            setPosView();
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

        private  void setQuestionsView() {
            mPageCells.mQuestionCells.clear();
            for(Question question : mQuestions) {
                mPageCells.mQuestionCells.add(new QuestionCell(mActivity, question, mFragment,  mMainPageAdapter));
            }
            mPageCells.hasUpdate = true;
            mMainPageAdapter.notifyDataSetChanged();
        }

        private void setHeaderAdvCell() {
            mHeaderAdvCell.setAdvertises(mAdvertises);
            mMainPageAdapter.notifyDataSetChanged();
        }

        private  void setPosView() {
            mPageCells.hasUpdate = true;
            mPageCells.mPosApplyCell.setPos(mPos);
            mMainPageAdapter.notifyDataSetChanged();
        }

        public void onStopHandler() {
            mLoading.hide();
            mHeaderAdvCell.stopPlay();
        }

        public  void onStartHandler() {
            mHeaderAdvCell.startPlay();
        }



        public class MainPageAdapter extends ArrayAdapter<ListViewCell> {
            private PageCells mPageCells;

            public MainPageAdapter(Activity activity, PageCells cells) {
                super(activity, 0);
                mPageCells = cells;
            }

            @Override
            public int getCount() {
                return mPageCells.getCells().size();
            }

            @Override
            public ListViewCell getItem(int position) {
                return mPageCells.getCells().get(position);
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                //LogHelper.d(TAG, "position: " + position);
                //LogHelper.d(TAG, "cell: " + mPageCells.getCells().get(0));
                ListViewCell item = getItem(position);
                return item.getView(convertView);
            }


            @Override
            public int getItemViewType(int position) {
                ListViewCell item = getItem(position);
                return item.getItemViewType();
            }

            @Override
            public int getViewTypeCount() {
                return 16;
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
                //mSwipeRefreshLayout.setRefreshing(false);

                if (!response.isSuccess()) {
                    LogHelper.e(TAG, response.getErrorMessage());
                    return;
                }

                mCourses = response.getCourses();
                mZhuanLans = response.getZhuanLans();
                mJpks = response.getJpks();
                mPos = response.getPos();
                setZhuanLanAndCoursesView();
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
                //mSwipeRefreshLayout.setRefreshing(false);
                mSwipeRefreshLayout.finishRefresh();
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
                //mSwipeRefreshLayout.setRefreshing(false);
                mSwipeRefreshLayout.finishRefresh();
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

        private class GetQuestionsTask extends AsyncTask<Void, Void, GetQuestionResponse> {
            protected GetQuestionResponse doInBackground(Void... voids) {
                GetQuestionRequest request = new GetQuestionRequest();
                return new BasicService().sendRequest(request);
            }

            protected void onPostExecute(GetQuestionResponse response) {
                super.onPostExecute(response);
                //mSwipeRefreshLayout.setRefreshing(false);
                mSwipeRefreshLayout.finishRefresh();
                if (!response.isSuccess()){
                    LogHelper.e(TAG, response.getStatus(), response.getErrorMessage());
                    return;
                }

                List<Question> questions = response.getQuestions();
                if (questions.size() == 0) {
                    return;
                }
                mQuestions = questions;
                setQuestionsView();
            }

        }


        private class GetHeaderAdvTask extends AsyncTask<Void, Void, GetMainPageAdsResponse> {

            @Override
            protected GetMainPageAdsResponse doInBackground(Void... voids) {
                GetMainPageAdsRequest request = new GetMainPageAdsRequest();
                //request.setType(mType);
                return new BasicService().sendRequest(request);
            }


            @Override
            protected void onPostExecute(GetMainPageAdsResponse response) {
                super.onPostExecute(response);
                mSwipeRefreshLayout.finishRefresh();
                if (!response.isSuccess()) {
                    LogHelper.e(TAG, response.getErrorMessage());
                    return;
                }
                List<Advertise> advs = response.getAdvertises();
                if (response.getPopupAd() != null && !"".equals(response.getPopupAd().getImageUrl())) {
                    LogHelper.d(TAG, "popup url:" + response.getPopupAd().getImageUrl());
                    Advertise popAd = response.getPopupAd();
                    //((BottomTabLayoutActivity)mActivity).showPopupAd();
                    String cacheImageUrl = dao.getValue(KeyValueDao.KEY_POPUPAD_IMAGEURL, "");
                    if (  !popAd.getImageUrl().equals(cacheImageUrl) ) {
                        dao.saveOrUpdate(KeyValueDao.KEY_POPUPAD_IMAGEURL, popAd.getImageUrl());
                        dao.saveOrUpdate(KeyValueDao.KEY_POPUPAD_CLICKURL, popAd.getClickUrl());
                        dao.saveOrUpdate(KeyValueDao.KEY_POPUPAD_TITLE, popAd.getTitle());
                        ((BottomTabLayoutActivity)mActivity).showPopupAd();
                    }
                }

                mAdvertises = advs;
                setHeaderAdvCell();
                LogHelper.d(TAG, "need to update HaderAdv");
            }
        }

    }
}




