package com.jinjunhang.onlineclass.ui.fragment.album;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.framework.service.ServerResponse;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.Comment;
import com.jinjunhang.onlineclass.model.Course;
import com.jinjunhang.onlineclass.model.Song;
import com.jinjunhang.onlineclass.service.GetCourseInfoRequest;
import com.jinjunhang.onlineclass.service.GetCourseInfoResponse;
import com.jinjunhang.onlineclass.service.GetCoursesRequest;
import com.jinjunhang.onlineclass.service.GetCoursesResponse;
import com.jinjunhang.onlineclass.service.GetLiveCommentsRequest;
import com.jinjunhang.onlineclass.service.GetLiveCommentsResponse;
import com.jinjunhang.onlineclass.ui.cell.ListViewCell;
import com.jinjunhang.onlineclass.ui.cell.comment.CommentHeaderCell;
import com.jinjunhang.onlineclass.ui.cell.player.CourseOverViewCell;
import com.jinjunhang.onlineclass.ui.cell.player.NewCommentCell;
import com.jinjunhang.onlineclass.ui.cell.player.NewCommentHeaderCell;
import com.jinjunhang.onlineclass.ui.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class CourseOverviewFragment extends BaseFragment {


    private MyListAdapter mListAdapter;
    private ListView mListView;
    private List<Comment> mComments = new ArrayList<>();
    private List<ListViewCell> mCells = new ArrayList<>();
    public NewLiveSongFragment mSongFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_fragment_nestedlistview, container, false);

        mListView = (ListView) v.findViewById(R.id.listView);

        //去掉列表的分割线
        mListView.setDividerHeight(0);
        mListView.setDivider(null);

        mCells.add(new CourseOverViewCell(getActivity(), new Course()));
        mCells.add(new NewCommentHeaderCell(getActivity()));

        mListAdapter = new MyListAdapter(getActivity(), mCells);
        mListView.setAdapter(mListAdapter);

        if (mSongFragment != null)
             mSongFragment.resetViewPagerHeight(0);

        new GetLiveSongCommentsTask().execute();
        new GetCourseInfoTask().execute();
        return v;
    }

    private void setComments() {
        for (Comment comment : mComments) {
            mCells.add(new NewCommentCell(getActivity(), comment));
        }
        mListAdapter.notifyDataSetChanged();
        if (mSongFragment != null && mSongFragment.getCurrentSelectPage() == 0)
            mSongFragment.resetViewPagerHeight(0);
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


    public
    String mLastCommentId = "-1";
    private class GetLiveSongCommentsTask extends AsyncTask<Void, Void, GetLiveCommentsResponse> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected GetLiveCommentsResponse doInBackground(Void... params) {
            GetLiveCommentsRequest req = new GetLiveCommentsRequest();
            req.setSong(new Song());
            req.setLastId(mLastCommentId);
            return new BasicService().sendRequest(req);
        }

        @Override
        protected void onPostExecute(GetLiveCommentsResponse resp) {
            if (resp.getStatus() != ServerResponse.SUCCESS) {
                return;
            }
            //commentCount += resp.getCommentList().size();
            if (resp.getCommentList().size() > 0) {
                mLastCommentId = resp.getCommentList().get(0).getId() + "";
            }

            mComments = resp.getCommentList();
            setComments();
        }
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

            Course course = resp.getCourse();
            CourseOverViewCell cell = (CourseOverViewCell) mListView.getItemAtPosition(0);
            cell.setCourse(course);
            mListAdapter.notifyDataSetChanged();
        }
    }


}
