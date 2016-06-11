package com.jinjunhang.framework.service;

import com.jinjunhang.framework.service.ServerRequest;
import com.jinjunhang.framework.service.ServerResponse;
import com.jinjunhang.onlineclass.model.BaseModelObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by lzn on 16/6/10.
 */
public abstract class PagedServerResponse<T extends BaseModelObject> extends ServerResponse {

    private int mTotalNumber;
    protected List<T> mResultSet;

    public int getTotalNumber() {
        return mTotalNumber;
    }

    public List<T> getResultSet() {
        return mResultSet;
    }

    public void setTotalNumber(int totalNumber) {
        mTotalNumber = totalNumber;
    }

    public void setResultSet(List<T> resultSet) {
        mResultSet = resultSet;
    }


    public void parse(ServerRequest request, JSONObject json) throws JSONException {
        if (getStatus() == ServerResponse.SUCCESS) {
            mTotalNumber = json.getInt("totalNumber");
        }
    }

}
