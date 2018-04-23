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


    private String mType = "";

    public void setType(String type) {
        mType = type;
    }

    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.GetTuijianCoursesUrl();
    }

    @Override
    public Class getServerResponseClass() {
        return GetTuijianCoursesResponse.class;
    }


    @Override
    public Map<String, Object> getParams() {
        Map<String, Object> params = super.getParams();
        params.put("type", mType);
        return params;
    }

}
