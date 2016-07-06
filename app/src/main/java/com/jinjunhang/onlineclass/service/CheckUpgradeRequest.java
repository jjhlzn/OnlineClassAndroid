package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;

/**
 * Created by jjh on 2016-7-6.
 */
public class CheckUpgradeRequest extends ServerRequest {

    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.CheckUpgradeUrl();
    }

    @Override
    public Class getServerResponseClass() {
        return CheckUpgradeResponse.class;
    }
}
