package com.jinjunhang.onlineclass.ui.activity.user;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.jinjunhang.framework.controller.SingleFragmentActivity;
import com.jinjunhang.framework.lib.LoadingAnimation;
import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.framework.service.ServerResponse;
import com.jinjunhang.framework.wx.Util;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.db.LoginUserDao;
import com.jinjunhang.onlineclass.model.LoginUser;
import com.jinjunhang.onlineclass.service.GetCheckCodeRequest;
import com.jinjunhang.onlineclass.service.GetCheckCodeResponse;
import com.jinjunhang.onlineclass.service.MobileLoginRequest;
import com.jinjunhang.onlineclass.service.MobileLoginResponse;
import com.jinjunhang.onlineclass.ui.activity.mainpage.BottomTabLayoutActivity;
import com.jinjunhang.onlineclass.ui.fragment.user.MobileLoginFragment;
import com.jinjunhang.onlineclass.ui.fragment.user.SignupFragment;
import com.jinjunhang.onlineclass.ui.lib.WeixinLoginManager;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lzn on 16/6/28.
 */
public class MobileLoginActivity extends FragmentActivity {

    private LoadingAnimation mLoading;
    private TextView mCheckCodeButtonMessage;
    private Button mGetCheckCodeButton;

    private String mDeviceToken;

    private LoginUserDao mLoginUserDao;

    //控制
    private static final long UPDATE_MESSAGE_INTERNAL = 1000;
    private static final long PROGRESS_UPDATE_INITIAL_INTERVAL = 100;
    private int mSeconds = 59;
    private final Handler mHandler = new Handler();
    private ScheduledFuture<?> mScheduleFuture;
    private final ScheduledExecutorService mExecutorService = Executors.newSingleThreadScheduledExecutor();
    private final Runnable mUpdateProgressTask = new Runnable() {
        @Override
        public void run() {
            updateMessage();
        }
    };

    public static final int WEIXIN_LOGIN = 1;
    public static int loginType = -1;
    public static String code = "";

    private static final String TAG = LogHelper.makeLogTag(LoginActivity.class);
    private EditText mUserNameEditText;
    private EditText mPasswordEditText;

    private WeixinLoginManager mWXLoginManager;

    @BindView(R.id.weixinBtn) ImageView mWeixinBtn;
    //@BindView(R.id.qqBtn) ImageView mQQBtn;

    @OnClick(R.id.weixinBtn) void weixinClick() {
        mWXLoginManager.loginStep1();
    }

    @BindView(R.id.mobileBtn)
    ImageView mMobleBtn;
    @OnClick(R.id.mobileBtn) void mobileBtnClick() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }
    private View mView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_login_using_code);
        mView = findViewById(R.id.fragmentContainer);
        mLoading = new LoadingAnimation(this);

        mWXLoginManager = new WeixinLoginManager(this);

        ButterKnife.bind(this);

        Button loginButton = (Button) mView.findViewById(R.id.login_button);
        mGetCheckCodeButton = (Button) mView.findViewById(R.id.get_checkcode_button);
        mCheckCodeButtonMessage = (TextView) mView.findViewById(R.id.get_checkcode_message);
        final EditText phoneField = (EditText) mView.findViewById(R.id.login_userName);
        final EditText checkCodeField = (EditText) mView.findViewById(R.id.signup_checkcode);

        mLoginUserDao = LoginUserDao.getInstance(this);

        XGPushManager.registerPush( this, new XGIOperateCallback() {
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

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = phoneField.getText().toString();
                String checkCode = checkCodeField.getText().toString();

                if (phoneNumber.trim() == "") {
                    Utils.showErrorMessage(MobileLoginActivity.this, "手机号码不能为空");
                    return;
                }

                if (checkCode.trim() == "") {
                    Utils.showErrorMessage(MobileLoginActivity.this, "验证码不能为空");
                    return;
                }



                MobileLoginRequest req = new MobileLoginRequest();
                req.setMobile(phoneNumber);
                req.setCheckCode(checkCode);
                if ( Util.isVirtualEmulator() || !"".equals(mDeviceToken)) {
                    req.setDeviceToken(mDeviceToken);
                    new MobileLoginTask().execute(req);
                } else {
                    XGPushManager.registerPush(MobileLoginActivity.this, new XGIOperateCallback() {
                        @Override
                        public void onSuccess(Object o, int i) {
                            Log.d(TAG, "register device succes, devicetoken = " + o.toString());
                            mDeviceToken = o.toString();
                            req.setDeviceToken(o.toString());
                            new MobileLoginTask().execute(req);
                        }

                        @Override
                        public void onFail(Object o, int i, String s) {
                            Log.d(TAG, "register device fail");
                            mLoading.hide();
                            Utils.showMessage(MobileLoginActivity.this, "您的网络不给力，请检查网络是否正常!");
                        }
                    });
                }
            }
        });


        mGetCheckCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetCheckCodeRequest request = new GetCheckCodeRequest();
                String phoneNumber = phoneField.getText().toString();
                request.setPhoneNumber(phoneNumber);

                if (phoneNumber == null || "".equals(phoneNumber.trim())) {
                    Utils.showErrorMessage(MobileLoginActivity.this, "手机号码不能为空！");
                    return;
                }

                getCheckCodeButtonPressed();

                new GetCheckCodeTask().execute(request);
            }
        });


        Utils.setupUI4HideKeybaord(mView, this);

        ImmersionBar.with(this).statusBarDarkFont(true).init();
    }

    private void getCheckCodeButtonPressed() {
        mGetCheckCodeButton.setVisibility(View.INVISIBLE);
        mCheckCodeButtonMessage.setVisibility(View.VISIBLE);
        scheduleCheckCodeButtonMessage();

    }

    private void scheduleCheckCodeButtonMessage() {
        stopMessageUpdate();
        if (!mExecutorService.isShutdown()) {
            mScheduleFuture = mExecutorService.scheduleAtFixedRate(
                    new Runnable() {
                        @Override
                        public void run() {
                            mHandler.post(mUpdateProgressTask);
                        }
                    }, PROGRESS_UPDATE_INITIAL_INTERVAL,
                    UPDATE_MESSAGE_INTERNAL, TimeUnit.MILLISECONDS);
        }
    }

    private void stopMessageUpdate() {
        if (mScheduleFuture != null) {
            mScheduleFuture.cancel(false);
        }
    }

    private void updateMessage() {
        mSeconds--;

        if (mSeconds <= 0) {
            mSeconds = 59;
            stopMessageUpdate();
            mGetCheckCodeButton.setVisibility(View.VISIBLE);
            mCheckCodeButtonMessage.setVisibility(View.INVISIBLE);
            return;
        }

        mCheckCodeButtonMessage.setText(mSeconds+"秒后重新获取");
    }


    private class MobileLoginTask extends AsyncTask<MobileLoginRequest, Void, MobileLoginResponse> {
        private MobileLoginRequest mLoginRequest;

        @Override
        protected MobileLoginResponse doInBackground(MobileLoginRequest... params) {
            mLoginRequest = params[0];

            return new BasicService().sendRequest(mLoginRequest);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoading.show("登陆中...");
        }

        @Override
        protected void onPostExecute(MobileLoginResponse loginResponse) {
            super.onPostExecute(loginResponse);
            mLoading.hide();

            if (loginResponse.getStatus() != ServerResponse.SUCCESS) {
                Utils.showErrorMessage(MobileLoginActivity.this, loginResponse.getErrorMessage());
                return;
            }

            LoginUser loginUser = new LoginUser(loginResponse);
            loginUser.setUserName(loginResponse.getUserId());
            loginUser.setPassword("mobilelogin");

            mLoginUserDao.save(loginUser);

            mLoading.hide();
            Intent i = new Intent(MobileLoginActivity.this, BottomTabLayoutActivity.class);
            startActivity(i);
        }
    }

    private class GetCheckCodeTask extends AsyncTask<GetCheckCodeRequest, Void, GetCheckCodeResponse> {
        @Override
        protected GetCheckCodeResponse doInBackground(GetCheckCodeRequest... params) {
            GetCheckCodeRequest req = params[0];
            return new BasicService().sendRequest(req);
        }

        @Override
        protected void onPostExecute(GetCheckCodeResponse getCheckCodeResponse) {
            super.onPostExecute(getCheckCodeResponse);
            if (getCheckCodeResponse.getStatus() != ServerResponse.SUCCESS) {
                Utils.showErrorMessage(MobileLoginActivity.this, getCheckCodeResponse.getErrorMessage());
                return;
            }
        }
    }


    @Override
    public void onBackPressed() {
        return;
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogHelper.d(TAG, "onResume called");
        if (loginType == WEIXIN_LOGIN) {
            LogHelper.d(TAG, "code: " + code);
            loginType = -1;

            mWXLoginManager.loginStep2(code, mLoading, mDeviceToken);
        }
    }
}
