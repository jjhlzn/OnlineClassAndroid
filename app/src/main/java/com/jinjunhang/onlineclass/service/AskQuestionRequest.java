package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;
import com.jinjunhang.onlineclass.model.Question;

import java.util.Map;

/**
 * Created by lzn on 16/6/20.
 */
public class AskQuestionRequest extends ServerRequest {

    private String mContent;



    public String getContent() {
        return mContent;
    }


    public void setContent(String content) {
        mContent = content;
    }



    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.AskQuestionUrl();
    }

    @Override
    public Class getServerResponseClass() {
        return AskQuestionResponse.class;
    }

    public Map<String, Object> getParams() {
        Map<String, Object> params = super.getParams();
        params.put("content", mContent);
        return params;
    }
}
