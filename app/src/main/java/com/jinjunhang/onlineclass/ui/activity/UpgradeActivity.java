package com.jinjunhang.onlineclass.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.db.LoginUserDao;
import com.jinjunhang.onlineclass.model.LoginUser;
import com.jinjunhang.onlineclass.ui.activity.user.LoginActivity;
import com.jinjunhang.onlineclass.ui.lib.CustomApplication;
import com.jinjunhang.framework.lib.LogHelper;

/**
 * Created by lzn on 16/6/20.
 */
public class UpgradeActivity extends AppCompatActivity {

    private final static String TAG = LogHelper.makeLogTag(UpgradeActivity.class);

    public final static String EXTRA_URL = "EXTRA_URL";
    public final static String EXTRA_TITLE = "EXTRA_TITLE";
    public final static String EXTRA_ISFORCEUPGRADE = "EXTRA_ISFORCEUPGRADE";

    private String mUrl;
    private String mTitle;
    private WebView mWebView;
    private LoginUserDao mLoginUserDao;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_browser);
        mLoginUserDao = LoginUserDao.getInstance(this);

        mUrl = getIntent().getStringExtra(EXTRA_URL);
        mUrl = addUserInfo(mUrl);
        LogHelper.d(TAG, mUrl);
        mTitle = getIntent().getStringExtra(EXTRA_TITLE);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View customView = getLayoutInflater().inflate(R.layout.actionbar, null);
        ((TextView)customView.findViewById(R.id.actionbar_text)).setText(mTitle);
        getSupportActionBar().setCustomView(customView);
        Toolbar parent =(Toolbar) customView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        //设置返回按键
        boolean isForceUpgrade = getIntent().getBooleanExtra(EXTRA_ISFORCEUPGRADE, false);
        if (!isForceUpgrade) {
            ImageButton backButton = (ImageButton) getSupportActionBar().getCustomView().findViewById(R.id.actionbar_back_button);
            backButton.setVisibility(View.VISIBLE);
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //onBackPressed();
                    checkLogin();
                }
            });
        }

        mWebView = (WebView) findViewById(R.id.webview);

        mWebView.getSettings().setJavaScriptEnabled(true); // enable javascript
        mWebView.setWebViewClient(new MyWebViewClient());
        openURL();
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
    }

    private String addUserInfo(String url) {
        if (url == null) {
            return url;
        }

        if (url.indexOf("?") == -1) {
            url += "?";
        }

        LoginUser user = LoginUserDao.getInstance(CustomApplication.get()).get();
        if (user == null) {
            return url;
        }
        url += "userid=" + user.getUserName() + "&" + "token=" + user.getToken();
        return url;
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

}
