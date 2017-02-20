package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;

/**
 * Created by jinjunhang on 17/2/20.
 */

public class GetCourseNotifyRequest extends ServerRequest {

    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.GetCourseNotifyUrl();
    }

    @Override
    public Class getServerResponseClass() {
        return GetCourseNotifyResponse.class;
    }
}
