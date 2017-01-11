package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;

/**
 * Created by jinjunhang on 17/1/8.
 */

public class GetFooterAdvsRequest extends ServerRequest {

    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.GetFootAdvUrl();
    }

    @Override
    public Class getServerResponseClass() {
        return GetFooterAdvsResponse.class;
    }
}
