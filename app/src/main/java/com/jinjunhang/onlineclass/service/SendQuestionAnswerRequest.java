package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;
import com.jinjunhang.onlineclass.model.Question;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by lzn on 16/6/20.
 */
public class SendQuestionAnswerRequest extends ServerRequest {

    private Question mQuestion;
    private String mContent;
    private String mToUser;

    public Question getQuestion() {
        return mQuestion;
    }

    public String getContent() {
        return mContent;
    }

    public void setQuestion(Question question) {
        mQuestion = question;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public void setToUser(String toUser) {
        mToUser = toUser;
    }

    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.SendQuestionAnswerUrl();
    }

    @Override
    public Class getServerResponseClass() {
        return SendQuestionAnswerResponse.class;
    }

    public Map<String, Object> getParams() {
        Map<String, Object> params = super.getParams();
        params.put("questionId", mQuestion.getId());
        params.put("comment", mContent);
        params.put("toUserId", mToUser);
        return params;
    }
}
