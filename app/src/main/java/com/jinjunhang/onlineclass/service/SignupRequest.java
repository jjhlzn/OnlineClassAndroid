package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;

import java.util.Map;

/**
 * Created by lzn on 16/6/28.
 */
public class SignupRequest extends ServerRequest {

    private String mPhoneNumber;
    private String mCheckCode;
    private String mInvitePhone;
    private String mPassword;

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public String getCheckCode() {
        return mCheckCode;
    }

    public String getInvitePhone() {
        return mInvitePhone;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPhoneNumber(String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }

    public void setCheckCode(String checkCode) {
        mCheckCode = checkCode;
    }

    public void setInvitePhone(String invitePhone) {
        mInvitePhone = invitePhone;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.SingupUrl();
    }

    @Override
    public Class getServerResponseClass() {
        return SignupResponse.class;
    }

    @Override
    public Map<String, Object> getParams() {
        Map<String, Object> params = super.getParams();
        params.put("phoneNumber", mPhoneNumber);
        params.put("checkCode", mCheckCode);
        params.put("password",mPassword);
        params.put("invitePhone", mInvitePhone);
        return params;
    }
}
