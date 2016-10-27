package com.jinjunhang.onlineclass.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jinjunhang.framework.lib.LoadingAnimation;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.wx.Util;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.ui.lib.WeixinShareManager;
import com.jinjunhang.framework.lib.LogHelper;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by lzn on 16/6/20.
 */
public class WebBrowserActivity extends AppCompatActivity {

    private final static String TAG = LogHelper.makeLogTag(WebBrowserActivity.class);

    public final static String EXTRA_URL = "EXTRA_URL";
    public final static String EXTRA_TITLE = "EXTRA_TITLE";

    private String mUrl;
    private String mTitle;
    private WebView mWebView;
    private TextView mCloseButton;
    private IWXAPI mWXAPI;

    private LoadingAnimation mLoadingAnimation;

    private WeixinShareManager mWeixinShareManager;
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

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
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
        setWeixinShareIfNeed(findViewById(R.id.rootView));
        mWebView = (WebView) findViewById(R.id.webview);

        mWebView.getSettings().setJavaScriptEnabled(true); // enable javascript
        mWebView.setWebViewClient(new MyWebViewClient());

        WebSettings settings = mWebView.getSettings();
        settings.setDomStorageEnabled(true);
        openURL();
    }


    private void  setWeixinShareIfNeed(View v) {
       if ("提额秘诀".equals(mTitle)) {
           mWeixinShareManager = new WeixinShareManager(this, v);
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

            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
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
