package com.jinjunhang.onlineclass.ui.fragment;

import android.app.Activity;
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

import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.Album;
import com.jinjunhang.onlineclass.model.ZhuanLan;
import com.jinjunhang.onlineclass.service.GetAlbumsRequest;
import com.jinjunhang.onlineclass.service.GetAlbumsResponse;
import com.jinjunhang.onlineclass.service.GetZhuanLansRequest;
import com.jinjunhang.onlineclass.service.GetZhuanLansResponse;
import com.jinjunhang.onlineclass.ui.activity.WebBrowserActivity;
import com.jinjunhang.onlineclass.ui.activity.ZhuanLanListActivity;
import com.jinjunhang.onlineclass.ui.cell.ListViewCell;
import com.jinjunhang.onlineclass.ui.cell.MainPageCourseCell;
import com.jinjunhang.onlineclass.ui.cell.mainpage.TuijianCourseHeaderCell;
import com.jinjunhang.onlineclass.ui.cell.mainpage.ZhuanLanCell;
import com.jinjunhang.onlineclass.ui.cell.ZhuanLanInListCell;
import com.jinjunhang.onlineclass.ui.fragment.mainpage.Page;


import java.util.ArrayList;
import java.util.List;

public class ZhuanLanListFragment extends BottomPlayerFragment implements SwipeRefreshLayout.OnRefreshListener   {
    private final static String TAG = LogHelper.makeLogTag(ZhuanLanListFragment.class);

    public static final String EXTRA_TYPE = "EXTRA_TYPE";
    public static final String TYPE_ZHUANLAN = "TYPE_ZHUANLAN";
    public static final String TYPE_JPK = "TYPE_JPK";

    private String mType = "";
    private MyAdapter mAdapter;
    private List<ListViewCell> mCells = new ArrayList<>();
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean mIsLoading = false;
    private List<ZhuanLan> mZhuanLans = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_fragment_pushdownrefresh_white, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout)v.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mType = getActivity().getIntent().getStringExtra(EXTRA_TYPE);
        if (mType == null) {
            mType = TYPE_ZHUANLAN;
        }
        TextView titleTV = (TextView)((AppCompatActivity)getActivity()).getSupportActionBar()
                .getCustomView().findViewById(R.id.actionbar_text);
        if (titleTV != null) {
            if (mType.equals(TYPE_JPK)) {
                titleTV.setText("精品课列表");
            } else {
                titleTV.setText("专栏列表");
            }
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
        return v;
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
            text.setText("订阅");

            setLightStatusBar(customView, activity);
            Utils.setNavigationBarMusicButton(activity);
        }
    }

    @Override
    public void onRefresh() {
        new GetZhuanLanTask().execute();
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
            mSwipeRefreshLayout.setRefreshing(false);
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
