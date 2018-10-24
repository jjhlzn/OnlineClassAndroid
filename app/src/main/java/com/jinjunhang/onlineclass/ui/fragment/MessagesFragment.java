package com.jinjunhang.onlineclass.ui.fragment;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.jinjunhang.framework.lib.LoadingAnimation;
import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.framework.wx.Util;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.Message;
import com.jinjunhang.onlineclass.service.GetMessagesRequest;
import com.jinjunhang.onlineclass.service.GetMessagesResponse;
import com.jinjunhang.onlineclass.ui.cell.ListViewCell;
import com.jinjunhang.onlineclass.ui.cell.MessageCell;
import com.jinjunhang.onlineclass.ui.fragment.user.MeFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class MessagesFragment extends BaseFragment {
    private static final String TAG = LogHelper.makeLogTag(MessagesFragment.class);

    private MessagesAdapter mMessagesAdapter;

    private LoadingAnimation mLoadingAnimation;

    @BindView(R.id.listView) ListView mListView;
    @BindView(R.id.swipe_refresh_layout) SmartRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.actionbar_text) TextView mToolBarTitle;
    @OnClick(R.id.actionbar_back_button) void backClick() {
        getActivity().finish();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_fragment_messages;
    }

    @Override
    protected boolean isNeedTopMargin() {
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, v);

        mLoadingAnimation = new LoadingAnimation(getActivity());

        mMessagesAdapter = new MessagesAdapter(getActivity(), new ArrayList<ListViewCell>());
        mListView.setAdapter(mMessagesAdapter);
        mListView.setDividerHeight(1);


        mToolBarTitle.setText("消息中心");
        ImmersionBar.setTitleBar(getActivity(), mToolbar);
        if (getUserVisibleHint()) {
            ImmersionBar.with(this).statusBarDarkFont(true).init();
        }

        Utils.setRefreshHeader(getActivity(), mSwipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                new GetMessagesTask().execute();
                refreshLayout.finishRefresh(7000/*,false*/);//传入false表示刷新失败
            }
        });

        mLoadingAnimation.show("");
        new GetMessagesTask().execute();

        return v;
    }


    private class GetMessagesTask extends AsyncTask<Void, Void, GetMessagesResponse> {
        @Override
        protected GetMessagesResponse doInBackground(Void... voids) {
            return new BasicService().sendRequest(new GetMessagesRequest());
        }

        @Override
        protected void onPostExecute(GetMessagesResponse getMessagesResponse) {
            mLoadingAnimation.hide();
            if (!getMessagesResponse.isSuccess()) {
                Utils.showMessage(getActivity(), getMessagesResponse.getErrorMessage());
                return;
            }
            List<Message> messages = getMessagesResponse.getMessages();
            LogHelper.d(TAG, "messages.count = " + messages.size());
            mMessagesAdapter.setMessages(messages);
            mSwipeRefreshLayout.finishRefresh();
        }


    }


    private class MessagesAdapter extends ArrayAdapter {
        private List<ListViewCell> mViewCells;

        public MessagesAdapter(Activity activity, List<ListViewCell> cells) {
            super(activity, 0, cells);
            mViewCells = cells;
        }

        private void setMessages(List<Message> messages) {
            mViewCells.clear();
            for(int i = 0; i< messages.size(); i++) {
                mViewCells.add(new MessageCell(getActivity(), messages.get(i)));
            }
            this.notifyDataSetChanged();
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
