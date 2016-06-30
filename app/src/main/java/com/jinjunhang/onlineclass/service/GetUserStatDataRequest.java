package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;

/**
 * Created by jjh on 2016-6-30.
 */
public class GetUserStatDataRequest extends ServerRequest {
    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.GetUserStatData();
    }

    @Override
    public Class getServerResponseClass() {
        return GetUserStatDataResponse.class;
    }
}
