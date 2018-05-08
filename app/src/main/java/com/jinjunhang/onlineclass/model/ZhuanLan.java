package com.jinjunhang.onlineclass.model;

public class ZhuanLan {
    private String mName = "";
    private String mLatest = "";
    private String mUpdateTime = "";
    private String mPriceInfo = "";
    private String mDescription = "";
    private String mImageUrl = "";
    private String mUrl = "";
    private String mAuthor = "";
    private String mAuthorTitle = "";
    private int mDingYue = 0;

    public String getName() {
        return mName;
    }

    public String getLatest() {
        return mLatest;
    }

    public String getUpdateTime() {
        return mUpdateTime;
    }

    public String getPriceInfo() {
        return mPriceInfo;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setLatest(String latest) {
        mLatest = latest;
    }

    public void setUpdateTime(String updateTime) {
        mUpdateTime = updateTime;
    }

    public void setPriceInfo(String priceInfo) {
        mPriceInfo = priceInfo;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getAuthorTitle() {
        return mAuthorTitle;
    }

    public int getDingYue() {
        return mDingYue;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public void setAuthorTitle(String authorTitle) {
        mAuthorTitle = authorTitle;
    }

    public void setDingYue(int dingYue) {
        mDingYue = dingYue;
    }
}
