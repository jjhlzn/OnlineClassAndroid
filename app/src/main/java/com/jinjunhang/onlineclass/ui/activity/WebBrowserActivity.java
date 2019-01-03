package com.jinjunhang.onlineclass.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;
import com.jinjunhang.framework.controller.BaseActivity;
import com.jinjunhang.framework.lib.LoadingAnimation;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.wx.Util;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.db.LoginUserDao;
import com.jinjunhang.onlineclass.ui.activity.user.LoginActivity;
import com.jinjunhang.onlineclass.ui.lib.BrowserUtils;
import com.jinjunhang.onlineclass.ui.lib.ParseHtmlPageTask;
import com.jinjunhang.onlineclass.ui.lib.ShareManager;
import com.jinjunhang.framework.lib.LogHelper;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by lzn on 16/6/20.
 */
public class WebBrowserActivity extends BaseActivity {
    private final static String TAG = LogHelper.makeLogTag(WebBrowserActivity.class);

    public final static String EXTRA_URL = "EXTRA_URL";
    public final static String EXTRA_TITLE = "EXTRA_TITLE";
    public final static String EXTRA_BACK_TO_MAIN_ACTIVITY = "EXTRA_BACK_TO_MAIN_ACTIVITY";

    private LoginUserDao mLoginUserDao = LoginUserDao.getInstance(this);

    private String mUrl;
    private String mTitle;
    private boolean mIsBackToMainActivity = false;
    private WebView mWebView;
    private TextView mCloseButton;
    private IWXAPI mWXAPI;
    private Toolbar mToolbar;

    private LoadingAnimation mLoadingAnimation;

    private ShareManager mShareManager;

    @Override
    protected void setContentView() {
       // supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_web_browser);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWXAPI = WXAPIFactory.createWXAPI(this, null);
        mWXAPI.registerApp("wx73653b5260b24787");

        mLoadingAnimation = new LoadingAnimation(this);
        //getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        mToolbar = findViewById(R.id.toolbar);

        mUrl = getIntent().getStringExtra(EXTRA_URL);
        mUrl = Util.addUserInfo(mUrl);
        mUrl = Util.addDeviceInfo(mUrl);
        LogHelper.d(TAG, mUrl);
        mTitle = getIntent().getStringExtra(EXTRA_TITLE);
        LogHelper.d(TAG, "title = " + mTitle);
        mIsBackToMainActivity = getIntent().getBooleanExtra(EXTRA_BACK_TO_MAIN_ACTIVITY, false);

        setToolBar();
        mWebView = (WebView) findViewById(R.id.webview);

        registerForContextMenu(mWebView);

        mWebView.getSettings().setJavaScriptEnabled(true); // enable javascript
        mWebView.setWebViewClient(new MyWebViewClient());

        WebSettings settings = mWebView.getSettings();
        settings.setDomStorageEnabled(true);
        openURL();

        mShareManager = new ShareManager(this, this.findViewById(R.id.rootView));
        mShareManager.setUseQrCodeImage(false);

        new ParseHtmlPageTask().setShareManager(mShareManager).execute(mUrl);

        ImmersionBar.setTitleBar(this, mToolbar);
        ImmersionBar.with(this).statusBarColor(R.color.white).statusBarDarkFont(true).init();
    }

    protected void setToolBar() {
        //设置返回按键
        ((TextView)mToolbar.findViewById(R.id.actionbar_text)).setText(mTitle);
        ImageButton backButton = mToolbar.findViewById(R.id.actionbar_back_button);
        mCloseButton = mToolbar.findViewById(R.id.actionbar_close_button);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogHelper.d(TAG, "mWebView.canGoBack() = " + mWebView.canGoBack());
                if (mWebView.canGoBack()) {

                    mWebView.goBack();

                } else {
                    onBackPressed();
                }
            }
        });

        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (this.mIsBackToMainActivity) {
            checkLogin();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Utils.updateNavigationBarButton(this);
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

    private void  setWeixinShareIfNeed(View v) {
       if ("提额秘诀".equals(mTitle)) {
           mShareManager = new ShareManager(this, v);
       }
    }

    /** Opens the URL in a browser */
    private void openURL() {
        mWebView.loadUrl(mUrl);
        mWebView.requestFocus();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLoadingAnimation.hide();
    }


    private byte[] downloadImage(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            InputStream is = (InputStream) url.getContent();
            byte[] buffer = new byte[8192];
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            while ((bytesRead = is.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
            return output.toByteArray();
        } catch (MalformedURLException e) {
            LogHelper.e(TAG, e);
            return null;
        } catch (IOException e) {
            LogHelper.e(TAG, e);
            return null;
        }
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            String imageUrl = params[0];
            byte[] imageBytes = downloadImage(imageUrl);
            if (imageBytes != null) {
                Bitmap bm = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                MediaStore.Images.Media.insertImage(getContentResolver(), bm, "" , "");


                WebBrowserActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WebBrowserActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        final WebView.HitTestResult result = mWebView.getHitTestResult();
        MenuItem.OnMenuItemClickListener handler = new MenuItem.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                // handle on context menu click
                LogHelper.d(TAG, "save image click");
                String imageUrl = result.getExtra();
                new DownloadImageTask().execute(imageUrl);
                return true;
            }
        };

        if (result.getType() == WebView.HitTestResult.IMAGE_TYPE ||
                result.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {


            menu.setHeaderTitle("");
            menu.add(0, 666, 0, "保存图片").setOnMenuItemClickListener(handler);
        }
    }



    private class MyWebViewClient extends WebViewClient {


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            LogHelper.d(TAG, "url = " + url);
            if (url.contains("/app/jfzs")) {
                view.getContext().startActivity(
                        new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                return true;
            }
            //支持微信支付
            if (Utils.handleWechatPay(mWXAPI, url, mLoadingAnimation)) {
                return true;
            }

            if (BrowserUtils.handleAlipayProtocol(url, WebBrowserActivity.this)) {
                return true;
            }

            if (BrowserUtils.handlePhoneCallProtocol(url, WebBrowserActivity.this)) {
                return true;
            }

            new ParseHtmlPageTask().setShareManager(mShareManager).execute(url);

            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            LogHelper.d(TAG, "onPageFinished: ", url);
            LogHelper.d("mWebView.canGoBack(): " + mWebView.canGoBack());
            super.onPageFinished(view, url);
            //这里是重新执行一遍是为了goback的时候，只有这个位置是可以的（对于没个链接，new ParseHtmlPageTask().execute()都被执行了两遍）
            new ParseHtmlPageTask().setShareManager(mShareManager).execute(url);
            if (mWebView.canGoBack()) {
                mCloseButton.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            LogHelper.d("onPageStarted: " + url);
            LogHelper.d("mWebView.canGoBack(): " + mWebView.canGoBack());
            if (mWebView.canGoBack()) {
                mCloseButton.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error){
            handler.proceed();
        }
    }



}
