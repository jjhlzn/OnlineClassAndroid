package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;
import com.jinjunhang.framework.service.ServerResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jinjunhang on 17/6/20.
 */

public class GetLaunchAdvResponse extends ServerResponse {

    private String mImageUrl = "";
    private String mAdvUrl = "";
    private String mAdvTitle = "";

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getAdvUrl() {
        return mAdvUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public void setAdvUrl(String advUrl) {
        mAdvUrl = advUrl;
    }

    public String getAdvTitle() {
        return mAdvTitle;
    }

    public void setAdvTitle(String advTitle) {
        mAdvTitle = advTitle;
    }

    @Override
    public void parse(ServerRequest request, JSONObject jsonObject) throws JSONException {
        JSONObject json = jsonObject.getJSONObject("result");
        setAdvUrl(json.getString("advUrl"));
        setImageUrl(json.getString("imageUrl"));
        setAdvTitle(json.getString("advTitle"));
    }
}
