package com.jinjunhang.onlineclass.ui.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.db.LoginUserDao;
import com.jinjunhang.onlineclass.model.LoginUser;
import com.jinjunhang.onlineclass.service.CheckUpgradeRequest;
import com.jinjunhang.onlineclass.service.CheckUpgradeResponse;
import com.jinjunhang.onlineclass.ui.fragment.MainPageFragment;
import com.jinjunhang.onlineclass.ui.fragment.ShopWebBrowserFragment;
import com.jinjunhang.onlineclass.ui.fragment.user.MeFragment;
import com.jinjunhang.onlineclass.ui.fragment.SettingsFragment;
import com.jinjunhang.player.utils.LogHelper;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

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


public class MainActivity extends BaseMusicActivity  {

    public static final int REQUEST_ME_UPDATE_USER_IAMGE = 1;
    public static final int REQUEST_ME_UPDATE_PERSONAL_INFO = 2;

    private static final String TAG = LogHelper.makeLogTag(MainActivity.class);

    private BottomBar mBottomBar;
    private Map<Class, Fragment> fragmentMap;

    public final static String EXTRA_TAB = "selecttab";

    private LoginUserDao mLoginUserDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoginUserDao = LoginUserDao.getInstance(this);

        LoginUser loginUser = mLoginUserDao.get();
        LogHelper.d(TAG, "loginuser = " + loginUser);

        setContentView(R.layout.activity_main);

        setCommonActionBar();

        fragmentMap = new HashMap();
        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.setMaxFixedTabs(5);
        mBottomBar.setItemsFromMenu(R.menu.bottombar, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                String title = "";
                FragmentManager fm = getSupportFragmentManager();
                Fragment fragment = null;
                switch (menuItemId) {
                    case R.id.bottomBarHome:
                        title = "巨方助手";
                        fragment = getFragment(MainPageFragment.class);
                        setCommonActionBar();
                        break;
                    case R.id.bottomBarSearch:
                        title = "申请";
                        fragment = getFragment(ShopWebBrowserFragment.class);
                        setCommonActionBar();
                        break;
                    case R.id.bottomBarMe:
                        title = "";
                        fragment = getFragment(MeFragment.class);
                        setCommonActionBar();
                        break;
                    case R.id.bottomBarSetting:
                        title = "设置";
                        setCommonActionBar();
                        fragment = getFragment(SettingsFragment.class);
                        break;
                }

                if (fragment != null) {
                    android.support.v4.app.FragmentTransaction transaction = fm.beginTransaction();
                    transaction.replace(R.id.fragmentContainer, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }

                if (menuItemId != R.id.bottomBarSearch)
                    ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.actionbar_text)).setText(title);
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {

            }
        });

        mBottomBar.noTopOffset();
        mBottomBar.hideShadow();

        mBottomBar.post(new Runnable() {
            @Override
            public void run() {
                int height = mBottomBar.getBar().getHeight();
                Utils.BOTTOM_BAR_HEIGHT = height;
                LogHelper.d(TAG, "bottom bar height = " + Utils.BOTTOM_BAR_HEIGHT+", " + height);
            }
        });

        int selectTab = getIntent().getIntExtra(EXTRA_TAB, 0);
        LogHelper.d(TAG, "selectTab = " + selectTab);
        mBottomBar.selectTabAtPosition(selectTab, true);

        mPlayerController.attachToView(mBottomBar);

        new CheckUpgradeTask().execute();


    }

    private void setCommonActionBar() {
        getSupportActionBar().show();
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        final View customView = getLayoutInflater().inflate(R.layout.actionbar, null);
        getSupportActionBar().setCustomView(customView);
        Toolbar parent =(Toolbar) customView.getParent();

        customView.post(new Runnable(){
            public void run(){
                int height = customView.findViewById(R.id.action_bar).getHeight();
                int heightInPixel = Utils.dip2px(MainActivity.this, height);
                LogHelper.d(TAG, "action bar = " + height);
            }
        });
        parent.setContentInsetsAbsolute(0, 0);

    }

    /*
    private void setSearchActionBar() {
        getSupportActionBar().show();
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View customView = getLayoutInflater().inflate(R.layout.actionbar_search, null);
        getSupportActionBar().setCustomView(customView);
        Toolbar parent =(Toolbar) customView.getParent();
        parent.setContentInsetsAbsolute(0, 0);
    }*/


    private <T extends Fragment> Fragment getFragment(Class<T> fragmentClass) {
        Fragment fragment = fragmentMap.get(fragmentClass);
        if (fragment == null) {
            try {
                fragment = fragmentClass.newInstance();
            } catch (Exception ex) {

            }
            fragmentMap.put(fragmentClass, fragment);
        }
        return fragment;
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPlayerController.attachToView(mBottomBar);
        mPlayerController.updateView();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPlayerController.updateView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ME_UPDATE_USER_IAMGE) {
            getFragment(MeFragment.class).onActivityResult(requestCode, resultCode, data);
        } else if (requestCode == REQUEST_ME_UPDATE_PERSONAL_INFO) {
            getFragment(MeFragment.class).onActivityResult(requestCode, resultCode, data);
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

                showForceUpgradeMessage(MainActivity.this, title, !isForceUpgrade,  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LogHelper.d(TAG, "click which = " + which);
                        if (which == 0) {
                            LogHelper.d(TAG, "cancel clicked");
                        } else {
                            chooseUpgrade = true;
                            LogHelper.d(TAG, "upgrade clicked");
                            progress = new ProgressDialog(MainActivity.this);
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

                writeToFile(response.body().byteStream(), MainActivity.this);

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
