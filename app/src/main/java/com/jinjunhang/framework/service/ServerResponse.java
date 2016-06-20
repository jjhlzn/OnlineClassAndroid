package com.jinjunhang.framework.service;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lzn on 16/3/23.
 */

public abstract class ServerResponse {

    public final static int SUCCESS = 0;
    public final static int FAIL = -1;

    public ServerResponse() {}
    private int mStatus;
    private String mErrorMessage;

    public int getStatus() {
        return mStatus;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }

    public void setStatus(int status) {
        mStatus = status;
    }

    public void setErrorMessage(String errorMessage) {
        mErrorMessage = errorMessage;
    }

    public abstract void parse(ServerRequest request, JSONObject json) throws JSONException;
}



