package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;

/**
 * Created by jinjunhang on 17/6/20.
 */

public class GetLaunchAdvRequest extends ServerRequest {
    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.GetLaunchAdvUrl();
    }

    @Override
    public Class getServerResponseClass() {
        return GetLaunchAdvResponse.class;
    }
}
