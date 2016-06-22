package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.PagedServerRequest;
import com.jinjunhang.onlineclass.model.Song;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by lzn on 16/6/22.
 */
public class GetSongCommentsRequest extends PagedServerRequest {

    private Song mSong;

    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.GetSongCommentsUrl();
    }

    @Override
    public Class getServerResponseClass() {
        return GetSongCommentsResponse.class;
    }

    public void setSong(Song song) {
        mSong = song;
    }

    public Song getSong() {
        return mSong;
    }

    @Override
    public Map<String, Object> getParams() {
        Map<String, Object> params = super.getParams();

        Map<String, Object> child = new LinkedHashMap<>();
        child.put("id", mSong.getId());
        params.put("song", child);
        return params;

    }
}
