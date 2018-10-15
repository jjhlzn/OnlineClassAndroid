package com.jinjunhang.onlineclass.ui.fragment;

import android.app.Activity;
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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.ZhuanLan;
import com.jinjunhang.onlineclass.service.GetZhuanLansRequest;
import com.jinjunhang.onlineclass.service.GetZhuanLansResponse;
import com.jinjunhang.onlineclass.ui.activity.WebBrowserActivity;
import com.jinjunhang.onlineclass.ui.cell.ListViewCell;
import com.jinjunhang.onlineclass.ui.cell.ZhuanLanInListCell;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class ZhuanLanListFragment extends BaseFragment   {
    private final static String TAG = LogHelper.makeLogTag(ZhuanLanListFragment.class);

    public static final String EXTRA_TYPE = "EXTRA_TYPE";
    public static final String TYPE_ZHUANLAN = "TYPE_ZHUANLAN";
    public static final String TYPE_JPK = "TYPE_JPK";

    private String mType = "";
    private MyAdapter mAdapter;
    private List<ListViewCell> mCells = new ArrayList<>();
    private SmartRefreshLayout mSwipeRefreshLayout;
    private boolean mIsLoading = false;
    private List<ZhuanLan> mZhuanLans = new ArrayList<>();
    private Toolbar mToolbar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_fragment_pushdownrefresh_smart;
    }

    @Override
    protected boolean isNeedTopMargin() {
        return false;
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
                new GetZhuanLanTask().execute();
                mSwipeRefreshLayout.finishRefresh(7000);
            }
        });

        mType = getActivity().getIntent().getStringExtra(EXTRA_TYPE);
        if (mType == null) {
            mType = TYPE_ZHUANLAN;
        }


        mAdapter = new MyAdapter(getActivity(), mCells);
        ListView listView = (ListView)v.findViewById(R.id.listView);
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

        new GetZhuanLanTask().execute();

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
        if (mType.equals(TYPE_JPK)) {
            text.setText("精品课列表");
        } else {
            text.setText("专栏列表");
        }

        Utils.updateNavigationBarButton(getActivity());

    }


    private void setZhuanLansView() {
        mCells.clear();
        for(ZhuanLan zhuanLan : mZhuanLans) {
            mCells.add(new ZhuanLanInListCell(getActivity(), zhuanLan));
        }
        mAdapter.notifyDataSetChanged();
    }


    private class GetZhuanLanTask extends AsyncTask<Void, Void, GetZhuanLansResponse> {

        @Override
        protected GetZhuanLansResponse doInBackground(Void... params) {
            GetZhuanLansRequest request = new GetZhuanLansRequest();
            request.setType(mType);
            LogHelper.d(TAG, "mtype = " + mType);
            return new BasicService().sendRequest(request);
        }

        @Override
        protected void onPostExecute(GetZhuanLansResponse resp) {
            super.onPostExecute(resp);
            //mLoading.hide();
            mSwipeRefreshLayout.finishRefresh();
            mIsLoading = false;

            if (!resp.isSuccess()) {
                LogHelper.e(TAG, resp.getErrorMessage());
                Utils.showErrorMessage(getActivity(), resp.getErrorMessage());
                return;
            }

            mZhuanLans = resp.getZhuanLans();
            setZhuanLansView();
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

            return item.getView();
        }
    }

}
