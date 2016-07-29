package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lzn on 16/7/29.
 */
public class GetParameterInfoRequest extends ServerRequest {

    private List<String> mKeywords = new ArrayList<>();

    public List<String> getKeywords() {
        return mKeywords;
    }

    public void setKeywords(List<String> keywords) {
        mKeywords = keywords;
    }

    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.GetParameterInfoUrl();
    }

    @Override
    public Class getServerResponseClass() {
        return GetParameterInfoResponse.class;
    }

    @Override
    public Map<String, Object> getParams() {
        Map<String, Object> params = super.getParams();
        params.put("keywords", mKeywords.toArray());
        return params;
    }
}
