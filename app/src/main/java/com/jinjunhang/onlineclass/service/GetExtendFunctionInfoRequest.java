package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;

/**
 * Created by jinjunhang on 17/5/15.
 */

public class GetExtendFunctionInfoRequest extends ServerRequest {
    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.GetExtendFunctionInfoUrl();
    }

    @Override
    public Class getServerResponseClass() {
        return GetExtendFunctionInfoResponse.class;
    }
}


