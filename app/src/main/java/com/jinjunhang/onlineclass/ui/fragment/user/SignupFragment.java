package com.jinjunhang.onlineclass.ui.fragment.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jinjunhang.framework.lib.LoadingAnimation;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.framework.service.ServerResponse;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.service.GetCheckCodeRequest;
import com.jinjunhang.onlineclass.service.GetCheckCodeResponse;
import com.jinjunhang.onlineclass.service.SignupRequest;
import com.jinjunhang.onlineclass.service.SignupResponse;
import com.jinjunhang.onlineclass.ui.activity.user.LoginActivity;
import com.jinjunhang.onlineclass.ui.fragment.BaseFragment;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by lzn on 16/6/28.
 */
public class SignupFragment extends BaseFragment {

    private LoadingAnimation mLoading;
    private TextView mCheckCodeButtonMessage;
    private Button mGetCheckCodeButton;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_fragment_signup, container, false);
        mLoading = new LoadingAnimation(getActivity());

        Button singupButton = (Button) v.findViewById(R.id.singup_button);
        mGetCheckCodeButton = (Button) v.findViewById(R.id.get_checkcode_button);
        mCheckCodeButtonMessage = (TextView) v.findViewById(R.id.get_checkcode_message);
        final EditText phoneField = (EditText) v.findViewById(R.id.signup_phone);
        final EditText checkCodeField = (EditText) v.findViewById(R.id.signup_checkcode);
        final EditText invitePhoneField = (EditText) v.findViewById(R.id.signup_invitePhone);
        final EditText passwordField = (EditText) v.findViewById(R.id.signup_password);

        singupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = phoneField.getText().toString();
                String checkCode = checkCodeField.getText().toString();
                String invitePhone = invitePhoneField.getText().toString();
                String password = passwordField.getText().toString();

                if (phoneNumber.trim() == "") {
                    Utils.showErrorMessage(getActivity(), "手机号码不能为空");
                    return;
                }

                if (checkCode.trim() == "") {
                    Utils.showErrorMessage(getActivity(), "验证码不能为空");
                    return;
                }


                if (password.trim() == "") {
                    Utils.showErrorMessage(getActivity(), "密码不能为空");
                    return;
                }

                if (password.length() < 6) {
                    Utils.showErrorMessage(getActivity(), "密码至少为6位");
                    return;
                }

                SignupRequest req = new SignupRequest();
                req.setPhoneNumber(phoneNumber);
                req.setCheckCode(checkCode);
                req.setInvitePhone(invitePhone);
                req.setPassword(password);

                new SignupTask().execute(req);
            }
        });


        mGetCheckCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetCheckCodeRequest request = new GetCheckCodeRequest();
                String phoneNumber = phoneField.getText().toString();
                request.setPhoneNumber(phoneNumber);

                getCheckCodeButtonPressed();

                new GetCheckCodeTask().execute(request);
            }
        });

        Utils.setupUI4HideKeybaord(v, getActivity());
        return v;
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


    private class SignupTask extends AsyncTask<SignupRequest, Void, SignupResponse> {
        @Override
        protected SignupResponse doInBackground(SignupRequest... params) {
            SignupRequest request = params[0];

            return new BasicService().sendRequest(request);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoading.show("注册中...");
        }

        @Override
        protected void onPostExecute(SignupResponse signupResponse) {
            super.onPostExecute(signupResponse);
            mLoading.hide();

            if (signupResponse.getStatus() != ServerResponse.SUCCESS) {
                Utils.showErrorMessage(getActivity(), signupResponse.getErrorMessage());
                return;
            }

            Utils.showMessage(getActivity(), "注册成功", new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(i);
                }
            });
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
                Utils.showErrorMessage(getActivity(), getCheckCodeResponse.getErrorMessage());
                return;
            }
        }
    }


}
