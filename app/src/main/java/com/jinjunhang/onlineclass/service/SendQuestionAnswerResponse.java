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
 * Created by jjh on 2016-7-4.
 */
public class SendQuestionAnswerResponse extends ServerResponse {

    public SendQuestionAnswerResponse(){
    }


    @Override
    public void parse(ServerRequest request, JSONObject jsonObject) throws JSONException {

    }
}
