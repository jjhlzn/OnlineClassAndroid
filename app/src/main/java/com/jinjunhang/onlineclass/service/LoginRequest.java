package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;

import java.util.Map;

/**
 * Created by lzn on 16/6/27.
 */
public class LoginRequest extends ServerRequest {

    private String mUserName;
    private String mPassword;
    private String mDeviceToken;

    public String getUserName() {
        return mUserName;
    }

    public String getPassword() {
        return mPassword;
    }

    public String getDeviceToken() {
        return mDeviceToken;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public void setDeviceToken(String deviceToken) {
        mDeviceToken = deviceToken;
    }

    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.LoginUrl();
    }

    @Override
    public Class getServerResponseClass() {
        return LoginResponse.class;
    }

    @Override
    public Map<String, Object> getParams() {
        Map<String, Object> params = super.getParams();
        params.put("username", mUserName);
        params.put("password", mPassword);
        params.put("deviceToken", mDeviceToken);
        return params;
    }
}
