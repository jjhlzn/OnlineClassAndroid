package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;
import com.jinjunhang.framework.service.ServerResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lzn on 16/7/29.
 */
public class GetParameterInfoResponse extends ServerResponse{

    public final static String LIVE_DESCRIPTON = "livedescription";
    public final static String PAY_DESCRIPTON = "vipdescription";
    public final static String BEFORE_DESCRIPTON = "beforedescription";
    public final static String LIVE_COURSE_NAME = "liveCourseName";
    public final static String PAY_COURSE_NAME = "payCourseName";

    private Map<String, String> mMap = new HashMap<>();


    public String getValue(String key, String defaultValue) {
        if (!mMap.containsKey(key)) {
            return defaultValue;
        }
        return mMap.get(key);
    }

    @Override
    public void parse(ServerRequest request, JSONObject jsonObject) throws JSONException {
        JSONArray jsonArray = jsonObject.getJSONArray("result");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            String key = json.getString("keyword");
            String value = json.getString("value");
            mMap.put(key, value);
        }
    }
}
