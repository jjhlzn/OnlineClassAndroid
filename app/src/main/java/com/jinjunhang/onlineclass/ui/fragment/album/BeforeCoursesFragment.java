package com.jinjunhang.onlineclass.ui.fragment.album;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.framework.service.ServerResponse;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.Course;
import com.jinjunhang.onlineclass.service.GetCourseInfoRequest;
import com.jinjunhang.onlineclass.service.GetCourseInfoResponse;
import com.jinjunhang.onlineclass.ui.cell.ListViewCell;
import com.jinjunhang.onlineclass.ui.cell.player.BeforeCourseHeaderCell;
import com.jinjunhang.onlineclass.ui.cell.player.BeforeCourseItemCell;
import com.jinjunhang.onlineclass.ui.cell.player.CourseOverViewCell;
import com.jinjunhang.onlineclass.ui.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class BeforeCoursesFragment extends BaseFragment {

    private MyListAdapter mListAdapter;
    private ListView mListView;
    private List<Course> mCourses = new ArrayList<>();
    private List<ListViewCell> mCells = new ArrayList<>();
    public NewLiveSongFragment mSongFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_fragment_listview_white, container, false);

        mListView = (ListView) v.findViewById(R.id.listView);


        //去掉列表的分割线
        mListView.setDividerHeight(0);
        mListView.setDivider(null);

        mCells.add(new BeforeCourseHeaderCell(getActivity()));

        mListAdapter = new MyListAdapter(getActivity(), mCells);
        mListView.setAdapter(mListAdapter);

        new GetCourseInfoTask().execute();
        return v;
    }

    private void setBeforeCourses() {
        for (Course course : mCourses) {
            mCells.add(new BeforeCourseItemCell(getActivity(), course));
        }
        if (mSongFragment != null && mSongFragment.getCurrentSelectPage() == 1)
            mSongFragment.resetViewPagerHeight(1);
    }

    private class MyListAdapter extends ArrayAdapter<ListViewCell> {
        private List<ListViewCell> mViewCells;

        public MyListAdapter(Activity activity, List<ListViewCell> cells) {
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

    public int getListViewHeightBasedOnChildren() {
        return Utils.getListViewHeightBasedOnChildren(mListView);
    }

    private class GetCourseInfoTask extends AsyncTask<Void, Void, GetCourseInfoResponse> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected GetCourseInfoResponse doInBackground(Void... params) {
            GetCourseInfoRequest req = new GetCourseInfoRequest();
            req.setId("");
            return new BasicService().sendRequest(req);
        }

        @Override
        protected void onPostExecute(GetCourseInfoResponse resp) {
            if (resp.getStatus() != ServerResponse.SUCCESS) {
                return;
            }

            mCourses = resp.getCourse().getBeforeCourses();
            setBeforeCourses();
            mListAdapter.notifyDataSetChanged();
        }
    }

}
