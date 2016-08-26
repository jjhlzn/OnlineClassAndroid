package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;
import com.jinjunhang.onlineclass.model.Song;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by jjh on 2016-7-4.
 */
public class SendLiveCommentRequest extends ServerRequest {

    private Song mSong;
    private String mComment;
    private String mLastId;

    public Song getSong() {
        return mSong;
    }

    public String getComment() {
        return mComment;
    }

    public String getLastId() {
        return mLastId;
    }

    public void setSong(Song song) {
        mSong = song;
    }

    public void setComment(String comment) {
        mComment = comment;
    }

    public void setLastId(String lastId) {
        mLastId = lastId;
    }

    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.SendLiveCommentUrl();
    }

    @Override
    public Class getServerResponseClass() {
        return SendLiveCommentResponse.class;
    }

    @Override
    public Map<String, Object> getParams() {
        Map<String, Object> params = super.getParams();
        params.put("comment", mComment);
        Map<String, Object> songParams = new LinkedHashMap<>();
        songParams.put("id", mSong.getId());
        params.put("song", songParams);
        params.put("lastId", mLastId);
        return params;
    }

}
