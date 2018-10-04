package com.jinjunhang.onlineclass.ui.fragment.user;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.jinjunhang.framework.lib.LoadingAnimation;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.db.LoginUserDao;
import com.jinjunhang.onlineclass.model.LoginUser;
import com.jinjunhang.onlineclass.service.SetPasswordRequest;
import com.jinjunhang.onlineclass.service.SetPasswordResponse;
import com.jinjunhang.onlineclass.ui.fragment.BaseFragment;
import com.jinjunhang.framework.lib.LogHelper;

/**
 * Created by jjh on 2016-7-2.
 */
public class SetPasswordFragment extends BaseFragment {
    private static final String TAG = LogHelper.makeLogTag(SetPasswordFragment.class);
    private LoadingAnimation mLoading;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_fragment_set_password;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater  inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView =  super.onCreateView(inflater, container, savedInstanceState);
        mLoading = new LoadingAnimation(getActivity());

        final EditText originPassword = (EditText) mView.findViewById(R.id.origin_password);
        final EditText newPassword = (EditText) mView.findViewById(R.id.new_password);
        final EditText repeatNewPassword = (EditText) mView.findViewById(R.id.repeat_new_password);

        ((AppCompatActivity)getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.actionbar_right_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser user = LoginUserDao.getInstance(getActivity()).get();

                String originPass = originPassword.getText().toString().trim();
                String newPass = newPassword.getText().toString().trim();
                String repeatNewPass = repeatNewPassword.getText().toString().trim();
                LogHelper.d(TAG, "originPass = " + user.getPassword() + ", oldPass = " + originPass);
                if (!user.getPassword().equals(originPass)) {
                    Utils.showErrorMessage(getActivity(), "原密码错误");
                    return;
                }

                if (!newPass.equals(repeatNewPass)) {
                    Utils.showErrorMessage(getActivity(), "新密码两次输入不同");
                    return;
                }

                if (newPass.length() < 6) {
                    Utils.showErrorMessage(getActivity(), "密码必须6位以上");
                    return;
                }

                SetPasswordRequest request = new SetPasswordRequest();
                request.setmNewPassword(newPass);
                request.setmOldPassword(originPass);

                new SetPasswordTask().execute(request);
            }
        });

        return mView;
    }

    private class SetPasswordTask extends AsyncTask<SetPasswordRequest, Void, SetPasswordResponse> {
        private SetPasswordRequest mRequest;

        @Override
        protected SetPasswordResponse doInBackground(SetPasswordRequest... params) {
            mRequest = params[0];
            return new BasicService().sendRequest(mRequest);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoading.show("更新中...");
        }

        @Override
        protected void onPostExecute(SetPasswordResponse resp) {
            super.onPostExecute(resp);
            mLoading.hide();
            if (!resp.isSuccess()) {
                Utils.showErrorMessage(getActivity(), resp.getErrorMessage());
                return;
            }

            LoginUser user = LoginUserDao.getInstance(getActivity()).get();
            user.setPassword(mRequest.getmNewPassword());
            LoginUserDao.getInstance(getActivity()).save(user);
            Utils.showMessage(getActivity(), "更新成功", new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    getActivity().finish();
                }
            });
        }
    }
}
