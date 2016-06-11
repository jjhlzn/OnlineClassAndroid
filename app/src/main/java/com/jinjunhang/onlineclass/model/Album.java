package com.jinjunhang.onlineclass.model;

import java.util.List;

/**
 * Created by lzn on 16/6/10.
 */
public class Album extends BaseModelObject {

    private String mId;
    private String mName;
    private String mAuthor;
    private String mImage;
    private int mCount;
    private String mListenCount;
    private AlbumType mAlbumType;
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
}


