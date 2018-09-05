package com.jinjunhang.onlineclass.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Question extends BaseModelObject implements Serializable{

    private String mId;
    private String mUserId;
    private String mUserName;
    private String mContent;
    private String mTime;
    private boolean mIsLiked;
    private int mAnswerCount;
    private int mThumbCount;
    private List<Answer> mAnswers = new ArrayList<>();

    public String getUserId() {
        return mUserId;
    }

    public String getUserName() {
        return mUserName;
    }

    public String getContent() {
        return mContent;
    }

    public String getTime() {
        return mTime;
    }

    public List<Answer> getAnswers() {
        return mAnswers;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public void setTime(String time) {
        mTime = time;
    }

    public void setAnswers(List<Answer> answers) {
        mAnswers = answers;
    }

    public int getAnswerCount() {
        return mAnswerCount;
    }

    public int getThumbCount() {
        return mThumbCount;
    }

    public void setAnswerCount(int answerCount) {
        mAnswerCount = answerCount;
    }

    public void setThumbCount(int thumbCount) {
        mThumbCount = thumbCount;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public boolean isLiked() {
        return mIsLiked;
    }

    public void setLiked(boolean isLiked) {
        this.mIsLiked = isLiked;
    }
}
