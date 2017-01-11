package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;

/**
 * Created by jinjunhang on 17/1/8.
 */

public class GetHeaderAdvRequest extends ServerRequest {

    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.GetHeaderAdvUrl();
    }

    @Override
    public Class getServerResponseClass() {
        return GetHeaderAdvResponse.class;
    }
}
