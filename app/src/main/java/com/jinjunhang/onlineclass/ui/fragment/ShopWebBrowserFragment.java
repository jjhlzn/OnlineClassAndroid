package com.jinjunhang.onlineclass.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.wx.Util;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.onlineclass.model.ServiceLinkManager;
import com.jinjunhang.onlineclass.ui.lib.ParseHtmlPageTask;
import com.jinjunhang.onlineclass.ui.lib.ShareManager;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by lzn on 2016/9/25.
 */

public class ShopWebBrowserFragment extends android.support.v4.app.Fragment {
    private final static String TAG = LogHelper.makeLogTag(ShopWebBrowserFragment.class);

    public final static String EXTRA_URL = "EXTRA_URL";
    public final static String EXTRA_TITLE = "EXTRA_TITLE";

    private String mUrl;
    private WebView mWebView;
    private ImageButton mBackButton;
    private IWXAPI mWXAPI;

    private ShareManager mShareManager;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.web_browser, container, false);

        mWXAPI = WXAPIFactory.createWXAPI(getActivity(), null);
        mWXAPI.registerApp("wx73653b5260b24787");

        mUrl = ServiceLinkManager.ShenqingUrl();
        mUrl = Util.addUserInfo(mUrl);
        mUrl = Util.addDeviceInfo(mUrl);
        LogHelper.d(TAG, mUrl);

        //设置返回按键
        AppCompatActivity activity = (AppCompatActivity)getActivity();
        mBackButton = (ImageButton) activity.getSupportActionBar().getCustomView().findViewById(R.id.actionbar_back_button);
        ((TextView) activity.getSupportActionBar().getCustomView().findViewById(R.id.actionbar_text)).setText("申请");
        mBackButton.setVisibility(View.INVISIBLE);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                } else {
                    //onBackPressed();
                    mBackButton.setVisibility(View.INVISIBLE);
                }
            }
        });

        mWebView = (WebView) v.findViewById(R.id.webview);

        mWebView.getSettings().setJavaScriptEnabled(true); // enable javascript
        mWebView.setWebViewClient(new ShopWebBrowserFragment.MyWebViewClient());

        WebSettings settings = mWebView.getSettings();
        settings.setDomStorageEnabled(true);
        openURL();

        mShareManager = new ShareManager((AppCompatActivity)getActivity(), v);
        mShareManager.setUseQrCodeImage(false);
        mShareManager.setShareButtonVisible(false);

        new ParseHtmlPageTask().setShareManager(mShareManager).execute(mUrl);
        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        mShareManager.setShareButtonVisible(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        mShareManager.setShareButtonVisible(true);
    }

    /** Opens the URL in a browser */
    private void openURL() {
        mWebView.loadUrl(mUrl);
        mWebView.requestFocus();
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
            if (Utils.handleWechatPay(mWXAPI, url, null)) {
                return true;
            }
            view.loadUrl(url);
            return true;
        }




        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            new ParseHtmlPageTask().setShareManager(mShareManager).execute(url);
            if (mWebView.canGoBack()) {
                mBackButton.setVisibility(View.VISIBLE);
            } else {
                mBackButton.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (mWebView.canGoBack()) {
                mBackButton.setVisibility(View.VISIBLE);
            } else {
                mBackButton.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error){
            handler.proceed();
        }
    }
}


