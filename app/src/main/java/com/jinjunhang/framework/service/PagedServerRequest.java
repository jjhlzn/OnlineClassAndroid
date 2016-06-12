package com.jinjunhang.framework.service;

import java.util.Map;

/**
 * Created by lzn on 16/6/12.
 */
public abstract class PagedServerRequest extends ServerRequest {

    private int pageIndex = 0;
    private int pageSize = 15;

    public int getPageIndex() {
        return pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public Map<String, Object> getParams() {
        Map<String, Object> params = super.getParams();
        params.put("pageno", pageIndex);
        params.put("pagesize", pageSize);
        return params;
    }
}
