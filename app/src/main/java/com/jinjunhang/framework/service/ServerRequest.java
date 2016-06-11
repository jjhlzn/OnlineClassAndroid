package com.jinjunhang.framework.service;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by lzn on 16/6/10.
 */
public abstract class ServerRequest {

    public abstract String getServiceUrl();
    public Map<String, Object> getParams() {
        Map<String, Object> params = new LinkedHashMap<>();

        return params;
    }
    public abstract Class getServerResponseClass();

}
