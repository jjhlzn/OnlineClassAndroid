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
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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

import com.jinjunhang.framework.lib.LoadingAnimation;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.framework.wx.Util;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.db.LoginUserDao;
import com.jinjunhang.onlineclass.model.Comment;
import com.jinjunhang.onlineclass.model.LoginUser;
import com.jinjunhang.onlineclass.service.GetFooterAdvsRequest;
import com.jinjunhang.onlineclass.service.GetFooterAdvsResponse;
import com.jinjunhang.onlineclass.ui.activity.user.LoginActivity;
import com.jinjunhang.onlineclass.ui.cell.mainpage.FooterCell;
import com.jinjunhang.onlineclass.ui.lib.ParseHtmlPageTask;
import com.jinjunhang.onlineclass.ui.lib.ShareManager;
import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.onlineclass.ui.lib.ShareManager2;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by lzn on 16/6/20.
 */
public class WebBrowserActivity extends AppCompatActivity {

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

    private LoadingAnimation mLoadingAnimation;

    private ShareManager mShareManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWXAPI = WXAPIFactory.createWXAPI(this, null);
        mWXAPI.registerApp("wx73653b5260b24787");

        mLoadingAnimation = new LoadingAnimation(this);

        setContentView(R.layout.web_browser);
        mUrl = getIntent().getStringExtra(EXTRA_URL);
        mUrl = Util.addUserInfo(mUrl);
        mUrl = Util.addDeviceInfo(mUrl);
        LogHelper.d(TAG, mUrl);
        mTitle = getIntent().getStringExtra(EXTRA_TITLE);
        LogHelper.d(TAG, "title = " + mTitle);
        mIsBackToMainActivity = getIntent().getBooleanExtra(EXTRA_BACK_TO_MAIN_ACTIVITY, false);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setElevation(0);
        View customView = getLayoutInflater().inflate(R.layout.actionbar_browser, null);
        ((TextView)customView.findViewById(R.id.actionbar_text)).setText(mTitle);
        getSupportActionBar().setCustomView(customView);
        Toolbar parent =(Toolbar) customView.getParent();
        parent.setContentInsetsAbsolute(0, 0);
        //设置返回按键
        ImageButton backButton = (ImageButton) getSupportActionBar().getCustomView().findViewById(R.id.actionbar_back_button);
        mCloseButton = (TextView) getSupportActionBar().getCustomView().findViewById(R.id.actionbar_close_button);
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

        Utils.setLightStatusBar(customView, this);

        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setWeixinShareIfNeed(findViewById(R.id.rootView));
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
    }

    @Override
    public void onBackPressed() {
        if (this.mIsBackToMainActivity) {
            checkLogin();
        } else {
            super.onBackPressed();
        }
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

            new ParseHtmlPageTask().setShareManager(mShareManager).execute(url);

            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            LogHelper.d(TAG, "onPageFinished: ", url);
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
