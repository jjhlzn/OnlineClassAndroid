package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;
import com.jinjunhang.onlineclass.model.Song;

import java.util.Map;

/**
 * Created by jjh on 2016-8-28.
 */
public class HeartbeatRequest extends ServerRequest {

    private Song mSong;

    public Song getSong() {
        return mSong;
    }

    public void setSong(Song song) {
        mSong = song;
    }

    public HeartbeatRequest(Song song) {
        mSong = song;
    }

    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.SendHeartbeatUrl();
    }

    @Override
    public Class getServerResponseClass() {
        return GetSongInfoResponse.class;
    }

    @Override
    public Map<String, Object> getParams() {
        Map<String, Object> params = super.getParams();

        params.put("id", mSong.getId());
        return params;
    }

}
