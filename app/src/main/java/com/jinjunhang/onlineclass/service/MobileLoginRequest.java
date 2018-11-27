package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;

import java.util.Map;

public class MobileLoginRequest extends ServerRequest {

    private String mMobile;
    private String mCheckCode;
    private String mDeviceToken;

    public void setMobile(String mobile) {
        mMobile = mobile;
    }

    public void setCheckCode(String checkCode) {
        mCheckCode = checkCode;
    }

    public void setDeviceToken(String deviceToken) {
        mDeviceToken = deviceToken;
    }

    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.MobileLoginUrl();
    }

    @Override
    public Class getServerResponseClass() {
        return MobileLoginResponse.class;
    }


    @Override
    public Map<String, Object> getParams() {
        Map<String, Object> params = super.getParams();
        params.put("mobile", mMobile);
        params.put("checkCode", mCheckCode);
        params.put("deviceToken", mDeviceToken);
        return params;
    }
}
