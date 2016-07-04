package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;
import com.jinjunhang.framework.service.ServerResponse;
import com.jinjunhang.onlineclass.model.Comment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jjh on 2016-7-3.
 */
public class GetLiveCommentsResponse extends ServerResponse {

    public GetLiveCommentsResponse() {
        mCommentList = new ArrayList<>();
    }

    private List<Comment> mCommentList;

    public List<Comment> getCommentList() {
        return mCommentList;
    }
    @Override
    public void parse(ServerRequest request, JSONObject jsonObject) throws JSONException {
        GetLiveCommentsRequest req = (GetLiveCommentsRequest)request;
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
            //comment.setManager(json.getBoolean("isManager"));
            mCommentList.add(comment);
        }
    }
}
