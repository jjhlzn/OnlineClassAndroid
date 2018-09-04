package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;

public class GetFinanceToutiaoRequest extends ServerRequest {
    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.GetFinanceToutiaoUrl();
    }

    @Override
    public Class getServerResponseClass() {
        return GetFinanceToutiaoResponse.class;
    }
}
