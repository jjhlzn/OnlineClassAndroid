package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;

import java.util.Map;
import java.util.Objects;

/**
 * Created by jjh on 2016-7-1.
 */
public class SetNameRequest extends ServerRequest {

    private String mNewName;

    public String getNewName() {
        return mNewName;
    }

    public void setNewName(String newName) {
        mNewName = newName;
    }

    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.SetNameUrl();
    }

    @Override
    public Class getServerResponseClass() {
        return SetNameResponse.class;
    }

    @Override
    public Map<String, Object> getParams() {
        Map<String, Object> params = super.getParams();
        params.put("newName", mNewName);
        return params;
    }
}
