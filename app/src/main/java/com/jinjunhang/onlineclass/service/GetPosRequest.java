package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;

/**
 * Created by lzn on 16/6/20.
 */
public class GetPosRequest extends ServerRequest {

    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.GetPos();
    }

    @Override
    public Class getServerResponseClass() {
        return GetPosResponse.class;
    }
}
