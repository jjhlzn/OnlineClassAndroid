package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;

public class GetZhuanLanAndTuijianCourseRequest extends ServerRequest {
    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.GetZhuanLanAndTuijianCoursesUrl();
    }

    @Override
    public Class getServerResponseClass() {
        return GetZhuanLanAndTuijianCourseResponse.class;
    }
}

