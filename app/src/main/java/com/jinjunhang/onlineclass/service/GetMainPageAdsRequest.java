package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;

import java.util.Map;

/**
 * Created by jjh on 2016-7-6.
 */
public class GetMainPageAdsRequest extends ServerRequest {

    private String mType;

    public void setType(String type) {
        mType = type;
    }

    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.GetMainPageAdsUrl();
    }

    @Override
    public Class getServerResponseClass() {
        return GetMainPageAdsResponse.class;
    }


    @Override
    public Map<String, Object> getParams() {
        Map<String, Object> params = super.getParams();
        params.put("type", mType);
        return params;
    }
}
