package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;

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
}
