package com.jinjunhang.onlineclass.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.AlbumType;
import com.jinjunhang.onlineclass.model.ServiceLinkManager;
import com.jinjunhang.onlineclass.ui.activity.AlbumListActivity;
import com.jinjunhang.onlineclass.ui.activity.WebBrowserActivity;
import com.jinjunhang.onlineclass.ui.cell.AlbumTypeCell;
import com.jinjunhang.onlineclass.ui.cell.CellClickListener;
import com.jinjunhang.onlineclass.ui.cell.ListViewCell;
import com.jinjunhang.onlineclass.ui.cell.SectionSeparatorCell;
import com.jinjunhang.onlineclass.ui.cell.me.CommonCell;
import com.jinjunhang.onlineclass.ui.cell.me.FirstSectionCell;
import com.jinjunhang.onlineclass.ui.cell.me.LineRecord;
import com.jinjunhang.onlineclass.ui.cell.me.SecondSectionCell;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jjh on 2016-6-29.
 */
public class MeFragment extends BaseFragment {

    private List<ListViewCell> mCells = new ArrayList<>();
    private ListView mListView;
    private MeAdapter mMeAdapter;

    private List<LineRecord> mThirdSections = new ArrayList<LineRecord>();
    private List<LineRecord> mFourthSections = new ArrayList<>();
    private List<LineRecord> mFifthSections = new ArrayList<>();
    private WebBroserClickListener webBroserClickListener = new WebBroserClickListener();
    private DummyClickListener dummyClickListener = new DummyClickListener();

    private void initSections() {
        if (mThirdSections.size() == 0) {
            mThirdSections.add(new LineRecord(R.drawable.log, "我的推荐", webBroserClickListener, ServiceLinkManager.MyTuiJianUrl()));
            mThirdSections.add(new LineRecord(R.drawable.log, "我的订单", webBroserClickListener, ServiceLinkManager.MyOrderUrl()));
            mThirdSections.add(new LineRecord(R.drawable.log, "我的团队", webBroserClickListener, ServiceLinkManager.MyTeamUrl()));
            mThirdSections.add(new LineRecord(R.drawable.log, "我要提现", webBroserClickListener, ServiceLinkManager.MyExchangeUrl()));
        }

        if (mFourthSections.size() == 0) {
            mFourthSections.add(new LineRecord(R.drawable.log, "我的资料", dummyClickListener, ""));
            mFourthSections.add(new LineRecord(R.drawable.log, "我的二维码", dummyClickListener, ""));
        }

        if (mFifthSections.size() == 0) {
            mFifthSections.add(new LineRecord(R.drawable.log, "申请代理", webBroserClickListener, ServiceLinkManager.MyAgentUrl()));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_fragment_pushdownrefresh, container, false);
        initSections();
        mListView = (ListView) v.findViewById(R.id.listView);

        //去掉列表的分割线
        mListView.setDividerHeight(0);
        mListView.setDivider(null);

        if (mCells.size() == 0) {
            FirstSectionCell item = new FirstSectionCell(getActivity());
            mCells.add(item);

            mCells.add(new SecondSectionCell(getActivity()));
            mCells.add(new SectionSeparatorCell(getActivity()));

            for(int i = 0; i < mThirdSections.size(); i++) {
                CommonCell cell = new CommonCell(getActivity(), mThirdSections.get(i));
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
        }


        mMeAdapter = new MeAdapter(mCells);
        mListView.setAdapter(mMeAdapter);


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 3) {
                    ListViewCell cell = mMeAdapter.getItem(position);
                    cell.onClick();
                }
            }
        });
        return v;
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
            LineRecord record = commonCell.getmRecord();
            Intent i = new Intent(getActivity(), WebBrowserActivity.class)
                    .putExtra(WebBrowserActivity.EXTRA_URL, record.getmUrl())
                    .putExtra(WebBrowserActivity.EXTRA_TITLE, record.getmTitle());
            startActivity(i);
        }
    }

    private class DummyClickListener implements CellClickListener {
        @Override
        public void onClick(ListViewCell cell) {

        }
    }

}
