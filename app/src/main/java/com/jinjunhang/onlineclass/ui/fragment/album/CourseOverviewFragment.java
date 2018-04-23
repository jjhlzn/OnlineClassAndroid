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
import com.jinjunhang.onlineclass.ui.fragment.album.player.ChatManager;

import java.util.ArrayList;
import java.util.List;

public class CourseOverviewFragment extends BaseFragment {

    public final static int MAX_COMMENT_COUNT = 10;

    private MyListAdapter mListAdapter;
    private ListView mListView;
    private List<Comment> mComments = new ArrayList<>();
    private List<ListViewCell> mCells = new ArrayList<>();
    public NewLiveSongFragment mSongFragment;
    private ChatManager mChatManager;

    public void setChatManager(ChatManager chatManager) {
        mChatManager = chatManager;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_fragment_listview, container, false);

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

        mChatManager.loadComments();
        new GetCourseInfoTask().execute();
        return v;
    }

    public void setCommentsView(List<Comment> comments) {
        mComments = comments;
        setCommentsView0(mCells);
    }

    private void setCommentsView0(List<ListViewCell> cells) {
        for (Comment comment : mComments) {
            cells.add(new NewCommentCell(getActivity(), comment));
        }
        mListAdapter.setCells(cells);
        mListAdapter.notifyDataSetChanged();
        if (mSongFragment != null && mSongFragment.getCurrentSelectPage() == 0)
            mSongFragment.resetViewPagerHeight(0);
    }

    public void newCommentHanlder(Comment comment) {
        //shiftComments();
        mComments.add(0, comment);
        if (mComments.size() > MAX_COMMENT_COUNT) {
            mComments.remove(mComments.size() -1);
        }

        List<ListViewCell> newCells = new ArrayList<>();
        newCells.add(mCells.get(0));
        newCells.add(mCells.get(1));
        setCommentsView0(newCells);
    }


    private class MyListAdapter extends ArrayAdapter<ListViewCell> {
        private List<ListViewCell> mViewCells;

        public void setCells(List<ListViewCell> cells) {
            mViewCells = cells;

        }

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

            Course course = resp.getCourse();
            CourseOverViewCell cell = (CourseOverViewCell) mListView.getItemAtPosition(0);
            cell.setCourse(course);
            mListAdapter.notifyDataSetChanged();
        }
    }


}
