package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.PagedServerRequest;
import com.jinjunhang.framework.service.ServerRequest;
import com.jinjunhang.onlineclass.model.AlbumType;

import java.util.Map;

/**
 * Created by lzn on 16/6/10.
 */
public class GetAlbumsRequest extends PagedServerRequest {

    private String mAlbumTypeCode;

    public GetAlbumsRequest(AlbumType albumType) {
        this.mAlbumTypeCode = albumType.getTypeCode();
    }

    public GetAlbumsRequest(String typeCode) {
        this.mAlbumTypeCode = typeCode;
    }

    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.GetAlbumsUrl();
    }

    @Override
    public Map<String, Object> getParams() {
        Map<String, Object> params = super.getParams();
        params.put("type", mAlbumTypeCode);
        return params;
    }

    @Override
    public Class getServerResponseClass() {
        return GetAlbumsResponse.class;
    }

}
