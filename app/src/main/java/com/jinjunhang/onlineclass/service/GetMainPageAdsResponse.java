package com.jinjunhang.onlineclass.service;

import com.google.gson.JsonObject;
import com.jinjunhang.framework.service.ServerRequest;
import com.jinjunhang.framework.service.ServerResponse;
import com.jinjunhang.onlineclass.model.Advertise;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jjh on 2016-7-6.
 */
public class GetMainPageAdsResponse extends ServerResponse {

    private List<Advertise> mAdvertises;
    private Advertise popupAd = new Advertise();

    public List<Advertise> getAdvertises() {
        return mAdvertises;
    }

    public GetMainPageAdsResponse() {
        mAdvertises = new ArrayList<>();
    }

    public Advertise getPopupAd() {
        return popupAd;
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

        JSONObject popupJson = jsonObject.getJSONObject("popupAd");
        if (popupJson.has("imageUrl")) {
            popupAd.setTitle(popupJson.getString("title"));
            popupAd.setClickUrl(popupJson.getString("clickUrl"));
            popupAd.setImageUrl(popupJson.getString("imageUrl"));

        }
    }

}
