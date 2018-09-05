package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.PagedServerRequest;
import com.jinjunhang.onlineclass.model.AlbumType;

import java.util.Map;

/**
 * Created by lzn on 16/6/10.
 */
public class GetPagedQuestionsRequest extends PagedServerRequest {

    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.GetPagedQuestionsUrl();
    }

    @Override
    public Map<String, Object> getParams() {
        Map<String, Object> params = super.getParams();
        return params;
    }

    @Override
    public Class getServerResponseClass() {
        return GetPagedQuestionsResponse.class;
    }

}
