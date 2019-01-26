package com.jinjunhang.onlineclass.ui.lib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.jinjunhang.framework.lib.LoadingAnimation;
import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.framework.wx.Util;
import com.jinjunhang.onlineclass.db.KeyValueDao;
import com.jinjunhang.onlineclass.db.LoginUserDao;
import com.jinjunhang.onlineclass.model.LoginUser;
import com.jinjunhang.onlineclass.service.BindWeixinRequest;
import com.jinjunhang.onlineclass.service.BindWeixinResponse;
import com.jinjunhang.onlineclass.service.GetWeixinTokenRequest;
import com.jinjunhang.onlineclass.service.GetWeixinTokenResponse;
import com.jinjunhang.onlineclass.service.OAuthRequest;
import com.jinjunhang.onlineclass.service.OAuthResponse;
import com.jinjunhang.onlineclass.ui.activity.mainpage.BottomTabLayoutActivity;
import com.jinjunhang.onlineclass.ui.activity.user.MobileLoginActivity;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONObject;

import java.io.IOException;

public class WeixinLoginManager {

    private static final String TAG = LogHelper.makeLogTag(WeixinLoginManager.class);

    private LoginUserDao mLoginUserDao;
    private KeyValueDao mKeyValueDao;
    protected IWXAPI api;
    private Activity mContext;
    private LoadingAnimation mLoadingAnimation;
    private BindWeixinSuccessInterface mListener;
    private String mDeviceToken;

    public WeixinLoginManager(Activity context) {
        mContext = context;
        mKeyValueDao= KeyValueDao.getInstance(context);
        mLoginUserDao = LoginUserDao.getInstance(context);
        api = WXAPIFactory.createWXAPI(context, Utils.WEIXIN_SHERE_APP_ID, true);
        api.registerApp(Utils.WEIXIN_SHERE_APP_ID);
    }

    public void loginStep1() {
        // send oauth request
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "";
        api.sendReq(req);
    }



    public void loginStep2(String code, LoadingAnimation loadingAnimation, String deviceToken) {
        this.mDeviceToken = deviceToken;
        mLoadingAnimation = loadingAnimation;
        final String url = String.format("https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code", WXConstants.APPID, WXConstants.SECRET, code);
        LogHelper.d(TAG, url);
        MobileLoginActivity.loginType = -1;
        new GetCodeTask().execute(code, "");
    }

    public void bindWexin(String code,  LoadingAnimation loadingAnimation, BindWeixinSuccessInterface listener) {
        mLoadingAnimation = loadingAnimation;
        mListener = listener;
        final String url = String.format("https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code"
                , WXConstants.APPID, WXConstants.SECRET, code);
        LogHelper.d(TAG, url);
        new GetCodeTask().execute(code, "bind");
    }

    private void hideLoading() {
        if (mLoadingAnimation != null) {
            ((Activity)mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLoadingAnimation.hide();
                }
            });
        }
    }

    private class GetCodeTask extends AsyncTask<String, Void, GetWeixinTokenResponse> {
        private boolean isBind;

        @Override
        protected GetWeixinTokenResponse doInBackground(String... args) {
            String code = args[0];
            isBind = args[1] == "bind";

            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLoadingAnimation.show("");
                }
            });

            GetWeixinTokenRequest getTokenReq = new GetWeixinTokenRequest();
            getTokenReq.code = code;
            return new BasicService().sendRequest(getTokenReq);

        }

        @Override
        protected void onPostExecute(GetWeixinTokenResponse getWeixinTokenResponse) {
            if (!getWeixinTokenResponse.isSuccess()) {
                hideLoading();
                return;
            }

            String resp = getWeixinTokenResponse.getResponseString();
            //解析结果
            try {
                JSONObject json = new JSONObject(resp);
                if (json.has("access_token")) {
                    String access_token = json.getString("access_token");
                    String openid = json.getString("openid");
                    String unionid = json.getString("unionid");

                    if (isBind) {
                        BindWeixinRequest req = new BindWeixinRequest();
                        req.setAccessToken(access_token);
                        req.setOpenId(openid);
                        req.setUnionId(unionid);
                        req.setRespStr(resp);
                        new BindWeixinTask().execute(req);
                    } else {
                        final OAuthRequest req = new OAuthRequest();
                        req.setAccessToken(access_token);
                        req.setOpenId(openid);
                        req.setUnionId(unionid);
                        req.setRespStr(resp);


                        if ( Util.isVirtualEmulator() || !"".equals(mDeviceToken)) {
                            req.setDeviceToken(mDeviceToken);
                            new OAuthTask().execute(req);
                        } else {
                            XGPushManager.registerPush(mContext, new XGIOperateCallback() {
                                @Override
                                public void onSuccess(Object o, int i) {
                                    Log.d(TAG, "register device succes, devicetoken = " + o.toString());
                                    mDeviceToken = o.toString();
                                    req.setDeviceToken(o.toString());
                                    new OAuthTask().execute(req);
                                }

                                @Override
                                public void onFail(Object o, int i, String s) {
                                    Log.d(TAG, "register device fail");
                                    hideLoading();
                                }
                            });
                        }

                    }

                } else {
                    LogHelper.e(TAG, "结果出错了");
                    hideLoading();
                }

            } catch (Exception ex) {
                LogHelper.e(TAG, ex);
                hideLoading();
            }


        }
    }


    private class OAuthTask extends AsyncTask<OAuthRequest, Void, OAuthResponse> {
        @Override
        protected OAuthResponse doInBackground(OAuthRequest... oAuthRequests) {
            OAuthResponse response = new BasicService().sendRequest(oAuthRequests[0]);


            if (response.isSuccess()) {

                LoginUser loginUser = new LoginUser(response);
                loginUser.setUserName(response.getUserId());
                loginUser.setPassword("loginwithweixin");

                mLoginUserDao.save(loginUser);

                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(mContext, BottomTabLayoutActivity.class);
                        mContext.startActivity(i);
                    }
                });

            } else {
                hideLoading();
            }

            return response;
        }
    }

    private class BindWeixinTask extends AsyncTask<BindWeixinRequest, Void, BindWeixinResponse> {
        @Override
        protected BindWeixinResponse doInBackground(BindWeixinRequest... requests) {
            final BindWeixinResponse response = new BasicService().sendRequest(requests[0]);

            hideLoading();
            if (response.isSuccess()) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mKeyValueDao.saveOrUpdate(KeyValueDao.KEY_USER_IS_BIND_WEIXIN, "1");
                        Toast.makeText(mContext, "微信绑定成功", Toast.LENGTH_SHORT).show();
                        if (mListener != null) {
                            mListener.onSuccess();
                        }
                    }
                });

            } else {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, response.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
            return response;
        }
    }


}
