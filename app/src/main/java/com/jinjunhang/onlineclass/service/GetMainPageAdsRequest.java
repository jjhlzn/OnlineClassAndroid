package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;

/**
 * Created by jjh on 2016-7-6.
 */
public class GetMainPageAdsRequest extends ServerRequest {

    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.GetMainPageAdsUrl();
    }

    @Override
    public Class getServerResponseClass() {
        return GetMainPageAdsResponse.class;
    }
}
