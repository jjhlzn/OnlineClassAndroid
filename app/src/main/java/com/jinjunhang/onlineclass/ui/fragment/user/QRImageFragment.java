package com.jinjunhang.onlineclass.ui.fragment.user;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.gyf.barlibrary.ImmersionBar;
import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.framework.wx.Util;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.service.GetShareImagesRequest;
import com.jinjunhang.onlineclass.service.GetShareImagesResponse;
import com.jinjunhang.onlineclass.ui.fragment.BaseFragment;
import com.jinjunhang.onlineclass.ui.lib.ShareManager;
import com.jinjunhang.onlineclass.ui.lib.ShareManager2;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jjh on 2016-7-1.
 */
public class QRImageFragment extends BaseFragment {

    private static final String TAG = LogHelper.makeLogTag(QRImageFragment.class);

    private ShareManager mShareManager;
    private BaseFragment[] mFragmensts;
    private MyPagerAdapter mMyPagerAdapter;
    private List<String> mShareImageUrls = new ArrayList<>();

    @BindView(R.id.viewpager)  ViewPager mViewPager;
    @BindView(R.id.overlay_bg) ViewGroup mOverlayBg;
    @BindView(R.id.share_btn) Button mShareBtn;
    @BindView(R.id.share_view) ViewGroup mShareView;
    @BindView(R.id.toolbar) android.support.v7.widget.Toolbar mToolbar;


    @OnClick(R.id.share_btn)
    void shareClick() {
        mShareBtn.setVisibility(View.INVISIBLE);
        mOverlayBg.setVisibility(View.VISIBLE);
        mViewPager.setEnabled(false);
        mShareView.setVisibility(View.VISIBLE);
    }
    @OnClick(R.id.overlay_bg)
    void cancelShareClick() {
        mShareBtn.setVisibility(View.VISIBLE);
        mOverlayBg.setVisibility(View.INVISIBLE);
        mViewPager.setEnabled(true);
        mShareView.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.sharez_menu)
    void shareMenuClick() {
    }

    @OnClick(R.id.actionbar_back_button)
    void backClick() {
        getActivity().finish();
    }

    @BindView(R.id.actionbar_text) TextView mTitleView;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_fragment_qrimage;
    }

    @Override
    protected boolean isCompatibleActionBar() {
        return false;
    }

    @Override
    protected boolean isNeedTopMargin() {
        return false;
    }

    public static final int PageInterWidth = 150;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, mView);

        mFragmensts = DataGenerator.getFragments();
        mMyPagerAdapter = new MyPagerAdapter(getActivity().getSupportFragmentManager());
        mViewPager.setAdapter(mMyPagerAdapter);


        //mViewPager.setBackgroundColor( getActivity().getResources().getColor(R.color.red) );

        mViewPager.setClipToPadding(false);
        mViewPager.setPadding(PageInterWidth,0,PageInterWidth,0);
        mViewPager.setPageMargin(80);
        mTitleView.setText("分享");

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position < mShareImageUrls.size()) {
                    mShareManager.setShareUrl( mShareImageUrls.get(position));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        new GetShareImagesTask().execute();

        mShareManager = new ShareManager2((AppCompatActivity) getActivity(), mView);
        ImmersionBar.setTitleBar(getActivity(), mToolbar);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
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
