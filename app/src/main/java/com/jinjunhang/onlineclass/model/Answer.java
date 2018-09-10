package com.jinjunhang.onlineclass.model;

import java.io.Serializable;

public class Answer implements Serializable {


    private Question mQuestion;
    private String mFromUserId;
    private String mFromUserName;
    private String mToUserId;
    private String mToUserName;
    private String mContent;
    private boolean isFromManager;

    public String getFromUserId() {
        return mFromUserId;
    }

    public String getFromUserName() {
        return mFromUserName;
    }

    public String getToUserId() {
        return mToUserId;
    }

    public String getToUserName() {
        return mToUserName;
    }

    public String getContent() {
        return mContent;
    }

    public void setFromUserId(String fromUserId) {
        mFromUserId = fromUserId;
    }

    public void setFromUserName(String fromUserName) {
        mFromUserName = fromUserName;
    }

    public void setToUserId(String toUserId) {
        mToUserId = toUserId;
    }

    public void setToUserName(String toUserName) {
        mToUserName = toUserName;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public Question getQuestion() {
        return mQuestion;
    }

    public void setQuestion(Question question) {
        mQuestion = question;
    }

    public boolean isFromManager() {
        return isFromManager;
    }

    public void setFromManager(boolean fromManager) {
        isFromManager = fromManager;
    }
}
