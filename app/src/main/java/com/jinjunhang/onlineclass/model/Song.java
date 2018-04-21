package com.jinjunhang.onlineclass.model;

import com.jinjunhang.onlineclass.service.GetTuijianCoursesResponse;

/**
 * Created by lzn on 16/6/10.
 */
public class Song extends BaseModelObject {



    private String mId;
    private String mName;
    private String mDesc;
    private String mDate;
    private String mUrl;
    private String imageUrl = "";
    private SongSetting settings = new SongSetting();
    private Album album;
    private String mShareTitle = "";
    private String mShareUrl = "";
    private String mAuthor = "";

    public Song () {}
    public Song(GetTuijianCoursesResponse.Course course) {
        this.mId = course.getId();
        this.mName = course.getName();
        this.mUrl = course.getUrl();
        this.imageUrl = course.getImageUrl();
        this.mShareTitle = course.getShareTitle();
        this.mShareUrl = course.getShareUrl();
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getDesc() {
        return mDesc;
    }

    public String getDate() {
        return mDate;
    }

    public String getUrl() {
        return mUrl;
    }

    public SongSetting getSettings() {
        return settings;
    }

    public Album getAlbum() {
        return album;
    }

    public void setId(String id) {
        mId = id;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setDesc(String desc) {
        mDesc = desc;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public void setSettings(SongSetting settings) {
        this.settings = settings;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public boolean isLive() {
        if (album == null)
            return true;
        return album.isLive();
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getShareTitle() {
        return mShareTitle;
    }

    public String getShareUrl() {
        return mShareUrl;
    }

    public void setShareTitle(String shareTitle) {
        this.mShareTitle = shareTitle;
    }

    public void setShareUrl(String shareUrl) {
        mShareUrl = shareUrl;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }
}
