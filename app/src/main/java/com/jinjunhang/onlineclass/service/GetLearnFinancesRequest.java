package com.jinjunhang.onlineclass.service;


import com.jinjunhang.framework.service.ServerRequest;

public class GetLearnFinancesRequest extends ServerRequest {

    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.GetLearnFinancesUrl();
    }

    @Override
    public Class getServerResponseClass() {
        return GetLearnFinancesResponse.class;
    }
}
