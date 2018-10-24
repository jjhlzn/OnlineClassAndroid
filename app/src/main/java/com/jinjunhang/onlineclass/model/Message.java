package com.jinjunhang.onlineclass.model;

public class Message extends BaseModelObject {

    private String mTitle;
    private String mDetail;
    private String mClickTitle;
    private String mClickUrl;
    private String mTime;

    public String getTitle() {
        return mTitle;
    }

    public String getDetail() {
        return mDetail;
    }

    public String getClickTitle() {
        return mClickTitle;
    }

    public String getClickUrl() {
        return mClickUrl;
    }

    public String getTime() {
        return mTime;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setDetail(String detail) {
        mDetail = detail;
    }

    public void setClickTitle(String clickTitle) {
        mClickTitle = clickTitle;
    }

    public void setClickUrl(String clickUrl) {
        mClickUrl = clickUrl;
    }

    public void setTime(String time) {
        mTime = time;
    }
}
