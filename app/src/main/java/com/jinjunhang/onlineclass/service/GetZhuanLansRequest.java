package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;

import java.util.Map;

public class GetZhuanLansRequest extends ServerRequest {

    private String mType = "";

    public void setType(String type) {
        mType = type;
    }

    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.GetZhuanLansUrl();
    }

    @Override
    public Class getServerResponseClass() {
        return GetZhuanLansResponse.class;
    }

    @Override
    public Map<String, Object> getParams() {
        Map<String, Object> params = super.getParams();
        params.put("type", mType);
        return params;
    }
}
