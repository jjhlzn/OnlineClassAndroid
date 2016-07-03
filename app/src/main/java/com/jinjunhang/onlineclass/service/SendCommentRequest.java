package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;
import com.jinjunhang.framework.service.ServerResponse;
import com.jinjunhang.onlineclass.model.Song;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by jjh on 2016-7-3.
 */
public class SendCommentRequest extends ServerRequest {

    private Song mSong;
    private String mComment;

    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.SendCommentUrl();
    }

    @Override
    public Class getServerResponseClass() {
        return SendCommentResponse.class;
    }

    public Song getSong() {
        return mSong;
    }

    public String getComment() {
        return mComment;
    }

    public void setSong(Song song) {
        mSong = song;
    }

    public void setComment(String comment) {
        mComment = comment;
    }

    @Override
    public Map<String, Object> getParams() {
        Map<String, Object> params = super.getParams();
        params.put("comment", mComment);
        Map<String, Object> songParams = new LinkedHashMap<>();
        songParams.put("id", mSong.getId());
        params.put("song", songParams);
        return params;
    }
}
