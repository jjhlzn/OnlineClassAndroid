package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;

/**
 * Created by lzn on 16/7/19.
 */
public class GetServiceLocatorRequest extends ServerRequest {

    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.GetServiceLocatorUrl();
    }

    @Override
    public Class getServerResponseClass() {
        return GetServiceLocatorResponse.class;
    }
}
