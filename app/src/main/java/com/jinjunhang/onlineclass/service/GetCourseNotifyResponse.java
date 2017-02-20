package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;
import com.jinjunhang.framework.service.ServerResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jinjunhang on 17/2/20.
 */

public class GetCourseNotifyResponse extends ServerResponse {

    private List<String> mCourseNotifies = new ArrayList<>();

    public List<String> getCourseNotifies() {
        return mCourseNotifies;
    }

    @Override
    public void parse(ServerRequest request, JSONObject json) throws JSONException {
        JSONArray array = json.getJSONArray("notifies");
        for (int i = 0; i < array.length(); i++) {
            mCourseNotifies.add(array.getString(i));
        }
    }
}
