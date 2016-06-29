package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.PagedServerRequest;
import com.jinjunhang.framework.service.ServerRequest;

import java.util.Map;

/**
 * Created by lzn on 16/6/29.
 */
public class SearchCourseRequest extends PagedServerRequest {

    private String mKeyword;

    public String getKeyword() {
        return mKeyword;
    }

    public void setKeyword(String keyword) {
        mKeyword = keyword;
    }

    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.SearchCourseUrl();
    }

    @Override
    public Class getServerResponseClass() {
        return SearchCourseResponse.class;
    }

    @Override
    public Map<String, Object> getParams() {
        Map<String, Object> params = super.getParams();
        params.put("keyword", mKeyword);
        return params;
    }
}
