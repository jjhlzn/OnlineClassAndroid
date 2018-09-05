package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;

/**
 * Created by jjh on 2016-7-6.
 */
public class GetQuestionRequest extends ServerRequest {

    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.GetQuestionUrl();
    }

    @Override
    public Class getServerResponseClass() {
        return GetQuestionResponse.class;
    }
}
