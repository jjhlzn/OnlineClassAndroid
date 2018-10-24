package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;

public class GetMessagesRequest extends ServerRequest {

    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.GetMessagesUrl();
    }

    @Override
    public Class getServerResponseClass() {
        return GetMessagesResponse.class;
    }
}
