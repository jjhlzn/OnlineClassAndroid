package com.jinjunhang.onlineclass.ui.cell.player;


import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinjunhang.onlineclass.model.Course;

import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.ui.cell.BaseListViewCell;
import com.jinjunhang.onlineclass.ui.fragment.album.NewLiveSongFragment;
import com.jinjunhang.player.MusicPlayer;

public class CourseOverViewCell extends BaseListViewCell {

    private Course mCourse;
    private TextView mContentView;
    private String mTitle = "";
    private NewLiveSongFragment mSongFragment;

    public CourseOverViewCell(Activity activity, Course course, NewLiveSongFragment songFragment) {
        super(activity);
        mCourse = course;
        MusicPlayer musicPlayer = MusicPlayer.getInstance(null);
        mTitle = musicPlayer.getCurrentPlaySong().getName();
        mSongFragment = songFragment;
    }

    public void setCourse(Course course) {
        mCourse = course;
    }

    @Override
    public ViewGroup getView() {
        View v = mActivity.getLayoutInflater().inflate(R.layout.new_player_view_course_overview, null);
        mContentView = (TextView) v.findViewById(R.id.content);
        mContentView.setText(mCourse.getIntroduction());
        TextView titleTextView = (TextView) v.findViewById(R.id.title);
        titleTextView.setText(mTitle);
        //if (mSongFragment != null && mSongFragment.getCurrentSelectPage() == 0)
          //  mSongFragment.resetViewPagerHeight(0);
        return (RelativeLayout)v.findViewById(R.id.container);

    }

    public int getMeasuredHeight() {
        return getView().getMeasuredHeight();
    }
}