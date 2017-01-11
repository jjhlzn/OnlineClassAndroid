package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.framework.service.ServerRequest;
import com.jinjunhang.framework.service.ServerResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;



/**
 * Created by jinjunhang on 17/1/7.
 */

public class GetFunctionMessageResponse extends ServerResponse {
    private static final String TAG = LogHelper.makeLogTag(GetFunctionMessageResponse.class);

    private Map<String, Integer> map = new HashMap<>();

    public Map<String, Integer> getMap() {
        return map;
    }

    @Override
    public void parse(ServerRequest request, JSONObject jsonObject) throws JSONException {
        JSONArray array = jsonObject.getJSONArray("result");
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject json = array.getJSONObject(i);
                map.put(json.getString("code"), json.getInt("value"));
            }
            catch (Exception ex) {
                LogHelper.d(TAG, ex);
            }
        }
    }
}
