package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;

/**
 * Created by lzn on 16/6/20.
 */
public class GetAdsRequest extends ServerRequest {

    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.GetAdsUrl();
    }

    @Override
    public Class getServerResponseClass() {
        return GetAdsResponse.class;
    }
}
