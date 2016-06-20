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
public class GetAdsResponse extends ServerResponse {

    private List<Advertise> mAdvertises;

    public List<Advertise> getAdvertises() {
        return mAdvertises;
    }

    public GetAdsResponse() {
        mAdvertises = new ArrayList<>();
    }

    public void parse(ServerRequest request, JSONObject jsonObject) throws JSONException {
        JSONArray adsJson = jsonObject.getJSONArray("ads");
        for(int i = 0; i < adsJson.length(); i++) {
            JSONObject json = adsJson.getJSONObject(i);
            Advertise adv = new Advertise();
            adv.setTitle(json.getString("title"));
            adv.setClickUrl(json.getString("clickUrl"));
            adv.setImageUrl(json.getString("imageUrl"));
            mAdvertises.add(adv);
        }
    }
}
