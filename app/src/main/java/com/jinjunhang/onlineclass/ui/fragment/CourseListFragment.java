package com.jinjunhang.onlineclass.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jinjunhang.framework.controller.PagableController;
import com.jinjunhang.framework.lib.LoadingAnimation;
import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.framework.service.ServerResponse;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.Album;
import com.jinjunhang.onlineclass.model.AlbumType;
import com.jinjunhang.onlineclass.model.LiveSong;
import com.jinjunhang.onlineclass.model.Song;
import com.jinjunhang.onlineclass.service.GetAlbumSongsRequest;
import com.jinjunhang.onlineclass.service.GetAlbumSongsResponse;
import com.jinjunhang.onlineclass.service.GetAlbumsRequest;
import com.jinjunhang.onlineclass.service.GetAlbumsResponse;
import com.jinjunhang.onlineclass.service.GetCoursesRequest;
import com.jinjunhang.onlineclass.service.GetCoursesResponse;
import com.jinjunhang.onlineclass.service.GetTuijianCoursesResponse;
import com.jinjunhang.onlineclass.ui.activity.WebBrowserActivity;
import com.jinjunhang.onlineclass.ui.activity.album.AlbumDetailActivity;
import com.jinjunhang.onlineclass.ui.activity.album.NewLiveSongActivity;
import com.jinjunhang.onlineclass.ui.activity.album.SongActivity;
import com.jinjunhang.onlineclass.ui.cell.ListViewCell;
import com.jinjunhang.onlineclass.ui.cell.LiveCourseCell;
import com.jinjunhang.onlineclass.ui.fragment.album.AlbumDetailFragment;
import com.jinjunhang.onlineclass.ui.fragment.album.BaseSongFragment;
import com.jinjunhang.player.ExoPlayerNotificationManager;
import com.jinjunhang.player.MusicPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jinjunhang on 2018/4/9.
 */

public class CourseListFragment extends BaseFragment  {
    private final static String TAG = LogHelper.makeLogTag(CourseListFragment.class);

    private LoadingAnimation mLoading;
    private ImageButton everydayBtn, vipBtn, agentBtn;
    private CoursePage[] pages;
    private ViewGroup[] mViewGroups;
    private ExoPlayerNotificationManager mNotificationManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mNotificationManager = ExoPlayerNotificationManager.getInstance(getActivity());

        View v = inflater.inflate(R.layout.fragment_couselist, null, false);

        mLoading = new LoadingAnimation(getActivity(), (ViewGroup)v.findViewById(R.id.fragmentContainer));

        everydayBtn = (ImageButton) v.findViewById(R.id.everyday_btn);
        vipBtn = (ImageButton) v.findViewById(R.id.vip_btn);
        agentBtn = (ImageButton) v.findViewById(R.id.agent_btn);

        pages = new CoursePage[3];
        mViewGroups = new ViewGroup[3];
        for(int i = 0; i < 3; i++) {
            pages[i] = new CoursePage(inflater, container, savedInstanceState);
        }

        mViewGroups[0] = (ViewGroup)v.findViewById(R.id.frag_1);
        mViewGroups[1] = (ViewGroup)v.findViewById(R.id.frag_2);
        mViewGroups[2] = (ViewGroup)v.findViewById(R.id.frag_3);

        mViewGroups[0].addView(pages[0].v);
        mViewGroups[1].addView(pages[1].v);
        mViewGroups[2].addView(pages[2].v);




        everydayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                everydayBtn.setImageDrawable(getActivity().getDrawable(R.drawable.everyday_s));
                vipBtn.setImageDrawable(getActivity().getDrawable(R.drawable.vip));
                agentBtn.setImageDrawable(getActivity().getDrawable(R.drawable.agent));

                showPage(0);

            }
        });

        vipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                everydayBtn.setImageDrawable(getActivity().getDrawable(R.drawable.everyday));
                vipBtn.setImageDrawable(getActivity().getDrawable(R.drawable.vip_s));
                agentBtn.setImageDrawable(getActivity().getDrawable(R.drawable.agent));

                showPage(1);
            }
        });

        agentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                everydayBtn.setImageDrawable(getActivity().getDrawable(R.drawable.everyday));
                vipBtn.setImageDrawable(getActivity().getDrawable(R.drawable.vip));
                agentBtn.setImageDrawable(getActivity().getDrawable(R.drawable.agent_s));

                showPage(2);
            }
        });


        showPage(0);
        return v;
    }



    private void showPage(int index) {

        for(int i = 0; i < 3; i++) {
            if (i == index) {
                mViewGroups[i].setVisibility(View.VISIBLE);
                pages[i].v.setVisibility(View.VISIBLE);
                if (!pages[i].loadCompleted) {
                    pages[i].loadCourses();
                }
            } else {
                mViewGroups[i].setVisibility(View.INVISIBLE);
                pages[i].v.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        //放在这里为了防止双击
        mLoading.hide();
    }

    @Override
    public void onResume() {
        super.onResume();
        //changeActionBar();
    }

    @Override
    public void changeActionBar() {
        LogHelper.d(TAG, "changeActionBar called");
        AppCompatActivity activity = (AppCompatActivity)getActivity();
        if (activity != null) {
            activity.getSupportActionBar().show();
            activity.getSupportActionBar().setElevation(0);
            activity.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            final View customView = activity.getLayoutInflater().inflate(R.layout.actionbar_courselist, null);
            activity.getSupportActionBar().setCustomView(customView);
            Toolbar parent = (Toolbar) customView.getParent();

            parent.setContentInsetsAbsolute(0, 0);

            TextView text = (TextView)customView.findViewById(R.id.actionbar_text);
            text.setText("直播");
            //customView.findViewById(R.id.actionbar_right_button).setVisibility(View.INVISIBLE);

            setLightStatusBar(customView, activity);
        }
    }


    private class CoursePage implements SwipeRefreshLayout.OnRefreshListener {

        public View v;
        //private LoadingAnimation mLoading;
        private ListView mListView;
        private MyAdapter mMyAdapter;
        private SwipeRefreshLayout mSwipeRefreshLayout;
        private List<ListViewCell> mCells = new ArrayList<>();
        private List<Album> mCourses = new ArrayList<>();
        private boolean mIsLoading = false;
        public boolean loadCompleted = false;
        //private GetAlbumSongsTask mGetAlbumSongsTask;

        @Override
        public void onRefresh() {
            if (mIsLoading) {
                return;
            }
            mIsLoading = true;

            loadCourses();
        }


        public CoursePage(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            v = inflater.inflate(R.layout.activity_fragment_pushdownrefresh_white, container, false);

            mSwipeRefreshLayout = (SwipeRefreshLayout)v.findViewById(R.id.swipe_refresh_layout);
            mSwipeRefreshLayout.setOnRefreshListener(this);

            //mGetAlbumSongsTask = new GetAlbumSongsTask();
            mListView = (ListView)v.findViewById(R.id.listView);
            //去掉列表的分割线
            mListView.setDividerHeight(0);
            mListView.setDivider(null);

            mCells = new ArrayList<>();

            mMyAdapter = new MyAdapter(getActivity(), mCells);
            mListView.setAdapter(mMyAdapter);

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    final Album album = mCourses.get(i);
                    mLoading.show("");
                    GetAlbumSongsRequest request = new GetAlbumSongsRequest();
                    request.setAlbum(album);
                    new GetAlbumSongsTask().execute(request);
                }
            });

        }

        public void loadCourses() {
            GetAlbumsRequest request = new GetAlbumsRequest(AlbumType.LiveAlbumType);
            new GetAlbumsTask().execute(request);
        }

        public void SetCourseListView() {
            mCells.clear();
            for (Album course : mCourses) {
                mCells.add(new LiveCourseCell(getActivity(), course));
            }
            mMyAdapter.notifyDataSetChanged();
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


        public class GetAlbumSongsTask extends AsyncTask<GetAlbumSongsRequest, Void, GetAlbumSongsResponse> {

            private GetAlbumSongsRequest request;
            private CoursePage mPage;

            @Override
            protected GetAlbumSongsResponse doInBackground(GetAlbumSongsRequest... params) {
                request = params[0];
                LogHelper.d(TAG, "GetAlbumSongsTask.GetAlbumSongsTask() called");
                return new BasicService().sendRequest(request);
            }

            @Override
            protected void onPostExecute(GetAlbumSongsResponse resp) {
                super.onPostExecute(resp);

                if (!resp.isSuccess()) {
                    mLoading.hide();
                    LogHelper.e(TAG, resp.getErrorMessage());
                    Utils.showErrorMessage(getActivity(), resp.getErrorMessage());
                    return;
                }

                if (resp.getResultSet().size() >= 1) {
                    LiveSong song = (LiveSong) resp.getResultSet().get(0);
                    MusicPlayer musicPlayer = MusicPlayer.getInstance(getActivity());
                    if (!musicPlayer.isPlay(song)) {
                        musicPlayer.pause();
                        musicPlayer.play(resp.getResultSet(), 0);
                        mNotificationManager.display();
                    }
                    Intent i = new Intent(getActivity(), NewLiveSongActivity.class);
                    startActivity(i);
                    return;
                } else {
                    mLoading.hide();
                    Utils.showErrorMessage(getActivity(), "服务端出错");
                    return;
                }

            }
        }


        private class GetAlbumsTask extends AsyncTask<GetAlbumsRequest, Void, GetAlbumsResponse> {

            private GetAlbumsRequest request;
            @Override
            protected GetAlbumsResponse doInBackground(GetAlbumsRequest... params) {
                request = params[0];
                return new BasicService().sendRequest(request);
            }

            @Override
            protected void onPostExecute(GetAlbumsResponse resp) {
                super.onPostExecute(resp);
                //mLoading.hide();
                mSwipeRefreshLayout.setRefreshing(false);
                mIsLoading = false;

                if (!resp.isSuccess()) {
                    LogHelper.e(TAG, resp.getErrorMessage());
                    Utils.showErrorMessage(getActivity(), resp.getErrorMessage());
                    return;
                }

                mCourses = resp.getResultSet();
                SetCourseListView();
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //mLoading.show("");
            }
        }
    }
}
