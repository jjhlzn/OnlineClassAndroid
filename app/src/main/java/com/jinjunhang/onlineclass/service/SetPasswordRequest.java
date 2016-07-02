package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;
import com.jinjunhang.onlineclass.model.ServiceLinkManager;

import java.util.Map;
import java.util.Objects;

/**
 * Created by jjh on 2016-7-2.
 */
public class SetPasswordRequest extends ServerRequest {

    private String  mOldPassword;
    private String  mNewPassword;

    public String getmOldPassword() {
        return mOldPassword;
    }

    public String getmNewPassword() {
        return mNewPassword;
    }

    public void setmOldPassword(String mOldPassword) {
        this.mOldPassword = mOldPassword;
    }

    public void setmNewPassword(String mNewPassword) {
        this.mNewPassword = mNewPassword;
    }

    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.ResetPassword();
    }

    @Override
    public Class getServerResponseClass() {
        return SetPasswordResponse.class;
    }

    @Override
    public Map<String, Object> getParams() {
        Map<String, Object> params = super.getParams();
        params.put("oldPassword", mOldPassword);
        params.put("newPassword", mNewPassword);
        return params;
    }
}
