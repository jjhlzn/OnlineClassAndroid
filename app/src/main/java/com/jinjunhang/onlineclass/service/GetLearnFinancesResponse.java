package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;
import com.jinjunhang.framework.service.ServerResponse;
import com.jinjunhang.onlineclass.model.LearnFinanceItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetLearnFinancesResponse extends ServerResponse {


    private List<LearnFinanceItem> mLearnFinanceItems = new ArrayList<>();


    public List<LearnFinanceItem> getLearnFinanceItems() {
        return mLearnFinanceItems;
    }

    @Override
    public void parse(ServerRequest request, JSONObject jsonObject) throws JSONException {
        mLearnFinanceItems = parseLearnFinanceItems(jsonObject.getJSONArray("learnFinanceItems"));
    }


    private List<LearnFinanceItem> parseLearnFinanceItems(JSONArray jsonArray) throws JSONException {
        List<LearnFinanceItem> items = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            LearnFinanceItem item = new LearnFinanceItem();
            item.setId(json.getString("id"));
            item.setSongId(json.getString("songId"));
            item.setAudioUrl(json.getString("audioUrl"));
            item.setTitle(json.getString("title"));
            items.add(item);
        }
        return items;
    }
}
