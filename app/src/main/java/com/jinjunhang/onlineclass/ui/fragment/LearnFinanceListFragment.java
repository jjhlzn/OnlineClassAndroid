package com.jinjunhang.onlineclass.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.LearnFinanceItem;
import com.jinjunhang.onlineclass.service.GetLearnFinancesRequest;
import com.jinjunhang.onlineclass.service.GetLearnFinancesResponse;
import com.jinjunhang.onlineclass.ui.activity.WebBrowserActivity;
import com.jinjunhang.onlineclass.ui.cell.ListViewCell;
import com.jinjunhang.onlineclass.ui.cell.ZhuanLanInListCell;
import com.jinjunhang.onlineclass.ui.cell.mainpage.LearnFinanceCell;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class LearnFinanceListFragment extends BaseFragment {
    private final static String TAG = LogHelper.makeLogTag(LearnFinanceListFragment.class);

    
    private MyAdapter mAdapter;
    private List<ListViewCell> mCells = new ArrayList<>();
    private SmartRefreshLayout mSwipeRefreshLayout;
    private boolean mIsLoading = false;
    private List<LearnFinanceItem> mLearnFinanceItems = new ArrayList<>();
    private Toolbar mToolbar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_fragment_pushdownrefresh_smart;
    }

    @Override
    protected boolean isNeedTopMargin() {
        return false;
    }

    public void refreshList() {
        mAdapter.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        mSwipeRefreshLayout = v.findViewById(R.id.swipe_refresh_layout);
        Utils.setRefreshHeader(getActivity(), mSwipeRefreshLayout);

        mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                new GetLearnFinanceListTask().execute();
                mSwipeRefreshLayout.finishRefresh(7000);
            }
        });

        mAdapter = new MyAdapter(getActivity(), mCells);
        ListView listView = v.findViewById(R.id.listView);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ZhuanLanInListCell cell = (ZhuanLanInListCell)mAdapter.getItem(i);

                Intent intent = new Intent(getActivity(), WebBrowserActivity.class)
                        .putExtra(WebBrowserActivity.EXTRA_TITLE, cell.getZhuanLan().getName())
                        .putExtra(WebBrowserActivity.EXTRA_URL, cell.getZhuanLan().getUrl());
                getActivity().startActivity(intent);
            }
        });

        new GetLearnFinanceListTask().execute();

        mToolbar = v.findViewById(R.id.toolbar);
        ImmersionBar.setTitleBar(getActivity(), mToolbar);
        ImmersionBar.with(this).statusBarDarkFont(true).init();


        setToolbar();
        return v;
    }

    @Override
    public Toolbar getToolBar() {
        return mToolbar;
    }

    public void setToolbar() {

        ImageButton backButton = mToolbar.findViewById(R.id.actionbar_back_button);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        TextView text = mToolbar.findViewById(R.id.actionbar_text);
        text.setText("学点金融");

        Utils.updateNavigationBarButton(getActivity());

    }


    private void setLearnFinancesView() {
        mCells.clear();
        for(LearnFinanceItem item : mLearnFinanceItems) {
            mCells.add(new LearnFinanceCell(getActivity(), this, item));
        }
        mAdapter.notifyDataSetChanged();
    }


    private class GetLearnFinanceListTask extends AsyncTask<Void, Void, GetLearnFinancesResponse> {

        @Override
        protected GetLearnFinancesResponse doInBackground(Void... params) {
            GetLearnFinancesRequest request = new GetLearnFinancesRequest();
            return new BasicService().sendRequest(request);
        }

        @Override
        protected void onPostExecute(GetLearnFinancesResponse resp) {
            super.onPostExecute(resp);
            //mLoading.hide();
            mSwipeRefreshLayout.finishRefresh();
            mIsLoading = false;

            if (!resp.isSuccess()) {
                LogHelper.e(TAG, resp.getErrorMessage());
                Utils.showErrorMessage(getActivity(), resp.getErrorMessage());
                return;
            }

            mLearnFinanceItems = resp.getLearnFinanceItems();
            setLearnFinancesView();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mLoading.show("");
        }
    }

    private class MyAdapter extends ArrayAdapter<ListViewCell> {
        private List<ListViewCell> mViewCells;

        public MyAdapter(Activity activity, List<ListViewCell> cells) {
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

            return item.getView(convertView);
        }
    }
}
