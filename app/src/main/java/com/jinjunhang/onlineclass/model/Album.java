package com.jinjunhang.onlineclass.model;

import android.support.v4.media.MediaBrowserCompat;

import java.util.List;

/**
 * Created by lzn on 16/6/10.
 */
public class Album extends BaseModelObject {

    private String mId;
    private String mName;
    private String mDesc;
    private String mAuthor;
    private String mImage;
    private int mCount;
    private String mListenCount;
    private AlbumType mAlbumType;
    private boolean playing;
    private boolean isReady;
    private List<Song> mSongs;


    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getImage() {
        return mImage;
    }

    public int getCount() {
        return mCount;
    }

    public String getListenCount() {
        return mListenCount;
    }

    public AlbumType getAlbumType() {
        return mAlbumType;
    }

    public List<Song> getSongs() {
        return mSongs;
    }

    public void setId(String id) {
        mId = id;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public void setImage(String image) {
        mImage = image;
    }

    public void setCount(int count) {
        mCount = count;
    }

    public void setListenCount(String listenCount) {
        mListenCount = listenCount;
    }

    public void setAlbumType(AlbumType courseType) {
        mAlbumType = courseType;
    }

    public void setSongs(List<Song> songs) {
        mSongs = songs;
    }

    public String getDesc() {
        return mDesc;
    }

    public void setDesc(String desc) {
        mDesc = desc;
    }

    public boolean isPlaying() {
        return playing;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public static interface MediaBrowserProvider {
        MediaBrowserCompat getMediaBrowser();
    }

    public boolean isLive() {
        return this.getAlbumType().getName().equals(AlbumType.LiveAlbumType.getName()) || getAlbumType().getName().equals(AlbumType.VipAlbumType.getName());
    }
}


