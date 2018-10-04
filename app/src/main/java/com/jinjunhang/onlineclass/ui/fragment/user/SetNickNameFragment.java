package com.jinjunhang.onlineclass.ui.fragment.user;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.jinjunhang.framework.lib.LoadingAnimation;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.db.LoginUserDao;
import com.jinjunhang.onlineclass.model.LoginUser;
import com.jinjunhang.onlineclass.service.SetNameResponse;
import com.jinjunhang.onlineclass.service.SetNickNameRequest;
import com.jinjunhang.onlineclass.service.SetNickNameResponse;
import com.jinjunhang.onlineclass.ui.fragment.BaseFragment;

/**
 * Created by jjh on 2016-7-1.
 */
public class SetNickNameFragment extends BaseFragment {

    private LoadingAnimation mLoading;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_fragment_set_name_or_nickname;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = super.onCreateView(inflater, container, savedInstanceState);
        mLoading = new LoadingAnimation(getActivity());

        final EditText textField = (EditText) mView.findViewById(R.id.textField);
        textField.setText(LoginUserDao.getInstance(getActivity()).get().getNickName()
        );

        ((AppCompatActivity)getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.actionbar_right_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = textField.getText().toString().trim();

                if (newName.length() == 0) {
                    Utils.showErrorMessage(getActivity(), "昵称不能为空");
                    return;
                }

                SetNickNameRequest request = new SetNickNameRequest();
                request.setNickName(newName);

                new SetNickNameTask().execute(request);
            }
        });

        return mView;
    }

    private class SetNickNameTask extends AsyncTask<SetNickNameRequest, Void, SetNickNameResponse> {
        private  SetNickNameRequest mSetNickNameRequest;

        @Override
        protected SetNickNameResponse doInBackground(SetNickNameRequest... params) {
            mSetNickNameRequest = params[0];
            return new BasicService().sendRequest(params[0]);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoading.show("更新中...");
        }

        @Override
        protected void onPostExecute(SetNickNameResponse resp) {
            super.onPostExecute(resp);
            mLoading.hide();
            if (!resp.isSuccess()) {
                Utils.showErrorMessage(getActivity(), resp.getErrorMessage());
                return;
            }

            LoginUser user = LoginUserDao.getInstance(getActivity()).get();
            user.setNickName(mSetNickNameRequest.getNickName());
            LoginUserDao.getInstance(getActivity()).save(user);

            getActivity().finish();
        }
    }
}
