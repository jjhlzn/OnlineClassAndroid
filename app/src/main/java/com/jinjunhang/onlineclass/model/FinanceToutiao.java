package com.jinjunhang.onlineclass.model;

public class FinanceToutiao {
    private String mTitle = "";
    private String mContent = "";
    private String mLink = "";
    private int index;

    public String getTitle() {
        return mTitle;
    }

    public String getLink() {
        return mLink;
    }

    public int getIndex() {
        return index;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setLink(String link) {
        mLink = link;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }
}
