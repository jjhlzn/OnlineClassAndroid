package com.jinjunhang.onlineclass.model;

/**
 * Created by lzn on 16/6/22.
 */
public class Comment extends BaseModelObject {

    private String mId;
    private Song mSong;
    private String mUserId;
    private String mTime;
    private String mContent;

    public String getId() {
        return mId;
    }

    public Song getSong() {
        return mSong;
    }

    public String getUserId() {
        return mUserId;
    }

    public String getTime() {
        return mTime;
    }

    public String getContent() {
        return mContent;
    }

    public void setId(String id) {
        mId = id;
    }

    public void setSong(Song song) {
        mSong = song;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public void setTime(String time) {
        mTime = time;
    }

    public void setContent(String content) {
        mContent = content;
    }
}
