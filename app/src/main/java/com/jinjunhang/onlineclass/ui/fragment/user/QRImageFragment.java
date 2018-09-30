package com.jinjunhang.onlineclass.ui.fragment.user;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.db.LoginUserDao;
import com.jinjunhang.onlineclass.db.QrImageDao;
import com.jinjunhang.onlineclass.service.GetShareImagesRequest;
import com.jinjunhang.onlineclass.service.GetShareImagesResponse;
import com.jinjunhang.onlineclass.service.GetTuijianCoursesRequest;
import com.jinjunhang.onlineclass.service.GetTuijianCoursesResponse;
import com.jinjunhang.onlineclass.ui.activity.mainpage.BottomTabLayoutActivity;
import com.jinjunhang.onlineclass.ui.fragment.BaseFragment;
import com.jinjunhang.onlineclass.ui.fragment.ShopWebBrowserFragment;
import com.jinjunhang.onlineclass.ui.fragment.mainpage.MainPageFragment;
import com.jinjunhang.onlineclass.ui.lib.ShareManager;
import com.jinjunhang.onlineclass.ui.lib.ShareManager2;
import com.jinjunhang.framework.lib.BitmapHelper;
import com.jinjunhang.framework.lib.LogHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jjh on 2016-7-1.
 */
public class QRImageFragment extends BaseFragment {

    private static final String TAG = LogHelper.makeLogTag(QRImageFragment.class);

    private QrImageDao qrImageDao;
    private ShareManager mShareManager;

    private ViewPager mViewPager;
    private BaseFragment[] mFragmensts;

    private MyPagerAdapter mMyPagerAdapter;
    private ViewGroup mOverlayBg;
    private Button mShareBtn;
    private ViewGroup mShareView;
    private List<String> mShareImageUrls = new ArrayList<>();
    //private String selectUrl = "";


    @Override
    protected int getLayoutId() {
        return R.layout.activity_fragment_qrimage;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = super.onCreateView(inflater, container, savedInstanceState);
        mOverlayBg = (ViewGroup)mView.findViewById(R.id.overlay_bg);
        mShareBtn = (Button)mView.findViewById(R.id.share_btn);
        mShareView = (ViewGroup)mView.findViewById(R.id.share_view);

        mFragmensts = DataGenerator.getFragments();
        mMyPagerAdapter = new MyPagerAdapter(getActivity().getSupportFragmentManager());
        mViewPager = (ViewPager)mView.findViewById(R.id.viewpager);
        mViewPager.setAdapter(mMyPagerAdapter);
       // mViewPager.setCurrentItem(0);
        mViewPager.setClipToPadding(false);
        mViewPager.setPadding(120,0,120,0);
        mViewPager.setPageMargin(80);

        mShareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShareBtn.setVisibility(View.INVISIBLE);
                mOverlayBg.setVisibility(View.VISIBLE);
                mViewPager.setEnabled(false);
                mShareView.setVisibility(View.VISIBLE);
            }
        });

        mOverlayBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShareBtn.setVisibility(View.VISIBLE);
                mOverlayBg.setVisibility(View.INVISIBLE);
                mViewPager.setEnabled(true);
                mShareView.setVisibility(View.INVISIBLE);
            }
        }) ;
        mView.findViewById(R.id.sharez_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position < mShareImageUrls.size()) {
                    //selectUrl = mShareImageUrls.get(position);
                    mShareManager.setShareUrl( mShareImageUrls.get(position));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        new GetShareImagesTask().execute();

        mShareManager = new ShareManager2((AppCompatActivity) getActivity(), mView);


        /*
        qrImageDao = QrImageDao.getInstance(getActivity());

        final ImageView qrImage = (ImageView) v.findViewById(R.id.qr_image);
        if (qrImageDao.get() != null) {
            qrImage.setImageBitmap(qrImageDao.get());
        } */

        /*
        final String qrImageUrl = LoginUserDao.getInstance(getActivity()).get().getCodeImageUrl();


        (new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                try {
                    Bitmap bitmap =  BitmapHelper.fetchAndRescaleBitmap(qrImageUrl+"?"+ new Date().getTime(), 300, 300);
                    return  bitmap;
                } catch (IOException ex) {
                    LogHelper.e(TAG, ex);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                qrImageDao.saveOrUpdate(bitmap);
                qrImage.setImageBitmap(bitmap);
            }
        }).execute(); */

        return mView;
    }

    private void loadShareImages() {
        mFragmensts = DataGenerator.getFragments(mShareImageUrls);
        mMyPagerAdapter.notifyDataSetChanged();
        if (mShareImageUrls.size()> 0) {
            mShareManager.setShareUrl(mShareImageUrls.get(0));
        }
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

        public static BaseFragment[] getFragments(){
            BaseFragment fragments[] = new BaseFragment[0];
            return fragments;
        }

        public static BaseFragment[] getFragments(List<String> urls){
            int count = urls.size();
            BaseFragment fragments[] = new BaseFragment[count];
            try {
                for(int i = 0; i < count ; i++) {
                    fragments[i] = new  ShareImageFragment();
                    Bundle args = new Bundle();
                    args.putString("url", urls.get(i));
                    fragments[i].setArguments(args);
                    ((ShareImageFragment)fragments[i]).setUrl(urls.get(i));
                }
            }catch (Exception  ex) {
                LogHelper.e("DataGenerator", ex);
            }
            return fragments;
        }

    }


    private class GetShareImagesTask extends AsyncTask<Void, Void, GetShareImagesResponse> {

        @Override
        protected GetShareImagesResponse doInBackground(Void... voids) {
            GetShareImagesRequest request = new GetShareImagesRequest();
            return new BasicService().sendRequest(request);
        }

        @Override
        protected void onPostExecute(final GetShareImagesResponse response) {
            super.onPostExecute(response);

            if (!response.isSuccess()) {
                LogHelper.e(TAG, response.getErrorMessage());
                return;
            }

            mShareImageUrls = response.getShareImageUrls();
            loadShareImages();
        }
    }


}
