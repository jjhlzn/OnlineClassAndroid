package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.PagedServerResponse;
import com.jinjunhang.framework.service.ServerRequest;
import com.jinjunhang.onlineclass.model.Comment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lzn on 16/6/22.
 */
public class GetSongCommentsResponse extends PagedServerResponse {



    @Override
    public void parse(ServerRequest request, JSONObject jsonObject) throws JSONException {
        super.parse(request, jsonObject);
        GetSongCommentsRequest req = (GetSongCommentsRequest)request;
        JSONArray jsonArray = jsonObject.getJSONArray("comments");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);

            Comment comment = new Comment();
            comment.setSong(req.getSong());
            comment.setId(json.getInt("id")+"");
            comment.setContent(json.getString("content"));
            comment.setTime(json.getString("time"));
            comment.setUserId(json.getString("userId"));
            comment.setNickName(json.getString("name"));
            mResultSet.add(comment);
        }

    }


}
