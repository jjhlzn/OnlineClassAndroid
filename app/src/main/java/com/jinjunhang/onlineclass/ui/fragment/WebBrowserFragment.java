package com.jinjunhang.onlineclass.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.db.LoginUserDao;
import com.jinjunhang.onlineclass.model.LoginUser;
import com.jinjunhang.onlineclass.model.ServiceLinkManager;
import com.jinjunhang.onlineclass.ui.activity.WebBrowserActivity;
import com.jinjunhang.onlineclass.ui.lib.CustomApplication;
import com.jinjunhang.onlineclass.ui.lib.WeixinShareManager;
import com.jinjunhang.player.utils.LogHelper;

/**
 * Created by lzn on 2016/9/25.
 */

public class WebBrowserFragment extends android.support.v4.app.Fragment {
    private final static String TAG = LogHelper.makeLogTag(WebBrowserFragment.class);

    public final static String EXTRA_URL = "EXTRA_URL";
    public final static String EXTRA_TITLE = "EXTRA_TITLE";

    private String mUrl;
    private WebView mWebView;
    private ImageButton mBackButton;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.web_browser, container, false);

        //mUrl = ServiceLinkManager.MyAgentUrl();
        mUrl = "http://www.baidu.com";
        mUrl = addUserInfo(mUrl);
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
        mWebView.setWebViewClient(new WebBrowserFragment.MyWebViewClient());
        openURL();

        return v;
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
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
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
    }

    private String addUserInfo(String url) {


        LoginUser user = LoginUserDao.getInstance(CustomApplication.get()).get();
        if (user != null) {
            if (url.indexOf("?") == -1) {
                url += "?";
            }
            url += "userid=" + user.getUserName() + "&" + "token=" + user.getToken();
        }
        return url;
    }
}


