package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;

import java.util.Map;

/**
 * Created by lzn on 16/6/28.
 */
public class ForgetPasswordRequest extends ServerRequest {

    private String mCheckCode;
    private String mPassword;
    private String mPhoneNumber;

    public String getCheckCode() {
        return mCheckCode;
    }

    public String getPassword() {
        return mPassword;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setCheckCode(String checkCode) {
        mCheckCode = checkCode;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public void setPhoneNumber(String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }

    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.ForgetPasswordUrl();
    }

    @Override
    public Class getServerResponseClass() {
        return ForgetPasswordResponse.class;
    }

    @Override
    public Map<String, Object> getParams() {
        Map<String, Object> params = super.getParams();
        params.put("checkCode", mCheckCode);
        params.put("password", mPassword);
        params.put("phoneNumber", mPhoneNumber);
        return params;
    }
}
