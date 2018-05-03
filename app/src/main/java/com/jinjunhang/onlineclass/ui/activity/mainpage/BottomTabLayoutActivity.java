package com.jinjunhang.onlineclass.ui.activity.mainpage;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.ServiceLinkManager;
import com.jinjunhang.onlineclass.service.CheckUpgradeRequest;
import com.jinjunhang.onlineclass.service.CheckUpgradeResponse;
import com.jinjunhang.onlineclass.ui.activity.MainActivity;
import com.jinjunhang.onlineclass.ui.fragment.BaseFragment;
import com.jinjunhang.onlineclass.ui.fragment.CourseListFragment;
import com.jinjunhang.onlineclass.ui.fragment.mainpage.MainPageFragment;
import com.jinjunhang.onlineclass.ui.fragment.SettingsFragment;
import com.jinjunhang.onlineclass.ui.fragment.ShopWebBrowserFragment;
import com.jinjunhang.onlineclass.ui.fragment.user.MeFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by jinjunhang on 2018/3/29.
 */

public class BottomTabLayoutActivity extends AppCompatActivity {
    private static String TAG = LogHelper.makeLogTag(BottomTabLayoutActivity.class);

    private ViewPager mViewPager;

    private TabLayout mTabLayout;
    private BaseFragment[] mFragmensts;

    private MyPagerAdapter mMyPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tablayout);
        mFragmensts = DataGenerator.getFragments(this, "TabLayout Tab");
        setActionBar();
        initView();
        mMyPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager)findViewById(R.id.viewpager);
        mViewPager.setAdapter(mMyPagerAdapter);
        mViewPager.setCurrentItem(0);


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabLayout.getTabAt(position).select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mViewPager.setOffscreenPageLimit(4);
        setActionBar();
        new CheckUpgradeTask().execute();
    }

    public void setActionBar() {
        AppCompatActivity activity = this;
        LogHelper.d(TAG, "activity = " + activity);
        activity.getSupportActionBar().show();
        activity.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        final View customView = activity.getLayoutInflater().inflate(R.layout.actionbar_main, null);
        activity.getSupportActionBar().setCustomView(customView);
        Toolbar parent =(Toolbar) customView.getParent();

        parent.setContentInsetsAbsolute(0, 0);

        setLightStatusBar(customView, activity);
    }

    protected void setLightStatusBar(View view, Activity activity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            activity.getWindow().setStatusBarColor(Color.WHITE);
        }
    }

    private void initView() {
        mTabLayout = (TabLayout) findViewById(R.id.bottom_tab_layout);
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                onTabItemSelected(tab.getPosition());
                // Tab 选中之后，改变各个Tab的状态
                for (int i=0;i<mTabLayout.getTabCount();i++){
                    View view = mTabLayout.getTabAt(i).getCustomView();
                    ImageView icon = (ImageView) view.findViewById(R.id.tab_content_image);
                    TextView text = (TextView) view.findViewById(R.id.tab_content_text);
                    if(i == tab.getPosition()){ // 选中状态
                        icon.setImageResource(DataGenerator.mTabResPressed[i]);
                        text.setTextColor(getResources().getColor(R.color.tab_selected_color));
                    }else{// 未选中状态
                        icon.setImageResource(DataGenerator.mTabRes[i]);
                        text.setTextColor(getResources().getColor(android.R.color.darker_gray));
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        // 提供自定义的布局添加Tab
        for(int i=0;i<5;i++){
            mTabLayout.addTab(mTabLayout.newTab().setCustomView(DataGenerator.getTabView(this,i)));
        }

    }

    private void onTabItemSelected(int position){

        LogHelper.d(TAG, "tab selected position = " + position);
        if (mViewPager != null) {
            mViewPager.setCurrentItem(position);
        }

        mFragmensts[position].changeActionBar();
    }

    public class MyPagerAdapter extends FragmentPagerAdapter{

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

        public static final int []mTabRes = new int[]{R.drawable.icon_home,R.drawable.icon_study, R.drawable.icon_live, R.drawable.icon_shop,R.drawable.icon_my,};
        public static final int []mTabResPressed = new int[]{R.drawable.icon_home_s, R.drawable.icon_study_s, R.drawable.icon_live_s, R.drawable.icon_shop_s,R.drawable.icon_my_s,};
        public static final String []mTabTitle = new String[]{"首页", "学习", "直播", "商城","我的"};

        public static BaseFragment[] getFragments(Activity activity, String from){
            BaseFragment fragments[] = new BaseFragment[5];
            try {

                fragments[0] = MainPageFragment.class.newInstance();
                ShopWebBrowserFragment frag1 = new ShopWebBrowserFragment();
                Bundle args = new Bundle();
                args.putString("title", mTabTitle[1]);
                args.putString("url", ServiceLinkManager.StudyUrl());
                frag1.setArguments(args);
                fragments[1] = frag1;
                fragments[2] = CourseListFragment.class.newInstance();
                ShopWebBrowserFragment frag3 = new ShopWebBrowserFragment();
                Bundle args3 = new Bundle();
                args3.putString("title", mTabTitle[3]);
                args3.putString("url", ServiceLinkManager.MallUrl());
                frag3.setArguments(args3);
                fragments[3] = frag3;
                fragments[4] = MeFragment.class.newInstance();
            }catch (Exception  ex) {
                LogHelper.e("DataGenerator", ex);
            }
            return fragments;
        }

        /**
         * 获取Tab 显示的内容
         * @param context
         * @param position
         * @return
         */

        public static View getTabView(Context context, int position){
            View view = LayoutInflater.from(context).inflate(R.layout.tabitem,null);
            ImageView tabIcon = (ImageView) view.findViewById(R.id.tab_content_image);
            tabIcon.setImageResource(DataGenerator.mTabRes[position]);
            TextView tabText = (TextView) view.findViewById(R.id.tab_content_text);
            tabText.setText(mTabTitle[position]);
            return view;
        }
    }

    private long lastPressBack = 0;

    @Override
    public void onBackPressed() {

        long now = Calendar.getInstance().getTime().getTime();
        long delta = now - lastPressBack;
        lastPressBack = now;
        if (delta < 3000) {
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        } else {
            Toast.makeText(this, "再按一次退回键退出", Toast.LENGTH_SHORT).show();
        }


    }


    boolean chooseUpgrade = false;
    private ProgressDialog progress;

    private class CheckUpgradeTask extends AsyncTask<Void, Void, CheckUpgradeResponse> {
        @Override
        protected CheckUpgradeResponse doInBackground(Void... params) {
            CheckUpgradeRequest request = new CheckUpgradeRequest();
            return new BasicService().sendRequest(request);
        }


        @Override
        protected void onPostExecute(final CheckUpgradeResponse resp) {
            super.onPostExecute(resp);

            if (!resp.isSuccess()) {
                return;
            }

            if (resp.isNeedUpgrade()) {

                final boolean isForceUpgrade = "force".equals(resp.getUpgradeType());
                LogHelper.d(TAG, "need upgrade, and isForceUpgrade = " + isForceUpgrade);
                String title = "请升级新版本";
                if (!isForceUpgrade) {
                    title = "有新版本，去升级吗？";
                } else {
                    chooseUpgrade = true;
                }

                showForceUpgradeMessage(BottomTabLayoutActivity.this, title, !isForceUpgrade,  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LogHelper.d(TAG, "click which = " + which);
                        if (which == 0) {
                            LogHelper.d(TAG, "cancel clicked");
                        } else {
                            chooseUpgrade = true;
                            LogHelper.d(TAG, "upgrade clicked");
                            progress = new ProgressDialog(BottomTabLayoutActivity.this);
                            // Set your ProgressBar Title
                            progress.setTitle("新版本");
                            // Set your ProgressBar Message
                            progress.setMessage("下载新版本App, 请稍等!");
                            progress.setIndeterminate(false);
                            progress.setMax(100);
                            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                            // Show ProgressBar
                            progress.setCancelable(false);
                            //  mProgressDialog.setCanceledOnTouchOutside(false);
                            progress.show();
                            new DownloadTask().execute(resp.getUpgradeFileUrl());


                        }
                    }
                });
            }
        }
    }

    private void showForceUpgradeMessage(Context context, String message, boolean hasCancelButton, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(context);
        dlgAlert.setMessage(message);
        dlgAlert.setPositiveButton("去升级", listener);
        if (hasCancelButton)
            dlgAlert.setNegativeButton("取消", null);
        dlgAlert.setCancelable(false);

        dlgAlert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (!chooseUpgrade) {

                }
            }
        });
        dlgAlert.create().show();
    }


    private class DownloadTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            //显示下载的进度
            try {
                LogHelper.d(TAG, "url = " + params[0]);
                new Progress().run(params[0]);
            } catch (Exception ex) {
                LogHelper.e(TAG, ex);
            }

            return null;
        }
    }

    public final class Progress {

        public void run(String url) throws Exception {
            LogHelper.d(TAG, "Progress.run start");
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            final ProgressListener progressListener = new ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    LogHelper.e(TAG,  (100 * bytesRead) / contentLength + "% done");
                    //progress.setMessage("已下载"+(int)((100 * bytesRead) / contentLength)+"%");
                    progress.setProgress((int)((100 * bytesRead) / contentLength) );

                }
            };

            OkHttpClient client = new OkHttpClient.Builder()
                    .addNetworkInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Response originalResponse = chain.proceed(chain.request());
                            return originalResponse.newBuilder()
                                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                                    .build();
                        }
                    })
                    .build();

            try {
                LogHelper.d(TAG, "download will begin");
                Response response = client.newCall(request).execute();
                if (!response.isSuccessful())
                    throw new IOException("Unexpected code " + response);

                writeToFile(response.body().byteStream(), BottomTabLayoutActivity.this);

                LogHelper.d(TAG, "write file done.");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/download/" + "app.apk")), "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

            catch (Exception ex) {
                LogHelper.e(TAG, ex);
            }
        }

        private void writeToFile(InputStream data, Context context) {
            try {

                File outputFile = new File(Environment.getExternalStorageDirectory() + "/download/" + "app.apk");
                if(outputFile.exists()){
                    outputFile.delete();
                }
                FileOutputStream outputStreamWriter =new FileOutputStream(outputFile);
                byte[] buffer = new byte[1024];
                int len;
                while ((len = data.read(buffer)) != -1) {
                    outputStreamWriter.write(buffer, 0, len);
                }
                data.close();
                outputStreamWriter.close();
            }
            catch (IOException e) {
                LogHelper.e("Exception", "File write failed: " + e.toString());
            }
        }

    }


    private class ProgressResponseBody extends ResponseBody {

        private final ResponseBody responseBody;
        private final ProgressListener progressListener;
        private BufferedSource bufferedSource;

        public ProgressResponseBody(ResponseBody responseBody, ProgressListener progressListener) {
            this.responseBody = responseBody;
            this.progressListener = progressListener;
        }

        @Override public MediaType contentType() {
            return responseBody.contentType();
        }

        @Override public long contentLength() {
            return responseBody.contentLength();
        }

        @Override public BufferedSource source() {
            if (bufferedSource == null) {
                bufferedSource = Okio.buffer(source(responseBody.source()));
            }
            return bufferedSource;
        }

        private Source source(Source source) {
            return new ForwardingSource(source) {
                long totalBytesRead = 0L;

                @Override public long read(Buffer sink, long byteCount) throws IOException {
                    long bytesRead = super.read(sink, byteCount);
                    // read() returns the number of bytes read, or -1 if this source is exhausted.
                    totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                    progressListener.update(totalBytesRead, responseBody.contentLength(), bytesRead == -1);
                    return bytesRead;
                }
            };
        }
    }


    interface ProgressListener {
        void update(long bytesRead, long contentLength, boolean done);
    }

}
