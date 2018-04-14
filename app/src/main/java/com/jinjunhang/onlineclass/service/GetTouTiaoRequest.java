package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;

/**
 * Created by jjh on 2016-7-6.
 */
public class GetTouTiaoRequest extends ServerRequest {

    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.GetTouTiaoUrl();
    }

    @Override
    public Class getServerResponseClass() {
        return GetTouTiaoResponse.class;
    }
}
