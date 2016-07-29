package com.jinjunhang.framework.service;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by lzn on 16/6/10.
 */
public abstract class ServerRequest {

    private boolean isResendForInvalidToken = false;
    private String mTest;
    private int  connectionTimeout = 5; //in seconds
    private int  writeTimeout = 10; //in seconds
    private int  readTimeout = 10; //in seconds

    public String getTest() {
        return mTest;
    }

    public void setTest(String test) {
        mTest = test;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public int getWriteTimeout() {
        return writeTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public void setWriteTimeout(int writeTimeout) {
        this.writeTimeout = writeTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
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
