package com.jinjunhang.onlineclass.model;

public class LearnFinanceItem extends BaseModelObject {

    private String mId;
    private String mSongId;
    private String mAudioUrl;
    private String mTitle;

    public String getId() {
        return mId;
    }

    public String getAudioUrl() {
        return mAudioUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSongId() {
        return mSongId;
    }

    public void setSongId(String songId) {
        mSongId = songId;
    }

    public void setId(String id) {
        mId = id;
    }

    public void setAudioUrl(String audioUrl) {
        mAudioUrl = audioUrl;
    }

    public void setTitle(String title) {
        mTitle = title;
    }
}
