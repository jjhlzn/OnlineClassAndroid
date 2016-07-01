package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;

import java.util.Map;

/**
 * Created by jjh on 2016-7-1.
 */
public class SetNickNameRequest extends ServerRequest {

    private String mNickName;

    public String getNickName() {
        return mNickName;
    }

    public void setNickName(String nickName) {
        mNickName = nickName;
    }

    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.SetNickNameUrl();
    }

    @Override
    public Class getServerResponseClass() {
        return SetNickNameResponse.class;
    }

    @Override
    public Map<String, Object> getParams() {
        Map<String, Object> params = super.getParams();
        params.put("newNickName", mNickName);
        return params;
    }
}
