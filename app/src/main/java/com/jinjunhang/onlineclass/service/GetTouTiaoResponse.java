package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;
import com.jinjunhang.framework.service.ServerResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jjh on 2016-6-30.
 */
public class GetTouTiaoResponse extends ServerResponse {

   private String content = "";
   private String title = "";
   private String clickUrl = "";

    public String getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }

    public String getClickUrl() {
        return clickUrl;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setClickUrl(String clickUrl) {
        this.clickUrl = clickUrl;
    }

    @Override
    public void parse(ServerRequest request, JSONObject json) throws JSONException {
        JSONObject obj = json.getJSONObject("result");
        content = obj.getString("content");
        title = obj.getString("title");
        clickUrl = obj.getString("clickUrl");

    }
}
