package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;
import com.jinjunhang.framework.service.ServerResponse;
import com.jinjunhang.onlineclass.model.Answer;
import com.jinjunhang.onlineclass.model.Pos;
import com.jinjunhang.onlineclass.model.Question;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetPosResponse extends ServerResponse{
    private Pos mPos = new Pos();

    public Pos getPos() {
        return mPos;
    }

    @Override
    public void parse(ServerRequest request, JSONObject jsonObject) throws JSONException {
        JSONObject json = jsonObject.getJSONObject("result");
        mPos.setClickUrl(json.getString("clickUrl"));
        mPos.setImageUrl(json.getString("imageUrl"));
        mPos.setTitle(json.getString("title"));
    }


}
