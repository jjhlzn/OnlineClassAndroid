package com.jinjunhang.onlineclass.ui.fragment.album;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.framework.lib.MyEmojiParse;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.framework.service.ServerResponse;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.db.LoginUserDao;
import com.jinjunhang.onlineclass.model.BeforeCourse;
import com.jinjunhang.onlineclass.model.Comment;
import com.jinjunhang.onlineclass.model.LiveSong;
import com.jinjunhang.onlineclass.model.LoginUser;
import com.jinjunhang.onlineclass.model.ServiceLinkManager;
import com.jinjunhang.onlineclass.model.Song;
import com.jinjunhang.onlineclass.service.GetLiveCommentsRequest;
import com.jinjunhang.onlineclass.service.GetLiveCommentsResponse;
import com.jinjunhang.onlineclass.service.GetLiveListenerRequest;
import com.jinjunhang.onlineclass.service.GetLiveListenerResponse;
import com.jinjunhang.onlineclass.service.GetSongInfoRequest;
import com.jinjunhang.onlineclass.service.GetSongInfoResponse;
import com.jinjunhang.onlineclass.service.JoinRoomRequest;
import com.jinjunhang.onlineclass.service.SendLiveCommentRequest;
import com.jinjunhang.onlineclass.service.SendLiveCommentResponse;
import com.jinjunhang.onlineclass.ui.activity.WebBrowserActivity;
import com.jinjunhang.onlineclass.ui.activity.mainpage.BottomTabLayoutActivity;
import com.jinjunhang.onlineclass.ui.cell.LiveSongListViewCellAdapter;
import com.jinjunhang.onlineclass.ui.cell.player.LivePlayerCell;
import com.jinjunhang.onlineclass.ui.cell.player.PlayerCell;
import com.jinjunhang.onlineclass.ui.fragment.BaseFragment;
import com.jinjunhang.onlineclass.ui.fragment.CourseListFragment;
import com.jinjunhang.onlineclass.ui.fragment.SettingsFragment;
import com.jinjunhang.onlineclass.ui.fragment.ShopWebBrowserFragment;
import com.jinjunhang.onlineclass.ui.fragment.mainpage.MainPageFragment;
import com.jinjunhang.onlineclass.ui.fragment.user.MeFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by jjh on 2016-7-2.
 */
public class NewLiveSongFragment extends BaseFragment {

    public final static int MAX_COMMENT_COUNT = 10;

    private final static String TAG = LogHelper.makeLogTag(NewLiveSongFragment.class);


    public static String CHAT_MESSAGE_CMD = "chat message";
    public static String CHAT_JOIN_ROOM = "join room";


    private ViewPager mViewPager;
    private BaseFragment[] mFragmensts;
    private MyPagerAdapter mMyPagerAdapter;
    private Button couseOverViewBtn, otherCourseBtn, signUpBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        //View v = super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.activity_fragment_live_player, container, false);

        //final ScrollView scrollView = (ScrollView)v.findViewById(R.id.scrollView);

        couseOverViewBtn = (Button)v.findViewById(R.id.courseOverviewBtn);
        otherCourseBtn = (Button)v.findViewById(R.id.otherCoursesBtn);
        signUpBtn = (Button)v.findViewById(R.id.SignupBtn);

        couseOverViewBtn.setTextColor(getActivity().getResources().getColor(R.color.tab_selected_color));
        //otherCourseBtn.setTextColor(0x000000);

        couseOverViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                couseOverViewBtn.setTextColor(getActivity().getResources().getColor(R.color.tab_selected_color));
                otherCourseBtn.setTextColor(getActivity().getResources().getColor(R.color.black));
                mViewPager.setCurrentItem(0);
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), WebBrowserActivity.class)
                        .putExtra(WebBrowserActivity.EXTRA_TITLE, "我要报名")
                        .putExtra(WebBrowserActivity.EXTRA_URL, ServiceLinkManager.MyAgentUrl2());
                getActivity().startActivity(i);
            }
        });

        otherCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                couseOverViewBtn.setTextColor(getActivity().getResources().getColor(R.color.black));
                otherCourseBtn.setTextColor(getActivity().getResources().getColor(R.color.tab_selected_color));
                mViewPager.setCurrentItem(1);
            }
        });

        mFragmensts = DataGenerator.getFragments(getActivity(), "", this);
        mViewPager = (ViewPager)v.findViewById(R.id.viewpager);
        mMyPagerAdapter = new MyPagerAdapter(getActivity().getSupportFragmentManager());
        mViewPager.setAdapter(mMyPagerAdapter);

        //resetViewPagerHeight();
        //params.height = 3600;
        //mViewPager.setLayoutParams(params);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                resetViewPagerHeight(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return v;
    }

    public void resetViewPagerHeight(int index) {
        ViewGroup.LayoutParams params = mViewPager.getLayoutParams();
        if (index == 0)
            params.height= ((CourseOverviewFragment)mFragmensts[index]).getListViewHeightBasedOnChildren();
        else
            params.height= ((BeforeCoursesFragment)mFragmensts[index]).getListViewHeightBasedOnChildren();
        params.height = (int)(1.1 * params.height);
        LogHelper.d(TAG, "set viewpager height = " + params.height);
        mViewPager.setLayoutParams(params);
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmensts[position];
        }

        @Override
        public int getCount() {
            return mFragmensts.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "";
        }
    }


    public static class DataGenerator {

        public static BaseFragment[] getFragments(Activity activity, String from, NewLiveSongFragment fragment){
            BaseFragment fragments[] = new BaseFragment[2];
            try {

                fragments[0] = CourseOverviewFragment.class.newInstance();
                ((CourseOverviewFragment)fragments[0]).mSongFragment = fragment;
                fragments[1] = BeforeCoursesFragment.class.newInstance();
            }catch (Exception  ex) {
                LogHelper.e("DataGenerator", ex);
            }
            return fragments;
        }
    }

}

