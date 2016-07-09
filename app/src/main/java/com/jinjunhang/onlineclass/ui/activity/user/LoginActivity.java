package com.jinjunhang.onlineclass.ui.activity.user;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jinjunhang.framework.lib.LoadingAnimation;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.framework.service.ServerResponse;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.db.LoginUserDao;
import com.jinjunhang.onlineclass.model.LoginUser;
import com.jinjunhang.onlineclass.service.LoginRequest;
import com.jinjunhang.onlineclass.service.LoginResponse;
import com.jinjunhang.onlineclass.ui.activity.MainActivity;
import com.jinjunhang.onlineclass.ui.activity.other.ConfigurationActivity;
import com.jinjunhang.player.utils.LogHelper;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;

/**
 * Created by lzn on 16/6/27.
 */
public class LoginActivity extends android.support.v4.app.FragmentActivity {


    private static final String TAG = LogHelper.makeLogTag(LoginActivity.class);
    private EditText mUserNameEditText;
    private EditText mPasswordEditText;
    private LoadingAnimation mLoading;

    private LoginUserDao mLoginUserDao;
    private String mDeviceToken = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_login);

        XGPushManager.registerPush(LoginActivity.this, new XGIOperateCallback() {
            @Override
            public void onSuccess(Object o, int i) {
                Log.d(TAG, "register device succes, devicetoken = " + o.toString());
                mDeviceToken = o.toString();
            }

            @Override
            public void onFail(Object o, int i, String s) {
                Log.d(TAG, "register device fail");
            }
        });

        mLoading = new LoadingAnimation(this);
        mLoginUserDao = LoginUserDao.getInstance(this);

        Utils.setupUI4HideKeybaord(findViewById(R.id.fragmentContainer), this);

        mUserNameEditText = (EditText)findViewById(R.id.login_userName);
        mPasswordEditText = (EditText)findViewById(R.id.login_password);

        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = mUserNameEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();
                if (userName.isEmpty()) {
                    Utils.showMessage(LoginActivity.this, "必须输入用户名");
                    return;
                }

                if (password.isEmpty()) {
                    Utils.showMessage(LoginActivity.this, "必须输入密码");
                    return;
                }

                final LoginRequest req = new LoginRequest();
                req.setUserName(userName);
                req.setPassword(password);
                //TODO: mock device token
                mLoading.show("");
                if (!"".equals(mDeviceToken)) {
                    req.setDeviceToken(mDeviceToken);
                    new LoginTask().execute(req);
                } else {
                    XGPushManager.registerPush(LoginActivity.this, new XGIOperateCallback() {
                        @Override
                        public void onSuccess(Object o, int i) {
                            Log.d(TAG, "register device succes, devicetoken = " + o.toString());
                            mDeviceToken = o.toString();
                            req.setDeviceToken(o.toString());
                            new LoginTask().execute(req);
                        }

                        @Override
                        public void onFail(Object o, int i, String s) {
                            Log.d(TAG, "register device fail");
                            mLoading.hide();
                            Utils.showMessage(LoginActivity.this, "服务器出错，请重试!");
                        }
                    });
                }

            }
        });

        Button configButton = (Button)findViewById(R.id.login_configButton);
        configButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, ConfigurationActivity.class);
                startActivity(i);
            }
        });

        Button signupButton = (Button) findViewById(R.id.signup_button);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(i);
            }
        });

        Button forgetPasswordButton = (Button) findViewById(R.id.forget_password_button);
        forgetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(i);
            }
        });
    }


    private class LoginTask extends AsyncTask<LoginRequest, Void, LoginResponse> {
        private LoginRequest mLoginRequest;

        @Override
        protected LoginResponse doInBackground(LoginRequest... params) {
            mLoginRequest = params[0];
            return new BasicService().sendRequest(mLoginRequest);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(LoginResponse loginResponse) {
            super.onPostExecute(loginResponse);

            if (loginResponse.getStatus() != ServerResponse.SUCCESS) {
                mLoading.hide();
                Utils.showMessage(LoginActivity.this, loginResponse.getErrorMessage());
                return;
            }

            LoginUser loginUser = new LoginUser(loginResponse);
            loginUser.setUserName(mLoginRequest.getUserName());
            loginUser.setPassword(mLoginRequest.getPassword());

            mLoginUserDao.save(loginUser);

            mLoading.hide();
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
        }
    }
}
