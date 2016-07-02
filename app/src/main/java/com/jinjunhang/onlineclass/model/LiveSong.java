package com.jinjunhang.onlineclass.model;

/**
 * Created by lzn on 16/6/12.
 */
public class LiveSong extends Song {

    private String mImageUrl;
    private String mListenPeople;
    private String mStartDateTime;
    private String mEndDateTime;

    public String getStartDateTime() {
        return mStartDateTime;
    }

    public String getEndDateTime() {
        return mEndDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        mStartDateTime = startDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        mEndDateTime = endDateTime;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getListenPeople() {
        return mListenPeople;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public void setListenPeople(String listenPeople) {
        mListenPeople = listenPeople;
    }
}
