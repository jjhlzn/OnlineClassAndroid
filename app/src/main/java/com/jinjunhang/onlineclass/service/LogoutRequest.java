package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;

/**
 * Created by lzn on 16/6/28.
 */
public class LogoutRequest extends ServerRequest {

    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.LogoutUrl();
    }

    @Override
    public Class getServerResponseClass() {
        return LogoutResponse.class;
    }
}
