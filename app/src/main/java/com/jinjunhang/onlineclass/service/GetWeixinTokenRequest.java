package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;

import java.util.Map;

public class GetWeixinTokenRequest extends ServerRequest {

    public String code;

    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.GetWeixinTokenUrl();
    }

    @Override
    public Class getServerResponseClass() {
        return GetWeixinTokenResponse.class;
    }

    @Override
    public Map<String, Object> getParams() {
        Map<String, Object> params = super.getParams();
        params.put("code", code);
        return params;
    }
}
