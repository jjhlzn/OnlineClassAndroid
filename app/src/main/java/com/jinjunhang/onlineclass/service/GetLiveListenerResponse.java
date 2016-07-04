package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;
import com.jinjunhang.framework.service.ServerResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jjh on 2016-7-4.
 */
public class GetLiveListenerResponse extends ServerResponse {

    private int mListernerCount;


    public int getListernerCount() {
        return mListernerCount;
    }

    public void setListernerCount(int listernerCount) {
        mListernerCount = listernerCount;
    }

    @Override
    public void parse(ServerRequest request, JSONObject json) throws JSONException {
        mListernerCount = json.getInt("listerCount");
    }
}
