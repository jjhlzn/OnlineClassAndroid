package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;

import java.util.Map;

/**
 * Created by lzn on 16/6/28.
 */
public class GetCheckCodeRequest extends ServerRequest {

    private String mPhoneNumber;

    public void setPhoneNumber(String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.GetPhoneCheckCodeUrl();
    }

    @Override
    public Class getServerResponseClass() {
        return GetCheckCodeResponse.class;
    }

    @Override
    public Map<String, Object> getParams() {
        Map<String, Object> params = super.getParams();
        params.put("phoneNumber", mPhoneNumber);
        return params;
    }
}
