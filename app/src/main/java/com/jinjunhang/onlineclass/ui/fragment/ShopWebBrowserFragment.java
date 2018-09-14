package com.jinjunhang.onlineclass.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.jinjunhang.onlineclass.ui.fragment.mainpage.Page;
import com.jinjunhang.onlineclass.ui.lib.ParseHtmlPageTask;
import com.jinjunhang.onlineclass.ui.lib.ShareManager;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import android.os.Bundle;

/**
 * Created by lzn on 2016/9/25.
 */

public class ShopWebBrowserFragment extends BaseFragment  implements SwipeRefreshLayout.OnRefreshListener {
    private final static String TAG = LogHelper.makeLogTag(ShopWebBrowserFragment.class);

    public final static String EXTRA_URL = "EXTRA_URL";
    public final static String EXTRA_TITLE = "EXTRA_TITLE";
    public final static String EXTRA_NEED_REFRESH = "EXTRA_NEED_REFRESH";
    public View v;
    private String mUrl;
    private WebView mWebView;
    private ImageButton mBackButton;
    private IWXAPI mWXAPI;

    private ShareManager mShareManager;
    private SwipeRefreshLayout mRefreshLayout;


    private String mTitle = "";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        LogHelper.d(TAG, "onCreateView called");


        if (getArguments() != null && getArguments().getString("title") != null) {
            mTitle = getArguments().getString("title");
        }

        LogHelper.d(TAG, "onCreateView called");

        v = inflater.inflate(R.layout.web_browser, container, false);

        mRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        mRefreshLayout.setOnRefreshListener(this);

        mRefreshLayout.setEnabled(getArguments().getBoolean(EXTRA_NEED_REFRESH, false));



        mWXAPI = WXAPIFactory.createWXAPI(getActivity(), null);
        mWXAPI.registerApp("wx73653b5260b24787");

        mUrl = ServiceLinkManager.ShenqingUrl();
        if (getArguments() != null && getArguments().getString("url") != null) {
            mUrl = getArguments().getString("url");
        }
        //mUrl = "http://www.baidu.com";
        mUrl = Util.addUserInfo(mUrl);
        mUrl = Util.addDeviceInfo(mUrl);
        LogHelper.d(TAG, mUrl);

        mWebView = (WebView) v.findViewById(R.id.webview);

        mWebView.getSettings().setJavaScriptEnabled(true); // enable javascript
        mWebView.setWebViewClient(new ShopWebBrowserFragment.MyWebViewClient());


        WebSettings settings = mWebView.getSettings();
        settings.setDomStorageEnabled(true);
        openURL();

        //changeActionBar();

        return v;
    }

    @Override
    public void onRefresh() {
        mWebView.reload();
        mWebView.requestFocus();
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void changeActionBar() {
        AppCompatActivity activity = (AppCompatActivity)getActivity();
        //LogHelper.d(TAG, "activity = " + activity);
        if (activity != null) {

            activity.getSupportActionBar().show();
            activity.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            final View customView = activity.getLayoutInflater().inflate(R.layout.actionbar_browser, null);
            activity.getSupportActionBar().setCustomView(customView);
            Toolbar parent = (Toolbar) customView.getParent();

            parent.setContentInsetsAbsolute(0, 0);

            //设置返回按键
            mBackButton = (ImageButton) activity.getSupportActionBar().getCustomView().findViewById(R.id.actionbar_back_button);

            if (!"".equals(mTitle))
                ((TextView) activity.getSupportActionBar().getCustomView().findViewById(R.id.actionbar_text)).setText(mTitle);


            if (mWebView.canGoBack()) {
                //mWebView.goBack();
                mBackButton.setVisibility(View.VISIBLE);
            } else {
                //onBackPressed();
                mBackButton.setVisibility(View.INVISIBLE);
            }

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

            setLightStatusBar(customView, activity);
            Utils.setNavigationBarMusicButton(activity);
        }

        if (mShareManager == null) {
            mShareManager = new ShareManager((AppCompatActivity) getActivity(), getView());
            mShareManager.setUseQrCodeImage(false);
            mShareManager.setShareButtonVisible(false);

            new ParseHtmlPageTask().setShareManager(mShareManager).execute(mUrl);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        LogHelper.d(TAG, "onPause called");
        if (mShareManager != null)
            mShareManager.setShareButtonVisible(false);
    }


    @Override
    public void onResume() {
        super.onResume();
        LogHelper.d(TAG, "onResume called");
        if (mShareManager != null)
            mShareManager.setShareButtonVisible(true);

    }

    /** Opens the URL in a browser */
    private void openURL() {
        LogHelper.d(TAG, "openURL called");
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
            if (mBackButton != null) {
                if (view.canGoBack()) {
                    mBackButton.setVisibility(View.VISIBLE);
                } else {
                    mBackButton.setVisibility(View.INVISIBLE);
                }
            }
            return true;
        }


        @Override
        public void onPageFinished(WebView view, String url) {
            //super.onPageFinished(view, url);
            new ParseHtmlPageTask().setShareManager(mShareManager).execute(url);
            LogHelper.d(TAG, "onPageFinished: " + url);
            LogHelper.d(TAG,"mWebView.canGoBack(): " + view.canGoBack());
            if (mBackButton != null) {
                if (view.canGoBack()) {
                    mBackButton.setVisibility(View.VISIBLE);
                } else {
                    mBackButton.setVisibility(View.INVISIBLE);
                }
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //super.onPageStarted(view, url, favicon);
            LogHelper.d(TAG,"onPageStarted: " + url);
            LogHelper.d(TAG,"mWebView.canGoBack(): " + view.canGoBack());
            if (mBackButton != null) {
                if (view.canGoBack()) {
                    mBackButton.setVisibility(View.VISIBLE);
                } else {
                    mBackButton.setVisibility(View.INVISIBLE);
                }
            }
        }



        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error){
            handler.proceed();
        }
    }
}


