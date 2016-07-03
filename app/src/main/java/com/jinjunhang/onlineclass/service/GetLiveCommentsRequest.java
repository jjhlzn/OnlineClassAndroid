package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;
import com.jinjunhang.onlineclass.model.Song;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by jjh on 2016-7-3.
 */
public class GetLiveCommentsRequest extends ServerRequest {

    private Song mSong;
    private String mLastId;

    public Song getSong() {
        return mSong;
    }

    public String getLastId() {
        return mLastId;
    }

    public void setSong(Song song) {
        mSong = song;
    }

    public void setLastId(String lastId) {
        mLastId = lastId;
    }

    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.GetLiveCommentsUrl();
    }

    @Override
    public Class getServerResponseClass() {
        return GetLiveCommentsResponse.class;
    }

    @Override
    public Map<String, Object> getParams() {
        Map<String, Object> params = super.getParams();

        Map<String, Object> child = new LinkedHashMap<>();
        child.put("id", mSong.getId());
        params.put("song", child);
        params.put("lastId", mLastId);
        return params;

    }
}
