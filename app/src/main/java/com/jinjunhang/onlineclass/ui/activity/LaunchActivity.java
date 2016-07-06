package com.jinjunhang.onlineclass.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.framework.wx.Util;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.db.LoginUserDao;
import com.jinjunhang.onlineclass.service.CheckUpgradeRequest;
import com.jinjunhang.onlineclass.service.CheckUpgradeResponse;
import com.jinjunhang.onlineclass.ui.activity.user.LoginActivity;
import com.jinjunhang.player.utils.LogHelper;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
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

    private boolean chooseUpgrade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fragment_launch);

        api = WXAPIFactory.createWXAPI(this, APP_ID, true);
        api.registerApp(APP_ID);

        mLoginUserDao = LoginUserDao.getInstance(this);


       new CheckUpgradeTask().execute();

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
                checkLogin();
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

                showForceUpgradeMessage(LaunchActivity.this, title, !isForceUpgrade,  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LogHelper.d(TAG, "click which = " + which);
                        if (which == 0) {
                            LogHelper.d(TAG, "cancel clicked");
                            checkLogin();
                        } else {
                            chooseUpgrade = true;
                            LogHelper.d(TAG, "upgrade clicked");
                            Intent i = new Intent(LaunchActivity.this, UpgradeActivity.class)
                                    .putExtra(UpgradeActivity.EXTRA_URL, resp.getUpgradeUrl())
                                    .putExtra(UpgradeActivity.EXTRA_TITLE, "升级")
                                    .putExtra(UpgradeActivity.EXTRA_ISFORCEUPGRADE, isForceUpgrade);
                            startActivity(i);

                            //prevent return back
                            if (isForceUpgrade) {
                                finish();
                            }
                        }
                    }
                });
            } else {
                checkLogin();
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
                    checkLogin();
                }
            }
        });
        dlgAlert.create().show();
    }

}
