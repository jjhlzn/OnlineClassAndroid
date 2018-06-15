package com.jinjunhang.onlineclass.ui.fragment.user;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.ListView;
import android.widget.TextView;

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
import com.jinjunhang.onlineclass.ui.cell.me.ThirdSectionCell;
import com.jinjunhang.onlineclass.ui.fragment.BaseFragment;
import com.jinjunhang.onlineclass.ui.lib.BaseListViewOnItemClickListener;
import com.jinjunhang.framework.lib.LogHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jjh on 2016-6-29.
 */
public class MeFragment extends BaseFragment implements  SwipeRefreshLayout.OnRefreshListener {
    private final static String TAG = LogHelper.makeLogTag(MeFragment.class);

    private Boolean inited = false;
    private Boolean isLoading = false;
    private List<ListViewCell> mCells = new ArrayList<>();
    private ListView mListView;
    private MeAdapter mMeAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private KeyValueDao mKeyValueDao;

    private List<LineRecord> mSeventSections = new ArrayList<LineRecord>();
    private List<LineRecord> mFourthSections = new ArrayList<LineRecord>();
    private List<LineRecord> mFifthSections = new ArrayList<>();
    private List<LineRecord> mSixSections = new ArrayList<>();
    private WebBroserClickListener webBroserClickListener = new WebBroserClickListener();

    public View v;
    private LoadingAnimation mLoading;

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
            mFourthSections.add(new LineRecord(R.drawable.user4, "我的团队", webBroserClickListener, ServiceLinkManager.MyTeamUrl(), mKeyValueDao.getValue(KeyValueDao.KEY_USER_MY_TEAM, "0人")));
        }

        if (mFifthSections.size() == 0) {
            mFifthSections.add(new LineRecord(R.drawable.user5, "我的资料", new CellClickListener() {
                @Override
                public void onClick(ListViewCell cell) {
                    Intent i = new Intent(getActivity(), PersonalInfoActivity.class);
                    getActivity().startActivityForResult(i, MainActivity.REQUEST_ME_UPDATE_PERSONAL_INFO);
                }
            }, "", ""));
            mFifthSections.add(new LineRecord(R.drawable.user6, "我的二维码", new CellClickListener() {
                @Override
                public void onClick(ListViewCell cell) {
                    Intent i = new Intent(getActivity(), QRImageActivity.class);
                    startActivity(i);
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
    public void onRefresh() {
        if (isLoading) {
            return;
        }
        isLoading = true;
        new GetUserStatDataTask().execute();
    }

    @Override
    public void changeActionBar() {
        AppCompatActivity activity = (AppCompatActivity)getActivity();
        if (activity != null) {
            LogHelper.d(TAG, "activity = " + activity);
            activity.getSupportActionBar().show();
            activity.getSupportActionBar().setElevation(0);
            activity.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            final View customView = activity.getLayoutInflater().inflate(R.layout.actionbar_courselist, null);
            activity.getSupportActionBar().setCustomView(customView);
            Toolbar parent = (Toolbar) customView.getParent();

            parent.setContentInsetsAbsolute(0, 0);

            TextView text = (TextView)customView.findViewById(R.id.actionbar_text);
            text.setText("我的");

            setLightStatusBar(customView, activity);
            Utils.setNavigationBarMusicButton(activity);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = getActivity().getLayoutInflater().inflate(R.layout.activity_fragment_pushdownrefresh, null, false);
        mKeyValueDao = KeyValueDao.getInstance(getActivity());
        initSections();
        mListView = (ListView) v.findViewById(R.id.listView);

        //去掉列表的分割线
        mListView.setDividerHeight(0);
        mListView.setDivider(null);
        mLoading = new LoadingAnimation(getActivity(), (ViewGroup) v.findViewById(R.id.fragmentContainer));

        if (mCells.size() == 0) {
            FirstSectionCell item = new FirstSectionCell(getActivity());
            mCells.add(item);
            mCells.add(new SectionSeparatorCell(getActivity()));


            SecondSectionCell secondSectionCell = new SecondSectionCell(getActivity());
            secondSectionCell.setJiFen(mKeyValueDao.getValue(KeyValueDao.KEY_USER_JIFEN, "0"));
            secondSectionCell.setChaiFu(mKeyValueDao.getValue(KeyValueDao.KEY_USER_CAFIFU, "0"));
            secondSectionCell.setTeamPeople(mKeyValueDao.getValue(KeyValueDao.KEY_USER_TEAMPEOPLE, "0人"));
            mCells.add(secondSectionCell);
            mCells.add(new SectionSeparatorCell(getActivity()));

            /*
            ThirdSectionCell thirdSectionCell = new ThirdSectionCell(getActivity());
            thirdSectionCell.setAgentLevel(mKeyValueDao.getValue(KeyValueDao.KEY_USER_AGENT_LEVEL, ""));
            thirdSectionCell.setVipEndDate(mKeyValueDao.getValue(KeyValueDao.KEY_USER_VIP_END_DATE, ""));
            mCells.add(thirdSectionCell);
            mCells.add(new SectionSeparatorCell(getActivity())); */

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

        mSwipeRefreshLayout = (SwipeRefreshLayout)v.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

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

        //mLoading.show("");
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
            mSwipeRefreshLayout.setRefreshing(false);

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
            CommonCell cell2 = (CommonCell)mCells.get(9);

            /*
            ThirdSectionCell cell3 = (ThirdSectionCell) mCells.get(4);
            cell3.setVipEndDate(resp.getVipEndDate());
            cell3.setAgentLevel(resp.getLevel()); */

            cell0.getRecord().setOtherInfo(resp.getTuiJianPeople());
            mKeyValueDao.saveOrUpdate(KeyValueDao.KEY_USER_MY_TUIJIAN, resp.getTuiJianPeople());
            //cell0.updateView();
            cell1.getRecord().setOtherInfo(resp.getOrderCount());
            mKeyValueDao.saveOrUpdate(KeyValueDao.KEY_USER_MY_ORDER, resp.getOrderCount());
            //cell1.updateView();
            cell2.getRecord().setOtherInfo(resp.getTeamPeople());
            //cell2.updateView();

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
            //firstCell.update();

            mMeAdapter.notifyDataSetChanged();


            isLoading = false;
        }
    }

}
