package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;

/**
 * Created by lzn on 16/6/12.
 */
public class GetCoursesRequest extends ServerRequest {


    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.GetTuijianCoursesUrl();
    }

    @Override
    public Class getServerResponseClass() {
        return GetCoursesResponse.class;
    }



}
