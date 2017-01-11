package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jinjunhang on 17/1/7.
 */

public class ClearFunctionMessageRequest extends ServerRequest {

    private List<String> mCodes = new ArrayList<>();

    public void setCodes(List<String> codes) {
        this.mCodes = codes;
    }

    public void setCodes(String[] codes) {
        for (String code : codes) {
            mCodes.add(code);
        }
    }

    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.ClearFunctionMessageUrl();
    }

    @Override
    public Class getServerResponseClass() {
        return ClearFunctionMessageResponse.class;
    }

    @Override
    public Map<String, Object> getParams() {
        Map<String, Object> params = super.getParams();
        params.put("codes", mCodes.toArray());
        return params;
    }
}
