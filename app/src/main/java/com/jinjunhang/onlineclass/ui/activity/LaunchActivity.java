package com.jinjunhang.onlineclass.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.db.KeyValueDao;
import com.jinjunhang.onlineclass.db.LoginUserDao;
import com.jinjunhang.onlineclass.model.LoginUser;
import com.jinjunhang.onlineclass.service.GetLaunchAdvRequest;
import com.jinjunhang.onlineclass.service.GetLaunchAdvResponse;
import com.jinjunhang.onlineclass.service.GetServiceLocatorRequest;
import com.jinjunhang.onlineclass.service.GetServiceLocatorResponse;
import com.jinjunhang.onlineclass.service.ServiceConfiguration;
import com.jinjunhang.onlineclass.ui.activity.mainpage.BottomTabLayoutActivity;
import com.jinjunhang.onlineclass.ui.activity.user.LoginActivity;
import com.jinjunhang.framework.lib.LogHelper;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by lzn on 16/6/19.
 */
public class LaunchActivity extends Activity {
    private static final String TAG = LogHelper.makeLogTag(LaunchActivity.class);

    private LoginUserDao mLoginUserDao;

    private static final String APP_ID = "wx73653b5260b24787";
    private IWXAPI api;

    //BottomBar  mBottomBar;
    private KeyValueDao mKeyValueDao;

    private boolean checkingLogin = false;

    private final Handler mHandler = new Handler();
    private static final long UPDATE_MESSAGE_INTERNAL = 1000;
    private static final long PROGRESS_UPDATE_INITIAL_INTERVAL = 100;
    private int mSeconds = 2;
    private ScheduledFuture<?> mScheduleFuture;
    private final ScheduledExecutorService mExecutorService = Executors.newSingleThreadScheduledExecutor();



    private final Runnable mUpdateProgressTask = new Runnable() {
        @Override
        public void run() {
            updateMessage();
        }
    };
    private Handler goToNextActivityHandler = new Handler();
    private final Runnable goToNextActivityTask = new Runnable() {
        @Override
        public void run() {
            LogHelper.d(TAG, "after 5s delayed, check login");
            checkLogin();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.getScreenHeight(this);
        mKeyValueDao = KeyValueDao.getInstance(this);
        //int height =  Integer.parseInt(mKeyValueDao.getValue(KeyValueDao.BOTTOM_BAR_HEIGHT, -1 + ""));

        setContentView(R.layout.activity_fragment_launch_adv);


        TextView skipAdvButton =  (TextView) findViewById(R.id.skipAdvButton);
        skipAdvButton.setVisibility(View.INVISIBLE);
        TextView advTipText = (TextView) findViewById(R.id.advTipText);
        advTipText.setVisibility(View.INVISIBLE);

        int apiLevel = 0;
        try {
            apiLevel = Integer.parseInt(Build.VERSION.RELEASE ) ;
        }
        catch (Exception ex) {
            LogHelper.d(TAG, ex);
        }
        if (apiLevel >= 21) {
            api = WXAPIFactory.createWXAPI(this, APP_ID, true);
            api.registerApp(APP_ID);
        }

        mLoginUserDao = LoginUserDao.getInstance(this);

        registerXinGeAndGoToNextActivity();

        //NBSAppAgent.setLicenseKey("a200c16a118f4f99891ab5645fa2a13d").withLocationServiceEnabled(true).start(this.getApplicationContext());
        goToNextActivityHandler.postDelayed(goToNextActivityTask, 3000);
    }

    //注册信鸽，并进去下个页面
    private void registerXinGeAndGoToNextActivity() {
        XGPushManager.registerPush(this, new XGIOperateCallback() {
            @Override
            public void onSuccess(Object o, int i) {
                Log.d(TAG, "register device succes, devicetoken = " + o.toString());
                new GetAdvTask().execute();
            }

            @Override
            public void onFail(Object o, int i, String s) {
                Log.d(TAG, "register device fail");
                new GetAdvTask().execute();
            }
        });
    }

    private void checkLogin() {
        LogHelper.d(TAG, "check login");
        if (this.checkingLogin)
            return;

        this.checkingLogin = true;

        Intent i;
        LoginUser user = mLoginUserDao.get();
        LogHelper.d(TAG, "loginUser = " + user);


        if (user == null) {
            i = new Intent(this, LoginActivity.class);
        } else {
            i = new Intent(this, BottomTabLayoutActivity.class);
        }
        startActivity(i);

    }

    private void scheduleSkipAdv() {
        stopScheduleSkipAdv();
        if (!mExecutorService.isShutdown()) {
            mScheduleFuture = mExecutorService.scheduleAtFixedRate(
                    new Runnable() {
                        @Override
                        public void run() {
                            mHandler.post(mUpdateProgressTask);
                        }
                    }, PROGRESS_UPDATE_INITIAL_INTERVAL,
                    UPDATE_MESSAGE_INTERNAL, TimeUnit.MILLISECONDS);
        }
    }

    private void stopScheduleSkipAdv() {
        if (mScheduleFuture != null) {
            mScheduleFuture.cancel(false);
        }
    }

    private void updateMessage() {
        mSeconds--;

        if (mSeconds <= 0) {
            mSeconds = 0;
        }

        TextView skipButton = (TextView)findViewById(R.id.skipAdvButton);
        skipButton.setText("跳过广告  " + mSeconds);

        if (mSeconds == 0) {
            checkLogin();
        }
    }

    private class GetAdvTask extends AsyncTask<Void, Void, GetLaunchAdvResponse> {
        @Override
        protected GetLaunchAdvResponse doInBackground(Void... voids) {
            return new BasicService().sendRequest(new GetLaunchAdvRequest());
        }

        @Override
        protected void onPostExecute(GetLaunchAdvResponse resp) {
            if (resp.isSuccess()) {
                LogHelper.d(TAG, "imageUrl: " + resp.getImageUrl());
                if (!"".equals(resp.getImageUrl())) { //是否有图片
                    new GetImageTask().execute(resp.getImageUrl(), resp.getAdvTitle(), resp.getAdvUrl());
                } else {

                }
            } else {

            }
        }
    }

    private class GetImageTask extends AsyncTask<String, Void, Bitmap> {

        private String mAdvTitle = "";
        private String mAdvUrl = "";

        @Override
        protected Bitmap doInBackground(String...params) {
            String url = (String) params[0];
            mAdvTitle = params[1];
            mAdvUrl = params[2];
            Bitmap myBitmap = null;
            try {

                myBitmap = Glide.with(LaunchActivity.this)
                        .load(url)
                        .asBitmap()
                        .centerCrop()
                        .into(320, 480)
                        .get();

            } catch (Exception ex) {
                LogHelper.d(TAG, ex);
                return null;
            }
            return myBitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                ImageView imageView = (ImageView)findViewById(R.id.advImage);
                imageView.setImageBitmap(bitmap);

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!"".equals(mAdvUrl)) {
                            goToNextActivityHandler.removeCallbacks(goToNextActivityTask);
                            stopScheduleSkipAdv();
                            Intent i = new Intent(LaunchActivity.this, WebBrowserActivity.class)
                                    .putExtra(WebBrowserActivity.EXTRA_TITLE, mAdvTitle)
                                    .putExtra(WebBrowserActivity.EXTRA_URL, mAdvUrl)
                                    .putExtra(WebBrowserActivity.EXTRA_BACK_TO_MAIN_ACTIVITY, true);
                            LaunchActivity.this.startActivity(i);
                        }
                    }
                });

                TextView skipButton = (TextView)findViewById(R.id.skipAdvButton);
                skipButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        checkLogin();
                    }
                });

                TextView skipAdvButton =  (TextView) findViewById(R.id.skipAdvButton);
                skipAdvButton.setVisibility(View.VISIBLE);
                TextView advTipText = (TextView) findViewById(R.id.advTipText);
                advTipText.setVisibility(View.VISIBLE);

                scheduleSkipAdv();
            }
        }
    }
}
