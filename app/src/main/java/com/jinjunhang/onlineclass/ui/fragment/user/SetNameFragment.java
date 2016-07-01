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
import com.jinjunhang.onlineclass.service.SetNameRequest;
import com.jinjunhang.onlineclass.service.SetNameResponse;
import com.jinjunhang.onlineclass.ui.fragment.BaseFragment;

/**
 * Created by jjh on 2016-7-1.
 */
public class SetNameFragment extends BaseFragment {

    private LoadingAnimation mLoading;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_fragment_set_name_or_nickname, container, false);
        mLoading = new LoadingAnimation(getActivity());

        final EditText textField = (EditText) v.findViewById(R.id.textField);
        textField.setText(LoginUserDao.getInstance(getActivity()).get().getName());

        ((AppCompatActivity)getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.actionbar_right_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = textField.getText().toString().trim();

                if (newName.length() == 0) {
                    Utils.showErrorMessage(getActivity(), "姓名不能为空");
                    return;
                }

                SetNameRequest request = new SetNameRequest();
                request.setNewName(newName);

                new SetNameTask().execute(request);
            }
        });

        return v;
    }

    private class SetNameTask extends AsyncTask<SetNameRequest, Void, SetNameResponse> {
        private  SetNameRequest mSetNameRequest;

        @Override
        protected SetNameResponse doInBackground(SetNameRequest... params) {
            mSetNameRequest = params[0];
            return new BasicService().sendRequest(params[0]);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoading.show("更新中...");
        }

        @Override
        protected void onPostExecute(SetNameResponse resp) {
            super.onPostExecute(resp);
            mLoading.hide();
            if (!resp.isSuccess()) {
                Utils.showErrorMessage(getActivity(), resp.getErrorMessage());
                return;
            }

            LoginUser user = LoginUserDao.getInstance(getActivity()).get();
            user.setName(mSetNameRequest.getNewName());
            LoginUserDao.getInstance(getActivity()).save(user);

            getActivity().finish();
        }
    }
}
