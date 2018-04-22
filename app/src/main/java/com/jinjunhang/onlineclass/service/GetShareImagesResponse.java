package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;
import com.jinjunhang.framework.service.ServerResponse;
import com.jinjunhang.onlineclass.model.Advertise;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzn on 16/6/20.
 */
public class GetShareImagesResponse extends ServerResponse {

    private List<String> mShareImages;

    public List<String> getShareImageUrls() {
        return mShareImages;
    }

    public GetShareImagesResponse() {
        mShareImages = new ArrayList<>();
    }

    public void parse(ServerRequest request, JSONObject jsonObject) throws JSONException {
        JSONArray adsJson = jsonObject.getJSONArray("urls");
        for(int i = 0; i < adsJson.length(); i++) {
            String url = adsJson.getString(i);
            mShareImages.add(url);
        }
    }
}
