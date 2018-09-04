package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;
import com.jinjunhang.framework.service.ServerResponse;
import com.jinjunhang.onlineclass.model.FinanceToutiao;
import com.jinjunhang.onlineclass.model.ZhuanLan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetFinanceToutiaoResponse extends ServerResponse{
    private List<FinanceToutiao> mFinanceToutiaos = new ArrayList<>();

    public List<FinanceToutiao> getToutiaos() {
        return mFinanceToutiaos;
    }

    @Override
    public void parse(ServerRequest request, JSONObject jsonObject) throws JSONException {
        JSONArray jsonArray = jsonObject.getJSONArray("toutiaos");
        for(int i = 0 ; i < jsonArray.length(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            FinanceToutiao toutiao = new FinanceToutiao();
            toutiao.setIndex(json.getInt("index"));
            toutiao.setTitle(json.getString("title"));
            toutiao.setContent(json.getString("content"));
            toutiao.setLink(json.getString("link"));

            mFinanceToutiaos.add(toutiao);
        }
    }
}
