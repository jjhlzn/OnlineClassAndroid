package com.jinjunhang.onlineclass.model;

/**
 * Created by lzn on 16/6/10.
 */
public class Song extends BaseModelObject {

    private String mId;
    private String mName;
    private String mDesc;
    private String mDate;
    private String mUrl;
    private SongSetting settings = new SongSetting();
    private Album album;

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
}
