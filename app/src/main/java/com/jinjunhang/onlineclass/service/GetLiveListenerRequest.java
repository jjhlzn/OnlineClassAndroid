package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;
import com.jinjunhang.onlineclass.model.Song;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by jjh on 2016-7-4.
 */
public class GetLiveListenerRequest extends ServerRequest {

    private Song mSong;

    public void setSong(Song song) {
        mSong = song;
    }

    public Song getSong() {
        return mSong;
    }

    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.GetLiveSongListener();
    }

    @Override
    public Class getServerResponseClass() {
        return GetLiveListenerResponse.class;
    }

    @Override
    public Map<String, Object> getParams() {
        Map<String, Object> params = super.getParams();
        Map<String, Object> songParams = new LinkedHashMap<>();
        songParams.put("id", mSong.getId());
        params.put("song", songParams);
        return params;
    }
}
