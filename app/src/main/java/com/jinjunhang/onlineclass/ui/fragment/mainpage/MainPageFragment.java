package com.jinjunhang.onlineclass.ui.fragment.mainpage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;
import com.jinjunhang.framework.lib.LoadingAnimation;
import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.framework.service.ServerResponse;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.Album;
import com.jinjunhang.onlineclass.model.Answer;
import com.jinjunhang.onlineclass.model.FinanceToutiao;
import com.jinjunhang.onlineclass.model.LiveSong;
import com.jinjunhang.onlineclass.model.Pos;
import com.jinjunhang.onlineclass.model.Question;
import com.jinjunhang.onlineclass.model.ServiceLinkManager;
import com.jinjunhang.onlineclass.model.ZhuanLan;
import com.jinjunhang.onlineclass.service.GetAlbumSongsRequest;
import com.jinjunhang.onlineclass.service.GetAlbumSongsResponse;
import com.jinjunhang.onlineclass.service.GetExtendFunctionInfoRequest;
import com.jinjunhang.onlineclass.service.GetExtendFunctionInfoResponse;
import com.jinjunhang.onlineclass.service.GetFinanceToutiaoRequest;
import com.jinjunhang.onlineclass.service.GetFinanceToutiaoResponse;
import com.jinjunhang.onlineclass.service.GetQuestionRequest;
import com.jinjunhang.onlineclass.service.GetQuestionResponse;
import com.jinjunhang.onlineclass.service.GetZhuanLanAndTuijianCourseRequest;
import com.jinjunhang.onlineclass.service.GetZhuanLanAndTuijianCourseResponse;
import com.jinjunhang.onlineclass.service.ServiceConfiguration;
import com.jinjunhang.onlineclass.ui.activity.MainActivity;
import com.jinjunhang.onlineclass.ui.activity.WebBrowserActivity;
import com.jinjunhang.onlineclass.ui.activity.album.NewLiveSongActivity;
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
import com.jinjunhang.player.MusicPlayer;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnMultiPurposeListener;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

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

    private Page rongziPage;
    private ViewGroup rongziView;

    private boolean isIntied;
    private View v;
    private Toolbar mToolBar;

    private  ImmersionBar mImmersionBar;

    @Override
    protected boolean isCompatibleActionBar() {
        return false;
    }

    @Override
    protected boolean isNeedTopPadding() {
        return false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_main;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = super.onCreateView(inflater, container, savedInstanceState);

        rongziView = (ViewGroup)v.findViewById(R.id.frag_1);


        rongziPage = new Page(getActivity(), this, inflater, container, savedInstanceState, ExtendFunctionManager.RONGZI_TYPE);
        mToolBar = rongziPage.mToolbar;
        rongziView.addView(rongziPage.v);

        if (!isIntied) {
            setupToolBar();
            isIntied = true;
        }
        return v;
    }

    public void setupToolBar() {

        TextView searchField = mToolBar.findViewById(R.id.searchBar);
        searchField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), SearchActivity.class);
                getContext().startActivity(i);
            }
        });

        Utils.setNavigationBarMusicButton(getActivity());

    }

    @Override
    public void onStop() {
        LogHelper.d(TAG, "onStop() called");
        super.onStop();
        rongziPage.onStopHandler();
    }

    @Override
    public void onResume() {
        LogHelper.d(TAG, "onResume() called");
        super.onResume();
        rongziPage.onStartHandler();
    }

    @Override
    public void onPause() {
        LogHelper.d(TAG, "onPause() called");
        super.onPause();
        rongziPage.onStopHandler();
    }

    public void startBannerPlay() {
        if (rongziPage != null) {
            rongziPage.onStartHandler();
            rongziPage.updateToolBar(rongziPage.mToolbarAlpha);
        }
    }

    public void stopBannerPlay() {
        if (rongziPage != null)
            rongziPage.onStopHandler();
    }

    @Override
    public void changeActionBar() {
        /*
        AppCompatActivity activity = (AppCompatActivity)getActivity();
        if (activity == null)
            return;

        activity.getSupportActionBar().show();
        activity.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        //删除actionbar下面的阴影
        activity.getSupportActionBar().setElevation(0);
        final View customView = activity.getLayoutInflater().inflate(R.layout.actionbar_main, null);
        activity.getSupportActionBar().setCustomView(customView);
        Toolbar parent =(Toolbar) customView.getParent();

        parent.setContentInsetsAbsolute(0, 0);

        setLightStatusBar(customView, activity);

        TextView searchField = (TextView) ((AppCompatActivity)getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.search_edit_field);
        searchField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), SearchActivity.class);
                getContext().startActivity(i);
            }
        });


        kefuBtn = (ImageButton)((AppCompatActivity)getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.kefu_btn);
        //searchBtn = (ImageButton)((AppCompatActivity)getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.search_btn);

        kefuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), WebBrowserActivity.class)
                        .putExtra(WebBrowserActivity.EXTRA_TITLE, "客服")
                        .putExtra(WebBrowserActivity.EXTRA_URL, ServiceLinkManager.FunctionCustomerServiceUrl());
                getContext().startActivity(i);
            }
        });


        Utils.setNavigationBarMusicButton(getActivity());
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00ffffff")));
        actionBar.setSplitBackgroundDrawable(new ColorDrawable(Color.parseColor("#00ffffff")));

        //StatusBarCompat.translucentStatusBar(getActivity(), true);
        //sofia
        */
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
                    rongziPage.refreshListView(answer);
            }
        }
    }


    class Page  {
        private  final String TAG = LogHelper.makeLogTag(Page.class);

        public View v;
        private LoadingAnimation mLoading;
        private ListView mListView;
        private ExtendFunctionManager mFunctionManager;
        private MainPageAdapter mMainPageAdapter;
        private RefreshLayout mSwipeRefreshLayout;
        private PageCells mPageCells = new PageCells();
        private String mType;
        private HeaderAdvCell mHeaderAdvCell;
        private List<Album> mCourses;
        private List<ZhuanLan> mZhuanLans;
        private List<ZhuanLan> mJpks;
        private Pos mPos;
        private List<FinanceToutiao> mToutiaos = new ArrayList<>();
        private List<Question> mQuestions = new ArrayList<>();
        private FragmentActivity mActivity;
        private Fragment mFragment;

        //ToolBar相关的
        private Toolbar mToolbar;
        private TextView mSearchBar;
        private TextView mTitleView;
        private ImageView mMusicBtn;
        private float mToolbarAlpha = 0;

        private boolean mIsLoading;
        private ExoPlayerNotificationManager mNotificationManager;
        private ImmersionBar mImmersionBar;
        private float mLastAlpha = 0;

        class PageCells {
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

        public Page(FragmentActivity activity, final Fragment fragment, LayoutInflater inflater, ViewGroup container,
                    Bundle savedInstanceState, String type) {
            mNotificationManager = ExoPlayerNotificationManager.getInstance(activity);
            mActivity = activity;
            mFragment = fragment;
            mZhuanLans = new ArrayList<>();
            mCourses = new ArrayList<>();
            this.mType = type;

            v = inflater.inflate(R.layout.activity_fragment_pushdownrefresh_mainpage, container, false);
            mToolbar = v.findViewById(R.id.toolbar);
            mTitleView = v.findViewById(R.id.title);
            mSearchBar = v.findViewById(R.id.searchBar);
            mMusicBtn = v.findViewById(R.id.musicBtn);
            mListView = v.findViewById(R.id.listView);
            mListView.setVerticalScrollBarEnabled(false);
            mListView.setFocusable(true);

            mLoading = new LoadingAnimation(activity, (ViewGroup)v.findViewById(R.id.fragmentContainer));

            //去掉列表的分割线
            mListView.setDividerHeight(0);
            mListView.setDivider(null);

            mFunctionManager = ExtendFunctionManager.getInstance(mActivity);

            mHeaderAdvCell = new HeaderAdvCell(activity, mLoading, mType);
            mPageCells.mHeaderAdvCell = mHeaderAdvCell;
            mPageCells.mZhuanLanHeaderCell = new ZhuanLanHeaderCell(activity);
            mPageCells.mJpkHeaderCell = new JpkHeaderCell(activity);
            mPageCells.mTuijianCourseHeaderCell = new TuijianCourseHeaderCell(activity);
            mPageCells.mFinanceToutiaoHeaderCell = new FinanceToutiaoHeaderCell(activity);
            mPageCells.mQuestionHeaderCell = new QuestionHeaderCell(activity);
            mPageCells.mPosApplyCell = new PosApplyCell(mActivity, mPos);

            mHeaderAdvCell.updateAds();

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
                public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                    mHeaderAdvCell.updateAds();
                    new GetZhuanLanAndTuijianCoursesTask().execute();
                    new GetFunctionInfoRequestTask().execute();
                    new GetFinanceToutiaoTask().execute();
                    new GetQuestionsTask().execute();
                    refreshLayout.finishRefresh(7000/*,false*/);//传入false表示刷新失败
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

                        mLoading.show("");

                        GetAlbumSongsRequest request = new GetAlbumSongsRequest();
                        request.setAlbum(album);
                        request.setPageIndex(0);
                        request.setPageSize(200);
                        new GetAlbumSongsTask().execute(request);
                    }
                }
            });

            mImmersionBar = ImmersionBar.with(fragment);
            mImmersionBar.statusBarColorTransformEnable(false)
                    .init();
            LogHelper.d(TAG, "toolbar = " + mToolbar);
            ImmersionBar.setTitleBar(activity, mToolbar);

            mToolbar.setBackgroundColor(ColorUtils.blendARGB(Color.TRANSPARENT
                    , ContextCompat.getColor(mActivity, R.color.colorPrimary), 0));


            mListView.setOnScrollListener(new AbsListView.OnScrollListener(){
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                    if (mListView.getChildAt(0) != null) {

                        View c = mListView.getChildAt(0); //this is the first visible row
                        int scrollY = -c.getTop();

                        int top = scrollY;
                        int height = 70;
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

            new GetZhuanLanAndTuijianCoursesTask().execute();
            new GetFunctionInfoRequestTask().execute();
            new GetFinanceToutiaoTask().execute();
            new GetQuestionsTask().execute();

            updateToolBar(mToolbarAlpha);
        }

        private void  updateToolBar(float alpha) {

            mToolbar.setBackgroundColor(ColorUtils.blendARGB(Color.TRANSPARENT, ContextCompat.getColor(mActivity, R.color.colorPrimary), alpha));
            if (alpha > 0.7) {
                mMusicBtn.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.music_static));
                mTitleView.setVisibility(View.VISIBLE);
                mSearchBar.setVisibility(View.INVISIBLE);
            } else {
                mMusicBtn.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.music_static_white));
                mTitleView.setVisibility(View.INVISIBLE);
                mSearchBar.setVisibility(View.VISIBLE);
            }

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
                //mSwipeRefreshLayout.setRefreshing(false);
                mSwipeRefreshLayout.finishRefresh();
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




    }
}




