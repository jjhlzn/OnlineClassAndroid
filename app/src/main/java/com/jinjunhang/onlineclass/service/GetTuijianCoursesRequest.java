package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.PagedServerRequest;
import com.jinjunhang.framework.service.ServerRequest;
import com.jinjunhang.onlineclass.model.Album;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by lzn on 16/6/12.
 */
public class GetTuijianCoursesRequest extends ServerRequest {


    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.GetTuijianCoursesUrl();
    }

    @Override
    public Class getServerResponseClass() {
        return GetTuijianCoursesResponse.class;
    }



}
