package com.jinjunhang.onlineclass.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.onlineclass.db.LoginUserDao;
import com.jinjunhang.onlineclass.model.LoginUser;
import com.jinjunhang.onlineclass.service.OAuthRequest;
import com.jinjunhang.onlineclass.service.OAuthResponse;
import com.jinjunhang.onlineclass.ui.activity.mainpage.BottomTabLayoutActivity;
import com.jinjunhang.onlineclass.ui.activity.user.LoginActivity;
import com.jinjunhang.onlineclass.ui.activity.user.MobileLoginActivity;
import com.jinjunhang.onlineclass.ui.fragment.SettingsFragment;
import com.jinjunhang.onlineclass.ui.lib.WXConstants;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by lzn on 2016/10/8.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {



    private static String TAG = LogHelper.makeLogTag(WXEntryActivity.class);

    private IWXAPI api;
    private LoginUserDao mLoginUserDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_view);
        mLoginUserDao = LoginUserDao.getInstance(this);
        api = WXAPIFactory.createWXAPI(this, WXConstants.APPID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }


    public void onReq(BaseReq req) {

    }

    public void onResp(BaseResp resp) {
        LogHelper.d(TAG, "weixin type = " + resp.getType());
        LogHelper.d(TAG, "weixin errCode = " + resp.errCode);

        String message = "";

        switch (resp.getType()) {
            case ConstantsAPI.COMMAND_PAY_BY_WX:
                switch (resp.errCode) {
                    case BaseResp.ErrCode.ERR_OK:
                        message = "支付成功";
                        break;
                    default:
                        message = "支付失败";
                }
                LogHelper.d(TAG, "message = " + message);
                Utils.showMessage(this, message);

                finish();
                break;
           // case ConstantsAPI.COMMAND_
            case ConstantsAPI.COMMAND_SENDAUTH:

                if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
                    String code = ((SendAuth.Resp) resp).code;
                    LogHelper.d(TAG, "code = " + code);

                    if (mLoginUserDao.get() == null) {
                        LoginActivity.loginType = LoginActivity.WEIXIN_LOGIN;
                        LoginActivity.code = code;
                        MobileLoginActivity.loginType = LoginActivity.WEIXIN_LOGIN;
                        MobileLoginActivity.code = code;
                    } else {
                        SettingsFragment.loginType = LoginActivity.WEIXIN_LOGIN;
                        SettingsFragment.code = code;
                    }

                }
                finish();
                break;
        }
    }





}
