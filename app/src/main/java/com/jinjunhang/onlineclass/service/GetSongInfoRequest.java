package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;
import com.jinjunhang.onlineclass.model.Song;

import java.util.Map;

/**
 * Created by lzn on 16/7/24.
 */
public class GetSongInfoRequest extends ServerRequest {

    private Song mSong;

    public Song getSong() {
        return mSong;
    }

    public void setSong(Song song) {
        mSong = song;
    }

    public GetSongInfoRequest(Song song) {
        mSong = song;
    }

    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.GetSongInfoUrl();
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
