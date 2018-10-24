package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;

public class BindWeixinRequest extends OAuthRequest {

    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.BindWeixinUrl();
    }

    @Override
    public Class getServerResponseClass() {
        return BindWeixinResponse.class;
    }
}
