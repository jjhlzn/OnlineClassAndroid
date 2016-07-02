package com.jinjunhang.onlineclass.ui.activity.user;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

/**
 * Created by lzn on 16/6/27.
 */
public class LoginActivity extends android.support.v4.app.FragmentActivity {


    private static final String TAG = LogHelper.makeLogTag(LoginActivity.class);
    private EditText mUserNameEditText;
    private EditText mPasswordEditText;
    private LoadingAnimation mLoading;

    private LoginUserDao mLoginUserDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_login);

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

                LoginRequest req = new LoginRequest();
                req.setUserName(userName);
                req.setPassword(password);
                //TODO: mock device token
                req.setDeviceToken("");
                new LoginTask().execute(req);
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
            mLoading.show("");
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
