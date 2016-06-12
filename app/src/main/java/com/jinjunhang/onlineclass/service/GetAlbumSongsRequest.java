package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.PagedServerRequest;
import com.jinjunhang.framework.service.ServerRequest;
import com.jinjunhang.onlineclass.model.Album;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by lzn on 16/6/12.
 */
public class GetAlbumSongsRequest extends PagedServerRequest {

    private Album mAlbum;

    public Album getAlbum() {
        return mAlbum;
    }

    public void setAlbum(Album album) {
        mAlbum = album;
    }

    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.GetAlbumSongsUrl();
    }

    @Override
    public Class getServerResponseClass() {
        return GetAlbumSongsResponse.class;
    }

    @Override
    public Map<String, Object> getParams() {
        Map<String, Object> params = super.getParams();
        Map<String, Object> albumParam = new LinkedHashMap<>();
        albumParam.put("id", mAlbum.getId());
        params.put("album", albumParam);
        return params;
    }
}
