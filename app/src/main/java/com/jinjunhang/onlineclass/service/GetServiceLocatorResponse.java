package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;
import com.jinjunhang.framework.service.ServerResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lzn on 16/7/19.
 */
public class GetServiceLocatorResponse extends ServerResponse {

    private String mHttp;
    private String mServerName;
    private String mPort;

    public String getHttp() {
        return mHttp;
    }

    public String getServerName() {
        return mServerName;
    }

    public String getPort() {
        return mPort;
    }

    public void setHttp(String http) {
        mHttp = http;
    }

    public void setServerName(String serverName) {
        mServerName = serverName;
    }

    public void setPort(String port) {
        mPort = port;
    }

    @Override
    public void parse(ServerRequest request, JSONObject jsonObject) throws JSONException {
        JSONObject json = jsonObject.getJSONObject("result");
        mHttp = json.getString("http");
        mServerName = json.getString("serverName");
        mPort = json.getString("port");
    }
}
