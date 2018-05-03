package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by lzn on 16/6/20.
 */
public class GetCourseInfoRequest extends ServerRequest {

    private String mId;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.GetCourseInfoUrl();
    }

    @Override
    public Class getServerResponseClass() {
        return GetCourseInfoResponse.class;
    }

    @Override
    public Map<String, Object> getParams() {
        Map<String, Object> params = super.getParams();
        params.put("id", mId);
        return params;
    }
}
