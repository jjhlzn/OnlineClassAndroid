package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.PagedServerResponse;
import com.jinjunhang.framework.service.ServerRequest;
import com.jinjunhang.onlineclass.model.Album;
import com.jinjunhang.onlineclass.model.AlbumType;
import com.jinjunhang.onlineclass.model.Question;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by lzn on 16/6/10.
 */
public class GetPagedQuestionsResponse extends PagedServerResponse<Question> {

    public GetPagedQuestionsResponse() {
        mResultSet = new ArrayList<>();
    }

    @Override
    public void parse(ServerRequest request, JSONObject jsonObject) throws JSONException {
        super.parse(request, jsonObject);

        mResultSet = GetQuestionResponse.parse(jsonObject);

    }


}
