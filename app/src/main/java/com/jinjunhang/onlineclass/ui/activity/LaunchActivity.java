package com.jinjunhang.onlineclass.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.util.Log;
import android.view.View;

import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.db.KeyValueDao;
import com.jinjunhang.onlineclass.db.LoginUserDao;
import com.jinjunhang.onlineclass.service.GetServiceLocatorRequest;
import com.jinjunhang.onlineclass.service.GetServiceLocatorResponse;
import com.jinjunhang.onlineclass.service.ServiceConfiguration;
import com.jinjunhang.onlineclass.ui.activity.user.LoginActivity;
import com.jinjunhang.player.utils.LogHelper;
import com.networkbench.agent.impl.NBSAppAgent;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by lzn on 16/6/19.
 */
public class LaunchActivity extends Activity {
    private static final String TAG = LogHelper.makeLogTag(LaunchActivity.class);

    private LoginUserDao mLoginUserDao;

    private static final String APP_ID = "wx73653b5260b24787";
    private IWXAPI api;

    BottomBar  mBottomBar;
    private KeyValueDao mKeyValueDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.getScreenHeight(this);
        setContentView(R.layout.activity_fragment_launch);

        mKeyValueDao = KeyValueDao.getInstance(this);
        //mKeyValueDao.deleteAll();
        //mKeyValueDao.getAll();

        int height =  Integer.parseInt(mKeyValueDao.getValue(KeyValueDao.BOTTOM_BAR_HEIGHT, -1 + ""));
        LogHelper.d(TAG, "height = " + height);
        if (height == -1) {
            LogHelper.d(TAG, "compute bottom bar height");
            //下面这段代码是为了获取BottomBar的高度
            mBottomBar = BottomBar.attach(this, savedInstanceState);
            mBottomBar.setMaxFixedTabs(5);
            mBottomBar.setItemsFromMenu(R.menu.bottombar, new OnMenuTabClickListener() {
                @Override
                public void onMenuTabSelected(@IdRes int menuItemId) {
                }
                @Override
                public void onMenuTabReSelected(@IdRes int menuItemId) {
                }
            });
            mBottomBar.noTopOffset();
            mBottomBar.hideShadow();
            mBottomBar.setVisibility(View.INVISIBLE);
            mBottomBar.post(new Runnable() {
                @Override
                public void run() {
                    int height = mBottomBar.getBar().getHeight();
                    Utils.BOTTOM_BAR_HEIGHT = height;
                    LogHelper.d(TAG, "bottom bar height = " + Utils.BOTTOM_BAR_HEIGHT + ", " + height);
                    mKeyValueDao.saveOrUpdate(KeyValueDao.BOTTOM_BAR_HEIGHT, height + "");
                }
            });
        } else {
            Utils.BOTTOM_BAR_HEIGHT = height;
            LogHelper.d(TAG, "bottom bar height = " + Utils.BOTTOM_BAR_HEIGHT + ", " + height);
        }

        api = WXAPIFactory.createWXAPI(this, APP_ID, true);
        api.registerApp(APP_ID);

        mLoginUserDao = LoginUserDao.getInstance(this);

        if (mKeyValueDao.getValue(KeyValueDao.IS_GET_SERVICE_LOCATOR, "1").equals("1")) {
            new GetServiceLocatorTask().execute();
        } else {
            registerXinGeAndGoToNextActivity();
        }

        LogHelper.e(TAG, "CreateView finish");

        NBSAppAgent.setLicenseKey("a200c16a118f4f99891ab5645fa2a13d").withLocationServiceEnabled(true).start(this.getApplicationContext());
    }

    //注册信鸽，并进去下个页面
    private void registerXinGeAndGoToNextActivity() {
        XGPushManager.registerPush(this, new XGIOperateCallback() {
            @Override
            public void onSuccess(Object o, int i) {
                Log.d(TAG, "register device succes, devicetoken = " + o.toString());
                checkLogin();
            }

            @Override
            public void onFail(Object o, int i, String s) {
                Log.d(TAG, "register device fail");
                checkLogin();
            }
        });
    }

    private void checkLogin() {


        Intent i;
        if (mLoginUserDao.get() == null) {
            i = new Intent(this, LoginActivity.class);
        } else {
            i = new Intent(this, MainActivity.class);
        }
        startActivity(i);
    }



    private class GetServiceLocatorTask extends AsyncTask<Void, Void, GetServiceLocatorResponse> {

        @Override
        protected GetServiceLocatorResponse doInBackground(Void... params) {
            return new BasicService().sendRequest(new GetServiceLocatorRequest());
        }

        @Override
        protected void onPostExecute(GetServiceLocatorResponse resp) {
            if (resp.isSuccess()) {
                mKeyValueDao.saveOrUpdate(KeyValueDao.SERVER_HTTP, resp.getHttp());
                mKeyValueDao.saveOrUpdate(KeyValueDao.SERVER_HOST, resp.getServerName());
                mKeyValueDao.saveOrUpdate(KeyValueDao.SERVER_PORT, resp.getPort()+"");
                ServiceConfiguration.LOCATOR_HTTP = resp.getHttp();
                ServiceConfiguration.LOCATOR_SERVERNAME = resp.getServerName();
                try {
                    ServiceConfiguration.LOCATOR_PORT = Integer.parseInt( resp.getPort() );
                } catch (Exception ex){
                    LogHelper.e(TAG, ex);
                }
            }

            registerXinGeAndGoToNextActivity();
        }
    }
}
