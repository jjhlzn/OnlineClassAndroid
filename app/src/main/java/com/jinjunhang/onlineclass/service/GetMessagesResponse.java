package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;
import com.jinjunhang.framework.service.ServerResponse;
import com.jinjunhang.onlineclass.model.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetMessagesResponse extends ServerResponse {

    private List<Message> mMessages = new ArrayList<>();

    public List<Message> getMessages() {
        return mMessages;
    }

    @Override
    public void parse(ServerRequest request, JSONObject json) throws JSONException {
        JSONArray jsonArray = json.getJSONArray("messages");

        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject jo = jsonArray.getJSONObject(i);

            Message message = new Message();
            message.setClickTitle(jo.getString("clickTitle"));
            message.setClickUrl(jo.getString("clickUrl"));
            message.setDetail(jo.getString("detail"));
            message.setTime(jo.getString("time"));
            message.setTitle(jo.getString("title"));

            mMessages.add(message);
        }
    }
}
