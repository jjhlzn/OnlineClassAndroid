package com.jinjunhang.onlineclass.ui.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jinjunhang.framework.lib.LoadingAnimation;
import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.framework.service.ServerResponse;
import com.jinjunhang.onlineclass.BuildConfig;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.db.KeyValueDao;
import com.jinjunhang.onlineclass.db.LoginUserDao;
import com.jinjunhang.onlineclass.service.LogoutRequest;
import com.jinjunhang.onlineclass.service.LogoutResponse;
import com.jinjunhang.onlineclass.ui.activity.user.LoginActivity;
import com.jinjunhang.onlineclass.ui.activity.user.SetPasswordActivity;
import com.jinjunhang.onlineclass.ui.activity.user.SetPhoneActivity;
import com.jinjunhang.onlineclass.ui.lib.BaseListViewOnItemClickListener;
import com.jinjunhang.onlineclass.ui.lib.BindWeixinSuccessInterface;
import com.jinjunhang.onlineclass.ui.lib.WeixinLoginManager;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jinjunhang.onlineclass.ui.activity.user.LoginActivity.WEIXIN_LOGIN;

/**
 * Created by lzn on 16/6/28.
 */
public class SettingsFragment extends BaseFragment  {

    private static final String TAG = LogHelper.makeLogTag(SettingsFragment.class);

    public static int loginType = -1;
    public static String code = "";
    private WeixinLoginManager mWXLoginManager;

    private LoadingAnimation mLoading;
    private LoginUserDao mLoginUserDao;
    private KeyValueDao mKeyValueDao;

    @BindView(R.id.bindWeixinBtn) TextView mBindWeixinBtn;
    @OnClick(R.id.bindWeixinBtn) void bindWeixinClick() {
        mWXLoginManager.loginStep1();
    }

    @BindView(R.id.phoneText) TextView mPhoneText;
    @BindView(R.id.bindPhoneBtn) TextView mBindPhoneBtn;
    @OnClick(R.id.bindPhoneBtn) void bindPhoneClick() {
        Intent i = new Intent(getActivity(), SetPhoneActivity.class);
        startActivity(i);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_fragment_settings;
    }

    @Override
    public void onResume() {
        super.onResume();

        LogHelper.d(TAG, "onResume called");
        if (loginType == WEIXIN_LOGIN) {
            LogHelper.d(TAG, "code: " + code);
            loginType = -1;

            mWXLoginManager.bindWexin(code, mLoading, new BindWeixinSuccessInterface() {
                @Override
                public void onSuccess() {
                    mBindWeixinBtn.setText("重新绑定");
                }
            });
        }


        if (mKeyValueDao.getValue(KeyValueDao.KEY_USER_IS_BIND_WEIXIN, "0").equals("1")) {
            mBindWeixinBtn.setText("重新绑定");
            mBindWeixinBtn.setTextColor(getActivity().getResources().getColor(R.color.price_color));
        } else {
            mBindWeixinBtn.setText("尚未绑定");
            mBindWeixinBtn.setTextColor(getActivity().getResources().getColor(R.color.gray_text));
        }

        if (mKeyValueDao.hasBindPhone()) {
            mPhoneText.setText("手机号码 ("+ mLoginUserDao.get().getUserName()+")");
            mBindPhoneBtn.setText("重新绑定");
            mBindPhoneBtn.setTextColor(getActivity().getResources().getColor(R.color.price_color));

        } else {
            mPhoneText.setText("手机号码");
            mBindPhoneBtn.setText("尚未绑定");
            mBindPhoneBtn.setTextColor(getActivity().getResources().getColor(R.color.gray_text));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, v);

        mWXLoginManager = new WeixinLoginManager(getActivity());
        mKeyValueDao = KeyValueDao.getInstance(getActivity());
        mLoginUserDao = new LoginUserDao(getActivity());
        mLoading = new LoadingAnimation(getActivity());

        View setPassword = v.findViewById(R.id.set_password_button);
        setPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseListViewOnItemClickListener.onItemClickEffect(null,v, 0, 0);
                Intent i = new Intent(getActivity(), SetPasswordActivity.class);
                startActivity(i);
            }
        });

        TextView versionLabel = (TextView) v.findViewById(R.id.version_label);
        versionLabel.setText("版本号         " +  BuildConfig.VERSION_NAME + "." + BuildConfig.VERSION_CODE);

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
            getActivity().finish();

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoading.show("退出中");
        }
    }
}
