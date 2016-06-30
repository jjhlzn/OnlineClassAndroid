package com.jinjunhang.onlineclass.ui.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jinjunhang.framework.lib.LoadingAnimation;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.framework.service.ServerResponse;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.db.LoginUserDao;
import com.jinjunhang.onlineclass.service.LogoutRequest;
import com.jinjunhang.onlineclass.service.LogoutResponse;
import com.jinjunhang.onlineclass.ui.activity.user.LoginActivity;

/**
 * Created by lzn on 16/6/28.
 */
public class SettingsFragment extends android.support.v4.app.Fragment  {

    private LoadingAnimation mLoading;
    private LoginUserDao mLoginUserDao;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_fragment_settings, container, false);

        mLoginUserDao = new LoginUserDao(getActivity());
        mLoading = new LoadingAnimation(getActivity());

        Button logoutButton = (Button) v.findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LogoutTask().execute();
            }
        });

        return v;
    }

    private class LogoutTask extends AsyncTask<Void, Void, LogoutResponse> {
        @Override
        protected LogoutResponse doInBackground(Void... params) {
            LogoutRequest req = new LogoutRequest();
            return new BasicService().sendRequest(req);
        }

        @Override
        protected void onPostExecute(LogoutResponse logoutResponse) {
            super.onPostExecute(logoutResponse);
            if (logoutResponse.getStatus() != ServerResponse.SUCCESS) {
                mLoading.hide();
                Utils.showMessage(getActivity(), logoutResponse.getErrorMessage());
                return;
            }
            mLoginUserDao.deleteAll();
            Intent i = new Intent(getActivity(), LoginActivity.class);
            startActivity(i);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoading.show("退出中");
        }
    }
}
