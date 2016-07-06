package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;

import java.util.Map;

/**
 * Created by jjh on 2016-7-6.
 */
public class UpdateTokenRequest extends ServerRequest {

    private String mUserName;
    private String mPassword;

    public String getUserName() {
        return mUserName;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.UpdateTokenUrl();
    }

    @Override
    public Class getServerResponseClass() {
        return UpdateTokenResponse.class;
    }

    @Override
    public Map<String, Object> getParams() {
        Map<String, Object> params = super.getParams();
        params.put("username", mUserName);
        params.put("password", mPassword);
        return params;
    }
}
