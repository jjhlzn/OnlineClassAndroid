package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;

import java.util.Map;

public class BindPhoneRequest extends ServerRequest {

    private String mNewPhone;
    private String mCode;

    public void setNewPhone(String newPhone) {
        mNewPhone = newPhone;
    }

    public String getNewPhone() {
        return mNewPhone;
    }

    public void setCode(String code) {
        mCode = code;
    }

    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.BindPhoneUrl();
    }

    @Override
    public Class getServerResponseClass() {
        return BindPhoneResponse.class;
    }

    @Override
    public Map<String, Object> getParams() {
        Map<String, Object> params = super.getParams();
        params.put("newphone", mNewPhone);
        params.put("code", mCode);

        return params;
    }
}
