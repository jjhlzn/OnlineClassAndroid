package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;

import java.util.Map;

public class OAuthRequest extends ServerRequest {

    private String mAccessToken;
    private String mRefreshToken;
    private String mOpenId;
    private String mUnionId;
    private String mRespStr;
    private String mDeviceToken;

    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.OAuthUrl();
    }

    @Override
    public Class getServerResponseClass() {
        return OAuthResponse.class;
    }

    public void setAccessToken(String accessToken) {
        mAccessToken = accessToken;
    }

    public void setFefreshToken(String fefreshToken) {
        mRefreshToken = fefreshToken;
    }

    public void setOpenId(String openId) {
        mOpenId = openId;
    }

    public void setUnionId(String unionId) {
        mUnionId = unionId;
    }

    public void setRespStr(String respStr) {
        mRespStr = respStr;
    }

    public void setDeviceToken(String deviceToken) {
        mDeviceToken = deviceToken;
    }

    @Override
    public Map<String, Object> getParams() {
        Map<String, Object> params = super.getParams();
        params.put("access_token", mAccessToken);
        params.put("refresh_token", mRefreshToken);
        params.put("openid", mOpenId);
        params.put("unionid", mUnionId);
        params.put("respStr", mRespStr);
        params.put("deviceToken", mDeviceToken);
        return params;
    }
}
