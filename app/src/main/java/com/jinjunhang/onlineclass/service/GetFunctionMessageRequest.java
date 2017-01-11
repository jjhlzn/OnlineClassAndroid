package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;

/**
 * Created by jinjunhang on 17/1/7.
 */

public class GetFunctionMessageRequest extends ServerRequest {
    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.GetFunctionMessageUrl();
    }

    @Override
    public Class getServerResponseClass() {
        return GetFunctionMessageResponse.class;
    }
}
