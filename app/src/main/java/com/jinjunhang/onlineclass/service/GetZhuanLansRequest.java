package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;

public class GetZhuanLansRequest extends ServerRequest {
    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.GetZhuanLansUrl();
    }

    @Override
    public Class getServerResponseClass() {
        return GetZhuanLansResponse.class;
    }
}
