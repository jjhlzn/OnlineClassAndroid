package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;
import com.jinjunhang.framework.service.ServerResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzn on 16/6/28.
 */
public class GetHotSearchWordsResponse extends ServerResponse {

    private List<String> mKeywords;

    public List<String> getKeywords() {
        return mKeywords;
    }

    public void setKeywords(List<String> keywords) {
        mKeywords = keywords;
    }

    public GetHotSearchWordsResponse() {
        mKeywords = new ArrayList<>();
    }

    @Override
    public void parse(ServerRequest request, JSONObject jsonObject) throws JSONException {

        JSONArray array = jsonObject.getJSONArray("keywords");
        for (int i = 0; i < array.length(); i++) {
            mKeywords.add(array.getString(i));
        }
    }
}
