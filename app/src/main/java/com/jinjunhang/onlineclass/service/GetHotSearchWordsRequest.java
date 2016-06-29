package com.jinjunhang.onlineclass.service;

import com.google.common.util.concurrent.Service;
import com.jinjunhang.framework.service.ServerRequest;

/**
 * Created by lzn on 16/6/28.
 */
public class GetHotSearchWordsRequest extends ServerRequest {


    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.GetHotSearchWordsUrl();
    }

    @Override
    public Class getServerResponseClass() {
        return GetHotSearchWordsResponse.class;
    }
}
