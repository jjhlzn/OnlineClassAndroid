package com.jinjunhang.onlineclass.ui.fragment.user;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gyf.barlibrary.ImmersionBar;
import com.jinjunhang.framework.lib.LoadingAnimation;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.framework.service.ServerResponse;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.db.KeyValueDao;
import com.jinjunhang.onlineclass.db.LoginUserDao;
import com.jinjunhang.onlineclass.model.LoginUser;
import com.jinjunhang.onlineclass.service.BindPhoneRequest;
import com.jinjunhang.onlineclass.service.BindPhoneResponse;
import com.jinjunhang.onlineclass.service.GetCheckCodeRequest;
import com.jinjunhang.onlineclass.service.GetCheckCodeResponse;
import com.jinjunhang.onlineclass.ui.fragment.BaseFragment;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SetPhoneFragment extends BaseFragment {

    private KeyValueDao mKeyValueDao;
    private LoginUserDao mLoginUserDao;
    private LoadingAnimation mLoadingAnimation;

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.actionbar_text) TextView mToolBarText;

    @BindView(R.id.get_checkcode_button) Button mGetCheckCodeButton;
    @BindView(R.id.get_checkcode_message) TextView mCheckCodeButtonMessage;

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

    @Override
    protected boolean isNeedTopMargin() {
        return false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_fragment_set_phone;
    }

    @BindView(R.id.bindPhone1) EditText mPhone1;
    @BindView(R.id.bindPhone2) EditText mPhone2;
    @BindView(R.id.checkcode) EditText mCheckCode;
    @BindView(R.id.codeContainer) View mCodeContainer;

    @OnClick(R.id.get_checkcode_button) void clickGetCodeButton() {
        LoginUser user = mLoginUserDao.get();
        GetCheckCodeRequest request = new GetCheckCodeRequest();
        String phoneNumber = user.getUserName();
        request.setPhoneNumber(phoneNumber);

        getCheckCodeButtonPressed();

        new GetCheckCodeTask().execute(request);
    }

    @OnClick(R.id.actionbar_back_button) void backClick() {
        getActivity().finish();
    }

    @OnClick(R.id.actionbar_right_button) void saveClick() {

        String phone1 = mPhone1.getText().toString();
        String phone2 = mPhone2.getText().toString();

        if (phone1.isEmpty()) {
            Utils.showErrorMessage(getContext(), "手机号不同为空");
            return;
        }

        if (!phone1.equals(phone2)) {
            Utils.showErrorMessage(getContext(), "手机号输入不一致");
            return;
        }

        if (mKeyValueDao.hasBindPhone()) {
            if (mCheckCode.getText().toString().isEmpty()) {
                Utils.showErrorMessage(getContext(), "验证码不能为空");
                return;
            }
        }


        BindPhoneRequest req = new BindPhoneRequest();
        req.setNewPhone(phone1);
        req.setCode(mCheckCode.getText().toString());
        new BindPhoneTask().execute(req);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        mKeyValueDao = KeyValueDao.getInstance(getActivity());
        mLoginUserDao = LoginUserDao.getInstance(getActivity());
        ButterKnife.bind(this, v);

        mLoadingAnimation = new LoadingAnimation(getActivity());

        if (mKeyValueDao.hasBindPhone()) {
            mCodeContainer.setVisibility(View.VISIBLE);
        } else {
            mCodeContainer.setVisibility(View.INVISIBLE);
        }

        ImmersionBar.setTitleBar(getActivity(), mToolbar);
        mToolBarText.setText("手机号绑定");


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

    private class BindPhoneTask extends AsyncTask<BindPhoneRequest, Void, BindPhoneResponse> {

        private BindPhoneRequest mBindPhoneRequest;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingAnimation.show("");
        }

        @Override
        protected BindPhoneResponse doInBackground(BindPhoneRequest... bindPhoneRequests) {
            mBindPhoneRequest = bindPhoneRequests[0];
            return new BasicService().sendRequest(mBindPhoneRequest);
        }

        @Override
        protected void onPostExecute(BindPhoneResponse bindPhoneResponse) {
            super.onPostExecute(bindPhoneResponse);
            mLoadingAnimation.hide();
            if (!bindPhoneResponse.isSuccess()) {
                Utils.showMessage(getActivity(), bindPhoneResponse.getErrorMessage());
                return;
            }

            LoginUser user = mLoginUserDao.get();
            user.setUserName(mBindPhoneRequest.getNewPhone());

            //修改手机存储的手机号
            mLoginUserDao.save(user);
            mKeyValueDao.saveOrUpdate(KeyValueDao.KEY_USER_HAS_BIND_PHONE, "1");

            Utils.showMessage(getActivity(), "保存成功", new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

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
