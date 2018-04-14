package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.PagedServerRequest;

import java.util.Map;

/**
 * Created by lzn on 16/6/29.
 */
public class SearchRequest extends PagedServerRequest {

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
        return SearchResponse.class;
    }

    @Override
    public Map<String, Object> getParams() {
        Map<String, Object> params = super.getParams();
        params.put("keyword", mKeyword);
        return params;
    }
}
