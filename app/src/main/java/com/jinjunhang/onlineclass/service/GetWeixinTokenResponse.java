package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;
import com.jinjunhang.framework.service.ServerResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class GetWeixinTokenResponse extends ServerResponse {

    private String mResponseString;

    public String getResponseString() {
        return mResponseString;
    }

    @Override
    public void parse(ServerRequest request, JSONObject json) throws JSONException {
        mResponseString = json.getString("responseString");
    }
}
