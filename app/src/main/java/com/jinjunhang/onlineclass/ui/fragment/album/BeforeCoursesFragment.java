package com.jinjunhang.onlineclass.ui.fragment.album;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.BeforeCourse;
import com.jinjunhang.onlineclass.model.Comment;
import com.jinjunhang.onlineclass.ui.cell.ListViewCell;
import com.jinjunhang.onlineclass.ui.cell.player.BeforeCourseHeaderCell;
import com.jinjunhang.onlineclass.ui.cell.player.BeforeCourseItemCell;
import com.jinjunhang.onlineclass.ui.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class BeforeCoursesFragment extends BaseFragment {

    private MyListAdapter mListAdapter;
    private ListView mListView;
    private List<BeforeCourse> mBeforeCourses = new ArrayList<>();
    private List<ListViewCell> mCells = new ArrayList<>();
    public NewLiveSongFragment mSongFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_fragment_listview, container, false);

        mListView = (ListView) v.findViewById(R.id.listView);

        //去掉列表的分割线
        mListView.setDividerHeight(0);
        mListView.setDivider(null);

        mCells.add(new BeforeCourseHeaderCell(getActivity()));
        setBeforeCourses();

        mListAdapter = new MyListAdapter(getActivity(), mCells);
        mListView.setAdapter(mListAdapter);

        return v;
    }

    private void setBeforeCourses() {
        for(int i = 0; i < 10; i++) {
            BeforeCourse course = new BeforeCourse();
            course.setSequence(i + 1);
            course.setTitle("test");
            course.setTime("10分钟");
            mBeforeCourses.add(course);
            mCells.add(new BeforeCourseItemCell(getActivity(), course));
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
