package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;

import java.util.Map;

/**
 * Created by lzn on 16/6/20.
 */
public class LikeQuestionRequest extends ServerRequest {

    private String mQuestionId;

    public String getQuestionId() {
        return mQuestionId;
    }

    public void setQuestionId(String questionId) {
        mQuestionId = questionId;
    }

    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.LikeQuestionUrl();
    }

    @Override
    public Class getServerResponseClass() {
        return LikeQuestionResponse.class;
    }

    @Override
    public Map<String, Object> getParams() {
        Map<String, Object> params = super.getParams();

        params.put("questionId", mQuestionId);
        return params;
    }
}
