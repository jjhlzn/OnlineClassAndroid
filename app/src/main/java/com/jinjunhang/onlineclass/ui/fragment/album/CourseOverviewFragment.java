package com.jinjunhang.onlineclass.ui.fragment.album;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.Comment;
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

        mCells.add(new CourseOverViewCell(getActivity()));
        mCells.add(new NewCommentHeaderCell(getActivity()));
        setComments();

        mListAdapter = new MyListAdapter(getActivity(), mCells);
        mListView.setAdapter(mListAdapter);

        if (mSongFragment != null)
             mSongFragment.resetViewPagerHeight(0);
        return v;
    }

    private void setComments() {
        for(int i = 0; i < 10; i++) {
            Comment comment = new Comment();
            comment.setContent("1111111");
            comment.setNickName("test");
            comment.setTime("47分钟前");
            mComments.add(comment);
            mCells.add(new NewCommentCell(getActivity(), comment));
        }
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

}
