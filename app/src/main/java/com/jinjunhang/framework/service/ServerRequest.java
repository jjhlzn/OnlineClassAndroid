package com.jinjunhang.framework.service;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by lzn on 16/6/10.
 */
public abstract class ServerRequest {

    private boolean isResendForInvalidToken = false;
    private String mTest;

    public String getTest() {
        return mTest;
    }

    public void setTest(String test) {
        mTest = test;
    }

    public boolean isResendForInvalidToken() {
        return isResendForInvalidToken;
    }

    public void setResendForInvalidToken(boolean resendForInvalidToken) {
        isResendForInvalidToken = resendForInvalidToken;
    }

    public abstract String getServiceUrl();
    public Map<String, Object> getParams() {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("test", mTest);
        return params;
    }
    public abstract Class getServerResponseClass();

}
