package com.jinjunhang.onlineclass.ui.fragment.user;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.jinjunhang.framework.lib.LoadingAnimation;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.db.KeyValueDao;
import com.jinjunhang.onlineclass.db.LoginUserDao;
import com.jinjunhang.onlineclass.model.LoginUser;
import com.jinjunhang.onlineclass.model.ServiceLinkManager;
import com.jinjunhang.onlineclass.service.GetUserStatDataRequest;
import com.jinjunhang.onlineclass.service.GetUserStatDataResponse;
import com.jinjunhang.onlineclass.ui.activity.MainActivity;
import com.jinjunhang.onlineclass.ui.activity.MessagesActivity;
import com.jinjunhang.onlineclass.ui.activity.WebBrowserActivity;
import com.jinjunhang.onlineclass.ui.activity.user.PersonalInfoActivity;
import com.jinjunhang.onlineclass.ui.activity.user.QRImageActivity;
import com.jinjunhang.onlineclass.ui.activity.user.SettingsActivity;
import com.jinjunhang.onlineclass.ui.cell.CellClickListener;
import com.jinjunhang.onlineclass.ui.cell.ListViewCell;
import com.jinjunhang.onlineclass.ui.cell.SectionSeparatorCell;
import com.jinjunhang.onlineclass.ui.cell.me.CommonCell;
import com.jinjunhang.onlineclass.ui.cell.me.FirstSectionCell;
import com.jinjunhang.onlineclass.ui.cell.me.LineRecord;
import com.jinjunhang.onlineclass.ui.cell.me.SecondSectionCell;
import com.jinjunhang.onlineclass.ui.fragment.BaseFragment;
import com.jinjunhang.onlineclass.ui.lib.BaseListViewOnItemClickListener;
import com.jinjunhang.framework.lib.LogHelper;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jjh on 2016-6-29.
 */
public class MeFragment extends BaseFragment  {
    private final static String TAG = LogHelper.makeLogTag(MeFragment.class);

    private Boolean inited = false;
    private Boolean isLoading = false;
    private List<ListViewCell> mCells = new ArrayList<>();
    private ListView mListView;
    private MeAdapter mMeAdapter;
    private SmartRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.redDotImage) ImageView mRedDotImage;

    private KeyValueDao mKeyValueDao;

    private List<LineRecord> mSeventSections = new ArrayList<LineRecord>();
    private List<LineRecord> mFourthSections = new ArrayList<LineRecord>();
    private List<LineRecord> mFifthSections = new ArrayList<>();
    private List<LineRecord> mSixSections = new ArrayList<>();
    private WebBroserClickListener webBroserClickListener = new WebBroserClickListener();

    public View v;
    private LoadingAnimation mLoading;

    private Toolbar mToolbar;

    private void initSections() {
        if (mSeventSections.size() == 0) {
            mSeventSections.add(new LineRecord(R.drawable.user10, "我的服务", webBroserClickListener, ServiceLinkManager.MyServiceUrl(), ""));
        }

        if (mFourthSections.size() == 0) {
            mFourthSections.add(new LineRecord(R.drawable.user1, "邀请好友", new CellClickListener() {
                @Override
                public void onClick(ListViewCell cell) {
                    Intent i = new Intent(getActivity(), QRImageActivity.class);
                    startActivity(i);
                }
            }, "", ""));
            mFourthSections.add(new LineRecord(R.drawable.user2, "我的推荐", webBroserClickListener, ServiceLinkManager.MyTuiJianUrl(), mKeyValueDao.getValue(KeyValueDao.KEY_USER_MY_TUIJIAN, "0人")));
            mFourthSections.add(new LineRecord(R.drawable.user3, "我的订单", webBroserClickListener, ServiceLinkManager.MyOrderUrl(), mKeyValueDao.getValue(KeyValueDao.KEY_USER_MY_ORDER, "0笔")));
            //mFourthSections.add(new LineRecord(R.drawable.user4, "我的团队", webBroserClickListener, ServiceLinkManager.MyTeamUrl(), mKeyValueDao.getValue(KeyValueDao.KEY_USER_MY_TEAM, "0人")));
        }

        if (mFifthSections.size() == 0) {
            mFifthSections.add(new LineRecord(R.drawable.user5, "我的资料", new CellClickListener() {
                @Override
                public void onClick(ListViewCell cell) {
                    Intent i = new Intent(getActivity(), PersonalInfoActivity.class);
                    getActivity().startActivityForResult(i, MainActivity.REQUEST_ME_UPDATE_PERSONAL_INFO);
                }
            }, "", ""));

            mFifthSections.add(new LineRecord(R.drawable.user9, "申请合作", webBroserClickListener, ServiceLinkManager.HezuoUrl(), ""));
        }

        if (mSixSections.size() == 0) {
          //  mSixSections.add(new LineRecord(R.drawable.user7, "我要合作", webBroserClickListener, ServiceLinkManager.MyAgentUrl(), ""));
            mSixSections.add(new LineRecord(R.drawable.user8, "设置", new SettingsClickListener(), ServiceLinkManager.MyAgentUrl(), ""));
        }
    }

    @Override
    public Toolbar getToolBar() {
        return mToolbar;
    }

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
        return R.layout.activity_fragment_pushdownrefresh_me;
    }

    public void notifyListViewUpdate() {
        this.mMeAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.actionbar_message_button) void messageButtonClick() {
        mKeyValueDao.setHasNessageMessage(false);

        Intent i = new Intent(getActivity(), MessagesActivity.class);
        startActivity(i);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = super.onCreateView(inflater, container, savedInstanceState);

        ButterKnife.bind(this, v);

        mKeyValueDao = KeyValueDao.getInstance(getActivity());
        initSections();
        mListView =  v.findViewById(R.id.listView);
        mToolbar = v.findViewById(R.id.toolbar);

        //去掉列表的分割线
        mListView.setDividerHeight(0);
        mListView.setDivider(null);
        mLoading = new LoadingAnimation(getActivity(), (ViewGroup) v.findViewById(R.id.fragmentContainer));

        TextView titleView = v.findViewById(R.id.actionbar_text);
        titleView.setText("我的");

        if (mCells.size() == 0) {
            FirstSectionCell item = new FirstSectionCell(getActivity(), this);

            mCells.add(item);
            mCells.add(new SectionSeparatorCell(getActivity()));


            SecondSectionCell secondSectionCell = new SecondSectionCell(getActivity());
            secondSectionCell.setJiFen(mKeyValueDao.getValue(KeyValueDao.KEY_USER_JIFEN, "0"));
            secondSectionCell.setChaiFu(mKeyValueDao.getValue(KeyValueDao.KEY_USER_CAFIFU, "0"));
            secondSectionCell.setTeamPeople(mKeyValueDao.getValue(KeyValueDao.KEY_USER_TEAMPEOPLE, "0人"));
            mCells.add(secondSectionCell);
            mCells.add(new SectionSeparatorCell(getActivity()));


            for(int i = 0; i < mSeventSections.size(); i++) {
                CommonCell cell = new CommonCell(getActivity(), mSeventSections.get(i));
                mCells.add(cell);
            }
            mCells.add(new SectionSeparatorCell(getActivity()));

            for(int i = 0; i < mFourthSections.size(); i++) {
                CommonCell cell = new CommonCell(getActivity(), mFourthSections.get(i));
                mCells.add(cell);
            }
            mCells.add(new SectionSeparatorCell(getActivity()));

            for(int i = 0; i < mFifthSections.size(); i++) {
                CommonCell cell = new CommonCell(getActivity(), mFifthSections.get(i));
                mCells.add(cell);
            }
            mCells.add(new SectionSeparatorCell(getActivity()));

            for(int i = 0; i < mSixSections.size(); i++) {
                CommonCell cell = new CommonCell(getActivity(), mSixSections.get(i));
                mCells.add(cell);
            }
            mCells.add(new SectionSeparatorCell(getActivity()));
        }

        mSwipeRefreshLayout = v.findViewById(R.id.swipe_refresh_layout);
        Utils.setRefreshHeader(getActivity(), mSwipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (isLoading) {
                    refreshLayout.finishRefresh();
                    return;
                }
                isLoading = true;
                new GetUserStatDataTask().execute();
                refreshLayout.finishRefresh(7000/*,false*/);//传入false表示刷新失败
            }
        });

        mMeAdapter = new MeAdapter(mCells);
        mListView.setAdapter(mMeAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position > 2) {
                    BaseListViewOnItemClickListener.onItemClickEffect(parent, view, position, id);
                    ListViewCell cell = mMeAdapter.getItem(position);
                    cell.onClick();
                }
            }
        });

        if (!inited) {
            inited = true;
           new GetUserStatDataTask().execute();
        }

        setRedDotImage();
        ImmersionBar.setTitleBar(getActivity(), mToolbar);
        if (getUserVisibleHint()) {
            ImmersionBar.with(this).statusBarDarkFont(true).init();
        }
        return v;
    }

    private void setRedDotImage() {
        LogHelper.d(TAG, "hesMessage = " + mKeyValueDao.hasNewMessage());
        if (mKeyValueDao.hasNewMessage()) {
            mRedDotImage.setVisibility(View.VISIBLE);
        } else {
            mRedDotImage.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setRedDotImage();
        mMeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogHelper.d(TAG, "onActivityResult called");
        if (requestCode == MainActivity.REQUEST_ME_UPDATE_USER_IAMGE || requestCode == MainActivity.REQUEST_ME_UPDATE_PERSONAL_INFO) {
            ((FirstSectionCell)mCells.get(0)).update();
        }
    }

    private class MeAdapter extends ArrayAdapter<ListViewCell> {
        private List<ListViewCell> mViewCells;

        public MeAdapter(List<ListViewCell> cells) {
            super(getActivity(), 0, cells);
            mViewCells = cells;
        }

        @Override
        public int getCount() {
            return mCells.size();
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


    private class WebBroserClickListener implements CellClickListener {
        @Override
        public void onClick(ListViewCell cell) {
            CommonCell commonCell = (CommonCell)cell;
            LineRecord record = commonCell.getRecord();
            Intent i = new Intent(getActivity(), WebBrowserActivity.class)
                    .putExtra(WebBrowserActivity.EXTRA_URL, record.getUrl())
                    .putExtra(WebBrowserActivity.EXTRA_TITLE, record.getTitle());
            startActivity(i);
        }
    }

    private class SettingsClickListener implements CellClickListener {
        @Override
        public void onClick(ListViewCell cell) {
            Intent i = new Intent(getActivity(), SettingsActivity.class);
            startActivity(i);
        }
    }


    private class GetUserStatDataTask extends AsyncTask<Void, Void, GetUserStatDataResponse> {
        @Override
        protected GetUserStatDataResponse doInBackground(Void... params) {
            return new BasicService().sendRequest(new GetUserStatDataRequest());
        }

        @Override
        protected void onPostExecute(GetUserStatDataResponse resp) {
            mSwipeRefreshLayout.finishRefresh();

            if (!resp.isSuccess()) {
                LogHelper.e(TAG, resp.getErrorMessage());
                return;
            }

            SecondSectionCell cell = (SecondSectionCell) mCells.get(2);
            cell.setJiFen(resp.getJifen());
            mKeyValueDao.saveOrUpdate(KeyValueDao.KEY_USER_JIFEN, resp.getJifen());
            cell.setChaiFu(resp.getChaiFu());
            mKeyValueDao.saveOrUpdate(KeyValueDao.KEY_USER_CAFIFU, resp.getChaiFu());
            cell.setTeamPeople(resp.getTeamPeople());
            mKeyValueDao.saveOrUpdate(KeyValueDao.KEY_USER_TEAMPEOPLE, resp.getTeamPeople());
            cell.updateView();
            mKeyValueDao.saveOrUpdate(KeyValueDao.KEY_USER_VIP_END_DATE, resp.getVipEndDate());
            mKeyValueDao.saveOrUpdate(KeyValueDao.KEY_USER_AGENT_LEVEL, resp.getLevel());

            CommonCell cell0  = (CommonCell)mCells.get(7);
            CommonCell cell1 = (CommonCell)mCells.get(8);


            cell0.getRecord().setOtherInfo(resp.getTuiJianPeople());
            mKeyValueDao.saveOrUpdate(KeyValueDao.KEY_USER_MY_TUIJIAN, resp.getTuiJianPeople());
            //cell0.updateView();
            cell1.getRecord().setOtherInfo(resp.getOrderCount());
            mKeyValueDao.saveOrUpdate(KeyValueDao.KEY_USER_MY_ORDER, resp.getOrderCount());
            mKeyValueDao.saveOrUpdate(KeyValueDao.KEY_USER_IS_BIND_WEIXIN, resp.isBindWeixin() ? "1" : "0");
            mKeyValueDao.saveOrUpdate(KeyValueDao.KEY_USER_HAS_NEW_MESSAGE, resp.hasNewMessage() ? "1" : "0");
            mKeyValueDao.saveOrUpdate(KeyValueDao.KEY_USER_HAS_BIND_PHONE, resp.hasBindPhone() ? "1" : "0");

            LogHelper.d(TAG, "KEY_USER_IS_BIND_WEIXIN = " + mKeyValueDao.getValue(KeyValueDao.KEY_USER_IS_BIND_WEIXIN, "0"));

            FirstSectionCell firstCell = (FirstSectionCell)mCells.get(0);
            LoginUser loginUser = LoginUserDao.getInstance(getActivity()).get();
            LogHelper.d(TAG, "loginUser = " + loginUser);
            loginUser.setName(resp.getName());
            loginUser.setNickName(resp.getNickName());
            loginUser.setLevel(resp.getLevel());
            loginUser.setBoss(resp.getBoss());
            loginUser.setCodeImageUrl(resp.getCodeImageUrl());
            loginUser.setSex(resp.getSex());
            loginUser.setVipEndDate(resp.getVipEndDate());
            LoginUserDao.getInstance(getActivity()).save(loginUser);

            mMeAdapter.notifyDataSetChanged();


            isLoading = false;
        }
    }

}
